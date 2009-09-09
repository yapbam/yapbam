package net.yapbam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import net.yapbam.data.event.DefaultListenable;
import net.yapbam.data.event.ModeAddedEvent;

/** This class represents a bank account */
public class Account extends DefaultListenable implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double initialBalance;
	private ArrayList<Mode> receiptModes;
	private ArrayList<Mode> expenseModes;
	private HashMap<String, Mode> modes;
//	private ArrayList<Transaction> transactions;
	
	public Account(String name, double initialBalance) {
		this.name = name;
		this.initialBalance = initialBalance;
		this.receiptModes = new ArrayList<Mode>();
		this.expenseModes = new ArrayList<Mode>();
		this.modes = new HashMap<String, Mode>();
		this.add(Mode.UNDEFINED);
//		this.transactions = new ArrayList<Transaction>();
	}

	public String getName() {
		return name;
	}

	public double getInitialBalance() {
		return this.initialBalance;
	}

	public int getModesNumber(boolean expense) {
		return expense?this.expenseModes.size():this.receiptModes.size();
	}
	
	public Mode getMode(int index, boolean expense) {
		return expense?this.expenseModes.get(index):this.receiptModes.get(index);
	}
	
	public Mode getMode(String name) {
		return this.modes.get(name.toUpperCase());
	}
	
	/*	public int getTransactionsNumber() {
		return transactions.size();
	}*/

	public void add(AbstractTransaction transaction) {
//		this.transactions.add(transaction);
	}

	public void add(Mode newMode) {
		if (this.getMode(newMode.getName())!=null) {
			throw new IllegalArgumentException("This account already contains the mode "+newMode.getName());
		}
		this.modes.put(newMode.getName().toUpperCase(), newMode);
		if (newMode.getExpenseVdc()!=null) this.expenseModes.add(newMode);
		if (newMode.getReceiptVdc()!=null) this.receiptModes.add(newMode);
		this.fireEvent(new ModeAddedEvent(this));
	}

	@Override
	public String toString() {
		return this.getName()+"["+this.initialBalance+"]";
	}

	public int findMode(Mode mode, boolean expense) {
		return expense?this.expenseModes.indexOf(mode):this.receiptModes.indexOf(mode);
	}
}
