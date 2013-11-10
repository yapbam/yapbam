package net.yapbam.gui.archive;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class AccountTableModel extends AbstractTableModel implements TableModel {
	private static final int ACCOUNT_COLUMN = 0;
	private static final int EXPORT_COLUMN = 1;
	public static final int STATEMENT_COLUMN = 2;
	
	private GlobalData data;
	private boolean[] selected;
	private String[] statements;

	public AccountTableModel(GlobalData data) {
		this.data = data;
		this.selected = new boolean[data.getAccountsNumber()];
		for (int i = 0; i < selected.length; i++) {
			this.selected[i] = true;
		}
		this.statements = new String[this.selected.length];
	}

	@Override
	public int getRowCount() {
		return this.data.getAccountsNumber();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==ACCOUNT_COLUMN) return data.getAccount(rowIndex).getName();
		if (columnIndex==EXPORT_COLUMN) return selected[rowIndex];
		if (columnIndex==STATEMENT_COLUMN) {
			return (!selected[rowIndex] || (this.statements[rowIndex]==null))?"-":this.statements[rowIndex];
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==EXPORT_COLUMN) return Boolean.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex==EXPORT_COLUMN) || (columnIndex==STATEMENT_COLUMN);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex==EXPORT_COLUMN) {
			this.selected[rowIndex] = ((Boolean)aValue);
		} else if (columnIndex==STATEMENT_COLUMN) {
			this.statements[rowIndex] = (String) aValue;
		}
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}

	@Override
	public String getColumnName(int column) {
		if (column == ACCOUNT_COLUMN) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (column == EXPORT_COLUMN) return "Export";
		if (column == STATEMENT_COLUMN) return "Statement";
		return super.getColumnName(column);
	}
	
	public void setExported(int accountIndex, boolean exported) {
		setValueAt(Boolean.valueOf(exported), accountIndex, EXPORT_COLUMN);
	}
}
