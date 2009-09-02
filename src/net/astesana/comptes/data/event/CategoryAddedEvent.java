package net.astesana.comptes.data.event;

public class CategoryAddedEvent extends DataEvent {
	private int categoryIndex;

	public CategoryAddedEvent(Object source, int accountIndex) {
		super(source);
		this.categoryIndex = accountIndex;
	}

	public int getCategoryIndex() {
		return categoryIndex;
	}
}
