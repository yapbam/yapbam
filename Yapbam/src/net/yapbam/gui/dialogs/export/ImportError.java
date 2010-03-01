package net.yapbam.gui.dialogs.export;

public class ImportError {
	private int lineNumber;
	private String[] fields;
	
	public ImportError(int lineNumber, String[] fields) {
		super();
		this.lineNumber = lineNumber;
		this.fields = fields;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public String[] getFields() {
		return fields;
	}
}
