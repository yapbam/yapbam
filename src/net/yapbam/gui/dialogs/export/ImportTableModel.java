package net.yapbam.gui.dialogs.export;

import javax.swing.table.AbstractTableModel;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class ImportTableModel extends AbstractTableModel {
	private boolean[] linked = new boolean[ExportTableModel.columns.length];
	private String[] values = new String[ExportTableModel.columns.length];
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return ExportTableModel.columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) {
			return ExportTableModel.columns[rowIndex];
		} else if (columnIndex==1) {
			return linked[rowIndex];
		} else {
			return values[rowIndex]==null?"-":values[rowIndex];
		}
	}

	@Override
	public String getColumnName(int column) {
		if (column==0) return LocalizationData.get("ImportDialog.YapbamFields");
		else if (column==1) return LocalizationData.get("ImportDialog.linkedTo");
		else return LocalizationData.get("ImportDialog.importedFields");
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex!=0;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex==1) this.linked[rowIndex] = (Boolean) aValue;
		if (columnIndex==2) this.values[rowIndex] = (String) aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==1) return Boolean.class;
		return super.getColumnClass(columnIndex);
	}
}
