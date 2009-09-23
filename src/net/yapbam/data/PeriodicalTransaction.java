package net.yapbam.data;

import java.util.Date;
import java.util.List;

import net.yapbam.date.helpers.DateHelper;
import net.yapbam.date.helpers.DateStepper;

public class PeriodicalTransaction extends AbstractTransaction {

	private int nextDate;
	private boolean enabled;
	private DateStepper nextDateBuilder;

	public PeriodicalTransaction(String description, double amount,
			Account account, Mode mode, Category category,
			List<SubTransaction> subTransactions, Date nextDate,
			boolean enabled, DateStepper nextDateBuilder) {
		super(description, amount, account, mode, category, subTransactions);
		this.nextDate = DateHelper.dateToInteger(nextDate);
		this.enabled = enabled;
		this.nextDateBuilder = nextDateBuilder;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public Date getNextDate() {
		return DateHelper.integerToDate(nextDate);
	}

	public DateStepper getNextDateBuilder() {
		return nextDateBuilder;
	}
}
