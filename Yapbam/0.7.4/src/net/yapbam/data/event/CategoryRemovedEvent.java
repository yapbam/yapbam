package net.yapbam.data.event;

import net.yapbam.data.Category;

public class CategoryRemovedEvent extends DataEvent {
	private Category removed;
	private int index;
	
	public CategoryRemovedEvent(Object source, int index, Category removed) {
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

	public Category getRemoved() {
		return removed;
	}
}
