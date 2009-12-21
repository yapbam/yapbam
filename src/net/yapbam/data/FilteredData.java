package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;

/** The global data class represents the whole data of a Yapbam running instance.
 * This class represents a filter on that data retaining only the account(s) and transaction(s) the user choose to view.
 * The only event sent is the FilterChanged event.
 */
public class FilteredData extends DefaultListenable {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	public static final int CHECKED=1;
	public static final int NOT_CHECKED=2;
	public static final int RECEIPT=4;
	public static final int EXPENSE=8;
	private static final int ALL = -1;
	private static final int CHECKED_MASK = (ALL ^ CHECKED) ^ NOT_CHECKED;
	private static final int EXPENSE_RECEIPT_MASK = (ALL ^ RECEIPT) ^ EXPENSE;

	private GlobalData data;
	private ArrayList<Transaction> transactions;
	private int filter;
	private HashSet<Account> validAccounts;
	private HashSet<Category> validCategories;
	private Date dateFrom;
	private Date dateTo;
	private Date valueDateFrom;
	private Date valueDateTo;
	private Comparator<Transaction> comparator = TransactionComparator.INSTANCE;
	private BalanceData balanceData;
	
	public FilteredData(GlobalData data) {
	    this.data = data;
	    this.data.addListener(new DataListener() {		
			@Override
			public void processEvent(DataEvent event) {
				if (eventImplySorting(event)) Collections.sort(transactions, comparator);
				if (event instanceof EverythingChangedEvent) {
					clear(); // If everything changed, reset the filter
					fireEvent(new EverythingChangedEvent(FilteredData.this));
				} else if (event instanceof AccountRemovedEvent) {
					Account account = ((AccountRemovedEvent)event).getRemoved();
					if ((validAccounts==null) || validAccounts.remove(account)) {
						double initialBalance = account.getInitialBalance();
						balanceData.updateBalance(initialBalance, false);
						fireEvent(new AccountRemovedEvent(FilteredData.this, -1, account)); //TODO index is not the right one
					}
				} else if (event instanceof CategoryRemovedEvent) {
					Category category = ((CategoryRemovedEvent)event).getRemoved();
					if ((validCategories==null) || validCategories.remove(category)) {
						fireEvent(new CategoryRemovedEvent(FilteredData.this, -1, category)); //TODO index is not the right one
	    			}
				} else if (event instanceof TransactionAddedEvent) {
					Transaction transaction = ((TransactionAddedEvent)event).getTransaction();
					if (isOk(transaction.getAccount())) { // If the added transaction match with the account filter
						balanceData.updateBalance(new Date(), transaction, true);
						if (isOk(transaction)) { // If the added transaction match with the whole filter
							int index = -Collections.binarySearch(transactions, transaction, comparator)-1;
							transactions.add(index, transaction);
							fireEvent(new TransactionAddedEvent(FilteredData.this, transaction));
						}
					}
				} else if (event instanceof TransactionRemovedEvent) {
					Transaction transaction = ((TransactionRemovedEvent)event).getRemoved();
					if (isOk(transaction.getAccount())) {
						balanceData.updateBalance(new Date(), transaction, false);
						int index = Collections.binarySearch(transactions, transaction, comparator);
						if (index>=0) {
							transactions.remove(index);
							fireEvent(new TransactionRemovedEvent(FilteredData.this, index, transaction));
						}
					}
				} else if (event instanceof AccountAddedEvent) {
					Account account = ((AccountAddedEvent)event).getAccount();
					if (isOk(account)) {
						balanceData.updateBalance(account.getInitialBalance(), true);
						if (isOk(CHECKED)) {
							fireEvent(new AccountAddedEvent(FilteredData.this, account));
						}
					}
				} else if (event instanceof CategoryAddedEvent) {
					Category category = ((CategoryAddedEvent)event).getCategory();
					if (isOk(category)) {
						fireEvent(new CategoryAddedEvent(FilteredData.this, category));
					}
				} else if (event instanceof AccountPropertyChangedEvent) {
					AccountPropertyChangedEvent evt = (AccountPropertyChangedEvent) event;
					if (isOk(evt.getAccount())) {
						if (evt.getProperty().equals(AccountPropertyChangedEvent.INITIAL_BALANCE)) {
							double amount = ((Double)evt.getNewValue())-((Double)evt.getOldValue());
							balanceData.updateBalance(amount, true);
						}
						fireEvent(event);
					}
				} else if (event instanceof CategoryPropertyChangedEvent) {
					CategoryPropertyChangedEvent evt = (CategoryPropertyChangedEvent) event;
					if (isOk(evt.getCategory())) {
						fireEvent(event);
					}
				}
			}
		});
	    this.balanceData = new BalanceData();
	    this.clear();
	}
	
