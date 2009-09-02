package net.astesana.comptes.data.event;

import net.astesana.comptes.data.Transaction;

public class TransactionRemovedEvent extends DataEvent {
	private Transaction removed;
	private int index;
	
	public TransactionRemovedEvent(Object source, int index, Transaction removed) {
		super(source);
		this.index = index;
		this.removed = removed;
	}

	/**
	 * Get index were the removed element was in the source. 
	 * @return index were the removed element was (be careful, the element isn't in the source anymore).
	 */
	public int getIndex() {
		return index;
	}

	public Transaction getRemoved() {
		return removed;
	}
}
