package net.yapbam.gui.dialogs.export;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public final class TableHtmlExporter extends TableAbstractExporter<ExporterHtmlFormat> {

	public TableHtmlExporter(OutputStream stream, Charset encoding, Locale locale) {
		super(new ExporterHtmlFormat(stream, encoding), locale);
	}

}