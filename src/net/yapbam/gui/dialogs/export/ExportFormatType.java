package net.yapbam.gui.dialogs.export;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public enum ExportFormatType {
	HTML("HyperText Markup Language", "html"), //
	CSV("Comma Separated Values", "csv"),
	JSON("JavaScript Object Notation", "json");

	private final String description;
	private final String extension;

	ExportFormatType(String description, String extension) {
		this.description = description;
		this.extension = extension;
	}

	public String getDescription() {
		return description;
	}

	public String getExtension() {
		return extension;
	}

	public IExportableFormat getTableExporter(OutputStream stream) {
		if (ExportFormatType.CSV.equals(this)) {
			return new ExporterCsvFormat(stream, ';', StandardCharsets.UTF_8);
		} else if (ExportFormatType.HTML.equals(this)) {
			return new ExporterHtmlFormat(stream, StandardCharsets.UTF_8);
		} else if(ExportFormatType.JSON.equals(this)) {
			return new ExporterJsonFormat(stream, StandardCharsets.UTF_8);
		} else {
			throw new IllegalStateException(); // Ouch we forgot a format !
		}
	}
}
