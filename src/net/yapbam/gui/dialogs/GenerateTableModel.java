package net.yapbam.gui.dialogs;

import java.util.Date;
import java.util.List;

import javax.swing.SwingConstants;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.transactiontable.DescriptionSettings;
import net.yapbam.gui.transactiontable.GenericTransactionTableModel;

@SuppressWarnings("serial")
class GenerateTableModel extends GenericTransactionTableModel {
	private static final int ACCOUNT_INDEX = 0;
	private static final int DESCRIPTION_INDEX = 1;
	private static final int DATE_INDEX = 2;
	private static final int AMOUNT_INDEX = 3;
	private static final int CANCELLED_INDEX = 4;
	private static final int POSTPONED_INDEX = 5;
	
	private GlobalData data;
	private Date date; 
	private Transaction[] transactions;
	private boolean[] cancelled;
	private boolean[] postponed;
	
	GenerateTableModel(GlobalData data) {
		this.data = data;
		this.date = null;
		this.transactions = new Transaction[0];
		this.cancelled = new boolean[0];
		this.postponed = new boolean[0];
	}

	@Override
	public int getColumnCount() {
		//TODO
		// A postponed column is scheduled, but it need some rewrite in PeriodicalTransactionGeneratorPanel
		// For now, we will hide the "postponed" column
		// The problem is due to the periodical transaction concept. When a transaction is postponed, it is 
		// the periodical transaction that is postponed, not the transaction. So we need to ensure that all transactions
		// after a postponed transaction and from the same periodical transaction are postponed to.
		// Probably not so easy to design an understandable interface.
		return 6;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==ACCOUNT_INDEX) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (columnIndex==DESCRIPTION_INDEX) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		if (columnIndex==DATE_INDEX) return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		if (columnIndex==AMOUNT_INDEX) return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		if (columnIndex==CANCELLED_INDEX) return LocalizationData.get("GeneratePeriodicalTransactionsDialog.cancelled"); //$NON-NLS-1$
		if (columnIndex==POSTPONED_INDEX) return LocalizationData.get("GeneratePeriodicalTransactionsDialog.postponed"); //$NON-NLS-1$
		throw new IllegalArgumentException();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==DATE_INDEX) return Date.class;
		if (columnIndex==AMOUNT_INDEX) return double[].class;
		if (columnIndex==CANCELLED_INDEX || columnIndex==POSTPONED_INDEX) return Boolean.class;
		return String.class;
	}

	@Override
	public int getAlignment(int column) {
		if (column==AMOUNT_INDEX) return SwingConstants.RIGHT;
    	if ((column==ACCOUNT_INDEX) || (column==DESCRIPTION_INDEX)) return SwingConstants.LEFT;
    	else return SwingConstants.CENTER;
	}

	@Override
	public int getRowCount() {
		return transactions.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Transaction t = transactions[rowIndex];
		if (columnIndex==ACCOUNT_INDEX) return t.getAccount().getName();
		if (columnIndex==DESCRIPTION_INDEX) return DescriptionSettings.getMergedDescriptionAndComment(t);
		if (columnIndex==DATE_INDEX) return t.getDate();
		if (columnIndex==AMOUNT_INDEX) return new double[]{t.getAmount()};
		if (columnIndex==CANCELLED_INDEX) return cancelled[rowIndex];
		if (columnIndex==POSTPONED_INDEX) return postponed[rowIndex];
		throw new IllegalArgumentException();
	}

	private void setTransactions(Transaction[] transactions) {
		this.transactions = transactions;
		this.cancelled = new boolean[transactions.length];
		this.postponed = new boolean[transactions.length];
		for (int i = 0; i < transactions.length; i++) {
			this.cancelled[i] = false;
			this.postponed[i] = false;
		}
		fireTableDataChanged();
	}

	public Transaction[] getTransactions() {
		return this.transactions;
	}

	/** Tests whether a transaction should be generated.
	 * <br>It should be if it is not cancelled nor postponed
	 * @param rowIndex The transaction's index in the model
	 * @return true if the transaction must be generated
	 */
	public boolean isValid(int rowIndex) {
		return !(this.cancelled[rowIndex] || this.postponed[rowIndex]);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex==CANCELLED_INDEX || columnIndex==POSTPONED_INDEX;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex==CANCELLED_INDEX) {
			this.cancelled[rowIndex] = !this.cancelled[rowIndex];
			this.postponed[rowIndex] = false;
		}
		if (columnIndex==POSTPONED_INDEX) {
			this.postponed[rowIndex] = !this.postponed[rowIndex];
			this.cancelled[rowIndex] = false;
		}
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}

	public void setTransaction(int row, Transaction transaction) {
		this.transactions[row] = transaction;
		this.fireTableRowsUpdated(row, row);	
	}

	@Override
	protected AbstractTransaction getTransaction(int rowIndex) {
		return this.transactions[rowIndex];
	}
	
	/** Sets the date.
	 * @param date The new date
	 * @return true if the date change adds or removes transactions to the transaction list
	 */
	public boolean setDate(Date date) {
		if (NullUtils.areEquals(date, this.date)) return false;
		//TODO Here is a simplified version of the generation
		// As transaction can be edited after this method is called, we should not change transactions that were already there before
		// the date change (or modification would be lost).
		// We also need to take in account that some transactions may be postponed
		// The best way to have a user friendly behavior seems to be to never forget a generated transaction. When the date is set to null,
		// or changed to a previous date, we should keep the previous transactions, but simply mark them "not used".
		// To achieve that, we will sort the transaction by date and remember how much transactions are available accordingly with the new date. 
		this.date = date;
		if (date==null) {
			setTransactions (new Transaction[0]);
		} else {
			setTransactions (generateTransactionsFromPeriodicals());
		}
		//TODO end
		return true;
	}
	
	/** Generate transactions from the periodical transactions until a date.
	 * The transactions are not added to the global data and the periodical transactions
	 * are not changed : their next date fields remains unchanged.
	 * @return a transaction array.
	 */
	private Transaction[] generateTransactionsFromPeriodicals() {
		List<Transaction> result = null;
		for (int i=0; i<data.getPeriodicalTransactionsNumber(); i++) {
			result = data.getPeriodicalTransaction(i).generate(date, result);
		}
		return result.toArray(new Transaction[result.size()]);
	}

	public Date getDate() {
		return this.date;
	}
}