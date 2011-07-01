package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;
import net.yapbam.util.NullUtils;
import net.yapbam.util.TextMatcher;

/** The filtered Data (the global data viewed through a filter).
 * </BR>A filter is based on all the attributes of a transaction (amount, category, account, ...).
 * </BR>Please note that a transaction is ok for a filter when, of course, the transaction itself is ok,
 * but also when one of its subtransactions is ok.
 * </BR>For instance if your filter is set to display only receipts of the category x, and you have
 * an expense transaction and category y with an expense subtransaction of category x, the whole
 * transaction would be considered as ok.
 * @see GlobalData
 */
public class FilteredData extends DefaultListenable implements Observer {
	private static boolean DEBUG = false;
	
	public static final int ALL = -1;
	private static final int CHECKED_MASK = (ALL ^ Filter.CHECKED) ^ Filter.NOT_CHECKED;
	private static final int NATURE_MASK = (ALL ^ Filter.EXPENSES) ^ Filter.RECEIPTS;

	private GlobalData data;
	private ArrayList<Transaction> transactions;
	private Comparator<Transaction> comparator = TransactionComparator.INSTANCE;
	private BalanceData balanceData;
	private boolean suspended;
	private boolean filteringHasToBeDone;
	private Filter filter;
	
	public FilteredData(GlobalData data) {
		this.data = data;
		this.filter = new Filter();
		this.filter.addObserver(this);
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				//FIXME be aware of mode removal
				if (eventImplySorting(event)) Collections.sort(transactions, comparator);
				if (event instanceof EverythingChangedEvent) {
					suspended = true;
					filter.clear(); // If everything changed, reset the filter
					suspended = false;
					filter();
				} else if (event instanceof AccountRemovedEvent) {
					Account account = ((AccountRemovedEvent)event).getRemoved();
					if ((filter.getValidAccounts()==null) || filter.getValidAccounts().remove(account)) {
						double initialBalance = account.getInitialBalance();
						balanceData.updateBalance(initialBalance, false);
						fireEvent(new AccountRemovedEvent(FilteredData.this, -1, account)); //TODO index is not the right one
					}
				} else if (event instanceof CategoryRemovedEvent) {
					Category category = ((CategoryRemovedEvent)event).getRemoved();
					if ((filter.getValidCategories()==null) || filter.getValidCategories().remove(category)) {
						fireEvent(new CategoryRemovedEvent(FilteredData.this, -1, category)); //TODO index is not the right one
					}
				} else if (event instanceof TransactionsAddedEvent) {
					Transaction[] ts = ((TransactionsAddedEvent)event).getTransactions();
					Collection<Transaction> accountOkTransactions = new ArrayList<Transaction>(ts.length);
					Collection<Transaction> okTransactions = new ArrayList<Transaction>(ts.length);
					double addedAmount = 0.0;
					for (Transaction transaction : ts) {
						if (filter.isOk(transaction.getAccount())) { // If the added transaction match with the account filter
							Date valueDate = transaction.getValueDate();
							if (NullUtils.compareTo(valueDate, getValueDateFrom(),true)<0) {
								addedAmount += transaction.getAmount();
							} else {
								accountOkTransactions.add(transaction);
								if (isOk(transaction)) { // If the added transaction matches with the whole filter
									okTransactions.add(transaction);
									int index = -Collections.binarySearch(transactions, transaction, comparator)-1;
									transactions.add(index, transaction);
								}
							}
						}
					}
					balanceData.updateBalance(addedAmount, true);
					// If some transactions in a valid account were removed, update the balance data
					if (accountOkTransactions.size()>0) balanceData.updateBalance(accountOkTransactions.toArray(new Transaction[accountOkTransactions.size()]), true);
					// If some valid transactions were removed, fire an event.
					if (okTransactions.size()>0) fireEvent(new TransactionsAddedEvent(FilteredData.this, okTransactions.toArray(new Transaction[okTransactions.size()])));
				} else if (event instanceof TransactionsRemovedEvent) {
					Transaction[] ts = ((TransactionsRemovedEvent)event).getRemoved();
					Collection<Transaction> accountOkTransactions = new ArrayList<Transaction>(ts.length);
					Collection<Transaction> okTransactions = new ArrayList<Transaction>(ts.length);
					double addedAmount = 0.0;
					for (Transaction transaction : ts) {
						if (filter.isOk(transaction.getAccount())) {
							Date valueDate = transaction.getValueDate();
							if (NullUtils.compareTo(valueDate, getValueDateFrom(),true)<0) {
								addedAmount -= transaction.getAmount();
							} else {
								accountOkTransactions.add(transaction);
								if (isOk(transaction)) { // If the added transaction matches with the whole filter
									okTransactions.add(transaction);
									int index = Collections.binarySearch(transactions, transaction, comparator);
									transactions.remove(index);
								}
							}
						}
					}
					balanceData.updateBalance(addedAmount, true);
					// If some transactions in a valid account were removed, update the balance data
					if (accountOkTransactions.size()>0) balanceData.updateBalance(accountOkTransactions.toArray(new Transaction[accountOkTransactions.size()]), false);
					// If some valid transactions were removed, fire an event.
					if (okTransactions.size()>0) fireEvent(new TransactionsRemovedEvent(FilteredData.this, okTransactions.toArray(new Transaction[okTransactions.size()])));
				} else if (event instanceof AccountAddedEvent) {
					Account account = ((AccountAddedEvent)event).getAccount();
					if (filter.isOk(account)) {
						balanceData.updateBalance(account.getInitialBalance(), true);
						if (filter.isOk(Filter.CHECKED)) {
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
					if (filter.isOk(evt.getAccount())) {
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
					System.out.println ("Be aware "+event+" is not propagated by the fileredData"); //FIXME Not sure it's really a bug
				}
			}
		});
		this.balanceData = new BalanceData();
		this.filteringHasToBeDone = false;
		this.suspended = false;
		this.filter();
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
				filter.isOk(((AccountPropertyChangedEvent)event).getAccount());
		boolean categoryRenamed = (event instanceof CategoryPropertyChangedEvent) &&
			isOk(((CategoryPropertyChangedEvent)event).getCategory());
		boolean modeRenamed = (event instanceof ModePropertyChangedEvent) &&
		((((ModePropertyChangedEvent)event).getChanges() & ModePropertyChangedEvent.NAME)!=0) &&
		isOk(((ModePropertyChangedEvent)event).getNewMode());
		boolean result = (accountRenamed || categoryRenamed || modeRenamed);
		return result;
	}
	
