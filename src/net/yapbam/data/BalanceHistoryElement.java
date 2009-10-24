package net.yapbam.data;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

/** This class represents a balance history element.
 * Such an element represents the value of the balance between to dates.
 * The history itself is a list of these elements.
 */
public class BalanceHistoryElement implements Serializable {
	private static final long serialVersionUID = 1L;

	private double balance;
	private Date from;
	private Date to;
	
	/** Constructor. Build an instance where balance is value between from and to (to is excluded).
	 * @param balance the balance
	 * @param from start date
	 * @param to end date
	 */
	public BalanceHistoryElement(double balance, Date from, Date to) {
		this.balance = balance;
		this.from = from;
		this.to = to;
	}
	
	public double getBalance() {
		return balance;
	}

	/**
	 * Returns the beginning
	 * @return the beginning or null if the beginning is "beginning of the universe"
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * Returns the end.
	 * The end date is excluded from the time interval of this element.
	 * @return the end or null if the end is "end of time"
	 */
	public Date getTo() {
		return to;
	}

	@Override
	public String toString() {
		return MessageFormat.format("[{0,date,short}->{1,date,short}:{2,number,currency}[", from, to, balance);
	}

	/** Returns the position of this element relative to a date.
	 * @param date a date
	 * @return 0 if the date is included in this. A negative number if the date is before this. A positive one if the date
	 * is after.
	 */
	public int getRelativePosition(Date date) {
		long timeFrom = (this.from==null) ? 0 : this.from.getTime();
		long timeTo = (this.to==null) ? Long.MAX_VALUE : this.to.getTime();
		long time = date.getTime();
		int result = 0;
		if (time < timeFrom) {
			result = -1;
		} else {
			if (time>=timeTo) {
				result = 1;
			}
		}
//		System.out.println(MessageFormat.format("{0}.relativePosition({1,date,short})={2}", this,date,result));//CU
		return result;
	}

	public void add(double amount) {
		this.balance += amount;
	}

	void setTo(Date date) {
		this.to = date;
	}
}
