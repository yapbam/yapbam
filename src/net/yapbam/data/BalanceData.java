package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;

/** This class represents balance information.
 * It extends the DefaultListenable class, so a client that want to be informed of balance changes can register a listener (calling addListener).
 * Every time a changes occurs, the listener will receive an EveryThingChangedEvent.
 * @see EverythingChangedEvent
 * @see DefaultListenable#addListener(DataListener)
 */
public class BalanceData extends DefaultListenable { //TODO send event when balance is changed
	private static final long serialVersionUID = 1L;

	private double finalBalance;
	private double checkedBalance;
	private double currentBalance;
	private BalanceHistory balanceHistory;
	
	BalanceData(FilteredData fdata) {}
	
	void clear(double initialBalance) {
		this.balanceHistory = new BalanceHistory(initialBalance);
		this.finalBalance = initialBalance;
		this.currentBalance = initialBalance;
		this.checkedBalance = initialBalance;
	}

	void enableEvents(boolean enabled) {
		super.setEventsEnabled(enabled);
		if (enabled==true) fireEvent(new EverythingChangedEvent(this));
	}

	void updateBalance(Date today, Transaction transaction, boolean add) {
		double amount = transaction.getAmount();
		if (amount==0) return;
		if (!add) amount = -amount;
		this.finalBalance += amount;
		if (transaction.isChecked()) this.checkedBalance += amount;
		if (!transaction.getValueDate().after(today)) this.currentBalance += amount;
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

	public double getCurrentBalance() {
		return this.currentBalance;
	}

	public double getFinalBalance() {
		return this.finalBalance;
	}

	public double getCheckedBalance() {
		return this.checkedBalance;
	}
	
	public BalanceHistory getBalanceHistory() {
		return this.balanceHistory;
	}
}