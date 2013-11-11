package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringEscapeUtils;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
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
		if (data!=null) data.addListener(this);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == settings.getSpreadColumn()) return SpreadState.class;
		if ((columnIndex == settings.getDateColumn())||(columnIndex == settings.getValueDateColumn())) return Date.class;
		if ((columnIndex == settings.getAmountColumn()) || (columnIndex == settings.getReceiptColumn()) || (columnIndex == settings.getExpenseColumn())) return double[].class;
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return settings.getColumnCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==settings.getSpreadColumn()) return LocalizationData.get("Transaction.0"); //$NON-NLS-1$
		if (columnIndex==settings.getAccountColumn()) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (columnIndex==settings.getDateColumn()) return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		if (columnIndex==settings.getDescriptionColumn()) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		if (columnIndex==settings.getAmountColumn()) return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		if (columnIndex==settings.getReceiptColumn()) return LocalizationData.get("StatementView.receipt"); //$NON-NLS-1$
		if (columnIndex==settings.getExpenseColumn()) return LocalizationData.get("StatementView.debt"); //$NON-NLS-1$
		if (columnIndex==settings.getCategoryColumn()) return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		if (columnIndex==settings.getModeColumn()) return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
		if (columnIndex==settings.getNumberColumn()) return LocalizationData.get("Transaction.number"); //$NON-NLS-1$
		if (columnIndex==settings.getValueDateColumn()) return LocalizationData.get("Transaction.valueDate"); //$NON-NLS-1$
		if (columnIndex==settings.getStatementColumn()) return LocalizationData.get("Transaction.statement"); //$NON-NLS-1$
		if (columnIndex==settings.getCommentColumn()) return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
		return "?"; //$NON-NLS-1$
	}

	@Override
	public int getRowCount() {
		return data==null?0:data.getTransactionsNumber();
	}
	
	public String getDescription (AbstractTransaction transaction) {
		return transaction.getDescription(!settings.isCommentSeparatedFromDescription());
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		boolean spread = isSpread(rowIndex);
		if (dateFormater==null) {
			dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, LocalizationData.getLocale());
		}
		Transaction transaction = (Transaction) this.getTransaction(rowIndex);
		if (columnIndex==settings.getSpreadColumn()) return new SpreadState(transaction.getSubTransactionSize()!=0, spread);
		else if (columnIndex==settings.getAccountColumn()) return transaction.getAccount().getName();
		else if (columnIndex==settings.getDateColumn()) return transaction.getDate();
		else if (columnIndex==settings.getDescriptionColumn()) {
			if (spread) {
				StringBuilder buf = new StringBuilder("<html><body>").append(StringEscapeUtils.escapeHtml3(getDescription(transaction))); //$NON-NLS-1$
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(StringEscapeUtils.escapeHtml3(transaction.getSubTransaction(i).getDescription())); //$NON-NLS-1$
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append(StringEscapeUtils.escapeHtml3(LocalizationData.get("Transaction.14"))); //$NON-NLS-1$ //$NON-NLS-2$
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				return buf.toString().replace(" ", "&nbsp;");
			} else {
				return getDescription(transaction);
			}
		} else if (columnIndex==settings.getAmountColumn()) {
			if (spread) {
				double complement = transaction.getComplement();
				int numberOfLines = transaction.getSubTransactionSize()+1;
				if (complement!=0) numberOfLines++;
				double[] result = new double[numberOfLines];
				result[0] = transaction.getAmount();
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					result[i+1] = transaction.getSubTransaction(i).getAmount();
				}
				if (complement!=0) result[result.length-1] = complement;
				return result;
			} else {
				return new double[]{transaction.getAmount()};
			}
		} else if (columnIndex==settings.getReceiptColumn()) {
			if (spread) {
				double complement = transaction.getComplement();
				int numberOfLines = transaction.getSubTransactionSize()+1;
				if (!isZero(complement)) numberOfLines++;
				double[] result = new double[numberOfLines];
				result[0] = !isExpense(transaction.getAmount()) ? transaction.getAmount() : Double.NaN;
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					double amount = transaction.getSubTransaction(i).getAmount();
					result[i+1] = !isExpense(amount) ? amount : Double.NaN;
				}
				if (!isZero(complement)) result[result.length-1] = !isExpense(complement) ? complement : Double.NaN;
				return result;
			} else {
				return (!isExpense(transaction.getAmount())) ? new double[]{transaction.getAmount()} : null;
			}
		} else if (columnIndex==settings.getExpenseColumn()) {
			if (spread) {
				double complement = transaction.getComplement();
				int numberOfLines = transaction.getSubTransactionSize()+1;
				if (!isZero(complement)) numberOfLines++;
				double[] result = new double[numberOfLines];
				result[0] = isExpense(transaction.getAmount()) ? transaction.getAmount() : Double.NaN;
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					double amount = transaction.getSubTransaction(i).getAmount();
					result[i+1] = isExpense(amount) ? amount : Double.NaN;
				}
				if (!isZero(complement)) result[result.length-1] = isExpense(complement) ? complement : Double.NaN;
				return result;
			} else {
				return (GlobalData.AMOUNT_COMPARATOR.compare(transaction.getAmount(), 0.0)<=0) ? new double[]{transaction.getAmount()} : null;
			}
		} else if (columnIndex==settings.getCategoryColumn()) {
			if (spread) {
				StringBuilder buf = new StringBuilder("<html><body>").append(StringEscapeUtils.escapeHtml3(getName(transaction.getCategory()))); //$NON-NLS-1$
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(StringEscapeUtils.escapeHtml3(getName(transaction.getSubTransaction(i).getCategory()))); //$NON-NLS-1$
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append(StringEscapeUtils.escapeHtml3(getName(transaction.getCategory()))); //$NON-NLS-1$
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				return buf.toString().replace(" ", "&nbsp;");
			} else {
				return getName(transaction.getCategory());
			}
		} else if (columnIndex==settings.getModeColumn()) {
			return transaction.getMode().getName();
		} else if (columnIndex==settings.getNumberColumn()) return transaction.getNumber();
		else if (columnIndex==settings.getValueDateColumn()) return transaction.getValueDate();
		else if (columnIndex==settings.getStatementColumn()) return transaction.getStatement();
		else if (columnIndex==settings.getCommentColumn()) return transaction.getComment();
		return null;
	}
	
	private boolean isExpense (double amount) {
		return GlobalData.AMOUNT_COMPARATOR.compare(amount, 0.0)<=0;
	}

	private boolean isZero (double amount) {
		return GlobalData.AMOUNT_COMPARATOR.compare(amount, 0.0)==0;
	}

	private String getName(Category category) {
		return category.equals(Category.UNDEFINED) ? "" : category.getName(); //$NON-NLS-1$
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
		if ((column == settings.getAmountColumn()) || (column == settings.getReceiptColumn()) || (column == settings.getExpenseColumn())) return SwingConstants.RIGHT;
		if ((column == settings.getAccountColumn()) || (column == settings.getDescriptionColumn())) return SwingConstants.LEFT;
		else return SwingConstants.CENTER;
	}

	@Override
	public AbstractTransaction getTransaction (int row) {
		return this.data.getTransaction(row);
	}
}
