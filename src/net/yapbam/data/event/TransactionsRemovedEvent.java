package net.yapbam.data.event;

import net.yapbam.data.Transaction;

/** This event is sent when one or more transactions are deleted.
 */
public class TransactionsRemovedEvent extends DataEvent {
	private Transaction[] removed;
	private int[] indexes;
	
	/** Constructor.
	 * @param source The object that thrown the event
	 * @param indexes indexes were the removed elements were (be careful, the elements aren't in the source anymore)
	 * @param removed the removed transactions
	 */
	public TransactionsRemovedEvent(Object source, int[] indexes, Transaction[] removed) {
		super(source);
		this.indexes = indexes;
		this.removed = removed;
	}

	/** Gets index were the removed element was in the source. 
	 * @return indexes were the removed elements were (be careful, the elements aren't in the source anymore).
	 */
	public int[] getIndexes() {
		return indexes;
	}

	/** Gets the removed transactions.
	 * @return a transaction array.
	 */
	public Transaction[] getRemoved() {
		return removed;
	}
}
