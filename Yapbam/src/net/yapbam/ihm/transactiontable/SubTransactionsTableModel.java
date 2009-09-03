package net.yapbam.ihm.transactiontable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;

//LOCAL
public class SubTransactionsTableModel extends AbstractTableModel implements GenericTransactionTableModel {
	private static final long serialVersionUID = 1L;

	private List<SubTransaction> subTransactions;
	
	public SubTransactionsTableModel() {
		this.subTransactions = new ArrayList<SubTransaction>();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==1) return double[].class;
		return String.class;
	}

	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex==0) return "Libellé";
		if (columnIndex==1) return "Montant";
		if (columnIndex==2) return "Catégorie";
		return "?";
	}

	public int getRowCount() {
		return this.subTransactions.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		SubTransaction st = this.subTransactions.get(rowIndex);
		if (columnIndex==0) return st.getDescription();
		if (columnIndex==1) return new double[]{st.getAmount()};
		if (columnIndex==2) return st.getCategory().getName();
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void fill(Transaction transaction) {
		this.subTransactions.clear();
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			this.subTransactions.add(transaction.getSubTransaction(i));
		}
	}
	
	public SubTransaction get(int index) {
		return this.subTransactions.get(index);
	}
	
	@Override
	public boolean isExpense(int row) {
		return this.subTransactions.get(row).getAmount()<0;
	}

	@Override
	public boolean isChecked(int row) {
		return false;
	}

	@Override
	public int getAlignment(int column) {
    	return SwingConstants.LEFT;
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
