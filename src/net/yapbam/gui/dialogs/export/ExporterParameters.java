package net.yapbam.gui.dialogs.export;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

import lombok.Getter;
import lombok.Setter;
import net.yapbam.export.ExportFormatType;
import net.yapbam.gui.LocalizationData;

@Getter
@Setter
public class ExporterParameters {

	private DateFormat dateFormat;
	private NumberFormat amountFormat;
	// For json and html exporters, separator is a non sense ... but its not a big deal
	private char separator;
	private ExportFormatType exportFormat;
	private ExporterExtendedParameters exporterExtendedParameters;

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
}