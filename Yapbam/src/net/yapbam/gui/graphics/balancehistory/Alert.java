package net.yapbam.gui.graphics.balancehistory;

import java.util.Date;

import net.yapbam.data.Account;
import net.yapbam.data.AlertThreshold;

/** A balance alert.
 * The balance history is the best place to display alerts when an account balance exceeds or is less than a predefined threshold.
 * This class represents an alert.
 */
class Alert {
	public static final int IS_LESS = 1;
	public static final int IS_MORE = 2;

	private Date date;
	private Account account;
	private int kind;
	private double balance;
	private double threshold;
	
	/** Constructor.
	 * @param date the alert's date
	 * @param account
	 * @param balance
	 */
	public Alert(Date date, Account account, double balance) {
		super();
		this.date = date;
		this.account = account;
		this.balance = balance;
		AlertThreshold t = this.account.getAlertThreshold();
		if (t.getTrigger(balance)>0) {
			this.kind = IS_MORE;
			this.threshold = t.getMoreThreshold();
		} else {
			this.kind = IS_LESS;
			this.threshold = t.getLessThreshold();
		}
	}

	public Date getDate() {
		return date;
	}

	public Account getAccount() {
		return account;
	}

	public int getKind() {
		return kind;
	}

	public double getThreshold() {
		return threshold;
	}

	public double getBalance() {
		return balance;
	}
}
