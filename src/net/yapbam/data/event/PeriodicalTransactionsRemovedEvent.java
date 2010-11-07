package net.yapbam.data.event;

import net.yapbam.data.PeriodicalTransaction;

/** This event is sent when one or more periodical transactions are deleted.
 */
public class PeriodicalTransactionsRemovedEvent extends DataEvent {
	private PeriodicalTransaction[] removed;
	private int[] indexes;
	
	public PeriodicalTransactionsRemovedEvent(Object source, int[] indexes, PeriodicalTransaction[] removed) {
		super(source);
		this.indexes = indexes;
		this.removed = removed;
	}

	/**
	 * Gets indexes where the removed elements were in the source. 
	 * @return indexes where the removed element were (be careful, the element isn't in the source anymore).
	 */
	public int[] getIndexes() {
		return indexes;
	}

	/** Gets the removed periodical transactions.
	 * @return a PeriodicalTransaction array
	 */
	public PeriodicalTransaction[] getRemoved() {
		return removed;
	}
}
