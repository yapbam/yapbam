package net.yapbam.export;

import java.text.DateFormat;
import java.util.Date;

import lombok.NonNull;
import net.yapbam.gui.LocalizationData;

abstract class BasicFormatParams implements FormatParams {
	private static final long serialVersionUID = 1L;

	private final DateFormatParams dateFormat;
	private final NumberFormatParams amountFormat;

	protected BasicFormatParams(@NonNull NumberFormatParams amountFormat) {
		this.dateFormat = new DateFormatParams(DateFormat.SHORT, LocalizationData.getLocale());
		this.amountFormat = amountFormat;
	}

	@Override
	public String format(Object obj) {
		if (obj == null) {
			return ""; //$NON-NLS-1$
		} else if (obj instanceof Date) {
			return dateFormat.format((Date)obj);
		} else if (obj instanceof Double) {
			return amountFormat.format((Double)obj);
		} else {
			return obj.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amountFormat == null) ? 0 : amountFormat.hashCode());
		result = prime * result + ((dateFormat == null) ? 0 : dateFormat.hashCode());
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
		BasicFormatParams other = (BasicFormatParams) obj;
		return amountFormat.equals(other.amountFormat) && dateFormat.equals(other.dateFormat);
	}
}
