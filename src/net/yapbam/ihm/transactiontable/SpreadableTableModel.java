package net.yapbam.ihm.transactiontable;

public interface SpreadableTableModel {
	public abstract boolean isSpreadable(int row);
	public abstract int getSpreadLines(int row);
	public abstract int getSpreadColumnNumber();
}