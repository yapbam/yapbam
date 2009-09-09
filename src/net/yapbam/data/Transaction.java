package net.yapbam.data;

import java.io.Serializable;
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
}
