package net.yapbam.gui.dialogs;

import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.transactiontable.GenericTransactionTableModel;

@SuppressWarnings("serial")
class GenerateTableModel extends AbstractTableModel implements GenericTransactionTableModel {
	private Transaction[] transactions;
	private boolean[] enabled;
	
	GenerateTableModel() {
		this.transactions = new Transaction[0];
		this.enabled = new boolean[0];
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (columnIndex==1) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		if (columnIndex==2) return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		if (columnIndex==3) return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		if (columnIndex==4) return LocalizationData.get("GeneratePeriodicalTransactionsDialog.ignored"); //$NON-NLS-1$
		throw new IllegalArgumentException();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==2) return Date.class;
		if (columnIndex==3) return double[].class;
		if (columnIndex==4) return Boolean.class;
		return String.class;
	}

	@Override
	public int getAlignment(int column) {
		if (column==3) return SwingConstants.RIGHT;
    	if ((column==0) || (column==1)) return SwingConstants.LEFT;
    	else return SwingConstants.CENTER;
	}

	@Override
	public int getRowCount() {
		return transactions.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Transaction t = transactions[rowIndex];
		if (columnIndex==0) return t.getAccount().getName();
		if (columnIndex==1) return t.getDescription();
		if (columnIndex==2) return t.getDate();
		if (columnIndex==3) return new double[]{t.getAmount()};
		if (columnIndex==4) return enabled[rowIndex];
		throw new IllegalArgumentException();
	}

	@Override
	public boolean isChecked(int row) {
		return false;
	}

	@Override
	public boolean isExpense(int row) {
		return transactions[row].getAmount()<0;
	}

	public void setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
		boolean[] enabled = new boolean[transactions.length];
		for (int i = 0; i < enabled.length; i++) {
			enabled[i] = true;
		}
		this.enabled = enabled;
		fireTableDataChanged();
	}

	public Transaction[] getTransactions() {
		return this.transactions;
	}

	public boolean isValid(int i) {
		return this.enabled[i];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex==4;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.enabled[rowIndex] = !this.enabled[rowIndex];
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}

	public void setTransaction(int row, Transaction transaction) {
		this.transactions[row] = transaction;
		this.fireTableRowsUpdated(row, row);	
	}
}