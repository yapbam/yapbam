package net.yapbam.gui.dialogs.export;

import javax.swing.table.AbstractTableModel;

import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
class ExportTableModel extends AbstractTableModel {
	static final int ACCOUNT_INDEX = 0;
	static final int DATE_INDEX = 1;
	static final int DESCRIPTION_INDEX = 2;
	static final int AMOUNT_INDEX = 3;
	static final int CATEGORY_INDEX = 4;
	static final int MODE_INDEX = 5;
	static final int NUMBER_INDEX = 6;
	static final int VALUE_DATE_INDEX = 7;
	static final int STATEMENT_INDEX = 8;
	
	static final String[] columns = new String[] {LocalizationData.get("Transaction.account"), LocalizationData.get("Transaction.date"),
		LocalizationData.get("Transaction.description"), LocalizationData.get("Transaction.amount"), LocalizationData.get("Transaction.category"),
		LocalizationData.get("Transaction.mode"), LocalizationData.get("Transaction.number"), LocalizationData.get("Transaction.valueDate"),
		LocalizationData.get("Transaction.statement")};
	
	private boolean[] allowed;

	ExportTableModel() {
		super();
		this.allowed = new boolean[columns.length];
		for (int i = 0; i < allowed.length; i++) {
			this.allowed[i] = true;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Boolean.class;
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return allowed[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		allowed[columnIndex] = (Boolean)aValue;
		this.fireTableCellUpdated(rowIndex, columnIndex);
	}
}
