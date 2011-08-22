package net.yapbam.gui.statementview;

import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.transactiontable.DescriptionSettings;

class StatementTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private Transaction[] transactions;
	
	StatementTableModel(JTable table, Transaction[] transactions) {
		super();
		this.transactions = transactions;
	}

	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		if (columnIndex==1) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		if (columnIndex==2) return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		if (columnIndex==3) return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
		if (columnIndex==4) return LocalizationData.get("Transaction.number"); //$NON-NLS-1$
		if (columnIndex==5) return LocalizationData.get("Transaction.valueDate"); //$NON-NLS-1$
		if (columnIndex==6) return LocalizationData.get("StatementView.debt"); //$NON-NLS-1$
		if (columnIndex==7) return LocalizationData.get("StatementView.receipt"); //$NON-NLS-1$
		return "?"; //$NON-NLS-1$
	}

	public int getRowCount() {
		return transactions.length;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if ((columnIndex==0)||(columnIndex==5)) return Date.class;
		return super.getColumnClass(columnIndex);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex==0) return transactions[rowIndex].getDate();
		else if (columnIndex==1) return DescriptionSettings.getMergedDescriptionAndComment(transactions[rowIndex]);
		else if (columnIndex==2) return transactions[rowIndex].getCategory().getName();
		else if (columnIndex==3) return transactions[rowIndex].getMode().getName();
		else if (columnIndex==4) return transactions[rowIndex].getNumber();
		else if (columnIndex==5) return transactions[rowIndex].getValueDate();
		else if (columnIndex==6) return transactions[rowIndex].getAmount()<0?-transactions[rowIndex].getAmount():null;
		else if (columnIndex==7) return transactions[rowIndex].getAmount()>=0?transactions[rowIndex].getAmount():null;
		return null;
	}

	public void setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
		this.fireTableDataChanged();
	}

	public Transaction[] getTransactions() {
		return this.transactions;
	}
}
