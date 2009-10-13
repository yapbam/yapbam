package net.yapbam.data.event;

import net.yapbam.data.Category;

public class CategoryPropertyChangedEvent extends DataEvent {
	public static final String NAME = "name";
	
	private Category category;
	private String property;
	private Object oldValue;
	private Object newValue;

	public CategoryPropertyChangedEvent(Object source, String propertyName, Category account, Object oldValue, Object newValue) {
		super (source);
		this.category = account;
		this.property = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Category getCategory() {
		return category;
	}

	public String getProperty() {
		return property;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}
}
