package net.yapbam.gui.statementview;

import java.util.Date;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.Category;
import net.yapbam.data.Mode;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.transactiontable.TransactionTableUtils;

class StatementTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	static final int DESCRIPTION_COLUMN = 1;

	private transient Transaction[] transactions;
	
	StatementTableModel(Transaction[] transactions) {
		super();
		if (transactions==null) {
			throw new NullPointerException();
		}
		this.transactions = transactions;
	}

	public int getColumnCount() {
		return 8;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) {
			return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		} else if (columnIndex==DESCRIPTION_COLUMN) {
			return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		} else if (columnIndex==2) {
			return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		} else if (columnIndex==3) {
			return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
		} else if (columnIndex==4) {
			return LocalizationData.get("Transaction.number"); //$NON-NLS-1$
		} else if (columnIndex==5) {
			return LocalizationData.get("Transaction.valueDate"); //$NON-NLS-1$
		} else if (columnIndex==6) {
			return LocalizationData.get("StatementView.debt"); //$NON-NLS-1$
		} else if (columnIndex==7) {
			return LocalizationData.get("StatementView.receipt"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	public int getRowCount() {
		return transactions.length;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if ((columnIndex==0)||(columnIndex==5)) {
			return Date.class;
		} else if ((columnIndex==6)||(columnIndex==7)) {
			return Double.class;
		} else {
			return super.getColumnClass(columnIndex);
		}
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Transaction transaction = transactions[rowIndex];
		if (columnIndex==0) {
			return transaction.getDate();
		} else if (columnIndex==DESCRIPTION_COLUMN) {
			return TransactionTableUtils.getDescription(transaction,false,true,false);
		} else if (columnIndex==2) {
			Category category = transaction.getCategory();
			return category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName(); //$NON-NLS-1$
		} else if (columnIndex==3) {
			Mode mode = transaction.getMode();
			return mode.equals(Mode.UNDEFINED)?LocalizationData.get("Mode.undefined"):mode.getName(); //$NON-NLS-1$
		} else if (columnIndex==4) {
			return transaction.getNumber();
		} else if (columnIndex==5) {
			return transaction.getValueDate();
		} else if (columnIndex==6) {
			return transaction.getAmount()<0?-transaction.getAmount():null;
		} else if (columnIndex==7) {
			return transaction.getAmount()>=0?transaction.getAmount():null;
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// Cells that allow to click on HTML links should be editable
		return (columnIndex==1) && columnIndex>=0;
	}

	public void setTransactions(Transaction[] transactions) {
		if (transactions==null) {
			throw new NullPointerException();
		}
		this.transactions = transactions;
		this.fireTableDataChanged();
	}

	public Transaction[] getTransactions() {
		return this.transactions;
	}

	public int find(Transaction transaction) {
		for (int i = 0; i < this.transactions.length; i++) {
			if (transactions[i].getId()==transaction.getId()) {
				return i;
			}
		}
		return -1;
	}
}
