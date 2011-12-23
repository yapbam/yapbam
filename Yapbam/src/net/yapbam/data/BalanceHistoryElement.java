package net.yapbam.data;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

/** A balance history element.
 * Such an element represents the value of the balance between two dates.
 * The history itself is a list of these elements.
 * @see BalanceHistory
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
	
	/** Returns the balance.
	 * @return the balance.
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Returns the beginning of this period of time
	 * @return the beginning or null if the beginning is "beginning of the times"
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * Returns the end of this period of time.
	 * The end date is excluded from the time interval of this element.
	 * @return the end or null if the end is "end of times"
	 */
	public Date getTo() {
		return to;
	}

	@Override
	public String toString() {
		return MessageFormat.format("[{0,date,short}->{1,date,short}:{2,number,currency}[", from, to, balance); //$NON-NLS-1$
	}

	/** Returns the position of this element relative to a date.
	 * @param date a date
	 * @return 0 if the date is included in this. A negative number if the date is before this. A positive one if the date
	 * is after.
	 */
	public int getRelativePosition(Date date) {
		long timeFrom = (this.from==null) ? Long.MIN_VALUE : this.from.getTime();
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

	void add(double amount) {
		this.balance += amount;
	}

	void setTo(Date date) {
		this.to = date;
	}
}
