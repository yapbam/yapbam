package net.yapbam.data;

import java.util.ArrayList;

/**
 * This utility class is use to make mass modification on transactions and PeriodicalTransactions.
 */
abstract class AbstractTransactionUpdater {
	GlobalData data;
	
	/** Constructor.
	 * @param data the global data onto make the mass modification.
	 */
	AbstractTransactionUpdater (GlobalData data) {
		this.data = data;
	}
	
	/** Performs the mass modification.
	 */
	void doIt() {
		// For performance concerns, we will remove all transactions that needed to be updated and then
		// add all their modified instantiations
		// First we build list of removed transactions and added ones.
		ArrayList<Transaction> newTransactions = new ArrayList<Transaction>();
		ArrayList<Transaction> removedTransactions = new ArrayList<Transaction>();
		for (int i=0 ; i<data.getTransactionsNumber() ; i++) {
			Transaction t = change(data.getTransaction(i));
			if (t!=null) {
				removedTransactions.add(data.getTransaction(i));
				newTransactions.add(t);
			}
		}
		// Then, we can remove the old transactions and add the new ones
		data.remove(removedTransactions.toArray(new Transaction[removedTransactions.size()]));
		data.add(newTransactions.toArray(new Transaction[newTransactions.size()]));
		// It's exactly the same problem for periodical transactions
		ArrayList<PeriodicalTransaction> newPTransactions = new ArrayList<PeriodicalTransaction>();
		ArrayList<PeriodicalTransaction> removedPTransactions = new ArrayList<PeriodicalTransaction>();
		for (int i=0 ; i<data.getPeriodicalTransactionsNumber() ; i++) {
			PeriodicalTransaction pt = change(data.getPeriodicalTransaction(i));
			if (pt!=null) {
				removedPTransactions.add(data.getPeriodicalTransaction(i));
				newPTransactions.add(pt);
			}
		}
		data.remove(removedPTransactions.toArray(new PeriodicalTransaction[removedPTransactions.size()]));
		data.add(newPTransactions.toArray(new PeriodicalTransaction[newPTransactions.size()]));
	}
	
	/** Gets the changed view of a transaction.
	 * @param transaction The transaction to be modified.
	 * @return the modified transaction or null if the transaction may remain unchanged.
	 */
	abstract Transaction change(Transaction transaction);
	/** Gets the changed view of a periodical transaction.
	 * @param transaction The periodical transaction to be modified.
	 * @return the modified periodical transaction or null if it may remain unchanged.
	 */
	abstract PeriodicalTransaction change(PeriodicalTransaction transaction);
}