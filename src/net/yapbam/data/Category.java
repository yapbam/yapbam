package net.yapbam.data;

import java.io.Serializable;

/** A category (Food, Sport, etc ...) */
public class Category implements Serializable, Comparable<Category> {
	private static final long serialVersionUID = 1L;
	/** The undefined category. */
	public static final Category UNDEFINED = new Category(""); //$NON-NLS-1$

	private String name;

	/** Constructor.
	 * @param name The name of the category.
	 * @throws IllegalArgumentException if the parameter is null.
	 */
	public Category(String name) {
		if (name==null) throw new IllegalArgumentException();
		this.name = name;
	}

	/** Gets the category name.
	 * @return a String
	 */
	public String getName() {
		return this.name;
	}

	/** Tests whether an object is equal to this.
	 * Two categories are equals if they have the same name.
	 * @param obj The object to compare with the category.
	 * @return true if the categories are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj==null) return false;
		return ((Category)obj).name.equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/** Compares this to another category.
	 * The categories are compared accordingly to their names with String.compareTo.
	 * @param o The object to compare with the category.
	 * @return an int.
	 */
	@Override
	public int compareTo(Category o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public String toString() {
		return name;
	}

	void setName(String name) {
		if (name==null) throw new IllegalArgumentException();
		this.name = name;
	}
	
	public Category getSuperCategory(char categorySeparator) {
		if (this==Category.UNDEFINED) return this;
		int index = name.indexOf(categorySeparator);
		if (index>=0) {
			return new Category(name.substring(0,index));
		} else {
			return this;
		}
	}

}
