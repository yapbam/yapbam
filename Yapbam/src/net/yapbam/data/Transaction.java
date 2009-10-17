package net.yapbam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.yapbam.date.helpers.DateHelper;

/** This class represents a transaction */
public class Transaction extends AbstractTransaction implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	
	private int date;
	private String number;
	private int valueDate;
	private String statementId;
	
	public Transaction(Date date, String number, String description, double amount,
			Account account, Mode mode, Category category, Date valueDate,
			String statementId, List<SubTransaction> subTransactions) {
		super(description, amount, account, mode, category, subTransactions);
		this.date = DateHelper.dateToInteger(date);
		this.number = number;
		this.valueDate = DateHelper.dateToInteger(valueDate);
		this.statementId = statementId;
	}

	public String getNumber() {
		return this.number;
	}

	public Date getDate() {
		return DateHelper.integerToDate(this.date);
	}

	public Date getValueDate() {
		return DateHelper.integerToDate(valueDate);
	}

	public String getStatement() {
		return statementId;
	}

	@Override
	public String toString() {
		return "["+this.getAccount()+"|"+this.date+"|"+this.getDescription()+"|"+this.getAmount()+"]";
	}

	public boolean isChecked() {
		return this.statementId!=null;
	}
	
	Transaction change(Category oldCategory, Category newCategory) {
		if (!hasCategory(oldCategory)) return null;
		List<SubTransaction> subTransactions = changeSubTransactions(oldCategory, newCategory);
		return new Transaction(getDate(), getNumber(), getDescription(), getAmount(), getAccount(), getMode(),
				(getCategory().equals(oldCategory)?newCategory:getCategory()), getValueDate(), getStatement(), subTransactions);
	}

	Transaction change(Account account, Mode oldMode, Mode newMode) {
		if (getAccount().equals(account) && getMode().equals(oldMode)) {
			ArrayList<SubTransaction> subTransactionsClone = new ArrayList<SubTransaction>();
			for (int i=0;i<getSubTransactionSize();i++) {
				subTransactionsClone.add(getSubTransaction(i));
			}
			return new Transaction(getDate(), getNumber(), getDescription(), getAmount(), getAccount(), newMode,
					getCategory(), getValueDate(), getStatement(), subTransactionsClone);
		} else {
			return null;
		}
	}
}
