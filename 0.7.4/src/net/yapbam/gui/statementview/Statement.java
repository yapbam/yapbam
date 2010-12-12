package net.yapbam.gui.statementview;

import net.yapbam.data.Transaction;
import net.yapbam.util.DateUtils;

/** A statement.
 */
public class Statement  {
	private String id;
	private double positiveBalance;
	private double negativeBalance;
	private int nbTransactions;
	private long dateSum;
	private double startBalance;
	
	/** Constructor.
	 * @param id the statement id of the statement.
	 */
	Statement(String id) {
		super();
		this.id = id;
		this.positiveBalance = 0;
		this.negativeBalance = 0;
		this.nbTransactions = 0;
		this.dateSum = 0;
	}

	void add(Transaction transaction) {
		this.nbTransactions++;
		double amount = transaction.getAmount();
		if (amount>0) this.positiveBalance += amount;
		else this.negativeBalance -= amount;
		this.dateSum += DateUtils.dateToInteger(transaction.getValueDate());
	}
	
	/** Gets the medium value date of the transactions in this statement.
	 * @return an integer. You may convert it to a java.util.date using DateUtils.integerToDate(int)
	 * @see DateUtils#integerToDate(int)
	 */
	public int getMediumDate() {
		return (int) (this.dateSum/this.nbTransactions);
	}

	public String getId() {
		return id;
	}

	public double getBalance() {
		return this.positiveBalance - this.negativeBalance;
	}

	
	/** Gets the number of transactions in the statement.
	 * @return an integer
	 */
	public int getNbTransactions() {
		return nbTransactions;
	}

	@Override
	public String toString() {
		return this.getId();
	}

	void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}

	public double getPositiveBalance() {
		return positiveBalance;
	}

	public double getNegativeBalance() {
		return negativeBalance;
	}

	public double getStartBalance() {
		return startBalance;
	}
	
	public double getEndBalance() {
		return startBalance + getBalance();
	}
}