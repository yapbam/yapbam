package net.yapbam.gui.actions;

import javax.swing.JTable;

import net.yapbam.data.Transaction;

/** A class that is able to set the selection of transactions in a JTable.
 * <br>This could be seen as very easy, but, unfortunately, there's some pitfalls (see implementation).
 * @author Jean-Marc Astesana
 */
public abstract class TransactionsJTableSelector {
	/** The JTable that contains the selected transactions.*/
	protected JTable table;
	
	/** Constructor
	 * @param table The JTable that contains the selected transactions.
	 */
	protected TransactionsJTableSelector(JTable table) {
		this.table = table;
	}
	
	/** Selects some transactions.
	 * @param transactions The transactions to select. If some transactions do not exist in the table, they are ignored. 
	 */
	public void setSelectedTransactions(Transaction[] transactions) {
		table.getSelectionModel().setValueIsAdjusting(true);
		table.getSelectionModel().clearSelection();
		int firstViewRow = -1;
		for (int i = 0; i < transactions.length; i++) {
			int row = getModelIndex(transactions[i]);
			if (row>=0) {
				row = table.convertRowIndexToView(row);
				if ((firstViewRow<0) || (firstViewRow>row)) firstViewRow = row;
				table.getSelectionModel().addSelectionInterval(row, row);
			}
		}
		table.getSelectionModel().setValueIsAdjusting(false);
		table.scrollRectToVisible(table.getCellRect(firstViewRow, 0, true));
	}
	
	/** Gets the model index of a transaction.
	 * @param transaction a transaction
	 * @return the index of the transaction in the table model (a negative number is the transaction is not in the table).
	 */
	protected abstract int getModelIndex(Transaction transaction);
}