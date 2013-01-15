package net.yapbam.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** A bank account */
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double initialBalance;
	private List<Mode> modes;
	private List<Checkbook> checkbooks;
	private int transactionNumber;
	private BalanceData balanceData;
	private AlertThreshold alertThreshold;
	private String comment;

	/** Constructor.
	 * <br>This constructor creates a new account with the default alerts and one payment mode: Mode.UNDEFINED.
	 * @param name The name of the account
	 * @param initialBalance The initial balance of the account
	 * @see Mode#UNDEFINED
	 * @see AlertThreshold#DEFAULT
	 */
	public Account(String name, double initialBalance) {
		this(name, initialBalance, AlertThreshold.DEFAULT);
	}

	/**
	 * Constructor.
	 * <br>This constructor creates a new account with one payment mode: Mode.UNDEFINED.
	 * @param name The account's name
	 * @param initialBalance The account's initial balance
	 * @param alerts The alerts set on the account.
	 */
	public Account(String name, double initialBalance, AlertThreshold alerts) {
		this(name, initialBalance, alerts, null);
	}

	public Account(String name, double initialBalance, AlertThreshold alerts, String comment) {
		this.name = name;
		this.initialBalance = initialBalance;
		this.alertThreshold = alerts;
		this.modes = new ArrayList<Mode>();
		this.checkbooks = new ArrayList<Checkbook>();
		this.balanceData = new BalanceData();
		this.balanceData.clear(initialBalance);
		this.add(Mode.UNDEFINED);
		this.setComment(comment);
	}

	/** Gets the account's name
	 * @return the account's name
	 */
	public String getName() {
		return name;
	}

	/** Gets the account's initial balance.
	 * @return the account's initial balance
	 */
	public double getInitialBalance() {
		return this.initialBalance;
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
	
	/** Gets the account's number of checkbooks.
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
	
	/** Gets the index of a checkbook in the account.
	 * @param book The checkbook to test.
	 * @return a negative integer if the checkbook is unknown in this account,
	 * or, else, the index of the checkbook.  
	 */
	public int indexOf(Checkbook book) {
		return this.checkbooks.indexOf(book);
	}
	
	void add(Checkbook book) {
		this.checkbooks.add(book);
	}

	void remove(Checkbook book) {
		this.checkbooks.remove(book);
	}
	
	/** Gets the number of transactions in this account.
	 * @return the number of transactions in this account.
	 */
	public int getTransactionsNumber() {
		return transactionNumber;
	}

	/** Adds transactions to the account.
	 * @param transactions the transaction to add
	 */
	void add(Transaction[] transactions) {
		transactionNumber += transactions.length;
		this.balanceData.updateBalance(transactions, true);
	}
	
	/** Removes transactions from this account.
	 * @param transactions the transactions to be removed.
	 */
	void remove(Transaction[] transactions) {
		transactionNumber = transactionNumber - transactions.length;
		this.balanceData.updateBalance(transactions, false);
	}

	void add(Mode newMode) {
		if (this.getMode(newMode.getName())!=null) {
			throw new IllegalArgumentException("This account already contains the mode "+newMode.getName()); //$NON-NLS-1$
		}
		this.modes.add(newMode);
	}

	void remove(Mode mode) {
		this.modes.remove(mode);
	}
	

	void replace(Mode oldMode, Mode newMode) {
		// Be aware not to really replace the mode, but update it (transactions have a pointer to their mode).
		oldMode.updateTo(newMode);
	}

	@Override
	public String toString() {
		return this.getName()+"["+this.initialBalance+"]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Gets the index of a payment mode in this account.
	 * @param mode The mode to find
	 * @return a negative number if the mode is unknown in this kind of transaction (expense/receipt),
	 * or the index if it was found.
	 */
	public int findMode(Mode mode) {
		return this.modes.indexOf(mode);
	}
	
	/** Gets this account's total number of payment modes (expense and receipt modes).
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

	/** Gets the index of a payment mode for this account.
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
		this.initialBalance = value;
		this.balanceData.updateBalance(this.initialBalance, true);
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

	/** Gets this account's balance data.
	 * @return a BalanceData
	 */
	public BalanceData getBalanceData() {
		return this.balanceData;
	}
	
	/** Gets this account's comment.
	 * @return a String or null if no comment is attached to this account
	 */
	public String getComment() {
		return this.comment;
	}
	
	void setComment(String comment) {
		this.comment = comment;
		if (this.comment!=null) {
			this.comment = this.comment.trim();
			if (this.comment.length()==0) this.comment = null;
		}
	}
}
