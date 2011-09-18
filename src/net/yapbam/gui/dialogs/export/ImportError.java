package net.yapbam.gui.dialogs.export;

import java.util.Arrays;

public class ImportError {
	private int lineNumber;
	private String[] fields;
	private boolean[] errors;
	
	/** Constructor.
	 * @param lineNumber The number of the erroneous line
	 * @param fields the fields of the line
	 * @param errors an array of boolean which indicates valid/invalid field (true means invalid).
	 * Be aware that if the line contains extra fields that are not used in the importation, there can be more elements in fields than in errors. 
	 */
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

	/** Tests whether a field is erroneous or not.
	 * @param index the field index
	 * @return true if the field caused an error.
	 */
	public boolean hasError(int index) {
		return index<errors.length && errors[index];
	}
}
