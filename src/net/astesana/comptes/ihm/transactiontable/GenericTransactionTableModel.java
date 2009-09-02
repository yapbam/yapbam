package net.astesana.comptes.ihm.transactiontable;

public interface GenericTransactionTableModel {
	public abstract boolean isExpense(int row);

	public abstract boolean isChecked(int row);
	
	public abstract int getAlignment(int column);
}