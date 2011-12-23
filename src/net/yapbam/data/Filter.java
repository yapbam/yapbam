package net.yapbam.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

import net.yapbam.util.NullUtils;
import net.yapbam.util.TextMatcher;

/** A data filter.
 */
public class Filter extends Observable {
	private static boolean DEBUG = false;
	public static final int CHECKED=1;
	public static final int NOT_CHECKED=2;
	public static final int EXPENSES=4;
	public static final int RECEIPTS=8;
	public static final int ALL = CHECKED+NOT_CHECKED+EXPENSES+RECEIPTS;

	private int filter;
	private HashSet<Account> validAccounts;
	private HashSet<String> validModes;
	private HashSet<Category> validCategories;
	private Date dateFrom;
	private Date dateTo;
	private Date valueDateFrom;
	private Date valueDateTo;
	private double minAmount;
	private double maxAmount;
	private TextMatcher descriptionMatcher;
	private TextMatcher commentMatcher;
	private TextMatcher numberMatcher;
	private TextMatcher statementMatcher;
	
	private boolean suspended;

	/** Constructor. */
	public Filter() {
		init();
		this.suspended = false;
	}

	public boolean isOk(int property) {
		if (DEBUG) {
			System.out.println("---------- isOK("+Integer.toBinaryString(property)+") ----------"); //$NON-NLS-1$ //$NON-NLS-2$
			System.out.println("filter  : "+Integer.toBinaryString(this.filter)); //$NON-NLS-1$
			System.out.println("result  : "+Integer.toBinaryString(property & this.filter)); //$NON-NLS-1$
		}
		return ((property & this.filter) != 0);
	}
	
	@Override
	protected void setChanged() {
		super.setChanged();
		if (!suspended) this.notifyObservers();
	}
	
	/** Sets the suspended state of the filter.
	 * When the filter is suspended, the filter changes don't automatically call the filter's observers.
	 * This refresh (and the event) is delayed until this method is called with false argument.
	 * Note that if this method is called with false argument, but no filter change occurs, nothing happens.
	 * @param suspended true to suspend observers notifications, false to restore it.
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
		if (!this.suspended && this.hasChanged()) this.notifyObservers();
	}

	/** Gets the valid accounts for this filter.
	 * <br>Note: There's no side effect between this instance and the returned array.
	 * @return the valid accounts (null means all accounts are ok).
	 */
	public List<Account> getValidAccounts() {
		if (validAccounts==null) return null;
		ArrayList<Account> result = new ArrayList<Account>(validAccounts.size());
		for (Account account:validAccounts) {
			result.add(account);
		}
		return result;
	}

	public boolean isOk(Account account) {
		return (validAccounts==null) || (validAccounts.contains(account));
	}	

	/** Sets the valid accounts for this filter.
	 * <br>Note: There's no side effect between this instance and the argument array.
	 * @param accounts the accounts that are allowed (null or the complete list of accounts to allow all accounts).
	 */
	public void setValidAccounts(List<Account> accounts) {
		if (!testEquals(accounts, this.validAccounts)) {
			if (accounts==null) {
				validAccounts = null;
			} else {
				validAccounts = new HashSet<Account>(accounts.size());
				this.validAccounts.addAll(accounts);
			}
			setChanged();
		}
	}
	
	private static <T> boolean testEquals(Collection<T> c1, Collection<T> c2) {
		if ((c1==null) && (c2==null)) return true; 
		if ((c1==null) || (c2==null)) return false;
		// Both are not null if we arrive here
		if (c1.size()!=c2.size()) return false;
		for (T element:c1) {
			if (!c2.contains(element)) return false;
		}
		return true;
	}

	/** Gets the valid modes names for this filter.
	 * <br>There's no side effect between this instance and the returned array.
	 * @return the valid modes names (null means, all modes are ok). Mode.Undefined is identified by an empty String in the returned list 
	 * @see #setValidModes(List)
	 */
	public List<String> getValidModes() {
		if (validModes==null) return null;
		ArrayList<String> result = new ArrayList<String>(validModes.size());
		for (String name:validModes) {
			result.add(name);
		}
		return result;
	}
	
	/** Tests whether a mode is valid or not.
	 * @param mode The mode to test
	 * @return true if the mode is valid
	 * @see #setValidModes(List)
	 */
	public boolean isOk(Mode mode) {
		if (validModes==null) return true;
		String name = mode.equals(Mode.UNDEFINED)?"":mode.getName(); //$NON-NLS-1$
		return (validModes.contains(name));
	}

	/** Sets the valid modes names for this filter.
	 * <br>There's no side effect between this instance and the argument of this method.
	 * @param validModes null to enable all modes, or a list of valid mode's names.
	 * Be aware of the Mode.UNDEFINED, as its name depends on the localization, it should be identified not by its name but by an empty string.
	 * @see #getValidModes()
	 */
	public void setValidModes(List<String> validModes) {
		if (!testEquals(validModes, this.validModes)) {
			if (validModes==null) {
				this.validModes = null;
			} else {
				this.validModes = new HashSet<String>(validModes.size());
				this.validModes.addAll(validModes);
			}
			setChanged();
		}
	}

