package net.yapbam.export;

import java.io.OutputStream;
import java.nio.charset.Charset;

import net.yapbam.gui.dialogs.export.ExporterParameters;

public enum ExportFormatType {
	HTML("HyperText Markup Language", "html"),
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

	public ExportWriter getTableExporter(OutputStream stream, ExporterParameters params) {
		if (ExportFormatType.CSV.equals(this)) {
			return new CsvFormatWriter(stream, params.getSeparator(), params.getPreferredEncoding());
		} else if (ExportFormatType.HTML.equals(this)) {
			return new HtmlFormatWriter(stream, Charset.forName("UTF-8"));
		} else if(ExportFormatType.JSON.equals(this)) {
			return new JsonFormatWriter(stream);
		} else {
			throw new IllegalStateException(); // Ouch we forgot a format !
		}
	}
}