	/** Returns the balance data.
	 * The balance data ignores all filters except the one on the accounts.
	 * @return the balance data.
	 */
	public BalanceData getBalanceData() {
		return this.balanceData;
	}
	
	private boolean eventImplySorting (DataEvent event) {
		boolean accountRenamed = (event instanceof AccountPropertyChangedEvent) &&
				((AccountPropertyChangedEvent)event).getProperty().equals(AccountPropertyChangedEvent.NAME) &&
				isOk(((AccountPropertyChangedEvent)event).getAccount());
		boolean categoryRenamed = (event instanceof CategoryPropertyChangedEvent) &&
		isOk(((CategoryPropertyChangedEvent)event).getCategory());
		boolean result = (accountRenamed || categoryRenamed); //TODO other change events
		return result;
	}
	
	/** Erases all filters.
	 */
	public void clear() {
		this.filter = ALL;
		this.dateFrom = null;
		this.dateTo = null;
		this.valueDateFrom = null;
		this.valueDateTo = null;
		this.validCategories = null;
		clearAccounts();
	}

	/** Erases account filter.
	 */
	public void clearAccounts() {
		setAccounts(null);
	}	

	/** Sets the valid accounts for this filter.
	 * There's no side effect between this instance and the argument array.
	 * @param accounts the accounts that are allowed (null or the complete list of accounts to allow all accounts).
	 */
	public void setAccounts(Account[] accounts) {
		if ((accounts==null) || (accounts.length==data.getAccountsNumber())) {
			this.validAccounts=null;
		} else {
			this.validAccounts = new HashSet<Account>(accounts.length);
			for (int i = 0; i < accounts.length; i++) {
				this.validAccounts.add(accounts[i]);
			}
		}
		this.filter();
		fireEvent(new FilterUpdatedEvent(this));
	}
	
