package net.yapbam.data;

import java.util.ArrayList;
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
	
	PeriodicalTransaction change(Category oldCategory, Category newCategory) {
		if (!hasCategory(oldCategory)) return null;
		List<SubTransaction> subTransactions = changeSubTransactions(oldCategory, newCategory);
		return new PeriodicalTransaction(getDescription(), getAmount(), getAccount(), getMode(),
				(getCategory().equals(oldCategory)?newCategory:getCategory()), subTransactions,
				getNextDate(), isEnabled(), getNextDateBuilder());
	}

	public PeriodicalTransaction change(Account account, Mode oldMode, Mode newMode) {
		if (getAccount().equals(account) && getMode().equals(oldMode)) {
			ArrayList<SubTransaction> subTransactionsClone = new ArrayList<SubTransaction>();
			for (int i=0;i<getSubTransactionSize();i++) {
				subTransactionsClone.add(getSubTransaction(i));
			}
			return new PeriodicalTransaction(getDescription(), getAmount(), getAccount(), newMode,
					getCategory(), subTransactionsClone, getNextDate(), isEnabled(), getNextDateBuilder());
		} else {
			return null;
		}
	}
}
