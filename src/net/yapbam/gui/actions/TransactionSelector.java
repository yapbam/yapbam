package net.yapbam.gui.actions;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;

/** A transaction selector : Something able to select a transaction.
 * @see DeleteTransactionAction
 */
public interface TransactionSelector {
	public Transaction getSelectedTransaction();
	
	public GlobalData getGlobalData();
}
