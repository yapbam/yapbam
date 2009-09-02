package net.astesana.comptes.data.event;

public class TransactionAddedEvent extends DataEvent {
	private int transactionIndex;
	
	public TransactionAddedEvent(Object source, int transactionIndex) {
		super(source);
		this.transactionIndex = transactionIndex;
	}

	public int getTransactionIndex() {
		return transactionIndex;
	}
}
