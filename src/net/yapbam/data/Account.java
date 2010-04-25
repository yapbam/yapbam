package net.yapbam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** This class represents a bank account */
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double initialBalance;
	private List<Mode> receiptModes;
	private List<Mode> expenseModes;
	private List<Mode> modes;
	private List<Checkbook> checkbooks;
	private int transactionNumber;
	private BalanceData balanceData;
	private AlertThreshold alertThreshold;

	/** Constructor.
	 * This constructor creates a new account with one payment mode: Mode.UNDEFINED.
	 * @param name The name of the account
	 * @param initialBalance The initial balance of the account
	 * @see Mode#UNDEFINED
	 */
	public Account(String name, double initialBalance) {
		this.name = name;
		this.initialBalance = initialBalance;
		this.alertThreshold = new AlertThreshold(0, true);
		this.receiptModes = new ArrayList<Mode>();
		this.expenseModes = new ArrayList<Mode>();
		this.modes = new ArrayList<Mode>();
		this.checkbooks = new ArrayList<Checkbook>();
		this.balanceData = new BalanceData();
		this.balanceData.clear(initialBalance);
		this.add(Mode.UNDEFINED);
	}

	/**
	 * Constructor.
	 * @param name The account's name
	 * @param initialBalance The account's inital balance
	 * @param modes The list of the account's avalailable payment modes.
	 */
	public Account(String name, double initialBalance, List<Mode> modes) {
		this(name, initialBalance);
		for (int i = 0; i < modes.size(); i++) {
			this.add(modes.get(i));
		}
	}

	/** Returns the account's name
	 * @return the account's name
	 */
	public String getName() {
		return name;
	}

	/** Returns the account's initial balance.
	 * @return the account's initial balance
	 */
	public double getInitialBalance() {
		return this.initialBalance;
	}

	/** Returns the account's expense/receipt modes number.
	 * @param expense true for expense modes, false for receipt modes.
	 * @return the account's expense/receipt modes number
	 */
	public int getModesNumber(boolean expense) {
		return expense?this.expenseModes.size():this.receiptModes.size();
	}
	
	/** Returns one of the account's expense/receipt payment modes.
	 * @param index the payment mode number.
	 * @param expense true for expense modes, false for receipt modes.
	 * @return the account's payment mode
	 */
	public Mode getMode(int index, boolean expense) {
		return expense?this.expenseModes.get(index):this.receiptModes.get(index);
	}
	
	/** Gets an account's payment mode by its name.
	 * @param name the payment mode's name
	 * @return The payment mode, or null, if no payment mode with that name exists
	 */
	public Mode getMode(String name) {
		for (int i = 0; i < this.modes.size(); i++) {
			if (this.modes.get(i).getName().equalsIgnoreCase(name)) return this.modes.get(i);
		}
		return null;
	}
	
	/** Returns the account's number of checkbooks.
	 * @return an integer
	 */
	public int getCheckbooksNumber() {
		return this.checkbooks.size();
	}
	
	/** Get's an account checkbook
	 * @param index checkbook index
	 * @return a checkbook
	 * @see #getCheckbooksNumber()
	 */
	public Checkbook getCheckbook(int index) {
		return this.checkbooks.get(index);
	}
	
	public int indexOf(Checkbook book) {
		return this.checkbooks.indexOf(book);
	}
	
	void add(Checkbook book) {
		this.checkbooks.add(book);
	}

	void remove(Checkbook book) {
		this.checkbooks.remove(book);
	}
	
	/** Returns the number of transactions in this account.
	 * @return the number of transactions in this account.
	 */
	public int getTransactionsNumber() {
		return transactionNumber;
	}

	/** Adds a transaction to the account.
	 * @param transaction the transaction to add
	 */
	public void add(Transaction transaction) {
		transactionNumber++;
		this.balanceData.updateBalance(transaction, true);
	}
	
	/** Removes a transaction from this account.
	 * @param transaction the transaction to be removed.
	 */
	public void removeTransaction(Transaction transaction) {
		transactionNumber--;
		this.balanceData.updateBalance(transaction, false);
	}

	void add(Mode newMode) {
		if (this.getMode(newMode.getName())!=null) {
			throw new IllegalArgumentException("This account already contains the mode "+newMode.getName());
		}
		this.modes.add(newMode);
		if (newMode.getExpenseVdc()!=null) this.expenseModes.add(newMode);
		if (newMode.getReceiptVdc()!=null) this.receiptModes.add(newMode);
	}

	void remove(Mode mode) {
		this.expenseModes.remove(mode);
		this.receiptModes.remove(mode);
		this.modes.remove(mode);
	}
	

	void replace(Mode oldMode, Mode newMode) {
		// Be aware not to really replace the mode, but update it (transactions have a pointer to their mode).
		oldMode.updateTo(newMode);
		// Rebuild the expensesModes and receiptModes if needed (these lists contain only modes usable for expenses/receipts).
		this.expenseModes.clear();
		this.receiptModes.clear();
		for (int i = 0; i < modes.size(); i++) {
			Mode mode = this.modes.get(i);
			if (mode.getExpenseVdc()!=null) this.expenseModes.add(mode);
			if (mode.getReceiptVdc()!=null) this.receiptModes.add(mode);
		}
	}

	@Override
	public String toString() {
		return this.getName()+"["+this.initialBalance+"]";
	}

	/** Returns the index of a expense/receipt payment mode for this account.
	 * @param mode The mode to find
	 * @param expense true to get the index in the expense payment modes. False for receipt payment modes.
	 * @return a negative number if the mode is unknown for this kind of transaction (expense/receipt),
	 * or the index if it was found.
	 */
	public int findMode(Mode mode, boolean expense) {
		return expense?this.expenseModes.indexOf(mode):this.receiptModes.indexOf(mode);
	}
	
	/** Returns this account's total number of payment modes (expense and receipt modes).
	 * @return this account's total number of payment modes
	 */
	public int getModesNumber() {
		return this.modes.size();
	}
	
	/** Gets a payment mode by its index.
	 * @param index the payment mode index.
	 * @return the payment mode.
	 */
	public Mode getMode(int index) {
		return this.modes.get(index);
	}

	/** Returns the index of a payment mode for this account.
	 * @param mode The mode to find
	 * @return a negative number if the mode is unknown, or the index if it was found.
	 */
	public int indexOf(Mode mode) {
		return this.modes.indexOf(mode);
	}
	
	void setName(String name) {
		this.name = name;
	}

	void setInitialBalance(double value) {
		this.balanceData.updateBalance(this.initialBalance, false);
		this.balanceData.updateBalance(this.initialBalance, true);
		this.initialBalance = value;
	}

	/** Gets the alert threshold.
	 * @return an {@link AlertThreshold}
	 */
	public AlertThreshold getAlertThreshold() {
		return this.alertThreshold;
	}

	void setAlertThreshold(AlertThreshold alertThreshold) {
		this.alertThreshold = alertThreshold;
	}
}