	/** Returns the valid categories for this filter.
	 * There's no side effect between this instance and the returned array.
	 * @return the valid categories (null means, all categories are ok).
	 */
	public List<Category> getValidCategories() {
		if (validCategories==null) return null;
		ArrayList<Category> result = new ArrayList<Category>(validCategories.size());
		for (Category account:validCategories) {
			result.add(account);
		}
		return result;
	}

	public boolean isOk(Category category) {
		return (validCategories==null) || (validCategories.contains(category));
	}

	/** Set the valid categories for this filter.
	 * There's no side effect between this instance and the argument array.
	 * @param validCategories the categories that are allowed (null or the complete list of categories to allow all categories).
	 */
	public void setValidCategories(List<Category> validCategories) {
		if (!testEquals(validCategories, this.validCategories)) {
			if (validCategories==null) {
				this.validCategories = null;
			} else {
				this.validCategories = new HashSet<Category>(validCategories.size());
				this.validCategories.addAll(validCategories);
			}
			setChanged();
		}
	}

	/** Gets the transaction date before which all transactions are rejected.
	 * @return a transaction date or null if there's no time limit. 
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/** Gets the transaction date after which all transactions are rejected.
	 * @return a transaction date or null if there's no time limit. 
	 */
	public Date getDateTo() {
		return dateTo;
	}

	/** Sets the filter on transaction date.
	 * @param from transactions strictly before <i>from</i> are rejected. A null date means "beginning of times".
	 * @param to transactions strictly after <i>to</i> are rejected. A null date means "end of times". 
	 */
	public void setDateFilter(Date from, Date to) {
		if (!NullUtils.areEquals(from, this.dateFrom) || !NullUtils.areEquals(to, this.dateTo)) {
			this.dateFrom = from;
			this.dateTo = to;
			this.setChanged();
		}
	}
	
	/** Gets the transaction value date before which all transactions are rejected.
	 * @return a transaction value date or null if there's no time limit. 
	 */
	public Date getValueDateFrom() {
		return valueDateFrom;
	}

	/** Gets the transaction value date after which all transactions are rejected.
	 * @return a transaction value date or null if there's no time limit. 
	 */
	public Date getValueDateTo() {
		return valueDateTo;
	}

	/** Sets the filter on transaction value date.
	 * @param from transactions with value date strictly before <i>from</i> are rejected. A null date means "beginning of times".
	 * @param to transactions with value date strictly after <i>to</i> are rejected. A null date means "end of times". 
	 */
	public void setValueDateFilter(Date from, Date to) {
		if (!NullUtils.areEquals(from, this.valueDateFrom) || !NullUtils.areEquals(to, this.valueDateTo)) {
			this.valueDateFrom = from;
			this.valueDateTo = to;
			this.setChanged();
		}
	}

	/** Gets the transaction minimum amount.
	 * <br>Please note that the minimum amount is always a positive or null number. 
	 * @return the minimum amount (0.0 if there's no low limit).
	 */
	public double getMinAmount() {
		return minAmount;
	}

	/** Gets the transaction maximum amount.
	 * @return the maximum amount (Double.POSITIVE_INFINITY if there's no high limit).
	 */
	public double getMaxAmount() {
		return maxAmount;
	}

	/** Sets the transaction minimum and maximum amounts.
	 * @param property An integer that codes if expenses or receipts, or both are ok.
	 * <br>Note that only EXPENSES, RECEIPTS and EXPENSES+RECEIPTS constants are valid arguments.
	 * Any other integer codes (for instance CHECKED) are ignored.
	 * @param minAmount The minimum amount (a positive or null double).
	 * @param maxAmount The maximum amount (Double.POSITIVE_INFINITY to set no high limit).
	 * @throws IllegalArgumentException if minAmount > maxAmount or if minimum amount is negative
	 */
	public void setAmountFilter(int property, double minAmount, double maxAmount) {
		if (minAmount>maxAmount) throw new IllegalArgumentException();
		if (minAmount<0) throw new IllegalArgumentException();
		int mask = Filter.EXPENSES+Filter.RECEIPTS;
		if ((GlobalData.AMOUNT_COMPARATOR.compare(minAmount, this.minAmount) != 0) ||
				(GlobalData.AMOUNT_COMPARATOR.compare(maxAmount, this.maxAmount) != 0) ||
				((property & mask)!=(filter & mask))) {
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			filter = (filter & ~mask) | (property & mask);
			if (DEBUG) System.out.println("-> filter : "+filter); //$NON-NLS-1$
			this.setChanged();
		}
	}
	
