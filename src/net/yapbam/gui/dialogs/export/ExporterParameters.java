package net.yapbam.gui.dialogs.export;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

import lombok.Getter;
import lombok.Setter;
import net.yapbam.export.ExportFormatType;
import net.yapbam.gui.LocalizationData;

@Setter
public class ExporterParameters<D> {
	private final DateFormat dateFormat;
	private final NumberFormat amountFormat;
	@Getter
	private final D dataExtension;
	@Getter
	private Object formatParams;
	@Getter
	private ExportFormatType exportFormat;

	public ExporterParameters(D dataExtension) {
		super();
		this.exportFormat = ExportFormatType.JSON;
		this.formatParams = null;
		this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale());
		this.amountFormat = CSVWriter.getDecimalFormater(LocalizationData.getLocale());
		this.dataExtension = dataExtension;
	}
	
	public void setFormat(ExportFormatType exportFormat, Object formatParams) {
		this.exportFormat = exportFormat;
		this.formatParams = formatParams;
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