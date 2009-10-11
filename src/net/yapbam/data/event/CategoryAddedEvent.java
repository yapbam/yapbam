package net.yapbam.data.event;

import net.yapbam.data.Category;

public class CategoryAddedEvent extends DataEvent {
	private Category category;

	public CategoryAddedEvent(Object source, Category category) {
		super(source);
		this.category = category;
	}

	public Category getCategory() {
		return this.category;
	}
}
