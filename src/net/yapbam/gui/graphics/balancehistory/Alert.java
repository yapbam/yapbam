package net.yapbam.gui.graphics.balancehistory;

import java.util.Date;

import net.yapbam.data.Account;

/** A balance alert.
 * The balance history is the best place to display alerts when an account balance exceeds or is less than a predefined threshold.
 * This class represents an alert.
 */
class Alert {
	private Date date;
	private Account account;
	
	
	Alert(Date date, Account account) {
		super();
		this.date = date;
		this.account = account;
	}


	public Date getDate() {
		return date;
	}


	public Account getAccount() {
		return account;
	}
}
