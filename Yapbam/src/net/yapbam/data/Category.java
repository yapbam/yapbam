package net.yapbam.data;

import java.io.Serializable;

/** Cette classe représente une catégorie (Loisirs, Salaire, etc ...) */
public class Category implements Serializable, Comparable<Category> {
	private static final long serialVersionUID = 1L;
	public static final Category UNDEFINED = new Category("");

	private String name;

	public Category(String name) {
		if (name==null) throw new IllegalArgumentException();
		this.name = name;
	}

	public String getName() {
		return this.name.length()==0 ? "Indéfinie" : this.name; //LOCAL
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
