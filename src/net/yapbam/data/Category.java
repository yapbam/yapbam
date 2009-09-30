package net.yapbam.data;

import java.io.Serializable;

import net.yapbam.gui.LocalizationData;

/** This class represents a category (Food, Sport, etc ...) */
public class Category implements Serializable, Comparable<Category> {
	private static final long serialVersionUID = 1L;
	public static final Category UNDEFINED = new Category(""); //$NON-NLS-1$

	private String name;

	public Category(String name) {
		if (name==null) throw new IllegalArgumentException();
		this.name = name;
	}

	public String getName() {
		return this.name.length()==0 ? LocalizationData.get("Category.undefined") : this.name; //$NON-NLS-1$
	}

	@Override
	public boolean equals(Object obj) {
		Category category = (Category)obj;
		return category.name.equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public int compareTo(Category o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public String toString() {
		return name;
	}
}