	public Filter getFilter() {
		return this.filter;
	}
	
	public void setFilter(Filter filter) {
		if (filter!=this.filter) {
			// if the instance as really changed
			// Stop looking for changes on the old instance
			this.filter.deleteObserver(this);
			this.filter = filter;
			this.filter.addObserver(this);
			this.filter();
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println ("the filter has changed");
		this.filter();
	}
	
	public boolean isOk(Mode mode) {
		return (this.filter.getValidModes()==null) || (this.filter.getValidModes().contains(mode));
	}
	
	/** Sets the valid modes for this filter.
	 * There's no side effect between this instance and the argument array.
	 * @param modes the modes that are allowed (null to allow all modes).
	 */
	public void setModes(Mode[] modes) {
		if (modes==null) {
			this.filter.setValidModes(null);
		} else {
			this.filter.setValidModes(new ArrayList<Mode>(modes.length));
			for (Mode mode : modes) {
				this.filter.getValidModes().add(mode);
			}
		}
		this.filter();
	}
	
	/** Gets the valid modes for this filter.
	 * There's no side effect between this instance and the returned array.
	 * @return the valid modes (null means, all modes are ok).
	 */
	public Mode[] getModes() {
		if (this.filter.getValidModes()==null) return null;
		Mode[] result = new Mode[filter.getValidModes().size()];
		Iterator<Mode> iterator = filter.getValidModes().iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iterator.next();
		}
		return result;
	}

	/** Gets a transaction's validity.
	 * Note about subtransactions : A transaction is also valid if one of its subtransactions,
	 *  considered as transaction (completed with transactions's date, statement, etc ...), is valid. 
	 * @param transaction The transaction to test.
	 * @return true if the transaction is valid.
	 */
	public boolean isOk(Transaction transaction) {
		if (!filter.isOk(transaction.getAccount())) return false;
		if (!isOk(transaction.getMode())) return false;
		if (!isStatementOk(transaction)) return false;
		if (!filter.isOk((transaction.getStatement()==null)?Filter.NOT_CHECKED:Filter.CHECKED)) return false;
		if (!isNumberOk(transaction.getNumber())) return false;
		if ((getDateFrom()!=null) && (transaction.getDate().compareTo(getDateFrom())<0)) return false;
		if ((getDateTo()!=null) && (transaction.getDate().compareTo(getDateTo())>0)) return false;
		if ((getValueDateFrom()!=null) && (transaction.getValueDate().compareTo(getValueDateFrom())<0)) return false;
		if ((getValueDateTo()!=null) && (transaction.getValueDate().compareTo(getValueDateTo())>0)) return false;
		if (isOk(transaction.getCategory()) && filter.isAmountOk(transaction.getAmount()) && filter.isDescriptionOk(transaction.getDescription())) return true;
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
		return isOk(subtransaction.getCategory()) && filter.isAmountOk(subtransaction.getAmount()) && filter.isDescriptionOk(subtransaction.getDescription());
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
		if ((transaction.getSubTransactionSize()!=0) && (GlobalData.AMOUNT_COMPARATOR.compare(amount,0.0)==0)) return false;
		return isOk(transaction.getCategory()) && filter.isAmountOk(amount) && filter.isDescriptionOk(transaction.getDescription());
	}
	
