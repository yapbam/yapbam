package net.yapbam.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.yapbam.date.helpers.DateStepper;
import net.yapbam.util.DateUtils;

/** A periodical transaction.
 * <br>A periodical transaction is a transaction that occurs at constant interval.
 * A perfect sample is the monthly Internet connection fee.
 */
public class PeriodicalTransaction extends AbstractTransaction {

	private int nextDate;
	private boolean enabled;
	private DateStepper nextDateBuilder;

	@Deprecated
	public PeriodicalTransaction(String description, double amount, Account account, Mode mode, Category category, List<SubTransaction> subTransactions,
			Date nextDate, boolean enabled, DateStepper nextDateBuilder) {
		this(description, null, amount, account, mode, category, subTransactions, nextDate, enabled, nextDateBuilder);
	}

	/** Constructor
	 * @param description The transaction's description
	 * @param comment The transaction's comment
	 * @param amount The transaction's amount
	 * @param account The transaction's account
	 * @param mode The transaction's payment mode
	 * @param category The transaction's category
	 * @param subTransactions The transaction's subtransactions 
	 * @param nextDate The transaction's next date or null if no next date is forcasted
	 * @param enabled true if the transaction is active (transactions have to be generated), false if the transaction is suspended.
	 * Please note that if the <b>nextDate</b> argument is null, this argument is ignored and the transaction is suspended.
	 * @param nextDateBuilder The DateStepper that will compute the next generation date.
	 */
	public PeriodicalTransaction(String description, String comment, double amount,
			Account account, Mode mode, Category category,
			List<SubTransaction> subTransactions, Date nextDate,
			boolean enabled, DateStepper nextDateBuilder) {
		super(description, comment, amount, account, mode, category, subTransactions);
		this.nextDate = DateUtils.dateToInteger(nextDate);
		this.enabled = enabled && (nextDate!=null);
		this.nextDateBuilder = nextDateBuilder;
	}
	
	/** Tests whether this periodical transaction is enabled or not.
	 * A disabled periodical transaction can't generate any "real" transaction.
	 * @return true if this periodical transaction is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/** Gets the date of the next transaction to be generated.
	 * @return a date or null if the periodical transaction expired.
	 * @see GlobalData#setPeriodicalTransactionNextDate(PeriodicalTransaction[], Date)
	 */
	public Date getNextDate() {
		return DateUtils.integerToDate(nextDate);
	}

	/** Gets a builder able to compute the date of next transaction to be generated.
	 * @return a date builder
	 */
	public DateStepper getNextDateBuilder() {
		return nextDateBuilder;
	}
	
	PeriodicalTransaction change(Category oldCategory, Category newCategory) {
		if (!hasCategory(oldCategory)) return null;
		List<SubTransaction> subTransactions = changeSubTransactions(oldCategory, newCategory);
		return new PeriodicalTransaction(getDescription(), getComment(), getAmount(), getAccount(), getMode(),
				(getCategory().equals(oldCategory)?newCategory:getCategory()), subTransactions,
				getNextDate(), isEnabled(), getNextDateBuilder());
	}

	PeriodicalTransaction change(Account account, Mode oldMode, Mode newMode) {
		if (getAccount().equals(account) && getMode().equals(oldMode)) {
			ArrayList<SubTransaction> subTransactionsClone = new ArrayList<SubTransaction>();
			for (int i=0;i<getSubTransactionSize();i++) {
				subTransactionsClone.add(getSubTransaction(i));
			}
			return new PeriodicalTransaction(getDescription(), getComment(), getAmount(), getAccount(), newMode,
					getCategory(), subTransactionsClone, getNextDate(), isEnabled(), getNextDateBuilder());
		} else {
			return null;
		}
	}
	
	/** Generate transactions from the periodical transactions until a date.
	 * <br>The transactions are not added to the global data and this periodical transaction
	 * is not changed : its next date field remains unchanged.
	 * @param date Date until the transactions had to be generated (inclusive)
	 * @param result an array where to put the transactions or null to return a new list
	 * @return a transaction list (the one passed as second argument).
	 */
	public List<Transaction> generate(Date date, List<Transaction> result) {
		if (result==null) result = new ArrayList<Transaction>();
		if (hasPendingTransactions(date)) {
			double amount = getAmount();
			Mode mode = getMode();
			DateStepper vdStepper = amount<0?mode.getExpenseVdc():mode.getReceiptVdc();
			// Be aware that the date stepper may not be available anymore (if the mode is no more usable for this kind of transaction)
			if (vdStepper==null) vdStepper = DateStepper.IMMEDIATE;
			//Be aware, when the transaction has an "end date", and the date is after this "end date", tDate become null
			for (Date tDate = getNextDate();((tDate!=null)&&(tDate.compareTo(date)<=0));tDate=getNextDateBuilder().getNextStep(tDate)) {
				result.add(new Transaction(tDate, null, getDescription(), getComment(), amount, getAccount(), mode, getCategory(),
						vdStepper.getNextStep(tDate), null, Arrays.asList(getSubTransactions())));
			}
		}
		return result;
	}

	/** Tests whether this periodical transaction has transactions pending at at fixed date.
	 * @param date The date to consider.
	 * @return true if there is pending transactions
	 */
	public boolean hasPendingTransactions(Date date) {
		return isEnabled() && (getNextDate().compareTo(date)<=0);
	}
}
