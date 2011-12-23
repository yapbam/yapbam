package net.yapbam.data.event;

import net.yapbam.data.Category;

public class CategoryPropertyChangedEvent extends DataEvent {
	public static final String NAME = "name"; //$NON-NLS-1$
	
	private Category category;
	private String property;
	private Object oldValue;
	private Object newValue;

	public CategoryPropertyChangedEvent(Object source, String propertyName, Category category, Object oldValue, Object newValue) {
		super (source);
		this.category = category;
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
