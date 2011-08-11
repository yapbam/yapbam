package net.yapbam.gui.dialogs.export;

import java.io.ObjectStreamException;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ImporterParameters implements Serializable {
	//See comment about serial uid in ExporterParameters
	private char separator;
	private String separatorString;
	private int ignoredLeadingLines;
	private int[] importedFileColumns;
	
	//FIXME ATTENTION, si on rajoute une colonne et qu'on récupère une vieille sérialisation, on obtient une instance en vrac.

	public ImporterParameters(char separator, int ignoredLeadingLines, int[] importedFileColumns) {
		super();
		if (importedFileColumns.length!=ExportTableModel.columns.length) throw new IllegalArgumentException();
		this.separator = separator;
		this.ignoredLeadingLines = ignoredLeadingLines;
		this.importedFileColumns = importedFileColumns;
	}
	
	Object readResolve() throws ObjectStreamException {
		// Some Yapbam version may add some attributes to a transaction
		if (this.importedFileColumns.length==ExportTableModel.columns.length) return this;
		if (this.importedFileColumns.length==9)  {
			// The comment column has been added
			int[] array = new int[ExportTableModel.columns.length];
			System.arraycopy(this.importedFileColumns, 0, array, 0, ExportTableModel.COMMENT_INDEX);
			array[ExportTableModel.COMMENT_INDEX] = -1;
			System.arraycopy(this.importedFileColumns, ExportTableModel.AMOUNT_INDEX-1, array, ExportTableModel.AMOUNT_INDEX, array.length-ExportTableModel.AMOUNT_INDEX);
			this.importedFileColumns = array;
		} else {
			throw new ObjectStreamException() {};
		}
		return this;
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
		this.separatorString = null;
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

	public String getSeparatorString() {
		if (separatorString==null) {
			separatorString = new String(new char[]{separator});
		}
		return separatorString;
	}
}