package net.yapbam.gui.dialogs.export;

import java.util.Arrays;

public class ImportError {
	private int lineNumber;
	private String[] fields;
	private boolean[] errors;
	
	public ImportError(int lineNumber, String[] fields, boolean errors[]) {
		super();
		this.lineNumber = lineNumber;
		this.fields = fields;
		this.errors = errors;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public String[] getFields() {
		return fields;
	}

	@Override
	public String toString() {
		return lineNumber+Arrays.asList(fields).toString();
	}

	public boolean hasError(int index) {
		return errors[index];
	}
}
