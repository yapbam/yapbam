package net.yapbam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import net.yapbam.util.TextMatcher;

public class Filter extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;

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

	public void setFilter(int filter) {
		this.filter = filter;
		touch();
	}
	
	private void touch() {
		if (!suspended) {
			this.setChanged();
			this.notifyObservers();
		}
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
		touch();
	}

	public List<Mode> getValidModes() {
		return validModes;
	}

	public void setValidModes(List<Mode> validModes) {
		this.validModes = validModes;
		this.touch();
	}

	public HashSet<Category> getValidCategories() {
		return validCategories;
	}

	public void setValidCategories(HashSet<Category> validCategories) {
		this.validCategories = validCategories;
		this.touch();
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
		this.touch();
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
		this.touch();
	}

	public Date getValueDateFrom() {
		return valueDateFrom;
	}

	public void setValueDateFrom(Date valueDateFrom) {
		this.valueDateFrom = valueDateFrom;
		this.touch();
	}

	public Date getValueDateTo() {
		return valueDateTo;
	}

	public void setValueDateTo(Date valueDateTo) {
		this.valueDateTo = valueDateTo;
		this.touch();
	}

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
		this.touch();
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
		this.touch();
	}

	public TextMatcher getDescriptionMatcher() {
		return descriptionMatcher;
	}

	public void setDescriptionMatcher(TextMatcher descriptionMatcher) {
		this.descriptionMatcher = descriptionMatcher;
		this.touch();
	}

	public TextMatcher getNumberMatcher() {
		return numberMatcher;
	}

	public void setNumberMatcher(TextMatcher numberMatcher) {
		this.numberMatcher = numberMatcher;
		this.touch();
	}

	public TextMatcher getStatementMatcher() {
		return statementMatcher;
	}

	public void setStatementMatcher(TextMatcher statementMatcher) {
		this.statementMatcher = statementMatcher;
		this.touch();
	}

	public void clear() {
		if (isActive()) {
			init();
			this.touch();
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