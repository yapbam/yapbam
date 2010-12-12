package net.yapbam.data.event;

import net.yapbam.data.Transaction;

/** This event is sent when one or more transactions are deleted.
 */
public class TransactionsRemovedEvent extends DataEvent {
	private Transaction[] removed;
	
	/** Constructor.
	 * @param source The object that thrown the event
	 * @param removed The removed transactions
	 */
	public TransactionsRemovedEvent(Object source, Transaction[] removed) {
		super(source);
		this.removed = removed;
	}

	/** Gets the removed transactions.
	 * @return a transaction array.
	 */
	public Transaction[] getRemoved() {
		return removed;
	}
}
