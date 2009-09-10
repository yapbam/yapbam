package net.yapbam.data.event;

import net.yapbam.data.Account;;

public class AccountRemovedEvent extends DataEvent {
	private Account removed;
	private int index;
	
	public AccountRemovedEvent(Object source, int index, Account removed) {
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

	public Account getRemoved() {
		return removed;
	}
}
