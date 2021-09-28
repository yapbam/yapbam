package net.yapbam.gui.dialogs.export;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public class TableJsonExporter  extends TableAbstractExporter<ExporterJsonFormat> {

	public TableJsonExporter(OutputStream stream, Charset encoding, Locale locale) {
		super(new ExporterJsonFormat(stream, encoding), locale);
	}
	
}