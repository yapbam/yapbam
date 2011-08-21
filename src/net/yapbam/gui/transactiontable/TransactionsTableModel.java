package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Mode;
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
	private DescriptionSettings descriptionSettings;

	TransactionsTableModel(TransactionTable table, FilteredData data) {
		super();
		this.descriptionSettings = new DescriptionSettings();
		this.data = data;
		data.addListener(this);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==0) return SpreadState.class;
		if ((columnIndex==2)||(columnIndex==8)) return Date.class;
		if (columnIndex==4) return double[].class;
		return String.class;
	}

	public int getColumnCount() {
		return descriptionSettings.isCommentSeparatedFromDescription()?11:10;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) return LocalizationData.get("Transaction.0"); //$NON-NLS-1$
		if (columnIndex==1) return LocalizationData.get("Transaction.account"); //$NON-NLS-1$
		if (columnIndex==2) return LocalizationData.get("Transaction.date"); //$NON-NLS-1$
		if (columnIndex==3) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		if (columnIndex==4) return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		if (columnIndex==5) return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		if (columnIndex==6) return LocalizationData.get("Transaction.mode"); //$NON-NLS-1$
		if (columnIndex==7) return LocalizationData.get("Transaction.number"); //$NON-NLS-1$
		if (columnIndex==8) return LocalizationData.get("Transaction.valueDate"); //$NON-NLS-1$
		if (columnIndex==9) return LocalizationData.get("Transaction.statement"); //$NON-NLS-1$
		if (columnIndex==10) return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
		return "?"; //$NON-NLS-1$
	}

	public int getRowCount() {
		return data.getTransactionsNumber();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		boolean spread = isSpread(rowIndex);
		if (dateFormater==null) {
			dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, LocalizationData.getLocale());
		}
		Transaction transaction = (Transaction) this.getTransaction(rowIndex);
		if (columnIndex==0) return new SpreadState(transaction.getSubTransactionSize()!=0, spread);
		else if (columnIndex==1) return transaction.getAccount().getName();
		else if (columnIndex==2) return transaction.getDate();
		else if (columnIndex==3) {
			if (spread) {
				StringBuilder buf = new StringBuilder("<html><body>").append(descriptionSettings.getDescription(transaction)); //$NON-NLS-1$
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(transaction.getSubTransaction(i).getDescription()); //$NON-NLS-1$
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append(LocalizationData.get("Transaction.14")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				return buf.toString();
			} else {
				return descriptionSettings.getDescription(transaction);
			}
		} else if (columnIndex==4) {
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
		} else if (columnIndex==5) {
			if (spread) {
				StringBuilder buf = new StringBuilder("<html><body>").append(getName(transaction.getCategory())); //$NON-NLS-1$
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(getName(transaction.getSubTransaction(i).getCategory())); //$NON-NLS-1$
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append(getName(transaction.getCategory())); //$NON-NLS-1$
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				return buf.toString();
			} else {
				return getName(transaction.getCategory());
			}
		} else if (columnIndex==6) {
			Mode mode = transaction.getMode();
			return mode.equals(Mode.UNDEFINED) ? "" : mode.getName(); //$NON-NLS-1$
		} else if (columnIndex==7) return transaction.getNumber();
		else if (columnIndex==8) return transaction.getValueDate();
		else if (columnIndex==9) return transaction.getStatement();
		else if (columnIndex==10) return transaction.getComment();
		return null;
	}
	
	private Object getName(Category category) {
		return category.equals(Category.UNDEFINED) ? "" : category.getName(); //$NON-NLS-1$
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

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
		if (column==4) return SwingConstants.RIGHT;
    	if ((column==1) || (column==3)) return SwingConstants.LEFT;
    	else return SwingConstants.CENTER;
	}

	@Override
	public AbstractTransaction getTransaction (int row) {
		return this.data.getTransaction(row);
	}
}