	/** Gets the valid accounts for this filter.
	 * There's no side effect between this instance and the returned array.
	 * @return the valid accounts (null means, all accounts are ok).
	 */
	public Account[] getAccounts() {
		if (this.validAccounts==null) return null;
		Account[] result = new Account[validAccounts.size()];
		Iterator<Account> iterator = validAccounts.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iterator.next();
		}
		return result;
	}
	
	public boolean hasFilterAccount() {
		return this.validAccounts != null;
	}

	public boolean isOk(Account account) {
		return (this.validAccounts==null) || (this.validAccounts.contains(account));
	}

	/** Gets a transaction's validity.
	 * Note about categories : A transaction is valid if it or one of its subtransactions has a valid category. 
	 * @param transaction The transaction to test.
	 * @return true if the transaction is valid.
	 */
	public boolean isOk(Transaction transaction) {
		if (!isOk(transaction.getAccount())) return false;
		if (!isOk((transaction.getStatement()==null)?NOT_CHECKED:CHECKED)) return false;
		if ((getDateFrom()!=null) && (transaction.getDate().compareTo(getDateFrom())<0)) return false;
		if ((getDateTo()!=null) && (transaction.getDate().compareTo(getDateTo())>0)) return false;
		if ((getValueDateFrom()!=null) && (transaction.getValueDate().compareTo(getValueDateFrom())<0)) return false;
		if ((getValueDateTo()!=null) && (transaction.getValueDate().compareTo(getValueDateTo())>0)) return false;
		if (isOk(transaction.getCategory()) && isOk((transaction.getAmount()>0)?RECEIPT:EXPENSE)) return true;
		// The transaction may also be valid if one of its subtransactions is valid 
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			if (isOk(transaction.getSubTransaction(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOk(SubTransaction subtransaction) {
		boolean categoryOk = isOk(subtransaction.getCategory());
		boolean amountOk = isOk((subtransaction.getAmount()>0?RECEIPT:EXPENSE));
		return categoryOk && amountOk;
	}
	
	/** Set the valid categories for this filter.
	 * There's no side effect between this instance and the argument array.
	 * @param categories the categories that are allowed (null or the complete list of categories to allow all categories).
	 */
	public void setCategories(Category[] categories) {
		if ((categories==null) || (categories.length==data.getCategoriesNumber())) {
			this.validCategories=null;
		} else {
			this.validCategories = new HashSet<Category>(categories.length);
			for (int i = 0; i < categories.length; i++) {
				this.validCategories.add(categories[i]);
			}
		}
		this.filter();
		fireEvent(new FilterUpdatedEvent(this));
	}

	/** Returns the valid categories for this filter.
	 * There's no side effect between this instance and the returned array.
	 * @return the valid categories (null means, all categories are ok).
	 */
	public Category[] getCategories() {
		if (this.validCategories==null) return null;
		Category[] result = new Category[validCategories.size()];
		Iterator<Category> iterator = validCategories.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iterator.next();
		}
		return result;
	}
	
	public boolean isOk(Category category) {
		return (this.validCategories==null) || (this.validCategories.contains(category));
	}

	public boolean isOk(int property) {
		if (DEBUG) {
			System.out.println("---------- isOK("+Integer.toBinaryString(property)+") ----------");
			System.out.println("filter  : "+Integer.toBinaryString(this.filter));
			System.out.println("result  : "+Integer.toBinaryString(property & this.filter));
		}
		return ((property & this.filter) != 0);
	}
	
	public void setFilter(int property) {
		if (DEBUG) System.out.println("---------- setFilter("+Integer.toBinaryString(property)+") ----------");
		int mask = ALL;
		if (((property & CHECKED) != 0) || ((property & NOT_CHECKED) != 0)) mask = mask & CHECKED_MASK;
		if (((property & RECEIPT) != 0) || ((property & EXPENSE) != 0)) mask = mask & EXPENSE_RECEIPT_MASK;
		if (mask == ALL) throw new IllegalArgumentException();
		if (DEBUG) System.out.println(Integer.toBinaryString(mask));//CU
		this.filter = (this.filter & mask) | property;
		if (DEBUG) System.out.println("filter : "+this.filter);
		filter();
	}
	
	/** Sets the filter on transaction date.
	 * @param from transactions strictly before <i>from</i> are rejected. A null date means "beginning of times".
	 * @param to transactions strictly after <i>to</i> are rejected. A null date means "end of times". 
	 */
	public void setDateFilter(Date from, Date to) {
		this.dateFrom = from;
		this.dateTo = to;
		filter();
	}
	
	/** Gets the transaction date before which all transactions are rejected.
	 * @return a transaction date or null if there's no time limit. 
	 */
	public Date getDateFrom() {
		return this.dateFrom;
	}
	
	/** Gets the transaction date after which all transactions are rejected.
	 * @return a transaction date or null if there's no time limit. 
	 */
	public Date getDateTo() {
		return this.dateTo;
	}

	/** Sets the filter on transaction value date.
	 * @param from transactions with value date strictly before <i>from</i> are rejected. A null date means "beginning of times".
	 * @param to transactions with value date strictly after <i>to</i> are rejected. A null date means "end of times". 
	 */
	public void setValueDateFilter(Date from, Date to) {
		this.valueDateFrom = from;
		this.valueDateTo = to;
		filter();
	}
	
	/** Gets the transaction value date before which all transactions are rejected.
	 * @return a transaction value date or null if there's no time limit. 
	 */
	public Date getValueDateFrom() {
		return this.valueDateFrom;
	}
	
	/** Gets the transaction value date after which all transactions are rejected.
	 * @return a transaction value date or null if there's no time limit. 
	 */
	public Date getValueDateTo() {
		return this.valueDateTo;
	}

	private void filter() {
		double initialBalance = 0;
		for (int i = 0; i < this.getGlobalData().getAccountsNumber(); i++) {
			Account account = this.getGlobalData().getAccount(i);
			if (isOk(account)) initialBalance += account.getInitialBalance();
		}
		balanceData.enableEvents(false);
		balanceData.clear(initialBalance);
	    Date today = new Date();
	    this.transactions = new ArrayList<Transaction>();
	    for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			if (isOk(transaction.getAccount())) {
				balanceData.updateBalance(today, transaction, true);
				if (isOk(transaction)) {
					int index = -Collections.binarySearch(transactions, transaction, comparator)-1;
					transactions.add(index, transaction);
				}
			}
		}
		balanceData.enableEvents(true);
		fireEvent(new EverythingChangedEvent(this));
	}

	public int getTransactionsNumber() {
		return this.transactions.size();
	}

	public Transaction getTransaction(int index) {
		return this.transactions.get(index);
	}
	
	public int indexOf(Transaction transaction) {
		return Collections.binarySearch(transactions, transaction, comparator);
	}

	public GlobalData getGlobalData() {
		return this.data;
	}
}