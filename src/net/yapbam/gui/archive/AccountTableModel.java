package net.yapbam.gui.archive;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.GlobalData;

@SuppressWarnings("serial")
public class AccountTableModel extends AbstractTableModel implements TableModel {
	private GlobalData data;
	private boolean[] selected;

	public AccountTableModel(GlobalData data) {
		this.data = data;
		this.selected = new boolean[data.getAccountsNumber()];
		for (int i = 0; i < selected.length; i++) {
			this.selected[i] = true;
		}
	}

	@Override
	public int getRowCount() {
		return this.data.getAccountsNumber();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) return data.getAccount(rowIndex).getName();
		if (columnIndex==1) return selected[rowIndex];
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==1) return Boolean.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex==1;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.selected[rowIndex] = ((Boolean)aValue);
		this.fireTableCellUpdated(rowIndex, columnIndex);
	}
}
