package net.yapbam.data.event;

import net.yapbam.data.PeriodicalTransaction;

public class PeriodicalTransactionsAddedEvent extends DataEvent {
	private PeriodicalTransaction[] transactions;

	public PeriodicalTransactionsAddedEvent(Object source, PeriodicalTransaction[] transactions) {
		super(source);
		this.transactions = transactions;
	}

	public PeriodicalTransaction[] getPeriodicalTransactions() {
		return transactions;
	}
}
