package net.yapbam.gui.statementview;

import net.yapbam.data.Transaction;
import net.yapbam.gui.TransactionSelector;

public abstract class TransactionsUpdater {
	protected abstract Transaction update(Transaction t);
	
	public Transaction[] update(TransactionSelector selector) {
		Transaction[] ts = selector.getSelectedTransactions();
		Transaction[] updated = new Transaction[ts.length];
		for (int i = 0; i < ts.length; i++) {
			Transaction t = ts[i];
			updated [i] = update(t);
		}
		selector.getFilteredData().getGlobalData().add(updated);
		selector.getFilteredData().getGlobalData().remove(ts);
		return updated;
	}
}
