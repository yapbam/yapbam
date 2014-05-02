package net.yapbam.gui.dialogs.export;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class ImporterParameters implements Serializable {
	private static final long serialVersionUID = 560758261413517776L;

	private char columnSeparator;
	private int ignoredLeadingLines;
	private int[] importedFileColumns;
    private Character decimalSeparator;

    public ImporterParameters(char columnSeparator, Character decimalSeparator, int ignoredLeadingLines, int[] importedFileColumns) {
		super();
		if (importedFileColumns.length!=ExportTableModel.COLUMNS.length) {
			throw new IllegalArgumentException();
		}
		this.columnSeparator = columnSeparator;
        this.decimalSeparator = decimalSeparator;
		this.ignoredLeadingLines = ignoredLeadingLines;
		this.importedFileColumns = importedFileColumns;
	}
	
	Object readResolve() throws ObjectStreamException {
		// Some Yapbam version may add some attributes to a transaction
		if (this.importedFileColumns.length==ExportTableModel.COLUMNS.length) {
			return this;
		}
		if (this.importedFileColumns.length==9)  {
			// The comment column has been added
			int[] array = new int[ExportTableModel.COLUMNS.length];
			System.arraycopy(this.importedFileColumns, 0, array, 0, ExportTableModel.COMMENT_INDEX);
			array[ExportTableModel.COMMENT_INDEX] = -1;
			System.arraycopy(this.importedFileColumns, ExportTableModel.AMOUNT_INDEX-1, array, ExportTableModel.AMOUNT_INDEX, array.length-ExportTableModel.AMOUNT_INDEX);
			this.importedFileColumns = array;
		} else {
			throw new ObjectStreamException() {private static final long serialVersionUID = 1L;};
		}
		return this;
	}

	public char getColumnSeparator() {
		return columnSeparator;
	}

	public void setColumnSeparator(char columnSeparator) {
		this.columnSeparator = columnSeparator;
	}

	public int getIgnoredLeadingLines() {
		return ignoredLeadingLines;
	}

	public void setIgnoredLeadingLines(int ignoredLeadingLines) {
		this.ignoredLeadingLines = ignoredLeadingLines;
	}

	public int[] getImportedFileColumns() {
		return importedFileColumns;
	}

	public void setImportedFilecolumns(int[] importedFilecolumns) {
		this.importedFileColumns = importedFilecolumns;
	}

    public Character getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(Character decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }
}