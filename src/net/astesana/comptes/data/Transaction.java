package net.astesana.comptes.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.astesana.comptes.date.helpers.DateHelper;

/** This class represents a transaction */
public class Transaction implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private static long currentId = 0;
	
	private long id;
	private int date;
	private String description;
	private String number;
	private double amount;
	private Account account;
	private Mode mode;
	private Category category;
	private int valueDate;
	private List<SubTransaction> subTransactions;
	private String statementId;
	
	public Transaction(Date date, String number, String description, double amount,
			Account account, Mode mode, Category category, Date valueDate,
			String statementId, List<SubTransaction> subTransactions) {
		super();
		this.id = Transaction.currentId++;
		this.date = DateHelper.dateToInteger(date);
		this.description = description;
		this.number = number;
		this.amount = amount;
		this.account = account;
		this.mode = mode;
		this.category = category;
		this.valueDate = DateHelper.dateToInteger(valueDate);
		this.statementId = statementId;
		this.subTransactions = subTransactions;
	}

	@Override
	public Object clone() {
		ArrayList<SubTransaction> list = new ArrayList<SubTransaction>();
		for (int i=0;i<getSubTransactionSize();i++) {
			list.add((SubTransaction) getSubTransaction(i).clone());
		}
		Transaction result = new Transaction(getDate(), number, description, amount, account, mode, category, getValueDate(), statementId, list);
		return result;
	}

	public Account getAccount() {
		return this.account;
	}
	
	public String getNumber() {
		return this.number;
	}

	public String getDescription() {
		return this.description;
	}

	public double getAmount() {
		return this.amount;
	}

	public Date getDate() {
		return DateHelper.integerToDate(this.date);
	}

	public Mode getMode() {
		return mode;
	}

	public Category getCategory() {
		return category;
	}

	public Date getValueDate() {
		return DateHelper.integerToDate(valueDate);
	}

	public int getSubTransactionSize() {
		return this.subTransactions.size();
	}
	
	public SubTransaction getSubTransaction(int index) {
		return this.subTransactions.get(index);
	}
	
	public String getStatement() {
		return statementId;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "["+this.account+"|"+this.date+"|"+this.description+"|"+this.amount+"]";
	}

	public boolean isChecked() {
		return this.statementId!=null;
	}
	
	public double getComplement() {
		double result = getAmount();
		for (int i = 0; i < getSubTransactionSize(); i++) {
			result -= getSubTransaction(i).getAmount();
		}
		return result;
	}
}
