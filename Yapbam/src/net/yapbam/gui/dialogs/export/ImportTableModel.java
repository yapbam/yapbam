package net.yapbam.gui.dialogs.export;

import javax.swing.table.AbstractTableModel;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
class ImportTableModel extends AbstractTableModel {

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return ExportTableModel.columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) return ExportTableModel.columns[rowIndex];
		return "?";
	}

	@Override
	public String getColumnName(int column) {
		if (column==0) return LocalizationData.get("ImportDialog.YapbamFields");
		return LocalizationData.get("ImportDialog.importedFields");
	}
}
