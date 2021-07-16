package net.yapbam.gui.transactiontable;

import java.util.HashSet;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import net.yapbam.data.AbstractTransaction;

@SuppressWarnings("serial")
public abstract class GenericTransactionTableModel<T extends AbstractTransaction> extends AbstractTableModel implements SpreadableTableModel, TransactionsModel {
	private Set<Long> spreadTransactionId;

	protected GenericTransactionTableModel() {
		this.spreadTransactionId = new HashSet<Long>();
	}
	
	@Override
	public boolean isSpreadable(int row) {
		return this.getTransaction(row).getSubTransactionSize()>0;
	}

	@Override
	public boolean isSpread(int rowIndex) {
		return spreadTransactionId.contains(this.getTransaction(rowIndex).getId());
	}

	@Override
	public void setSpread(int rowIndex, boolean spread) {
		this.setSpread(this.getTransaction(rowIndex), spread);
	}

	@Override
	public int getSpreadColumnNumber() {
		return 0;
	}

	@Override
	public int getSpreadLines(int row) {
		int lines = this.getTransaction(row).getSubTransactionSize()+1;
		if (getTransaction(row).getComplement()!=0) {
			lines++;
		}
		return lines;
	}
	
	protected void clearSpreadData() {
		this.spreadTransactionId.clear();
	}
	
	protected void setSpread (AbstractTransaction transaction, boolean spread) {
		if (spread) {
			this.spreadTransactionId.add(transaction.getId());
		} else {
			this.spreadTransactionId.remove(transaction.getId());
		}
	}
	
	public abstract T getTransaction(int rowIndex);
}