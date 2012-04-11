package net.yapbam.data;

import java.util.ArrayList;
import java.util.List;

/** An abstract transaction.
 * These transactions have a description, an amount, an account, a mode, a category and a list of subtransactions.
 */
public abstract class AbstractTransaction implements Cloneable {
	private static volatile long currentId = 0;
	
	private long id;
	private String description;
	private String comment;
	private double amount;
	private Account account;
	private Mode mode;
	private Category category;
	private List<SubTransaction> subTransactions;
	
	private static synchronized void setId(AbstractTransaction transaction) {
		if (currentId==Long.MAX_VALUE) throw new RuntimeException("Transaction counter has an overflow"); //$NON-NLS-1$
		transaction.id = currentId++;
	}

	/**
	 * Constructor.
	 * @param description The description
	 * @param comment The comment associated with the transaction
	 * @param amount The amount (negative for expenses)
	 * @param account The account
	 * @param mode the payment mode
	 * @param category the category
	 * @param subTransactions a subtransaction list
	 */
	public AbstractTransaction(String description, String comment, double amount, Account account, Mode mode, Category category, List<SubTransaction> subTransactions) {
		super();
		if ((mode==null) || (category==null) || (description==null) || (subTransactions==null)) throw new IllegalArgumentException();
		this.description = getCachedDescription(description);
		if ((comment!=null) && (comment.isEmpty())) comment = null;
		this.comment = comment;
		this.amount = amount;
		this.account = account;
		this.mode = mode;
		this.category = category;
		this.subTransactions = subTransactions;
		setId(this);
	}
	
	// The following lines are a test to implement a description cache in order to prevent from duplicating same description into memory.
	// In real life, it seems to not have a positive impact (HashMap size is greater than the saved String memory footprint).
	//	private static final WeakHashMap<String, String> descriptionCache = new WeakHashMap<String, String>();
	private String getCachedDescription(String description) {
		return description;
//		String result = descriptionCache.get(description);
//		if (result == null) {
//			result = description;
//			descriptionCache.put(description, description);
//		}
//		return result;
	}

	@Override
	public Object clone() {
		AbstractTransaction result = null;
		try {
			result = (AbstractTransaction) super.clone();
			result.subTransactions = new ArrayList<SubTransaction>();
			for (int i=0;i<getSubTransactionSize();i++) {
				result.subTransactions.add(getSubTransaction(i));
			}
			setId(result);
		} catch (CloneNotSupportedException e) {
		}
		return result;
	}

	/** Gets the transaction's account.
	 * @return the account
	 */
	public Account getAccount() {
		return this.account;
	}

	/** Gets the transaction's description.
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/** Gets the transaction's comment.
	 * @return the comment (null if the transaction has no comment)
	 */
	public String getComment() {
		return this.comment;
	}

	/** Gets the transaction's amount.
	 * @return the amount, a negative number if the transaction is an expense
	 */
	public double getAmount() {
		return this.amount;
	}

	/** Gets the transaction's payment mode.
	 * @return the payment mode
	 */
	public Mode getMode() {
		return mode;
	}

	/** Gets the transaction's category.
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/** Gets the number of transaction's subtransactions.
	 * @return a positive number specifying the number of subtransactions.
	 * This number never includes the complement. 
	 */
	public int getSubTransactionSize() {
		return this.subTransactions.size();
	}

	/** Gets one of the transaction's subtransactions.
	 * @param index the subtransaction's index
	 * @return a subtransaction
	 */
	public SubTransaction getSubTransaction(int index) {
		return this.subTransactions.get(index);
	}
	
	/** Gets the transaction's subtransactions.
	 * @return the subtransactions
	 */
	public SubTransaction[] getSubTransactions() {
		return this.subTransactions.toArray(new SubTransaction[this.subTransactions.size()]);
	}

	/** Gets the transaction's id.
	 * Each transaction has an unique id created in the constructor. It is guaranteed that two
	 * transactions always have different id
	 * @return a long that identifies the transaction
	 */
	public long getId() {
		return id;
	}

	/** Gets the complement of the subtransactions of this transaction.
	 * Transactions may have subtransactions. The complement is the transactions's amount minus the
	 * sum of all of its subtransactions. 
	 * @return the complement.
	 */
	public double getComplement() {
		double result = getAmount();
		for (int i = 0; i < getSubTransactionSize(); i++) {
			result -= getSubTransaction(i).getAmount();
		}
		if (GlobalData.AMOUNT_COMPARATOR.compare(result, 0.0)==0) {
			// See AMOUNT_COMPARATOR comment.
			result = 0.0;
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