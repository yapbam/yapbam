package net.yapbam.data;

import java.util.ArrayList;
import java.util.List;

public class AbstractTransaction implements Cloneable {
	private static long currentId = 0;
	
	private long id;
	private String description;
	private double amount;
	private Account account;
	private Mode mode;
	private Category category;
	private List<SubTransaction> subTransactions;

	public AbstractTransaction(String description, double amount, Account account, Mode mode, Category category, List<SubTransaction> subTransactions) {
		super();
		this.id = currentId++;
		this.description = description;
		this.amount = amount;
		this.account = account;
		this.mode = mode;
		this.category = category;
		this.subTransactions = subTransactions;
	}
	
	@Override
	public Object clone() {
		AbstractTransaction result = null;
		try {
			result = (AbstractTransaction) super.clone();
			result.id = currentId++;
			result.subTransactions = new ArrayList<SubTransaction>();
			for (int i=0;i<getSubTransactionSize();i++) {
				result.subTransactions.add(getSubTransaction(i));
			}
		} catch (CloneNotSupportedException e) {
		}
		return result;
	}

	public Account getAccount() {
		return this.account;
	}

	public String getDescription() {
		return this.description;
	}

	public double getAmount() {
		return this.amount;
	}

	public Mode getMode() {
		return mode;
	}

	public Category getCategory() {
		return category;
	}

	public int getSubTransactionSize() {
		return this.subTransactions.size();
	}

	public SubTransaction getSubTransaction(int index) {
		return this.subTransactions.get(index);
	}
	
	public SubTransaction[] getSubTransactions() {
		return this.subTransactions.toArray(new SubTransaction[this.subTransactions.size()]);
	}

	public long getId() {
		return id;
	}

	public double getComplement() {
		double result = getAmount();
		for (int i = 0; i < getSubTransactionSize(); i++) {
			result -= getSubTransaction(i).getAmount();
		}
		return result;
	}

	/** check if the transaction or one of its subtransaction has a specified category.
	 * @param category The category to check
	 * @return true if the category is used
	 */
	public boolean hasCategory(Category category) {
		if (getCategory().equals(category)) return true;
		for (int j = 0; j < getSubTransactionSize(); j++) {
			if (getSubTransaction(j).getCategory().equals(category)) return true;
		}
		return false;
	}
	
	List<SubTransaction> changeSubTransactions(Category oldCategory, Category newCategory) {
		List<SubTransaction> subTransactions = new ArrayList<SubTransaction>(getSubTransactionSize());
		for (int i = 0; i < getSubTransactionSize(); i++) {
			SubTransaction sub = getSubTransaction(i);
			subTransactions.add(sub.getCategory().equals(oldCategory) ?
					new SubTransaction(sub.getAmount(), sub.getDescription(), newCategory) : sub);
		}
		return subTransactions;
	}
}