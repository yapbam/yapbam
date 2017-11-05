package net.yapbam.gui.dialogs;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.Category;
import net.yapbam.data.SubTransaction;
import net.yapbam.gui.LocalizationData;

public class SubTransactionsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<SubTransaction> subTransactions;
	
	public SubTransactionsTableModel() {
		this.subTransactions = new ArrayList<SubTransaction>();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex==1 ? double[].class : String.class;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) {
			return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
		} else if (columnIndex==1) {
			return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$
		} else if (columnIndex==2) {
			return LocalizationData.get("Transaction.category"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}

	@Override
	public int getRowCount() {
		return this.subTransactions.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		SubTransaction st = this.subTransactions.get(rowIndex);
		if (columnIndex==0) {
			return st.getDescription();
		} else if (columnIndex==1) {
			return new double[]{st.getAmount()};
		} else if (columnIndex==2) {
			Category category = st.getCategory();
			return category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName(); //$NON-NLS-1$
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	public SubTransaction get(int index) {
		return this.subTransactions.get(index);
	}
	
	public void fill(AbstractTransaction transaction) {
		this.subTransactions.clear();
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			this.subTransactions.add(transaction.getSubTransaction(i));
		}
		this.fireTableDataChanged();
	}

	public void add(SubTransaction sub) {
		this.subTransactions.add(sub);
		this.fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
	}

	public void remove(int index) {
		this.subTransactions.remove(index);
		this.fireTableRowsDeleted(index, index);
	}

	public void replace(int index, SubTransaction sub) {
		this.subTransactions.set(index, sub);
		this.fireTableRowsUpdated(index, index);
	}
}
