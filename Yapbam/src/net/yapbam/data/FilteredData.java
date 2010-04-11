package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;
import net.yapbam.util.TextMatcher;

/** The global data class represents the whole data of a Yapbam running instance.
 * This class represents a filter on that data retaining only the account(s) and transaction(s) the user choose to view.
 * The only event sent is the FilterChanged event.
 */
public class FilteredData extends DefaultListenable {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	public static final int CHECKED=1;
	public static final int NOT_CHECKED=2;
	private static final int ALL = -1;
	private static final int CHECKED_MASK = (ALL ^ CHECKED) ^ NOT_CHECKED;

	private GlobalData data;
	private ArrayList<Transaction> transactions;
	private int filter;
	private HashSet<Account> validAccounts;
	private List<Mode> validModes;
	private HashSet<Category> validCategories;
	private Date dateFrom;
	private Date dateTo;
	private Date valueDateFrom;
	private Date valueDateTo;
	private double minAmount;
	private double maxAmount;
	private TextMatcher descriptionMatcher;
	private TextMatcher numberMatcher;
	private TextMatcher statementMatcher;
	
	private Comparator<Transaction> comparator = TransactionComparator.INSTANCE;
	private BalanceData balanceData;
	private boolean suspended;
	private boolean filteringHasToBeDone;
	
	public FilteredData(GlobalData data) {
	    this.data = data;
	    this.data.addListener(new DataListener() {		
			@Override
			public void processEvent(DataEvent event) {
				//FIXME be aware of mode removal
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
				} else if (event instanceof ModePropertyChangedEvent) {
					ModePropertyChangedEvent evt = (ModePropertyChangedEvent) event;
					if (isOk(evt.getNewMode())) {
						fireEvent(event);
					}
				} else if (event instanceof NeedToBeSavedChangedEvent) {
					fireEvent(event);
				} else {
					System.out.println ("Be aware "+event+" is not propagated by the fileredData"); //FIXME
				}
			}
		});
	    this.balanceData = new BalanceData();
	    this.filteringHasToBeDone = false;
	    this.suspended = false;
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
		boolean modeRenamed = (event instanceof ModePropertyChangedEvent) &&
		((((ModePropertyChangedEvent)event).getChanges() & ModePropertyChangedEvent.NAME)!=0) &&
		isOk(((ModePropertyChangedEvent)event).getNewMode());
		boolean result = (accountRenamed || categoryRenamed || modeRenamed);
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
		this.validModes = null;
		this.minAmount = Double.NEGATIVE_INFINITY;
		this.maxAmount = Double.POSITIVE_INFINITY;
		this.descriptionMatcher = null;
		this.numberMatcher = null;
		this.statementMatcher = null;
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
	
	public boolean isOk(Mode mode) {
		return (this.validModes==null) || (this.validModes.contains(mode));
	}
	
	/** Sets the valid modes for this filter.
	 * There's no side effect between this instance and the argument array.
	 * @param modes the modes that are allowed (null to allow all modes).
	 */
	public void setModes(Mode[] modes) {
		if (modes==null) {
			this.validModes=null;
		} else {
			this.validModes = new ArrayList<Mode>(modes.length);
			for (int i = 0; i < modes.length; i++) {
				this.validModes.add(modes[i]);
			}
		}
		this.filter();
	}
	
