package net.yapbam.gui.graphics.balancehistory;

import java.util.Date;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.BalanceData;
import net.yapbam.data.Category;
import net.yapbam.data.Mode;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.transactiontable.TransactionTableUtils;
import net.yapbam.gui.transactiontable.TransactionsModel;

/** The transaction's table model. */
final class BalanceHistoryModel extends AbstractTableModel implements TransactionsModel {
	private static final long serialVersionUID = 1L;
	
	private BalanceData data;
	private Date endDate;
	private TableSettings settings;
	private boolean hideIntermediateBalances;
	
	private int rowCount;

	/** Constructor. */
	public BalanceHistoryModel(BalanceData data) {
		super();
		this.settings = new TableSettings();
		this.data = data;
		this.endDate = null;
		this.hideIntermediateBalances = false;
		if (data==null) {
			this.rowCount = 0;
		} else {
			this.rowCount = data.getBalanceHistory().getTransactionsNumber();
			data.addListener(new DataListener() {
				@Override
				public void processEvent(DataEvent event) {
					updateRowCount();
					fireTableDataChanged();
				}
			});
		}
	}
	
	void setHideIntermediateBalances(boolean hide) {
		if (hide!=hideIntermediateBalances) {
			this.hideIntermediateBalances = hide;
			if (rowCount!=0) {
				fireTableChanged(new TableModelEvent(this, 0, rowCount-1, this.settings.getRemainingColumn(), TableModelEvent.UPDATE));
			}
		}
	}
	
	boolean getHideIntermediateBalances() {
		return hideIntermediateBalances;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==settings.getDateColumn() || columnIndex==settings.getValueDateColumn()) {
			return Date.class;
		} else {
			return super.getColumnClass(columnIndex);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Transaction transaction = getTransaction(rowIndex);
		if (columnIndex==settings.getAccountColumn()) {
			return transaction.getAccount().getName();
		} else if (columnIndex==settings.getDateColumn()) {
			return transaction.getDate();
		} else if (columnIndex==settings.getDescriptionColumn()) {
			return TransactionTableUtils.getDescription(transaction, false, !settings.isCommentSeparatedFromDescription(), false);
		} else if (columnIndex==settings.getAmountColumn()) {
			return transaction.getAmount();
		} else if (columnIndex==settings.getReceiptColumn()) {
			double[] expenseReceipt = TransactionTableUtils.getExpenseReceipt(transaction, false, false);
			return expenseReceipt==null?null:expenseReceipt[0];
		} else if (columnIndex==settings.getExpenseColumn()) {
			double[] expenseReceipt = TransactionTableUtils.getExpenseReceipt(transaction, false, true);
			return expenseReceipt==null?null:expenseReceipt[0];
		} else if (columnIndex==settings.getCategoryColumn()) {
			Category category = transaction.getCategory();
			return category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName(); //$NON-NLS-1$
		} else if (columnIndex==settings.getModeColumn()) {
			Mode mode = transaction.getMode();
			return mode.equals(Mode.UNDEFINED)?LocalizationData.get("Mode.undefined"):mode.getName(); //$NON-NLS-1$
		} else if (columnIndex==settings.getNumberColumn()) {
			return transaction.getNumber();
		} else if (columnIndex==settings.getValueDateColumn()) {
			return transaction.getValueDate();
		} else if (columnIndex==settings.getStatementColumn()) {
			return transaction.getStatement();
		} else if (columnIndex==settings.getRemainingColumn()) {
			return getRemaining(rowIndex);
		} else if (columnIndex==settings.getCommentColumn()) {
			return TransactionTableUtils.getComment(transaction);
		} else {
			return "?"; //$NON-NLS-1$
		}
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
			} else {
				break;
			}
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
		return settings.getColumnCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==settings.getAccountColumn()) {
			return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		} else if (columnIndex==settings.getDateColumn()) {
			return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		} else if (columnIndex==settings.getDescriptionColumn()) {
			return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		} else if (columnIndex==settings.getAmountColumn()) {
			return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		} else if (columnIndex==settings.getCategoryColumn()) {
			return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		} else if (columnIndex==settings.getModeColumn()) {
			return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
		} else if (columnIndex==settings.getNumberColumn()) {
			return LocalizationData.get("Transaction.number"); //$NON-NLS-1$
		} else if (columnIndex==settings.getValueDateColumn()) {
			return LocalizationData.get("Transaction.valueDate"); //$NON-NLS-1$
		} else if (columnIndex==settings.getStatementColumn()) {
			return LocalizationData.get("Transaction.statement"); //$NON-NLS-1$
		} else if (columnIndex==settings.getRemainingColumn()) {
			return LocalizationData.get("BalanceHistory.transaction.remainingBalance"); //$NON-NLS-1$
		} else if (columnIndex==settings.getCommentColumn()) {
			return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
		} else if (columnIndex==settings.getReceiptColumn()) {
			return LocalizationData.get("StatementView.receipt"); //$NON-NLS-1$
		} else if (columnIndex==settings.getExpenseColumn()) {
			return LocalizationData.get("StatementView.debt"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
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
				if (data.getBalanceHistory().getTransaction(i).getValueDate().after(this.endDate)) {
					break;
				}
				this.rowCount++;
			}
		}
	}

	TableSettings getSettings() {
		return settings;
	}

	public boolean isDayBalance(int row) {
		Date valueDate = getTransaction(row).getValueDate();
		return row==getRowCount()-1 || !getTransaction(row+1).getValueDate().equals(valueDate);
	}

	public Double getDayBalance(int row) {
		return data.getBalanceHistory().getBalance(getTransaction(row).getValueDate());
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// Cells that allow to click on HTML links should be editable
		return (columnIndex==settings.getDescriptionColumn() || columnIndex==settings.getCommentColumn()) && columnIndex>=0;
	}
}