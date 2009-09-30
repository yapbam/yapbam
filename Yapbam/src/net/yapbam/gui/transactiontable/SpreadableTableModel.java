package net.yapbam.gui.transactiontable;

public interface SpreadableTableModel {
	public abstract boolean isSpreadable(int row);
	public abstract int getSpreadLines(int row);
	public abstract int getSpreadColumnNumber();
}