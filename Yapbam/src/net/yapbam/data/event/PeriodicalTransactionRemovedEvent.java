package net.yapbam.data.event;

import net.yapbam.data.PeriodicalTransaction;

public class PeriodicalTransactionRemovedEvent extends DataEvent {
	private PeriodicalTransaction removed;
	private int index;
	
	public PeriodicalTransactionRemovedEvent(Object source, int index, PeriodicalTransaction removed) {
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

	public PeriodicalTransaction getRemoved() {
		return removed;
	}
}
