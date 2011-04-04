package net.yapbam.gui.dialogs.export;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ImporterParameters implements Serializable {
	//See comment about serial uid in ExporterParameters
	private char separator;
	private String separatorString;
	private int ignoredLeadingLines;
	private int[] importedFileColumns;

	public ImporterParameters(char separator, int ignoredLeadingLines, int[] importedFileColumns) {
		super();
		this.separator = separator;
		this.ignoredLeadingLines = ignoredLeadingLines;
		this.importedFileColumns = importedFileColumns;
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