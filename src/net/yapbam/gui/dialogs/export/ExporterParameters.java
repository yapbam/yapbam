package net.yapbam.gui.dialogs.export;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

import net.yapbam.export.ExportFormatType;
import net.yapbam.gui.LocalizationData;

public class ExporterParameters {
	private final DateFormat dateFormatter;
	private final NumberFormat amountFormatter;
	// For json and html exporters, separator is a non sense ... but its not a big deal
	private char separator;
	private ExportFormatType exportFormat;

	public ExporterParameters() {
		this(';', ExportFormatType.CSV);
	}
	
	public ExporterParameters(char separator, ExportFormatType exportFormat) {
		super();
		this.separator = separator;
		this.exportFormat = exportFormat;
		this.dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale());
		this.amountFormatter = CSVWriter.getDecimalFormater(LocalizationData.getLocale());
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}
	
	public char getSeparator() {
		return separator;
	}

	public ExportFormatType getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(ExportFormatType exportFormat) {
		this.exportFormat = exportFormat;
	}
	
	public DateFormat getDateFormat() {
		return this.dateFormatter;
	}
	
	public NumberFormat getAmountFormat() {
		return this.amountFormatter;
	}

	public Charset getPreferredEncoding() {
		// For Json, non UTF encoding are not JSON standard compliant, it's the reason why the method is named "preferred"
		return Charset.defaultCharset();
	}
}