package net.yapbam.gui.statementview;

import net.yapbam.data.Transaction;
import net.yapbam.gui.actions.TransactionSelector;

public abstract class TransactionsUpdater {
	protected abstract Transaction update(Transaction t);
	
	public void update(TransactionSelector selector) {
		Transaction[] ts = selector.getSelectedTransactions();
		Transaction[] tChecked = new Transaction[ts.length];
		for (int i = 0; i < ts.length; i++) {
			Transaction t = ts[i];
			tChecked [i] = update(t);
		}
		selector.getFilteredData().getGlobalData().add(tChecked);
		selector.getFilteredData().getGlobalData().remove(ts);
	}
}
