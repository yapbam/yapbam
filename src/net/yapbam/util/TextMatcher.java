package net.yapbam.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextMatcher {
	public static interface Kind {}
	
	public static final Kind REGULAR = new Kind() {};
	public static final Kind EQUALS = new Kind() {};
	public static final Kind CONTAINS = new Kind() {};

	private Kind kind;
	private String filter;
	private boolean caseSensitive;
	private boolean diacriticalSensitive;
	private Object internalFilter;
	
	public TextMatcher(Kind kind, String filter, boolean caseSensitive, boolean diacriticalSensitive) {
		super();
		this.kind = kind;
		this.filter = filter;
		this.caseSensitive = caseSensitive;
		this.diacriticalSensitive = diacriticalSensitive;
		// If diacriticals have to be ignored, we need to remove the diacritical marks from the filter string
		if (!diacriticalSensitive) {
			filter = removeDiacriticals(filter);
		}
		if (kind==REGULAR) {
			if (caseSensitive) {
				internalFilter = Pattern.compile(filter);
			} else {
				internalFilter = Pattern.compile(filter, Pattern.UNICODE_CASE+Pattern.CASE_INSENSITIVE);
			}
		} else if ((kind==EQUALS) || ((kind==CONTAINS) && caseSensitive)) {
			internalFilter = filter;
		} else {
			internalFilter = filter.toUpperCase();
		}
	}

	public static String removeDiacriticals(String filter) {
		return Normalizer.normalize(filter, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public Kind getKind() {
		return kind;
	}

	public String getFilter() {
		return filter;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public boolean isDiacriticalSensitive() {
		return diacriticalSensitive;
	}
	
	public boolean matches(String text) {
		if (!diacriticalSensitive) text = removeDiacriticals(text);
		if (kind==REGULAR) {
			return ((Pattern)internalFilter).matcher(text).matches();
		} else if (kind==EQUALS) {
			return caseSensitive?text.equals(internalFilter):text.equalsIgnoreCase((String) internalFilter);
		} else if (kind==CONTAINS) {
			if (caseSensitive) {
				return text.contains((CharSequence) internalFilter);
			} else {
				return text.toUpperCase().contains((CharSequence) internalFilter);
			}
		} else throw new UnsupportedOperationException();
	}
}
