package net.yapbam.data;

import java.util.Date;
import java.util.List;

import net.yapbam.date.helpers.DateHelper;
import net.yapbam.date.helpers.DateStepper;

public class PeriodicalTransaction {
	private String id;
	private String description;
	private double amount;
	private Account account;
	private Mode mode;
	private Category category;
	private List<SubTransaction> subTransactions;

	private int nextDate;
	private boolean enabled;
	private int anticipation;
	private DateStepper nextDateBuilder;

	public PeriodicalTransaction(String id, String description, double amount,
			Account account, Mode mode, Category category,
			List<SubTransaction> subTransactions, Date nextDate,
			boolean enabled, int creationAnticipation,
			DateStepper nextDateBuilder) {
		super();
		this.id = id;
		this.description = description;
		this.amount = amount;
		this.account = account;
		this.mode = mode;
		this.category = category;
		this.subTransactions = subTransactions;
		this.nextDate = DateHelper.dateToInteger(nextDate);
		this.enabled = enabled;
		this.anticipation = creationAnticipation;
		this.nextDateBuilder = nextDateBuilder;
	}
	
	public String getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public double getAmount() {
		return amount;
	}
	public Account getAccount() {
		return account;
	}
	public Mode getMode() {
		return mode;
	}
	public Category getCategory() {
		return category;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public Date getNextDate() {
		return DateHelper.integerToDate(nextDate);
	}
	public int getAnticipation() {
		return this.anticipation;
	}
	public DateStepper getNextDateBuilder() {
		return nextDateBuilder;
	}
	public int getSubTransactionsSize() {
		return this.subTransactions.size();
	}
	public SubTransaction getSubTransaction(int index) {
		return this.subTransactions.get(index);
	}
}
