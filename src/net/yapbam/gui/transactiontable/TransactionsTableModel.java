package net.yapbam.gui.transactiontable;

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
import net.yapbam.data.event.TransactionAddedEvent;
import net.yapbam.data.event.TransactionRemovedEvent;
import net.yapbam.gui.LocalizationData;

class TransactionsTableModel extends GenericTransactionTableModel implements DataListener {
	private static final long serialVersionUID = 1L;

	private transient DateFormat dateFormater;
	private FilteredData data;
	
	TransactionsTableModel(JTable table, FilteredData data) {
		super();
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
		return 10;
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
				StringBuffer buf = new StringBuffer("<html><body>").append(transaction.getDescription()); //$NON-NLS-1$
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(transaction.getSubTransaction(i).getDescription()); //$NON-NLS-1$
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append(LocalizationData.get("Transaction.14")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				buf.append("</body></html>"); //$NON-NLS-1$
				return buf.toString();
			} else {
				return transaction.getDescription();
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
				StringBuffer buf = new StringBuffer("<html><body>").append(getName(transaction.getCategory())); //$NON-NLS-1$
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
		} else if (event instanceof TransactionAddedEvent) {
			int index = this.data.indexOf(((TransactionAddedEvent)event).getTransaction());;
			fireTableRowsInserted(index, index);
		} else if (event instanceof TransactionRemovedEvent) {
			this.setSpread(((TransactionRemovedEvent)event).getRemoved(), false);
			int index = ((TransactionRemovedEvent)event).getIndex();
			fireTableRowsDeleted(index, index);
		} else if (event instanceof AccountPropertyChangedEvent) {
			if (((AccountPropertyChangedEvent)event).getProperty().equals(AccountPropertyChangedEvent.NAME)) {
				fireTableDataChanged();			
			}
		} else if (event instanceof CategoryPropertyChangedEvent) {
			fireTableDataChanged();			
		}
	}

	@Override
	public boolean isChecked(int row) {
		return this.data.getTransaction(row).isChecked();
	}
	
	@Override
	public int getAlignment(int column) {
		if (column==4) return SwingConstants.RIGHT;
    	if ((column==1) || (column==3)) return SwingConstants.LEFT;
    	else return SwingConstants.CENTER;
	}

	public AbstractTransaction getTransaction (int row) {
		return this.data.getTransaction(row);
	}
}
