package net.yapbam.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import net.yapbam.util.TextMatcher;

public class Filter implements Serializable {
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

	public Filter() {
		clear();
	}

	public int getFilter() {
		return filter;
	}

	public void setFilter(int filter) {
		this.filter = filter;
	}

	public HashSet<Account> getValidAccounts() {
		return validAccounts;
	}

	public void setValidAccounts(HashSet<Account> validAccounts) {
		this.validAccounts = validAccounts;
	}

	public List<Mode> getValidModes() {
		return validModes;
	}

	public void setValidModes(List<Mode> validModes) {
		this.validModes = validModes;
	}

	public HashSet<Category> getValidCategories() {
		return validCategories;
	}

	public void setValidCategories(HashSet<Category> validCategories) {
		this.validCategories = validCategories;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Date getValueDateFrom() {
		return valueDateFrom;
	}

	public void setValueDateFrom(Date valueDateFrom) {
		this.valueDateFrom = valueDateFrom;
	}

	public Date getValueDateTo() {
		return valueDateTo;
	}

	public void setValueDateTo(Date valueDateTo) {
		this.valueDateTo = valueDateTo;
	}

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public TextMatcher getDescriptionMatcher() {
		return descriptionMatcher;
	}

	public void setDescriptionMatcher(TextMatcher descriptionMatcher) {
		this.descriptionMatcher = descriptionMatcher;
	}

	public TextMatcher getNumberMatcher() {
		return numberMatcher;
	}

	public void setNumberMatcher(TextMatcher numberMatcher) {
		this.numberMatcher = numberMatcher;
	}

	public TextMatcher getStatementMatcher() {
		return statementMatcher;
	}

	public void setStatementMatcher(TextMatcher statementMatcher) {
		this.statementMatcher = statementMatcher;
	}

	public void clear() {
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
	

	/** Tests whether the filter filter something or not.
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