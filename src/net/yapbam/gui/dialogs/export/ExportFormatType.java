package net.yapbam.gui.dialogs.export;

public enum ExportFormatType {

	HTML("Hyper Text Markup Language", "html"), //
	CSV("Comma Separated Values", "csv");

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