	/** Tests whether an amount is ok or not.
	 * @param amount The amount to test
	 * @return true if the amount is ok.
	 */
	public boolean isAmountOk(double amount) {
		// We use the currency comparator to implement amount filtering because double are very tricky to compare.
		if ((GlobalData.AMOUNT_COMPARATOR.compare(amount, 0.0)<0) && (!isOk(EXPENSES))) return false;
		if ((GlobalData.AMOUNT_COMPARATOR.compare(amount, 0.0)>0) && (!isOk(RECEIPTS))) return false;
		amount = Math.abs(amount);
		if (GlobalData.AMOUNT_COMPARATOR.compare(amount, getMinAmount())<0) return false;
		return GlobalData.AMOUNT_COMPARATOR.compare(amount, getMaxAmount())<=0;
	}

	/** Gets the description filter.
	 * @return a TextMatcher or null if there is no description filter
	 */
	public TextMatcher getDescriptionMatcher() {
		return descriptionMatcher;
	}
	
	/** Gets the validity of a string according to the current description filter. 
	 * @param description The string to test
	 * @return true if the description is ok with the filter.
	 */
	public boolean isDescriptionOk(String description) {
		return descriptionMatcher==null?true:descriptionMatcher.matches(description);
	}

	/** Sets the description filter.
	 * @param matcher a TextMatcher instance or null to apply no filter on description
	 */
	public void setDescriptionMatcher(TextMatcher matcher) {
		if (!NullUtils.areEquals(matcher, this.descriptionMatcher)) {
			this.descriptionMatcher = matcher;
			this.setChanged();
		}
	}
	
	public TextMatcher getCommentMatcher() {
		return this.commentMatcher;
	}

	/** Gets the validity of a string according to the current comment filter. 
	 * @param comment The string to test
	 * @return true if the comment is ok with the filter.
	 */
	public boolean isCommentOk(String comment) {
		return commentMatcher==null?true:commentMatcher.matches(comment);
	}

	public void setCommentMatcher(TextMatcher textMatcher) {
		if (!NullUtils.areEquals(textMatcher, this.commentMatcher)) {
			this.commentMatcher = textMatcher;
			this.setChanged();
		}
	}

	public TextMatcher getNumberMatcher() {
		return numberMatcher;
	}

	/** Gets the validity of a string according to the current number filter. 
	 * @param number The string to test
	 * @return true if the number is ok with the filter.
	 */
	public boolean isNumberOk(String number) {
		return numberMatcher==null?true:numberMatcher.matches(number);
	}

	public void setNumberMatcher(TextMatcher numberMatcher) {
		if (!NullUtils.areEquals(numberMatcher, this.numberMatcher)) {
			this.numberMatcher = numberMatcher;
			this.setChanged();
		}
	}

	public TextMatcher getStatementMatcher() {
		return statementMatcher;
	}

	public void setStatementFilter (int property, TextMatcher statementFilter) {
		if (((property & Filter.CHECKED) == 0) && (statementFilter!=null)) throw new IllegalArgumentException();
		int mask = Filter.CHECKED+Filter.NOT_CHECKED;
		if (!NullUtils.areEquals(statementFilter, this.statementMatcher) || ((property & mask)!=(filter & mask))) {
			this.statementMatcher = statementFilter;
			filter = (filter & ~mask) | (property & mask);
			if (DEBUG) System.out.println("-> filter : "+filter); //$NON-NLS-1$
			this.setChanged();
		}
	}
		
	public boolean isStatementOk(String statement) {
		if (statement==null) { // Not checked transaction
			return isOk(Filter.NOT_CHECKED);
		} else { // Checked transaction
			if (!isOk(Filter.CHECKED)) return false;
			if (statementMatcher==null) return true;
			return statementMatcher.matches(statement);
		}
	}

	public void clear() {
		if (isActive()) {
			this.setSuspended(true);
			init();
			this.setSuspended(false);
		}
	}
	
	private void init() {
		this.setDateFilter(null, null);
		this.setValueDateFilter(null, null);
		this.setValidCategories(null);
		this.setValidModes(null);
		this.setAmountFilter(EXPENSES+RECEIPTS, 0.0, Double.POSITIVE_INFINITY);
		this.setDescriptionMatcher(null);
		this.setNumberMatcher(null);
		this.setStatementFilter(CHECKED+NOT_CHECKED, null);
		this.setValidAccounts(null);
	}
	
	/** Tests whether the filter filters something or not.
	 * @return false if no filter is set. Returns true if a filter is set
	 * even if it doesn't filter anything.
	 */
	public boolean isActive() {
		return (filter!=ALL) || (getDateFrom()!=null) || (getDateTo() != null) || (getValueDateFrom()!=null) || (getValueDateTo() != null) ||
			(getValidCategories() !=null) || (getValidModes() != null) || (getValidAccounts()!=null) ||
			(getMinAmount()!=0.0) || (getMaxAmount()!=Double.POSITIVE_INFINITY) ||
			(getDescriptionMatcher()!=null) || (getNumberMatcher()!=null) || (getStatementMatcher()!=null);
	}
}