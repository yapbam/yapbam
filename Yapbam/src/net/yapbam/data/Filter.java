package net.yapbam.data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import net.yapbam.util.TextMatcher;

public class Filter {
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
}