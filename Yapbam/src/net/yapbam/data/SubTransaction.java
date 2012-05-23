package net.yapbam.data;

import java.io.Serializable;

/** A subtransaction.
 * A single "physical" bank transactions may be composed of many "logical" transactions. Imagine you go to the supermarket
 * and buy food and clothes. You will have one bank transaction but you may want to have two subtransactions,
 * one for the food (with a food category) and another for the clothes (with another category)
 */
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
	
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "[{"+description+"}{"+category+"}{"+amount+"}]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
