package net.yapbam.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

/** A text matcher.
 * <br>A text matcher allows you to test whether a string matches a filter accordingly to some options as:<ul>
 * 	<li>What condition means "matches":<ul>
 * 		<li>String matches is equals to the filter.</li>
 * 		<li>String contains the filter.</li>
 * 		<li>String matches the filter (interpreted as a regular expression).</li> 
 * 		</ul></li>
 * 	<li>Case could be ignored ... or not.</li> 
 * 	<li>Diacritical marks could be ignored ... or not.</li> 
 * </ul>
 * @see Kind#REGULAR
 * @see Kind#EQUALS
 * @see Kind#CONTAINS
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class TextMatcher {
	/** A kind of comparison a TextMatcher can perform. */
	public enum Kind {
	/** Kind of comparison: Does a string matches the filter (filter is interpreted as a regular expression) ? */
		REGULAR,
	/** Kind of comparison: Does a string is equal to the filter ? */
		EQUALS,
	/** Kind of comparison: Does a string contains the filter ? */
		CONTAINS
	}
	
	private Kind kind;
	private String filter;
	private boolean caseSensitive;
	private boolean diacriticalSensitive;
	private Object internalFilter;
	
	/** Constructor.
	 * @param kind The kind of matcher.
	 * @param filter The filter string
	 * @param caseSensitive sets the case sensitivity of the comparison (true if the comparison is case sensitive) 
	 * @param diacriticalSensitive sets the diacritical sensitivity of the comparison (false to ignore diacritical marks). 
	 * @see Kind#REGULAR
	 * @see Kind#EQUALS
	 * @see Kind#CONTAINS
	 */
	public TextMatcher(Kind kind, String filter, boolean caseSensitive, boolean diacriticalSensitive) {
		super();
		if (kind==null) throw new IllegalArgumentException();
		this.kind = kind;
		this.filter = filter;
		this.caseSensitive = caseSensitive;
		this.diacriticalSensitive = diacriticalSensitive;
		// If diacriticals have to be ignored, we need to remove the diacritical marks from the filter string
		if (!diacriticalSensitive) {
			filter = removeDiacriticals(filter);
		}
		if (kind==Kind.REGULAR) {
			if (caseSensitive) {
				internalFilter = Pattern.compile(filter);
			} else {
				internalFilter = Pattern.compile(filter, Pattern.UNICODE_CASE+Pattern.CASE_INSENSITIVE);
			}
		} else if ((kind==Kind.EQUALS) || ((kind==Kind.CONTAINS) && caseSensitive)) {
			internalFilter = filter;
		} else {
			internalFilter = filter.toUpperCase();
		}
	}

	/** Removes the diacritical marks from a string.
	 * @param string The string to process
	 * @return a new String with no diacritical marks
	 */
	public static String removeDiacriticals(String string) {
		return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	/** Gets the kind of comparison.
	 * @return a Kind of comparison.
	 * @see Kind#REGULAR
	 * @see Kind#EQUALS
	 * @see Kind#CONTAINS
	 */
	public Kind getKind() {
		return kind;
	}

	/** Gets the filter string.
	 * @return a String
	 */
	public String getFilter() {
		return filter;
	}

	/** Tests whether this matcher is case sensitive.
	 * @return true if the matcher is case sensitive.
	 */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/** Tests whether this matcher is diacritical sensitive.
	 * @return true if the matcher is diacritical sensitive.
	 */
	public boolean isDiacriticalSensitive() {
		return diacriticalSensitive;
	}
	
	/** Tests whether a string matches this matcher.
	 * @param text the string to test
	 * @return true if the string matches. If the string is null, this method always returns false
	 */
	public boolean matches(String text) {
		if (text==null) return false;
		if (!diacriticalSensitive) text = removeDiacriticals(text);
		if (kind==Kind.REGULAR) {
			return ((Pattern)internalFilter).matcher(text).matches();
		} else if (kind==Kind.EQUALS) {
			return caseSensitive?text.equals(internalFilter):text.equalsIgnoreCase((String) internalFilter);
		} else if (kind==Kind.CONTAINS) {
			if (caseSensitive) {
				return text.contains((CharSequence) internalFilter);
			} else {
				return text.toUpperCase().contains((CharSequence) internalFilter);
			}
		} else throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		return filter.hashCode();
	}

	/**
	 * Tests whether the argument object is equals to this.
	 * <br>Two TextMatcher are equals if their kind, filter, caseSensitive and diacriticalSensitive attributes are equals.
	 * <br>Note that two equivalent TextMatchers can not be equals; For instance one with "FILTER" as filter, the other with "filter"
	 * and the caseSensitive attribute set to false.
	 * @param obj a textMatcher to test.
	 * @see #TextMatcher(Kind, String, boolean, boolean) 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TextMatcher) {
			TextMatcher other = (TextMatcher) obj;
			return this.kind.equals(other.kind) && this.filter.equals(other.filter)
			&& (this.caseSensitive==other.caseSensitive) && (this.diacriticalSensitive==other.diacriticalSensitive);
		}
		return false;
	}
}
