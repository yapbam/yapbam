package net.yapbam.gui.dialogs.periodicaltransaction;

import java.util.Date;

import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.transactiontable.GenericTransactionTableModel;
import net.yapbam.gui.transactiontable.TransactionTableUtils;

@SuppressWarnings("serial")
class GenerateTableModel extends GenericTransactionTableModel<Transaction> {
	static final int ACCOUNT_INDEX = 0;
	static final int DESCRIPTION_INDEX = 1;
	static final int DATE_INDEX = 2;
	static final int AMOUNT_INDEX = 3;
	static final int CANCELLED_INDEX = 4;
	static final int POSTPONED_INDEX = 5;
	
	private Generator generator; 
	
	GenerateTableModel(GlobalData data) {
		this.generator = new Generator(data);
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==ACCOUNT_INDEX) {
			return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		} else if (columnIndex==DESCRIPTION_INDEX) {
			return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		} else if (columnIndex==DATE_INDEX) {
			return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		} else if (columnIndex==AMOUNT_INDEX) {
			return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		} else if (columnIndex==CANCELLED_INDEX) {
			return LocalizationData.get("GeneratePeriodicalTransactionsDialog.cancelled"); //$NON-NLS-1$
		} else if (columnIndex==POSTPONED_INDEX) {
			return LocalizationData.get("GeneratePeriodicalTransactionsDialog.postponed"); //$NON-NLS-1$
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==DATE_INDEX) {
			return Date.class;
		} else if (columnIndex==AMOUNT_INDEX) {
			return double[].class;
		} else if (columnIndex==CANCELLED_INDEX || columnIndex==POSTPONED_INDEX) {
			return Boolean.class;
		} else {
			return String.class;
		}
	}

	@Override
	public int getRowCount() {
		return generator.getNbTransactions();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Transaction t = generator.getTransaction(rowIndex);
		if (columnIndex==ACCOUNT_INDEX) {
			return t.getAccount().getName();
		} else if (columnIndex==DESCRIPTION_INDEX) {
			return TransactionTableUtils.getDescription(t, false, true, false);
		} else if (columnIndex==DATE_INDEX) {
			return t.getDate();
		} else if (columnIndex==AMOUNT_INDEX) {
			return new double[]{t.getAmount()};
		} else if (columnIndex==CANCELLED_INDEX) {
			return generator.isCancelled(rowIndex);
		} else if (columnIndex==POSTPONED_INDEX) {
			return generator.isPostponed(rowIndex); 
		}
		throw new IllegalArgumentException();
	}

	/** Tests whether a transaction should be generated.
	 * <br>It should be if it is not cancelled nor postponed
	 * @param rowIndex The transaction's index in the model
	 * @return true if the transaction must be generated
	 */
	public boolean isValid(int rowIndex) {
		return !(this.generator.isCancelled(rowIndex) || this.generator.isPostponed(rowIndex));
	}

	/** Tests whether a transaction is postponed.
	 * @param rowIndex The transaction's index in the model
	 * @return true if the transaction is postponed
	 */
	public boolean isPostponed(int rowIndex) {
		return this.generator.isPostponed(rowIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex==CANCELLED_INDEX || columnIndex==POSTPONED_INDEX;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex==CANCELLED_INDEX) {
			this.generator.setCancelled(rowIndex, !generator.isCancelled(rowIndex));
			this.fireTableRowsUpdated(rowIndex, rowIndex);
		} else if (columnIndex==POSTPONED_INDEX) {
			this.generator.setPostponed(rowIndex, !generator.isPostponed(rowIndex));
			this.fireTableRowsUpdated(rowIndex, rowIndex);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void setTransaction(int row, Transaction transaction) {
		this.generator.setTransaction(row, transaction);
		this.fireTableRowsUpdated(row, row);	
	}

	@Override
	public Transaction getTransaction(int rowIndex) {
		return generator.getTransaction(rowIndex);
	}
	
	/** Sets the date.
	 * @param date The new date
	 * @return true if the date change adds or removes transactions to the transaction list
	 */
	public boolean setDate(Date date) {
		if (NullUtils.areEquals(date, this.generator.getDate())) {
			return false;
		}
		boolean result = generator.setDate(date);
		if (result) {
			fireTableDataChanged();
		}
		return result;
	}

	public Date getDate() {
		return this.generator.getDate();
	}

	/** Tests whether the actual user selection has an impact on the data.
	 * <br>An impact means there is some periodical transactions's "next date" that will be changed.  
	 * @return true if there is an impact
	 */
	boolean hasImpact() {
		int nb = getRowCount();
		if (nb==0) {
			return false;
		}
		for (int i = 0; i < nb; i++) {
			if (!generator.isPostponed(i)) {
				return true;
			}
		}
		return false;
	}

	/** Gets the postponed date of a periodical transaction.
	 * @param indexPeriodical The index of the periodical transaction in the global data.
	 * @return The postponed date, or null if the transaction is not postponed
	 */
	Date getPostponedDate(int indexPeriodical) {
		return generator.getPostponedDate(indexPeriodical);
	}
}