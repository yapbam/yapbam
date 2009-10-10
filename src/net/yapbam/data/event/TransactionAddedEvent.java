package net.yapbam.data.event;

import net.yapbam.data.Transaction;

public class TransactionAddedEvent extends DataEvent {
	private Transaction transaction;
	
	public TransactionAddedEvent(Object source, Transaction transaction) {
		super(source);
		this.transaction = transaction;
	}

	public Transaction getTransaction() {
		return transaction;
	}
}
