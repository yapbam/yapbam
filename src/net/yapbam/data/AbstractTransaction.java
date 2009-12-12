package net.yapbam.data;

import java.util.ArrayList;
import java.util.List;

/** This abstract class represents a transaction.
 * A transaction has a description, an amount, an account, a mode, a category and a list of subtransactions.
 */
public abstract class AbstractTransaction implements Cloneable {
	private static long currentId = 0;
	
	private long id;
	private String description;
	private double amount;
	private Account account;
	private Mode mode;
	private Category category;
	private List<SubTransaction> subTransactions;

	/**
	 * Constructor.
	 * @param description The description
	 * @param amount The amount (negative for expenses)
	 * @param account The account
	 * @param mode the payment mode
	 * @param category the category
	 * @param subTransactions a subtransaction list
	 */
	public AbstractTransaction(String description, double amount, Account account, Mode mode, Category category, List<SubTransaction> subTransactions) {
		super();
		if ((mode==null) || (category==null) || (description==null) || (subTransactions==null)) throw new IllegalArgumentException();
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

	/** Returns the transaction's account.
	 * @return the account
	 */
	public Account getAccount() {
		return this.account;
	}

	/** Returns the transaction's description.
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/** Returns the transaction's amount.
	 * @return the amount, a negative number if the transaction is an expense
	 */
	public double getAmount() {
		return this.amount;
	}

	/** Returns the transaction's payment mode.
	 * @return the payment mode
	 */
	public Mode getMode() {
		return mode;
	}

	/** Returns the transaction's category.
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/** Returns the number of transaction's subtransactions.
	 * @return a positive number specifying the number of subtransactions
	 */
	public int getSubTransactionSize() {
		return this.subTransactions.size();
	}

	/** Returns one of the transaction's subtransactions.
	 * @param index the subtransaction's index
	 * @return a subtransaction
	 */
	public SubTransaction getSubTransaction(int index) {
		return this.subTransactions.get(index);
	}
	
	/** Returns the transaction's subtransactions.
	 * @return the subtransactions
	 */
	public SubTransaction[] getSubTransactions() {
		return this.subTransactions.toArray(new SubTransaction[this.subTransactions.size()]);
	}

	/** Returns the transaction's id.
	 * Each transaction has an unique id created in the constructor. It is guaranted that two
	 * transactions always have different id
	 * @return a long that indentifying the transaction
	 */
	public long getId() {
		return id;
	}

	/** Returns the complement of the subtransactions of this transaction.
	 * Transactions may have subtransactions. The complement is the transactions's amount minus the
	 * sum of all of its subtransactions. 
	 * @return the complement.
	 */
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