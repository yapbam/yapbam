package net.astesana.comptes.ihm.transactiontable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import net.astesana.comptes.data.Category;
import net.astesana.comptes.data.FilteredData;
import net.astesana.comptes.data.Mode;
import net.astesana.comptes.data.Transaction;
import net.astesana.comptes.data.event.EverythingChangedEvent;
import net.astesana.comptes.data.event.DataEvent;
import net.astesana.comptes.data.event.DataListener;
import net.astesana.comptes.data.event.TransactionAddedEvent;
import net.astesana.comptes.data.event.TransactionRemovedEvent;
import net.astesana.comptes.ihm.LocalizationData;

//LOCAL
class TransactionsTableModel extends AbstractTableModel implements DataListener, GenericTransactionTableModel {
	private static final long serialVersionUID = 1L;

	private transient DateFormat dateFormater;
	private FilteredData data;
	private JTable table;
	
	TransactionsTableModel(JTable table, FilteredData data) {
		this.data = data;
		this.table = table;
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
		if (columnIndex==0) return "S/O";
		if (columnIndex==1) return "Compte";
		if (columnIndex==2) return "Date";
		if (columnIndex==3) return "Libellé";
		if (columnIndex==4) return "Montant";
		if (columnIndex==5) return "Catégorie";
		if (columnIndex==6) return "Mode";
		if (columnIndex==7) return "Numéro";
		if (columnIndex==8) return "Date de valeur";
		if (columnIndex==9) return "Relevé";
		return "?";
	}

	public int getRowCount() {
		return data.getTransactionsNumber();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		boolean spread = this.table.getRowHeight()!=this.table.getRowHeight(rowIndex);
		if (dateFormater==null) {
			dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, LocalizationData.getLocale());
		}
		Transaction transaction = this.data.getTransaction(rowIndex);
		if (columnIndex==0) return new SpreadState(transaction.getSubTransactionSize()!=0, spread);
		else if (columnIndex==1) return transaction.getAccount().getName();
		else if (columnIndex==2) return transaction.getDate();
		else if (columnIndex==3) {
			if (spread) {
				StringBuffer buf = new StringBuffer("<html><body>").append(transaction.getDescription());
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(transaction.getSubTransaction(i).getDescription());
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append("Reste");
				}
				buf.append("</body></html>");
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
				StringBuffer buf = new StringBuffer("<html><body>").append(getName(transaction.getCategory()));
				for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
					buf.append("<BR>&nbsp;&nbsp;").append(getName(transaction.getSubTransaction(i).getCategory()));
				}
				if (transaction.getComplement()!=0) {
					buf.append("<BR>&nbsp;&nbsp;").append(getName(transaction.getCategory()));
				}
				buf.append("</body></html>");
				return buf.toString();
			} else {
				return getName(transaction.getCategory());
			}
		} else if (columnIndex==6) {
			Mode mode = transaction.getMode();
			return mode.equals(Mode.UNDEFINED) ? "" : mode.getName();
		} else if (columnIndex==7) return transaction.getNumber();
		else if (columnIndex==8) return transaction.getValueDate();
		else if (columnIndex==9) return transaction.getStatement();
		return null;
	}

	private Object getName(Category category) {
		return category.equals(Category.UNDEFINED) ? "" : category.getName();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void processEvent(DataEvent event) {
		if (event instanceof EverythingChangedEvent) {
			fireTableDataChanged();
		} else if (event instanceof TransactionAddedEvent) {
			int index = ((TransactionAddedEvent)event).getTransactionIndex();
			fireTableRowsInserted(index, index);
		} else if (event instanceof TransactionRemovedEvent) {
			int index = ((TransactionRemovedEvent)event).getIndex();
			fireTableRowsDeleted(index, index);
		}
	}

	@Override
	public boolean isExpense(int row) {
		return this.data.getTransaction(row).getAmount()<0;
	}

	@Override
	public boolean isChecked(int row) {
		return this.data.getTransaction(row).isChecked();
	}
	
	public Transaction getTransaction (int row) {
		return this.data.getTransaction(row);
	}

	@Override
	public int getAlignment(int column) {
		if (column==4) return SwingConstants.RIGHT;
    	if ((column==1) || (column==3)) return SwingConstants.LEFT;
    	else return SwingConstants.CENTER;
	}
}
