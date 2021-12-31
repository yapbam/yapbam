package net.yapbam.export;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import lombok.NonNull;

class NumberFormatParams implements Serializable {
	private static final long serialVersionUID = 1L;

	private final boolean pureNumber;
	private final String locale;
	private transient NumberFormat amountFormat;
	
	NumberFormatParams(@NonNull Locale locale, boolean pureNumber) {
		super();
		this.pureNumber = pureNumber;
		this.locale = locale.toLanguageTag();
	}

	public String format(Double obj) {
		if (amountFormat == null) {
			amountFormat = NumberFormat.getInstance(Locale.forLanguageTag(locale));
			if (pureNumber) {
				NumberFormat newOne = NumberFormat.getInstance(Locale.forLanguageTag(locale));
				if (amountFormat instanceof DecimalFormat) {
					newOne.setMinimumFractionDigits(amountFormat.getMinimumFractionDigits());
					newOne.setMaximumFractionDigits(amountFormat.getMaximumFractionDigits());
				}
				amountFormat = newOne;
				amountFormat.setGroupingUsed(false);
			}
		}
		return amountFormat.format(obj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + locale.hashCode();
		result = prime * result + (pureNumber ? 1231 : 1237);
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
		NumberFormatParams other = (NumberFormatParams) obj;
		return locale.equals(other.locale) && pureNumber == other.pureNumber;
	}
}