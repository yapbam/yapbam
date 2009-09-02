package net.astesana.comptes.data;

import java.io.Serializable;

public class SubTransaction implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private double amount;
	private String description;
	private Category category;

	public SubTransaction(double amount, String description, Category category) {
		super();
		this.amount = amount;
		this.description = description;
		this.category = category;
	}
	public double getAmount() {
		return amount;
	}
	public String getDescription() {
		return description;
	}
	public Category getCategory() {
		return category;
	}
	@Override
	public Object clone() {
		return new SubTransaction(amount, description, category);
	}
	
	@Override
	public String toString() {
		return "[{"+description+"}{"+category+"}{"+amount+"}]";
	}
}
