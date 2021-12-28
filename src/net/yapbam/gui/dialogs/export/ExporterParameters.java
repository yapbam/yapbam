package net.yapbam.gui.dialogs.export;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

import lombok.Getter;
import lombok.Setter;
import net.yapbam.export.ExportFormatType;
import net.yapbam.gui.LocalizationData;

@Setter
public class ExporterParameters {
	private DateFormat dateFormat;
	private NumberFormat amountFormat;
	// For json and html exporters, separator is a non sense ... but its not a big deal
	@Getter
	private char separator;
	@Getter
	private ExportFormatType exportFormat;

	public ExporterParameters() {
		this(';', ExportFormatType.CSV);
	}
	
	public ExporterParameters(char separator, ExportFormatType exportFormat) {
		super();
		this.separator = separator;
		this.exportFormat = exportFormat;
		this.dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale());
		this.amountFormat = CSVWriter.getDecimalFormater(LocalizationData.getLocale());
	}

	public Charset getPreferredEncoding() {
		// For Json, non UTF encoding are not JSON standard compliant, it's the reason why the method is named "preferred"
		return Charset.defaultCharset();
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