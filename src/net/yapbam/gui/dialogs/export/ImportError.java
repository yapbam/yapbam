package net.yapbam.gui.dialogs.export;

public class ImportError {
	private int lineNumber;
	private String line;
	
	public ImportError(int lineNumber, String line) {
		super();
		this.lineNumber = lineNumber;
		this.line = line;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public String getLine() {
		return line;
	}
}
