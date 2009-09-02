package net.astesana.comptes.data.event;

public class PeriodicalTransactionAddedEvent extends DataEvent {
	private int index;

	public PeriodicalTransactionAddedEvent(Object source, int index) {
		super(source);
		this.index = index;
	}

	public int getPeriodicalTransactionIndex() {
		return index;
	}
}
