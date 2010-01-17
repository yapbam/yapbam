package net.yapbam.data;

import java.util.ArrayList;

/**
 * This utility class is use to make mass modification of transactions.
 */
abstract class AbstractTransactionUpdater {
	GlobalData data;
	
	AbstractTransactionUpdater (GlobalData data) {
		this.data = data;
	}
	
	void doIt() {
		// As the transactions list is sorted, we can't add the transactions in the same loop than we delete it
		// (or we could miss some transactions). We will first remove the transaction we need to remove
		ArrayList<Transaction> newTransactions = new ArrayList<Transaction>();
		for (int i = data.getTransactionsNumber()-1; i >= 0 ; i--) {
			Transaction t = change(data.getTransaction(i));
			if (t!=null) {
				data.remove(data.getTransaction(i));
				newTransactions.add(t);
			}
		}
		// Now, we can add the new transactions
		for (int i = 0; i < newTransactions.size(); i++) {
			data.add(newTransactions.get(i));
		}
		// It's exactly the same problem for periodical transactions
		ArrayList<PeriodicalTransaction> newPTransactions = new ArrayList<PeriodicalTransaction>();
		for (int i = data.getPeriodicalTransactionsNumber()-1; i >= 0 ; i--) {
			PeriodicalTransaction pt = change(data.getPeriodicalTransaction(i));
			if (pt!=null) {
				data.remove(data.getPeriodicalTransaction(i));
				newPTransactions.add(pt);
			}
		}
		for (int i = 0; i < newPTransactions.size(); i++) {
			data.add(newPTransactions.get(i));
		}
	}
	
	abstract Transaction change(Transaction t);
	abstract PeriodicalTransaction change(PeriodicalTransaction t);
}