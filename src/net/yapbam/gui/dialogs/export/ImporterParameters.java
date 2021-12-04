package net.yapbam.gui.dialogs.export;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.DateFormat;

public class ImporterParameters implements Serializable {
	private static final long serialVersionUID = 560758261413517776L;
	// WARNING. If we change the attribute list or their names and forget to change the serial uid,
	// we could have an inconsistent parameter instance that could cause crashes.
	// Changing serialVersionUID would just lead to ignore saved configuration. 
	private char separator;
	private int ignoredLeadingLines;
	private int[] importedFileColumns;
	private char decimalSeparator;
	// Warning making dateFormat not transient will make this class impossible to save in preferences => Yapbam would hang during import
	private transient DateFormat dateFormat;

    public ImporterParameters(char columnSeparator, char decimalSeparator, DateFormat dateFormat, int ignoredLeadingLines, int[] importedFileColumns) {
		super();
		if (importedFileColumns.length!=ExportTableModel.COLUMNS.length) {
			throw new IllegalArgumentException();
		}
		this.separator = columnSeparator;
		this.dateFormat = dateFormat;
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
			throw new ObjectStreamException() {
				private static final long serialVersionUID = 1L;
			};
		}
		return this;
	}
	
	public DateFormat getDateFormatter() {
		return dateFormat;
	}
	
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public char getColumnSeparator() {
		return separator;
	}

	public void setColumnSeparator(char columnSeparator) {
		this.separator = columnSeparator;
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

    public char getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(char decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

	public Charset getEncoding() {
		return Charset.defaultCharset();
	}
}