	/** Set the valid categories for this filter.
	 * There's no side effect between this instance and the argument array.
	 * @param categories the categories that are allowed (null or the complete list of categories to allow all categories).
	 */
	public void setCategories(Category[] categories) {
		if ((categories==null) || (categories.length==data.getCategoriesNumber())) {
			this.filter.setValidCategories(null);
		} else {
			this.filter.setValidCategories(new HashSet<Category>(categories.length));
			for (Category category : categories) {
				this.filter.getValidCategories().add(category);
			}
		}
		this.filter();
	}

	/** Returns the valid categories for this filter.
	 * There's no side effect between this instance and the returned array.
	 * @return the valid categories (null means, all categories are ok).
	 */
	public Category[] getCategories() {
		if (this.filter.getValidCategories()==null) return null;
		Category[] result = new Category[filter.getValidCategories().size()];
		Iterator<Category> iterator = filter.getValidCategories().iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iterator.next();
		}
		return result;
	}
	
	public boolean isOk(Category category) {
		return (this.filter.getValidCategories()==null) || (this.filter.getValidCategories().contains(category));
	}

	/** Sets the integer filter
	 * </BR>Boolean attributes like ("is the transaction a EXPENSE ?" or "Is the transaction checked ?"
	 *  are managed with integer codes.
	 *  @param the property
	 *  @see #setStatementFilter(int, TextMatcher)
	 *  @see #setNatureFilter(int)
	 */
	private void setFilter(int property) {
		if (DEBUG) System.out.println("---------- setFilter("+Integer.toBinaryString(property)+") ----------");
		int mask = ALL;
		if (((property & Filter.CHECKED) != 0) || ((property & Filter.NOT_CHECKED) != 0)) mask = mask & CHECKED_MASK;
		if (((property & Filter.EXPENSES) != 0) || ((property & Filter.RECEIPTS) != 0)) mask = mask & NATURE_MASK;
		if (mask == ALL) throw new IllegalArgumentException();
		if (DEBUG) System.out.println(Integer.toBinaryString(mask));//CU
		this.filter.setFilter((this.filter.getFilter() & mask) | property);
		if (DEBUG) System.out.println("filter : "+this.filter.getFilter());
		filter();
	}
	
	public void setStatementFilter (int property, TextMatcher statementFilter) {
		if (((property & Filter.CHECKED) == 0) && (statementFilter!=null)) {
			throw new IllegalArgumentException();
		}
		this.filter.setStatementMatcher(statementFilter);
		setFilter(property & (Filter.CHECKED+Filter.NOT_CHECKED));
	}
		
	public boolean isStatementOk(Transaction transaction) {
		String statement = transaction.getStatement();
		if (statement==null) { // Not checked transaction
			return filter.isOk(Filter.NOT_CHECKED);
		} else { // Checked transaction
			if (!filter.isOk(Filter.CHECKED)) return false;
			if (filter.getStatementMatcher()==null) return true;
			return filter.getStatementMatcher().matches(statement);
		}
	}
	
	public TextMatcher getNumberFilter () {
		return this.filter.getNumberMatcher();
	}
	
	/** Gets the validity of a string according to the current number filter. 
	 * @param number The string to test
	 * @return true if the number is ok with the filter.
	 */
	private boolean isNumberOk(String number) {
		return this.filter.getNumberMatcher()==null?true:this.filter.getNumberMatcher().matches(number);
	}
		
	/** Sets the filter on transaction date.
	 * @param from transactions strictly before <i>from</i> are rejected. A null date means "beginning of times".
	 * @param to transactions strictly after <i>to</i> are rejected. A null date means "end of times". 
	 */
	public void setDateFilter(Date from, Date to) {
		this.filter.setDateFrom(from);
		this.filter.setDateTo(to);
		filter();
	}
	
	/** Gets the transaction date before which all transactions are rejected.
	 * @return a transaction date or null if there's no time limit. 
	 */
	public Date getDateFrom() {
		return this.filter.getDateFrom();
	}
	
	/** Gets the transaction date after which all transactions are rejected.
	 * @return a transaction date or null if there's no time limit. 
	 */
	public Date getDateTo() {
		return this.filter.getDateTo();
	}

	/** Sets the filter on transaction value date.
	 * @param from transactions with value date strictly before <i>from</i> are rejected. A null date means "beginning of times".
	 * @param to transactions with value date strictly after <i>to</i> are rejected. A null date means "end of times". 
	 */
	public void setValueDateFilter(Date from, Date to) {
		this.filter.setValueDateFrom(from);
		this.filter.setValueDateTo(to);
		filter();
	}
	
	/** Gets the transaction value date before which all transactions are rejected.
	 * @return a transaction value date or null if there's no time limit. 
	 */
	public Date getValueDateFrom() {
		return this.filter.getValueDateFrom();
	}
	
	/** Gets the transaction value date after which all transactions are rejected.
	 * @return a transaction value date or null if there's no time limit. 
	 */
	public Date getValueDateTo() {
		return this.filter.getValueDateTo();
	}
	
	/** Sets the transaction minimum and maximum amounts.
	 * <BR>Note that setting this filter never change the expense/receipt filter.
	 * @param minAmount The minimum amount (a positive or null double).
	 * @param maxAmount The maximum amount (Double.POSITIVE_INFINITY to set no high limit).
	 * @param mask An integer that codes if expenses or receipts, or both are ok.
	 * <br>Note that only EXPENSES, RECEIPTS and EXPENSES+RECEIPTS constants are valid arguments.
	 * Any other integer codes (for instance CHECKED) are ignored.
	 * @throws IllegalArgumentException if minAmount > maxAmount or if minimum amount is negative
	 * @see #setFilter(int)
	 */
	public void setAmountFilter(int mask, double minAmount, double maxAmount) {
		if (minAmount>maxAmount) throw new IllegalArgumentException();
		if (minAmount<0) throw new IllegalArgumentException();
		this.filter.setMinAmount(minAmount);
		this.filter.setMaxAmount(maxAmount);
		this.setFilter(mask & (Filter.EXPENSES+Filter.RECEIPTS));
		filter();
	}
	
	/** Gets the transaction minimum amount.
	 * <br>Please note that the minimum amount is always a positive or null number. 
	 * @return the minimum amount (0.0 if there's no low limit).
	 */
	public double getMinimumAmount() {
		return this.filter.getMinAmount();
	}
	
	/** Gets the transaction maximum amount.
	 * @return the maximum amount (Double.POSITIVE_INFINITY if there's no high limit).
	 */
	public double getMaximumAmount() {
		return this.filter.getMaxAmount();
	}
	
	private void filter() {
		if (this.suspended) {
			this.filteringHasToBeDone = true;
		} else {
			double initialBalance = 0;
			for (int i = 0; i < this.getGlobalData().getAccountsNumber(); i++) {
				Account account = this.getGlobalData().getAccount(i);
				if (filter.isOk(account)) initialBalance += account.getInitialBalance();
			}
			balanceData.enableEvents(false);
			balanceData.clear(initialBalance);
			this.transactions = new ArrayList<Transaction>();
			Collection<Transaction> balanceTransactions = new ArrayList<Transaction>(data.getTransactionsNumber());
			double addedAmount = 0.0;
			for (int i = 0; i < data.getTransactionsNumber(); i++) {
				Transaction transaction = data.getTransaction(i);
				if (filter.isOk(transaction.getAccount())) {
					Date valueDate = transaction.getValueDate();
					if (NullUtils.compareTo(valueDate, getValueDateFrom(),true)<0) {
						addedAmount += transaction.getAmount();
					} else {
						// Here we have a hard choice to make: 
						// Ignore the transactions with a value date after the upper limit of the filter or not.
						// In the first case, users may be surprised that transactions excluded by the filter are taken into account
						// In the second one, the balance history after the filter upper limit is WRONG, and its probably dangerous !!!
						// Especially, if the end date is before today, the current balance will be false and be displayed false in the transactions panel. 
						// Uncomment the test to implement the second one.
						/*if (NullUtils.compareTo(valueDate, getValueDateTo(), false)<=0)*/ balanceTransactions.add(transaction);
						if (isOk(transaction)) {
							int index = -Collections.binarySearch(transactions, transaction, comparator)-1;
							transactions.add(index, transaction);
						}
					}
				}
			}
			balanceData.updateBalance(addedAmount, true);
			balanceData.updateBalance(balanceTransactions.toArray(new Transaction[balanceTransactions.size()]), true);
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
}