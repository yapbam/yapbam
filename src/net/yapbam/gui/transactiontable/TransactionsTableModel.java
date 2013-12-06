package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.CategoryPropertyChangedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.ModePropertyChangedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
import net.yapbam.data.event.TransactionsRemovedEvent;
import net.yapbam.gui.LocalizationData;

class TransactionsTableModel extends GenericTransactionTableModel implements DataListener {
	private static final long serialVersionUID = 1L;

	private transient DateFormat dateFormater;
	private FilteredData data;
	private TableSettings settings;

	TransactionsTableModel(TransactionTable table, FilteredData data) {
		super();
		this.settings = new TableSettings();
		this.data = data;
		if (data!=null) {
			data.addListener(this);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == settings.getSpreadColumn()) {
			return SpreadState.class;
		} else  if ((columnIndex == settings.getDateColumn())||(columnIndex == settings.getValueDateColumn())) {
			return Date.class;
		} else if ((columnIndex == settings.getAmountColumn()) || (columnIndex == settings.getReceiptColumn()) || (columnIndex == settings.getExpenseColumn())) {
			return double[].class;
		} else {
			return String.class;
		}
	}

	@Override
	public int getColumnCount() {
		return settings.getColumnCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==settings.getSpreadColumn()) {
			return LocalizationData.get("Transaction.0"); //$NON-NLS-1$
		} else if (columnIndex==settings.getAccountColumn()) {
			return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		} else if (columnIndex==settings.getDateColumn()) {
			return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		} else if (columnIndex==settings.getDescriptionColumn()) {
			return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		} else if (columnIndex==settings.getAmountColumn()) {
			return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		} else if (columnIndex==settings.getReceiptColumn()) {
			return LocalizationData.get("StatementView.receipt"); //$NON-NLS-1$
		} else if (columnIndex==settings.getExpenseColumn()) {
			return LocalizationData.get("StatementView.debt"); //$NON-NLS-1$
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
		} else if (columnIndex==settings.getCommentColumn()) {
			return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	@Override
	public int getRowCount() {
		return data==null?0:data.getTransactionsNumber();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		boolean spread = isSpread(rowIndex);
		if (dateFormater==null) {
			dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, LocalizationData.getLocale());
		}
		Transaction transaction = (Transaction) this.getTransaction(rowIndex);
		if (columnIndex==settings.getSpreadColumn()) {
			return new SpreadState(transaction.getSubTransactionSize()!=0, spread);
		} else if (columnIndex==settings.getAccountColumn()) {
			return transaction.getAccount().getName();
		} else if (columnIndex==settings.getDateColumn()) {
			return transaction.getDate();
		} else if (columnIndex==settings.getDescriptionColumn()) {
			return TransactionTableUtils.getDescription(transaction, spread, !settings.isCommentSeparatedFromDescription());
		} else if (columnIndex==settings.getAmountColumn()) {
			return TransactionTableUtils.getAmount(transaction, spread);
		} else if (columnIndex==settings.getReceiptColumn()) {
			return TransactionTableUtils.getExpenseReceipt(transaction, spread, false);
		} else if (columnIndex==settings.getExpenseColumn()) {
			return TransactionTableUtils.getExpenseReceipt(transaction, spread, true);
		} else if (columnIndex==settings.getCategoryColumn()) {
			return TransactionTableUtils.getCategory(transaction, spread);
		} else if (columnIndex==settings.getModeColumn()) {
			return transaction.getMode().getName();
		} else if (columnIndex==settings.getNumberColumn()) {
			return transaction.getNumber();
		} else if (columnIndex==settings.getValueDateColumn()) {
			return transaction.getValueDate();
		} else if (columnIndex==settings.getStatementColumn()) {
			return transaction.getStatement();
		} else if (columnIndex==settings.getCommentColumn()) {
			return transaction.getComment();
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void processEvent(DataEvent event) {
		if (event instanceof EverythingChangedEvent) {
			this.clearSpreadData();
			fireTableDataChanged();
			//TODO It would be a good idea to scroll to the end of table.
			//The problem is : The end of the table is not always the right place.
			//For instance, if the table is sorted by date in the reverse order,
			//the beginning of the table would be a better place.
		} else if (event instanceof TransactionsAddedEvent) {
			Transaction[] transactions = ((TransactionsAddedEvent)event).getTransactions();
			if (transactions.length==1) {
				int index = this.data.indexOf(transactions[0]);
				fireTableRowsInserted(index, index);
			} else {
				fireTableDataChanged();
			}
		} else if (event instanceof TransactionsRemovedEvent) {
			Transaction[] removed = ((TransactionsRemovedEvent)event).getRemoved();
			for (int i = 0; i < removed.length; i++) {
				this.setSpread(removed[i], false);
			}
			fireTableDataChanged();
		} else if (event instanceof AccountPropertyChangedEvent) {
			if (((AccountPropertyChangedEvent)event).getProperty().equals(AccountPropertyChangedEvent.NAME)) {
				fireTableDataChanged();			
			}
		} else if ((event instanceof CategoryPropertyChangedEvent) ||
				((event instanceof ModePropertyChangedEvent) && ((((ModePropertyChangedEvent)event).getChanges()&ModePropertyChangedEvent.NAME)!=0))) {
			fireTableDataChanged();			
		}
	}

	@Override
	public void setRowLook(Component renderer, JTable table, int row, boolean isSelected) {
		super.setRowLook(renderer, table, row, isSelected);
		boolean isChecked = this.data.getTransaction(row).isChecked();
		Font font = renderer.getFont().deriveFont(isChecked ? Font.ITALIC : Font.PLAIN + Font.BOLD);
		renderer.setFont(font);
	}
	
	@Override
	public int getAlignment(int column) {
		if ((column == settings.getAmountColumn()) || (column == settings.getReceiptColumn()) || (column == settings.getExpenseColumn())) {
			return SwingConstants.RIGHT;
		} else if ((column == settings.getAccountColumn()) || (column == settings.getDescriptionColumn())) {
			return SwingConstants.LEFT;
		} else {
			return SwingConstants.CENTER;
		}
	}

	@Override
	public AbstractTransaction getTransaction (int row) {
		return this.data.getTransaction(row);
	}
}
