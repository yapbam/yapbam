package net.yapbam.gui.graphics.balancehistory;

import java.util.Date;

import javax.swing.table.AbstractTableModel;

import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.BalanceData;
import net.yapbam.data.Category;
import net.yapbam.data.Mode;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.transactiontable.TransactionTableSettings;

/** The transaction's table model. */
final class BalanceHistoryModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	static final int VALUE_DATE_COLUMN = 7;
	
	private BalanceData data;
	private Date endDate;
	private TransactionTableSettings settings;
	
	private int rowCount;

	/** Constructor. */
	public BalanceHistoryModel(BalanceData data) {
		super();
		this.settings = new TransactionTableSettings();
		this.data = data;
		this.endDate = null;
		this.rowCount = data.getBalanceHistory().getTransactionsNumber();
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				updateRowCount();
				fireTableDataChanged();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==VALUE_DATE_COLUMN) return Date.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Transaction transaction = getTransaction(rowIndex);
		if (columnIndex==0) return transaction.getAccount().getName();
		if (columnIndex==1) return transaction.getDate();
		if (columnIndex==2) return transaction.getDescription(!settings.isCommentSeparatedFromDescription());
		if (columnIndex==3) return transaction.getAmount();
		if (columnIndex==4) {
			Category category = transaction.getCategory();
			return category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName();
		}
		if (columnIndex==5) {
			Mode mode = transaction.getMode();
			return mode.equals(Mode.UNDEFINED)?LocalizationData.get("Mode.undefined"):mode.getName();
		}
		if (columnIndex==6) return transaction.getNumber();
		if (columnIndex==VALUE_DATE_COLUMN) return transaction.getValueDate();
		if (columnIndex==8) return transaction.getStatement();
		if (columnIndex==9) return getRemaining(rowIndex);
		if (columnIndex==10) return transaction.getComment();
		return "?"; //$NON-NLS-1$
	}

	/** Gets the remaining amount after a transaction.
	 * @param rowIndex The transaction's row index
	 * @return a Double
	 */
	private Double getRemaining(int rowIndex) {
		Date valueDate = getTransaction(rowIndex).getValueDate();
		double balance = data.getBalanceHistory().getBalance(valueDate);
		for (int i=rowIndex+1;i<getRowCount();i++) {
			Transaction transaction = getTransaction(i);
			if (transaction.getValueDate().equals(valueDate)) {
				balance = balance - transaction.getAmount();
			} else break;
		}
		return balance;
	}

	/** Gets the transaction corresponding to a table row.
	 * @param rowIndex The transaction's row index
	 * @return a Transaction
	 */
	public Transaction getTransaction(int rowIndex) {
		return data.getBalanceHistory().getTransaction(rowIndex);
	}

	@Override
	public int getRowCount() {
		return this.rowCount;
	}

	@Override
	public int getColumnCount() {
		return settings.isCommentSeparatedFromDescription()?11:10;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (columnIndex==1) return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		if (columnIndex==2) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		if (columnIndex==3) return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		if (columnIndex==4) return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		if (columnIndex==5) return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
		if (columnIndex==6) return LocalizationData.get("Transaction.number"); //$NON-NLS-1$
		if (columnIndex==7) return LocalizationData.get("Transaction.valueDate"); //$NON-NLS-1$
		if (columnIndex==8) return LocalizationData.get("Transaction.statement"); //$NON-NLS-1$
		if (columnIndex==9) return LocalizationData.get("BalanceHistory.transaction.remainingBalance"); //$NON-NLS-1$
		if (columnIndex==10) return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
		return "?"; //$NON-NLS-1$
	}

	public int find(Transaction transaction) {
		return data.getBalanceHistory().find(transaction);
	}

	public void setEndDate(Date date) {
		if (!NullUtils.areEquals(date, this.endDate)) {
			this.endDate = date;
			updateRowCount();
			this.fireTableDataChanged();
		}
	}

	private void updateRowCount() {
		if (this.endDate==null) {
			this.rowCount = data.getBalanceHistory().getTransactionsNumber();
		} else {
			this.rowCount = 0;
			for (int i = 0; i < data.getBalanceHistory().getTransactionsNumber(); i++) {
				if (data.getBalanceHistory().getTransaction(i).getValueDate().after(this.endDate)) break;
				this.rowCount++;
			}
		}
	}
}