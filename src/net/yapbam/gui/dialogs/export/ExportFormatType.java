package net.yapbam.gui.dialogs.export;

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

}
