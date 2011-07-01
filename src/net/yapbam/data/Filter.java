package net.yapbam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

import net.yapbam.util.NullUtils;
import net.yapbam.util.TextMatcher;

public class Filter extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;
	private static boolean DEBUG = false;
	public static final int CHECKED=1;
	public static final int NOT_CHECKED=2;
	public static final int EXPENSES=4;
	public static final int RECEIPTS=8;

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
	
	private boolean suspended;

	public Filter() {
		init();
		this.suspended = false;
	}

	public int getFilter() {
		return filter;
	}

	public boolean isOk(int property) {
		if (DEBUG) {
			System.out.println("---------- isOK("+Integer.toBinaryString(property)+") ----------");
			System.out.println("filter  : "+Integer.toBinaryString(this.filter));
			System.out.println("result  : "+Integer.toBinaryString(property & this.filter));
		}
		return ((property & this.filter) != 0);
	}
	
	public void setFilter(int filter) {
		this.filter = filter;
		setChanged();
	}
	
	@Override
	protected void setChanged() {
		super.setChanged();
		if (!suspended) this.notifyObservers();
	}
	
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
		if (accounts==null) {
			validAccounts = null;
		} else {
			validAccounts = new HashSet<Account>(accounts.size());
			for (Account account : accounts) {
				validAccounts.add(account);
			}
		}
		// TODO Test if the accounts list was really changed before launching the event
		setChanged();
	}

	public List<Mode> getValidModes() {
		return validModes;
	}

	public void setValidModes(List<Mode> validModes) {
		this.validModes = validModes;
		this.setChanged();
	}

	public HashSet<Category> getValidCategories() {
		return validCategories;
	}

	public void setValidCategories(HashSet<Category> validCategories) {
		this.validCategories = validCategories;
		this.setChanged();
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
		this.setChanged();
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
		this.setChanged();
	}

	public Date getValueDateFrom() {
		return valueDateFrom;
	}

	public void setValueDateFrom(Date valueDateFrom) {
		this.valueDateFrom = valueDateFrom;
		this.setChanged();
	}

	public Date getValueDateTo() {
		return valueDateTo;
	}

	public void setValueDateTo(Date valueDateTo) {
		this.valueDateTo = valueDateTo;
		this.setChanged();
	}

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
		this.setChanged();
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
		this.setChanged();
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
	public void setDescriptionMatcher(TextMatcher descriptionMatcher) {
		if (!NullUtils.areEquals(descriptionMatcher, this.descriptionMatcher)) {
			this.descriptionMatcher = descriptionMatcher;
			this.setChanged();
		}
	}

	public TextMatcher getNumberMatcher() {
		return numberMatcher;
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

	public void setStatementMatcher(TextMatcher statementMatcher) {
		if (!NullUtils.areEquals(statementMatcher, this.statementMatcher)) {
			this.statementMatcher = statementMatcher;
			this.setChanged();
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
		this.setFilter(FilteredData.ALL);
		this.setDateFrom(null);
		this.setDateTo(null);
		this.setValueDateFrom(null);
		this.setValueDateTo(null);
		this.setValidCategories(null);
		this.setValidModes(null);
		this.setMinAmount(0.0);
		this.setMaxAmount(Double.POSITIVE_INFINITY);
		this.setDescriptionMatcher(null);
		this.setNumberMatcher(null);
		this.setStatementMatcher(null);
		this.setValidAccounts(null);
	}
	
	/** Tests whether the filter filters something or not.
	 * @return false if no filter is set. Returns true if a filter is set
	 * even if it doesn't filter anything.
	 */
	public boolean isActive() {
		return (getFilter()!=FilteredData.ALL) || (getDateFrom()!=null) || (getDateTo() != null) || (getValueDateFrom()!=null) || (getValueDateTo() != null) ||
			(getValidCategories() !=null) || (getValidModes() != null) || (getValidAccounts()!=null) ||
			(getMinAmount()!=0.0) || (getMaxAmount()!=Double.POSITIVE_INFINITY) ||
			(getDescriptionMatcher()!=null) || (getNumberMatcher()!=null) || (getStatementMatcher()!=null);
	}
}