	/** Gets the valid modes for this filter.
	 * There's no side effect between this instance and the returned array.
	 * @return the valid modes (null means, all modes are ok).
	 */
	public Mode[] getModes() {
		if (this.validModes==null) return null;
		Mode[] result = new Mode[validModes.size()];
		Iterator<Mode> iterator = validModes.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iterator.next();
		}
		return result;
	}

	/** Sets the description filter.
	 * @param matcher a TextMatcher instance or null to apply no filter on description
	 */
	public void setDescriptionFilter(TextMatcher matcher) {
		this.descriptionMatcher = matcher;
		this.filter();
	}
	
	/** Gets the validity of a string according to the current description filter. 
	 * @param description The string to test
	 * @return true if the description is ok with the filter.
	 */
	private boolean isDescriptionOk(String description) {
		return this.descriptionMatcher==null?true:this.descriptionMatcher.matches(description);
	}
	
	/** Gets the description filter.
	 * @return a TextMatcher or null if there is no description filter
	 */
	public TextMatcher getDescriptionFilter() {
		return this.descriptionMatcher;
	}
	
	/** Gets a transaction's validity.
	 * Note about subtransactions : A transaction is also valid if one of its subtransactions,
	 *  considered as transaction (completed with transactions's date, statement, etc ...), is valid. 
	 * @param transaction The transaction to test.
	 * @return true if the transaction is valid.
	 */
	public boolean isOk(Transaction transaction) {
		if (!isOk(transaction.getAccount())) return false;
		if (!isOk(transaction.getMode())) return false;
		if (!isStatementOk(transaction)) return false;
		if (!isOk((transaction.getStatement()==null)?NOT_CHECKED:CHECKED)) return false;
		if (!isNumberOk(transaction.getNumber())) return false;
		if ((getDateFrom()!=null) && (transaction.getDate().compareTo(getDateFrom())<0)) return false;
		if ((getDateTo()!=null) && (transaction.getDate().compareTo(getDateTo())>0)) return false;
		if ((getValueDateFrom()!=null) && (transaction.getValueDate().compareTo(getValueDateFrom())<0)) return false;
		if ((getValueDateTo()!=null) && (transaction.getValueDate().compareTo(getValueDateTo())>0)) return false;
		if (isOk(transaction.getCategory()) && (transaction.getAmount()>=getMinimumAmount()) &&
				(transaction.getAmount()<=getMaximumAmount()) && isDescriptionOk(transaction.getDescription())) return true;
		// The transaction may also be valid if one of its subtransactions is valid 
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			if (isOk(transaction.getSubTransaction(i))) {
				return true;
			}
			if (isComplementOk(transaction)) return true;
		}
		return false;
	}
	
	/** Gets a subtransaction validity.
	 * @param subtransaction the subtransaction to test
	 * @return true if the subtransaction is valid according to this filter.
	 * Be aware that no specific fields of the transaction are tested, so the subtransaction may be valid
	 * even if its transaction is not (for instance if its payment mode is not ok). So, usually, you'll have
	 * to also test the transaction.
	 * @see #isOk(Transaction)
	 */
	public boolean isOk(SubTransaction subtransaction) {
		boolean amountOk = (subtransaction.getAmount()>=getMinimumAmount()) && (subtransaction.getAmount()<=getMaximumAmount());
		return isOk(subtransaction.getCategory()) && amountOk && isDescriptionOk(subtransaction.getDescription());
	}
	
	/** Gets a transaction complement validity.
	 * @param transaction the transaction to test
	 * @return true if the transaction complement is valid according to this filter.
	 * Be aware that the complement is considered as a subtransaction. So the behaviour is the same
	 * than in isOk(Subtransaction) method. No specific fields of the transaction are tested, so the complement
	 * may be valid even if the whole transaction is not (for instance if its payment mode is not ok).
	 * So, usually, you'll have to also test the transaction.
	 * @see #isOk(Transaction)
	 */
	public boolean isComplementOk(Transaction transaction) {
		double amount = transaction.getComplement();
		if ((transaction.getSubTransactionSize()!=0) && (amount==0)) return false;
		boolean amountOk = (amount>=getMinimumAmount()) && (amount<=getMaximumAmount());
		return isOk(transaction.getCategory()) && amountOk && isDescriptionOk(transaction.getDescription());
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
	
	private void setFilter(int property) {
		if (DEBUG) System.out.println("---------- setFilter("+Integer.toBinaryString(property)+") ----------");
		int mask = ALL;
		if (((property & CHECKED) != 0) || ((property & NOT_CHECKED) != 0)) mask = mask & CHECKED_MASK;
		if (mask == ALL) throw new IllegalArgumentException();
		if (DEBUG) System.out.println(Integer.toBinaryString(mask));//CU
		this.filter = (this.filter & mask) | property;
		if (DEBUG) System.out.println("filter : "+this.filter);
		filter();
	}
	
	public void setStatementFilter (int property, TextMatcher statementFilter) {
		if (((property & CHECKED) == 0) && (statementFilter!=null)) {
			throw new IllegalArgumentException();
		}
		this.statementMatcher = statementFilter;
		setFilter(property);
	}
	
	public TextMatcher getStatementFilter () {
		return this.statementMatcher;
	}
	
	public boolean isStatementOk(Transaction transaction) {
		String statement = transaction.getStatement();
		if (statement==null) { // Not checked transaction
			return isOk(NOT_CHECKED);
		} else { // Checked transaction
			if (!isOk(CHECKED)) return false;
			if (statementMatcher==null) return true;
			return statementMatcher.matches(statement);
		}
	}
	
	public void setNumberFilter (TextMatcher numberFilter) {
		this.numberMatcher = numberFilter;
		filter();
	}
	
	public TextMatcher getNumberFilter () {
		return this.numberMatcher;
	}
	
	/** Gets the validity of a string according to the current number filter. 
	 * @param number The string to test
	 * @return true if the number is ok with the filter.
	 */
	private boolean isNumberOk(String number) {
		return this.numberMatcher==null?true:this.numberMatcher.matches(number);
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
	
	/** Sets the transaction minimum and maximum amounts.
	 * Note that setting this filter may change the expense/receipt filter.
	 * @param minAmount The minimum amount.
	 * @param maxAmount The maximum amount.
	 * @throws IllegalArgumentException if minAmount > maxAmount
	 * @see #setFilter(int)
	 */
	public void setAmountFilter(double minAmount, double maxAmount) {
		if (minAmount>maxAmount) throw new IllegalArgumentException();
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		filter();
	}
	
	/** Gets the transaction minimum amount.
	 * @return the minimum amount (Double.NEGATIVE_INFINITY if there's no low limit).
	 */
	public double getMinimumAmount() {
		return this.minAmount;
	}
	
	/** Gets the transaction maximum amount.
	 * @return the maximum amount (Double.POSITIVE_INFINITY if there's no high ow limit).
	 */
	public double getMaximumAmount() {
		return this.maxAmount;
	}

	private void filter() {
		if (this.suspended) {
			this.filteringHasToBeDone = true;
		} else {
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
	}

	/** Gets the number of transactions that match the filter. 
	 * @return number of transactions that match the filter
	 */
	public int getTransactionsNumber() {
		return this.transactions.size();
	}

	/** Gets a transactions that match the filter.
	 * @param index the index of the transaction (between 0 and getTransactionsNumber())
	 * @return the transaction.
	 */
	public Transaction getTransaction(int index) {
		return this.transactions.get(index);
	}
	
	/** Find the index of a transaction that matches the filter.
	 * @param transaction the transaction to find
	 * @return a negative integer if the transaction doesn't match ths filter,
	 * the index of the transaction if the transaction matches. 
	 */
	public int indexOf(Transaction transaction) {
		return Collections.binarySearch(transactions, transaction, comparator);
	}

	/** Gets the unfiltered data on which is based this FilteredData.
	 * @return the GlobalData instance
	 */
	public GlobalData getGlobalData() {
		return this.data;
	}
	
	/** Sets the suspended state of the filter.
	 * When the filter is suspended, the filter changes don't automatically refresh the transaction list,
	 * and no event is fire.
	 * This refresh (and the event) is delayed until this method is called with false argument.
	 * Note that if this method is called with false argument, but no filter change occurs, nothing happens.
	 * @param suspended true to suspend auto-filtering, false to restore it.
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
		if (!suspended && this.filteringHasToBeDone) filter();
	}
	
	/** Gets the suspended state of this filter.
	 * @return true if the filtering is suspended.
	 * @see #setSuspended(boolean)
	 */
	public boolean isSuspended() {
		return this.suspended;
	}

	/** Tests whether the filter filter something or not.
	 * @return false if no filter is set. Returns true if a filter is set
	 * even if it doesn't filter anything.
	 */
	public boolean hasFilter() {
		this.filter = ALL;
		this.dateFrom = null;
		this.dateTo = null;
		this.valueDateFrom = null;
		this.valueDateTo = null;
		this.validCategories = null;
		this.validModes = null;
		this.minAmount = Double.NEGATIVE_INFINITY;
		this.maxAmount = Double.POSITIVE_INFINITY;
		this.descriptionMatcher = null;
		this.numberMatcher = null;
		this.statementMatcher = null;
		return (filter!=ALL) || (dateFrom!=null) || (dateTo != null) || (valueDateFrom!=null) || (valueDateTo != null) ||
			(validCategories !=null) || (validModes != null) || (validAccounts!=null) ||
			(minAmount!=Double.NEGATIVE_INFINITY) || (maxAmount!=Double.POSITIVE_INFINITY) ||
			(descriptionMatcher!=null) || (numberMatcher!=null) || (statementMatcher!=null);
	}
}