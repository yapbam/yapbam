package net.yapbam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.yapbam.util.DateUtils;

/** A bank transaction */
public class Transaction extends AbstractTransaction implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int date;
	private String number;
	private int valueDate;
	private String statementId;
	
	/** Constructor.
	 * @param date The transaction's date
	 * @param number The transaction's number (null if the description has not any number)
	 * @param description The transaction's description
	 * @param comment The transaction's comment (null if the transaction has no comment)
	 * @param amount The transaction's amount (negative for an expense)
	 * @param account  The transaction's account
	 * @param mode The transaction's payment mode 
	 * @param category The transaction's category
	 * @param valueDate The transaction's value date
	 * @param statementId The transaction's the statement id (null if the transaction doesn't blong to a statement)
	 * @param subTransactions the subtransactions of the transaction (an empty List if the transaction has no subtransaction)
	 */
	public Transaction(Date date, String number, String description, String comment, double amount,
			Account account, Mode mode, Category category, Date valueDate,
			String statementId, List<SubTransaction> subTransactions) {
		super(description, comment, amount, account, mode, category, subTransactions);
		this.date = DateUtils.dateToInteger(date);
		this.number = number;
		if ((number!=null) && number.trim().isEmpty()) this.number = null;
		this.valueDate = DateUtils.dateToInteger(valueDate);
		this.statementId = statementId;
		if ((statementId!=null) && statementId.trim().isEmpty()) this.statementId=null;
	}

	public String getNumber() {
		return this.number;
	}

	public Date getDate() {
		return DateUtils.integerToDate(this.date);
	}

	public Date getValueDate() {
		return DateUtils.integerToDate(valueDate);
	}

	public String getStatement() {
		return statementId;
	}

	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "["+this.getAccount()+"|"+this.date+"|"+this.getDescription()+"|"+this.getAmount()+"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	public boolean isChecked() {
		return this.statementId!=null;
	}
	
	/** Creates a new transaction that differs only by the category.
	 * @param oldCategory The category to change.
	 * <br>Note that the modifications operate even on the subtransactions, so, this
	 * @param newCategory The replacement category.
	 * @return a new updated transaction or null if the old category was not used in this transaction.
	 */
	Transaction change(Category oldCategory, Category newCategory) {
		if (!hasCategory(oldCategory)) return null;
		List<SubTransaction> subTransactions = changeSubTransactions(oldCategory, newCategory);
		return new Transaction(getDate(), getNumber(), getDescription(), getComment(), getAmount(), getAccount(), getMode(),
				(getCategory().equals(oldCategory)?newCategory:getCategory()), getValueDate(), getStatement(), subTransactions);
	}

	Transaction change(Account account, Mode oldMode, Mode newMode) {
		if (getAccount().equals(account) && getMode().equals(oldMode)) {
			ArrayList<SubTransaction> subTransactionsClone = new ArrayList<SubTransaction>();
			for (int i=0;i<getSubTransactionSize();i++) {
				subTransactionsClone.add(getSubTransaction(i));
			}
			return new Transaction(getDate(), getNumber(), getDescription(), getComment(), getAmount(), getAccount(), newMode,
					getCategory(), getValueDate(), getStatement(), subTransactionsClone);
		} else {
			return null;
		}
	}
}
