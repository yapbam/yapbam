package net.yapbam.gui.dialogs.export;

public class BadImportFileException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	BadImportFileException(String message) {
		super(message);
	}
}
