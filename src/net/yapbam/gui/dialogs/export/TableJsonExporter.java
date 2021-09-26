package net.yapbam.gui.dialogs.export;

import java.util.Locale;

public class TableJsonExporter  extends TableAbstractExporter<ExporterJsonFormat> {

	public TableJsonExporter(Locale locale) {
		super(new ExporterJsonFormat(), locale);
	}
	
}