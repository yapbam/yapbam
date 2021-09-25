package net.yapbam.gui.dialogs.export;

import java.util.Locale;

public final class TableHtmlExporter extends TableAbstractExporter<ExporterHtmlFormat> {

	public TableHtmlExporter(Locale locale) {
		super(new ExporterHtmlFormat(), locale);
	}

}