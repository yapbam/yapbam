package net.yapbam.gui.dialogs.export;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

import lombok.Getter;
import lombok.Setter;
import net.yapbam.export.FormatParams;
import net.yapbam.gui.LocalizationData;

@Setter
public class ExporterParameters<D> implements Serializable {
	private static final long serialVersionUID = 3L;
	
	private final DateFormat dateFormat;
	private final NumberFormat amountFormat;
	@Getter
	private FormatParams formatParams;
	@Getter
	private final transient D dataExtension;

	public ExporterParameters(D dataExtension) {
		super();
		this.formatParams = null;
		this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale());
		this.amountFormat = CSVWriter.getDecimalFormater(LocalizationData.getLocale());
		this.dataExtension = dataExtension;
	}
	
	public String format(Object obj) {
		if (obj == null) {
			return ""; //$NON-NLS-1$
		} else if (obj instanceof Date) {
			return dateFormat.format(obj);
		} else if (obj instanceof Double) {
			return amountFormat.format(obj);
		} else {
			return obj.toString();
		}
	}

}