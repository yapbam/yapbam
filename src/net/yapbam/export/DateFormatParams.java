package net.yapbam.export;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.NonNull;

/** A DateFormatter compatible with storage of its serialization in Preferences (Default DateFormat instance are too big to be saved in preferences).
 */
class DateFormatParams implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int style;
	private final String locale;
	private transient DateFormat dateFormat;

	DateFormatParams(int style, @NonNull Locale locale) {
		this.style = style;
		this.locale = locale.toLanguageTag();
	}
	
	public String format(Date date) {
		if (dateFormat==null) {
			dateFormat = DateFormat.getDateInstance(style, Locale.forLanguageTag(locale));
		}
		return dateFormat.format(date);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + style;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DateFormatParams other = (DateFormatParams) obj;
		return locale.equals(other.locale) && style == other.style;
	}
}