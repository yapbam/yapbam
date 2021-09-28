package net.yapbam.gui.dialogs.export;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public final class TableCsvExporter extends TableAbstractExporter<ExporterCsvFormat> {

	public TableCsvExporter(OutputStream stream, char separator, Charset encoding, Locale locale) {
		super(new ExporterCsvFormat(stream, separator, encoding), locale);
	}

}
