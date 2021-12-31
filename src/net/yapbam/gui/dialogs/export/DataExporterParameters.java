package net.yapbam.gui.dialogs.export;

import java.io.Serializable;

import net.yapbam.util.ArrayUtils;

public class DataExporterParameters implements Serializable {
	private static final long serialVersionUID = 3L;
	// WARNING. If we change the attribute list or their names and forget to change the serial uid,
	// we could have an inconsistent parameter instance that could cause crashes.
	// Changing serialVersionUID would just lead to ignore saved configuration. 
	private int[] viewIndexesToModel;
	private boolean[] selectedModelColumns;
	private boolean insertHeader;
	private boolean exportInitialBalance;
	private boolean exportFinalBalance;
	private boolean exportFilteredData;

	private int[] exportedIndexes;

	public DataExporterParameters() {
		this(ArrayUtils.buildIntArray(ExportTableModel.COLUMNS.length, 0, 1),
				ArrayUtils.buildBooleanArray(ExportTableModel.COLUMNS.length, true), true, true, true, true);
	}
	
	public DataExporterParameters(int[] viewindexesToModel, boolean[] selectedModelColumns, boolean insertHeader,
			boolean exportInitialBalance, boolean exportFinalBalance, boolean exportFilteredData) {
		this.viewIndexesToModel = viewindexesToModel;
		this.selectedModelColumns = selectedModelColumns;
		this.insertHeader = insertHeader;
		this.exportInitialBalance = exportInitialBalance;
		this.exportFinalBalance = exportFinalBalance;
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

	public boolean isExportInitialBalance() {
		return exportInitialBalance;
	}

	public boolean isExportFinalBalance() {
		return exportFinalBalance;
	}

	public boolean isExportFilteredData() {
		return exportFilteredData;
	}

	public void setExportFilteredData(boolean exportFilteredData) {
		this.exportFilteredData = exportFilteredData;
	}
}