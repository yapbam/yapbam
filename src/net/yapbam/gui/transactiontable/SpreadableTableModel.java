package net.yapbam.gui.transactiontable;

/** This interface is a addition to the TableModel concept in order to describe that some
 * lines may contains more that one information line.
 * The best example is probably the transaction in the transaction table. Its subtransactions can't
 * be viewed without spreading the display to more than one line (one line per subtransaction).
 */
public interface SpreadableTableModel {
	/** Is the row spreadable ?
	 * @param row the row index 
	 * @return true if the row is "spreadable".
	 */
	public abstract boolean isSpreadable(int row);
	
	/** Get the number of lines needed to represent a spread row.
	 * @param row the row index
	 * @return the number of lines of the row
	 */
	public abstract int getSpreadLines(int row);
	
	/** Get the index of the column that contains the spread icon.
	 * @return the column index
	 */
	public abstract int getSpreadColumnNumber();
}