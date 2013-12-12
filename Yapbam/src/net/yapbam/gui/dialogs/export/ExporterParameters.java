package net.yapbam.gui.dialogs.export;

import java.io.Serializable;

import net.yapbam.util.ArrayUtils;

@SuppressWarnings("serial")
public class ExporterParameters implements Serializable {
	// There's no default serial uid, because if this class changes, the easiest way to prevent problems
	// is to do nothing. Deserialization of saved parameters will fail and they will be ignored. If we
	// change the class and forget to change the serial uid, we could have an inconsistent parameter instance
	// that could cause crashes.
	private int[] viewIndexesToModel;
	private boolean[] selectedModelColumns;
	private boolean insertHeader;
	private char separator;
	private boolean exportInitialBalance;
	private boolean exportFilteredData;
	
	private int[] exportedIndexes;

	public ExporterParameters() {
		this(ArrayUtils.buildIntArray(ExportTableModel.COLUMNS.length, 0, 1), ArrayUtils.buildBooleanArray(ExportTableModel.COLUMNS.length, true),
				true, ';', true, true);
	}
	
	public ExporterParameters(int[] viewindexesToModel, boolean[] selectedModelColumns, boolean insertHeader, char separator,
			boolean exportInitialBalance, boolean exportFilteredData) {
		super();
		this.viewIndexesToModel = viewindexesToModel;
		this.selectedModelColumns = selectedModelColumns;
		this.insertHeader = insertHeader;
		this.separator = separator;
		this.exportInitialBalance = exportInitialBalance;
		this.exportFilteredData = exportFilteredData;
		int nbSelected = 0;
		for (boolean b : selectedModelColumns) {
			if (b) {
				nbSelected++;
			}
		}
		int i = 0;
		this.exportedIndexes = new int[nbSelected];
		for (int j=0; j<viewindexesToModel.length; j++) {
			if (selectedModelColumns[viewindexesToModel[j]]) {
				this.exportedIndexes[i] = viewindexesToModel[j];
				i++;
			}
		}
	}

	public int[] getExportedIndexes() {
		return exportedIndexes;
	}

	public int[] getViewIndexesToModel() {
		return viewIndexesToModel;
	}

	public boolean[] getSelectedModelColumns() {
		return selectedModelColumns;
	}

	public boolean isInsertHeader() {
		return insertHeader;
	}

	public char getSeparator() {
		return separator;
	}

	public boolean isExportInitialBalance() {
		return exportInitialBalance;
	}

	public boolean isExportFilteredData() {
		return exportFilteredData;
	}

	public void setExportFilteredData(boolean exportFilteredData) {
		this.exportFilteredData = exportFilteredData;
	}
}