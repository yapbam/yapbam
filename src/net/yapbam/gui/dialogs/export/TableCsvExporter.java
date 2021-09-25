package net.yapbam.gui.dialogs.export;

import java.util.Locale;

public final class TableCsvExporter extends TableAbstractExporter<ExporterCsvFormat> {

	public TableCsvExporter(Locale locale) {
		super(new ExporterCsvFormat(';'), locale);
	}

}
