package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;
import net.yapbam.util.DateUtils;

/** Balance information.
 * It extends the DefaultListenable class, so a client that want to be informed of balance changes can register a listener (calling addListener).
 * Every time a changes occurs, the listener will receive an EveryThingChangedEvent.
 * @see EverythingChangedEvent
 * @see DefaultListenable#addListener(DataListener)
 * @see FilteredData#getBalanceData()
 */
public class BalanceData extends DefaultListenable {
	private double finalBalance;
	private double checkedBalance;
	private double currentBalance;
	private BalanceHistory balanceHistory;
	// As Yapbam may be opened a day and stay opened until the day after, we add to be cautious with
	// the "current date" concept. In order to never returns a outdated current balance, we will
	// compare the BalanceData initialisation date and the current date in getCurrentBalance().
	// The date is converted to simplify comparison.
	private int currentBalanceDate;
	
	BalanceData() {
	}
	
	void clear(double initialBalance) {
		this.currentBalanceDate = DateUtils.dateToInteger(new Date());
		this.balanceHistory = new BalanceHistory(initialBalance);
		this.finalBalance = initialBalance;
		this.currentBalance = initialBalance;
		this.checkedBalance = initialBalance;
	}

	void enableEvents(boolean enabled) {
		super.setEventsEnabled(enabled);
		if (enabled) fireEvent(new EverythingChangedEvent(this));
	}

	void updateBalance(Transaction transaction, boolean add) {
		double amount = transaction.getAmount();
		if (amount==0) return;
		if (!add) amount = -amount;
		this.finalBalance += amount;
		if (transaction.isChecked()) this.checkedBalance += amount;
		if (DateUtils.dateToInteger(transaction.getValueDate())<=this.currentBalanceDate) this.currentBalance += amount;
		this.balanceHistory.add(amount, transaction.getValueDate());
		this.fireEvent(new EverythingChangedEvent(this));
	}

	void updateBalance(double amount, boolean add) {
		if (amount==0) return;
		if (!add) amount = -amount;
		this.finalBalance += amount;
		this.checkedBalance += amount;
		this.currentBalance += amount;
		this.balanceHistory.add(amount, null);
		this.fireEvent(new EverythingChangedEvent(this));
	}

	/** Returns the current balance.
	 * Current means: today's balance.
	 * @return the current balance.
	 */
	public double getCurrentBalance() {
		Date today = new Date();
		int now = DateUtils.dateToInteger(today);
		if (now!=this.currentBalanceDate) {
			this.currentBalance = this.balanceHistory.getBalance(today);
			this.currentBalanceDate = now;
		}
		return this.currentBalance;
	}

	/** Returns the final balance.
	 * Final means: end of times balance.
	 * @return the final balance.
	 */
	public double getFinalBalance() {
		return this.finalBalance;
	}

	/** Returns the checked balance.
	 * Checked means: all transactions without a statement id are ignored.
	 * @return the checked balance.
	 */
	public double getCheckedBalance() {
		return this.checkedBalance;
	}
	
	/** Returns the balance history.
	 * @return the balance history.
	 */
	public BalanceHistory getBalanceHistory() {
		return this.balanceHistory;
	}
}