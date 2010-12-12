package net.yapbam.data.event;

import net.yapbam.data.Transaction;

/** This event is sent when one or more transactions are added.
 */
public class TransactionsAddedEvent extends DataEvent {
	private Transaction[] transactions;
	
	/** Constructor.
	 * @param source The object that thrown the event
	 * @param transactions The added transactions
	 */
	public TransactionsAddedEvent(Object source, Transaction[] transactions) {
		super(source);
		this.transactions = transactions;
	}

	/** Gets the added transactions.
	 * @return a transaction array.
	 */
	public Transaction[] getTransactions() {
		return transactions;
	}
}
