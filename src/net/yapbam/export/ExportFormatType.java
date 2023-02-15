package net.yapbam.export;

import java.io.OutputStream;

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

	public ExportWriter getTableExporter(OutputStream stream, ExporterParameters<?> params) {
		if (ExportFormatType.CSV.equals(this)) {
			return new CsvFormatWriter(stream, (CsvExportParameters)params.getFormatParams());
		} else if (ExportFormatType.HTML.equals(this)) {
			return new HtmlFormatWriter(stream, (HtmlExportParameters) params.getFormatParams());
		} else if(ExportFormatType.JSON.equals(this)) {
			return new JsonFormatWriter(stream);
		} else {
			throw new IllegalStateException(); // Ouch we forgot a format !
		}
	}
	
	public FormatParams getDefaultFormatParameters() {
		if (ExportFormatType.CSV.equals(this)) {
			return new CsvExportParameters();
		} else if (ExportFormatType.HTML.equals(this)) {
			return new HtmlExportParameters();
		} else if(ExportFormatType.JSON.equals(this)) {
			return new JsonExportParameters();
		} else {
			throw new IllegalStateException(); // Ouch we forgot a format !
		}
		
	}
}
