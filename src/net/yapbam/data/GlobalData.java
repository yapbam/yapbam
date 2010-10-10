package net.yapbam.data;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.yapbam.data.event.*;
import net.yapbam.data.xml.Serializer;
import net.yapbam.date.helpers.DateStepper;
import net.yapbam.util.NullUtils;

/** The whole Yapbam data.
 *  You can also have a look at FilteredData which presents a filtered view of Yapbam data.
 *  @see FilteredData
 */
public class GlobalData extends DefaultListenable {
	private List<Account> accounts;
	private List<PeriodicalTransaction> periodicals;
	private List<Transaction> transactions;
	private URI uri;
	private boolean somethingChanged;
	private List<Category> categories;
	private String password;

	private boolean eventsPending;

	private static Currency defaultCurrency;
	private static double defaultPrecision;
	static {
		setDefaultCurrency(Currency.getInstance(Locale.getDefault()));
	}

	private static final Comparator<Transaction> COMPARATOR = new Comparator<Transaction>() {
		@Override
		public int compare(Transaction o1, Transaction o2) {
			return Long.signum(o1.getId()-o2.getId());
		}
	};

	private static final Comparator<PeriodicalTransaction> PERIODICAL_COMPARATOR = new Comparator<PeriodicalTransaction>() {
		@Override
		public int compare(PeriodicalTransaction o1, PeriodicalTransaction o2) {
			int result = o1.getDescription().compareToIgnoreCase(o2.getDescription());
			if (result==0) result = Long.signum(o1.getId()-o2.getId());
			return result;
		}
	};
	
	/** As amount are represented by doubles, and doubles are unable to represent exactly decimal numbers,
	 * we have to take care when we compare two amounts, especially, if we intend to know if two amounts are equals.
	 * This comparator returns that the doubles are equals if their difference is less than the current currency precision.
	 * @see #setDefaultCurrency(Currency)
	 */
	public static final Comparator<Double> AMOUNT_COMPARATOR = new Comparator<Double>() {
		@Override
		public int compare(Double o1, Double o2) {
			// o1.equals(o2) is here because if the doubles are positive or negative infinity, their difference is not defined
			if (o1.equals(o2) || (Math.abs(o1-o2)<defaultPrecision)) return 0;
			return o1<o2?-1:1;
		}
	};
	
	/** Constructor
	 * Builds a new empty instance.
	 */
	public GlobalData() {
		super();
	    this.clear();
	}
	
	/** Sets the currency to be used in Yapbam.
	 * As amounts are represented by doubles, and doubles are unable to represent exactly decimal numbers,
	 * "amount is the same" is related to the currency precision.
	 * @param currency The currency to be used.
	 * @see #AMOUNT_COMPARATOR
	 */
	public static void setDefaultCurrency(Currency currency) {
		defaultCurrency = currency;
		defaultPrecision = Math.pow(10, -currency.getDefaultFractionDigits())/2;
	}
	
	/** Gets the default currency.
	 * @return a currency.
	 */
	public static Currency getDefaultCurrency() {
		return defaultCurrency;
	}

	/** Tests if the data is empty (no accounts, no transactions, no category, etc... , really nothing !)
	 * @return true if the data is empty
	 */
	public boolean isEmpty() {
		return this.accounts.size()==0;
	}
	
	/** Tests if the data needs to be saved.
	 * @return true if the data needs to be saved, false, if there's nothing to change (no changes since last save).
	 * @see #save(URI)
	 */
	public boolean somethingHasChanged() {
		return this.somethingChanged;
	}
	
	/** Gets the URI where the data is saved.
	 * @return an URI or null the data isn't attach to any location.
	 */
	public URI getURI() {
		return uri;
	}
	
	/** Gets the password that protects the data.
	 * @return a string or null if data is not password protected.
	 */
	public String getPassword() {
		return this.password;
	}

	/** Saves the data into a file.
	 * @param uri The URI where to save the data.
	 * @throws IOException if a problem occurs while saving the data.
	 * @see #read(URI, String)
	 */
	public void save(URI uri) throws IOException {
		Serializer.write(this, uri);
		this.somethingChanged = false;
		URI old = this.uri;
		this.uri = uri;
		fireEvent(new NeedToBeSavedChangedEvent(this));
		if (!this.uri.equals(old)) fireEvent(new URIChangedEvent(this));
	}

	/** Reads the data from an URI.
	 * The only DataEvent sent during the read is EverythingChangedEvent, where the read is successfully finished.
	 * @param uri The URI we want to read the data from.
	 * @param password the password that protects the data or null if there's no password.
	 * @throws IOException if a problem occurs while reading the data.
	 * @throws AccessControlException if the password is wrong
	 * @see EverythingChangedEvent
	 */	
	public void read(URI uri, String password) throws IOException {
		this.setEventsEnabled(false);
		try {
			Serializer.read (this, uri, password);
			this.uri = uri;
			// We do not want the file reading results in a "modified" state for the file,
			// even if, of course, a lot of things changed on the screen. But, the file
			// is unmodified.
			this.somethingChanged = false;
		} finally {
			this.setEventsEnabled(true);
		}
	}
	
	/** Sets the password used to protect the data (to encrypt the file containing it).
	 * @param password a string (null or an empty string if the data is not protected).
	 */
	public void setPassword(String password) {
		if ((password!=null) && (password.length()==0)) password = null;
		if (!NullUtils.areEquals(this.password, password)) {
			String old = this.password;
			this.password = password;
			fireEvent(new PasswordChangedEvent(this, old, this.password));
			this.setChanged();
		}
	}

	@Override
	/** Sets the events enabled.
	 * When events are enabled, every modification on the data results in a fired event.
	 * If you want to perform a lot of modifications on the data, this will results in a large
	 * amount of events, and a poor performance. Then, you can disable events, do the modifications,
	 * then enable events. When events are turn on, a EverythingChangedEvent is sent if some modification
	 * occurs since events were disabled.
	 * @param enabled true to enable events, false to disable events.
	 */
	public void setEventsEnabled(boolean enabled) {
		if (super.IsEventsEnabled()) eventsPending = false;
		super.setEventsEnabled(enabled);
		if (enabled && (eventsPending)) fireEvent(new EverythingChangedEvent(this));
	}

	/** Tests whether the events are enabled or not.
	 * @return true if the events are enabled.
	 */
	public boolean isEventsEnabled() {
		return super.IsEventsEnabled();
	}

	@Override
	protected void fireEvent(DataEvent event) {
		if (IsEventsEnabled()) {
			super.fireEvent(event);
		} else {
			eventsPending = true;
		}
	}

	public Account getAccount(String name) {
		for (int i = 0; i < this.accounts.size(); i++) {
			Account account = this.accounts.get(i);
			if (account.getName().equalsIgnoreCase(name)) return account;
		}
		return null;
	}
	
	public Account getAccount(int index) {
		return this.accounts.get(index);
	}

	public int getAccountsNumber() {
		return this.accounts.size();
	}

	public int indexOf(Account account) {
		return this.accounts.indexOf(account);
	}

	public void add(Account account) {
		if (getAccount(account.getName())!=null) throw new IllegalArgumentException("Duplicate account name : "+account);
		this.accounts.add(account);
		fireEvent(new AccountAddedEvent(this, account));
		this.setChanged();
	}

	public int getTransactionsNumber() {
		return this.transactions.size();
	}

	public Transaction getTransaction(int index) {
		return this.transactions.get(index);
	}

	public void add(Transaction transaction) {
		int index = -Collections.binarySearch(this.transactions, transaction, COMPARATOR)-1;
		this.transactions.add(index, transaction);
		Account account = transaction.getAccount();
		account.add(transaction);
		fireEvent(new TransactionAddedEvent(this, transaction));
		this.setChanged();
		if (transaction.getMode().isUseCheckBook() && (transaction.getAmount()<=0)) { // If transaction use checkbook
			// Detach check
			String number = transaction.getNumber();
			if (number!=null) {
				for (int i = 0; i < account.getCheckbooksNumber(); i++) {
					Checkbook checkbook = account.getCheckbook(i);
					BigInteger shortNumber = checkbook.getShortNumber(number);
					if (!checkbook.isEmpty() && (shortNumber!=null)) {
						if (shortNumber.compareTo(checkbook.getNext())>=0) {
							Checkbook newOne = new Checkbook(checkbook.getPrefix(), checkbook.getFirst(), checkbook.size(), shortNumber.equals(checkbook.getLast())?null:shortNumber.add(BigInteger.ONE));
							setCheckbook(account, checkbook, newOne);
						}
						break;
					}
				}
			}
		}
	}
	
	public boolean remove(Transaction transaction) {
		int index = indexOf(transaction);
		if (index>=0) {
			this.removeTransaction(index);
			return true;
		} else {
			return false;
		}
	}
	
	public int indexOf(Transaction transaction) {
		return Collections.binarySearch(this.transactions, transaction, COMPARATOR);
	}

	public int getCategoriesNumber() {
		return this.categories.size();
	}
	
	/** Get a category by its index.
	 * @param index category index
	 * @return the category (note : categories are always sorted by their name) 
	 */
	public Category getCategory(int index) {
		return this.categories.get(index);
	}

	public Category getCategory(String categoryId) {
		if (categoryId==null) return Category.UNDEFINED;
		int index = Collections.binarySearch(categories, new Category(categoryId));
		if (index<0) {
			return null;
		} else {
			return this.categories.get(index);
		}
	}

	public int indexOf(Category category) {
		return Collections.binarySearch(categories, category);
	}

	public void add(Category category) {
		if (category.getName()==null) throw new IllegalArgumentException();
		int index = -Collections.binarySearch(categories, category)-1;
		this.categories.add(index, category);
		fireEvent(new CategoryAddedEvent(this, category));
		setChanged();
	}

	public void clear() {
		this.categories = new ArrayList<Category>();
		this.categories.add(Category.UNDEFINED);
	    this.accounts = new ArrayList<Account>();
	    this.periodicals = new ArrayList<PeriodicalTransaction>();
	    this.transactions = new ArrayList<Transaction>();
	    this.uri = null;
	    this.password = null;
	    this.somethingChanged=false;
		fireEvent(new EverythingChangedEvent(this));
	}

	public void removeTransaction(int index) {
		Transaction removed = this.transactions.remove(index);
		removed.getAccount().removeTransaction(removed);
		this.fireEvent(new TransactionRemovedEvent(this, index, removed));
		setChanged();
	}
	
	public void add(PeriodicalTransaction periodical) {
		int index = -Collections.binarySearch(this.periodicals, periodical, PERIODICAL_COMPARATOR)-1;
		this.periodicals.add(index, periodical);
		fireEvent(new PeriodicalTransactionAddedEvent(this, index));
		setChanged();
	}
	
	public int getPeriodicalTransactionsNumber() {
		return this.periodicals.size();
	}
	
	public PeriodicalTransaction getPeriodicalTransaction(int index) {
		return this.periodicals.get(index);
	}
	
	/** Removes a periodical transaction identified by its index.
	 * @param periodical The periodical transaction to remove
	 * @return true if the transaction was found in tis data, false if it was not in this data
	 */
	public boolean remove (PeriodicalTransaction periodical) {
		int index = Collections.binarySearch(this.periodicals, periodical, PERIODICAL_COMPARATOR);
		if (index>=0) {
			this.removePeriodicalTransaction(index);
			return true;
		} else {
			return false;
		}
	}

	/** Removes a periodical transaction indentified by its index.
	 * @param index the periodical transaction index
	 */
	public void removePeriodicalTransaction(int index) {
		PeriodicalTransaction removed = this.periodicals.remove(index);
		this.fireEvent(new PeriodicalTransactionRemovedEvent(this, index, removed));
		setChanged();
	}

	/** Increments a periodical transaction next date until it becomes greater than a date.
	 * If the periodical transaction have no next date, this method does nothing. 
	 * @param index the periodical transaction index
	 * @param date the limit date the periodical transaction have to pass
	 */
	public void setPeriodicalTransactionNextDate(int index, Date date) {
		PeriodicalTransaction pt = getPeriodicalTransaction(index);
		Date nextDate = pt.getNextDate();
		if (nextDate!=null) {
			DateStepper ds = pt.getNextDateBuilder();
			if (ds == null) {
				nextDate = date;
			} else {
				while ((nextDate!=null) && (nextDate.compareTo(date)<=0)) {
					nextDate = ds.getNextStep(nextDate);
				}
			}
			pt = new PeriodicalTransaction(pt.getDescription(), pt.getAmount(), pt.getAccount(), pt.getMode(),
					pt.getCategory(), Arrays.asList(pt.getSubTransactions()), nextDate, pt.isEnabled(), ds);
			removePeriodicalTransaction(index);
			add(pt);
		}
	}
	
	/** Generate transactions from the periodical transactions until a date.
	 * The transactions are not added to the global data and the periodical transactions
	 * are not changed : their next date fields remains unchanged.
	 * @param date Date until the transactions had to be generated (inclusive)
	 * @return a transaction array.
	 */
	public Transaction[] generateTransactionsFromPeriodicals(Date date) {
		List<Transaction> result = new ArrayList<Transaction>();
		for (int i=0; i<getPeriodicalTransactionsNumber(); i++) {
			PeriodicalTransaction p = getPeriodicalTransaction(i);
			if (p.isEnabled() && (p.getNextDate().compareTo(date)<=0)) {
				double amount = p.getAmount();
				Mode mode = p.getMode();
				DateStepper vdStepper = amount<0?mode.getExpenseVdc():mode.getReceiptVdc();
				//Be aware, when the transaction has an "end date", and the date is after this "end date", tDate become null
				for (Date tDate = p.getNextDate();((tDate!=null)&&(tDate.compareTo(date)<=0));tDate=p.getNextDateBuilder().getNextStep(tDate)) {
					result.add(new Transaction(tDate, null, p.getDescription(), amount, p.getAccount(), mode, p.getCategory(),
							vdStepper.getNextStep(tDate), null, Arrays.asList(p.getSubTransactions())));
				}
			}
		}
		return result.toArray(new Transaction[result.size()]);
	}

	private void setChanged() {
		if (!this.somethingChanged) {
			this.somethingChanged = true;
			this.fireEvent(new NeedToBeSavedChangedEvent(this));
		}
	}

	/** Removes an account from the data.
	 * If some transactions were attached to the account, all these transactions will be also removed.
	 * @param account the account to be removed
	 */
	public void remove(Account account) {
		int index = this.accounts.indexOf(account);
		if (index>=0){
			if (account.getTransactionsNumber()!=0) {
				for (int i = this.transactions.size()-1; i >= 0 ; i--) {
					if (this.transactions.get(i).getAccount()==account) removeTransaction(i);
				}
			}
			for (int i = this.periodicals.size()-1; i >= 0 ; i--) {
				if (this.periodicals.get(i).getAccount()==account) removePeriodicalTransaction(i);
			}
			this.accounts.remove(index);
			this.fireEvent(new AccountRemovedEvent(this, index, account));
			this.setChanged();
		}
	}

	/** Changes the name of an account.
	 * @param account the account to be changed
	 * @param value the new account name
	 * @throws IllegalArgumentException if the name is already used for another account.
	 */
	public void setName(Account account, String value) {
		String old = account.getName();
		if (!old.equals(value)) {
			// Check that this account name is not already used
			if (getAccount(value) != null) throw new IllegalArgumentException("Account name already exists");
			account.setName(value);
			this.fireEvent(new AccountPropertyChangedEvent(this, AccountPropertyChangedEvent.NAME, account, old,value));
			this.setChanged();
		}
	}

	/** Changes the initial balance of an account.
	 * @param account the account to be changed
	 * @param value the new initial balance
	 * @throws IllegalArgumentException if the name is already used for another account.
	 */
	public void setInitialBalance(Account account, double value) {
		double old = account.getInitialBalance();
		if (old != value) {
			account.setInitialBalance(value);
			this.fireEvent(new AccountPropertyChangedEvent(this, AccountPropertyChangedEvent.INITIAL_BALANCE, account, old, value));
			this.setChanged();
		}
	}
	
	/** Changes the alert threshold for this account.
	 * @param account the account to be changed
	 * @param threshold the alert threshold to apply to this account 
	 */
	public void setAlertThreshold (Account account, AlertThreshold threshold) {
		AlertThreshold old = account.getAlertThreshold();
		if (!old.equals(threshold)) {
			account.setAlertThreshold(threshold);
			this.fireEvent(new AccountPropertyChangedEvent(this, AccountPropertyChangedEvent.ALERT_THRESHOLD, account, old, threshold));
			this.setChanged();
		}
	}

	class CategoryUpdater extends AbstractTransactionUpdater {
		private Category oldCategory;
		private Category newCategory;

		CategoryUpdater (Category oldMode, Category newMode) {
			super(GlobalData.this);
			this.oldCategory = oldMode;
			this.newCategory = newMode;
		}
		
		@Override
		Transaction change(Transaction t) {
			return t.change(oldCategory, newCategory);
		}

		@Override
		PeriodicalTransaction change(PeriodicalTransaction t) {
			return t.change(oldCategory, newCategory);
		}
	}

	/** Removes a category from the data
	 * All the transactions and the subtransactions attached to the deleted category are moved to the "undifined" category.
	 * @param category
	 */
	public void remove(Category category) {
		int index = this.categories.indexOf(category);
		if (index>=0){
			new CategoryUpdater(category, Category.UNDEFINED).doIt();
			this.categories.remove(index);
			this.fireEvent(new CategoryRemovedEvent(this, index, category));
			this.setChanged();
		}
	}

	public void setName(Category category, String value) {
		String old = category.getName();
		if (!old.equals(value)) {
			// Check that this category name is not already used
			if (getCategory(value) != null) throw new IllegalArgumentException("Category name already exists");
			// Category list is sorted by name => we have to change the category position
			this.categories.remove(indexOf(category));
			category.setName(value);
			int index = -Collections.binarySearch(categories, category)-1;
			this.categories.add(index, category);
			this.fireEvent(new CategoryPropertyChangedEvent(this, CategoryPropertyChangedEvent.NAME, category, old,value));
			this.setChanged();
		}
	}

	public void add(Account account, Mode mode) {
		account.add(mode);
		this.fireEvent(new ModeAddedEvent(this, account, mode));
		this.setChanged();
	}
	
	class ModeUpdater extends AbstractTransactionUpdater {
		private Account account;
		private Mode oldMode;
		private Mode newMode;

		ModeUpdater (Account account, Mode oldMode, Mode newMode) {
			super(GlobalData.this);
			this.account = account;
			this.oldMode = oldMode;
			this.newMode = newMode;
		}
		
		@Override
		Transaction change(Transaction t) {
			return t.change(account, oldMode, newMode);
		}

		@Override
		PeriodicalTransaction change(PeriodicalTransaction t) {
			return t.change(account, oldMode, newMode);
		}
	}

	public void remove(Account account, Mode mode) {
		int index = account.indexOf(mode);
		if (index>=0){
			new ModeUpdater(account, mode, Mode.UNDEFINED).doIt();
			account.remove(mode);
			this.fireEvent(new ModeRemovedEvent(this, index, account, mode));
			this.setChanged();
		}
	}

	public void setMode(Account account, Mode oldMode, Mode newMode) {
		ModePropertyChangedEvent event = new ModePropertyChangedEvent(this, account, oldMode, newMode);
		if (event.getChanges()!=0) {
			// oldMode object will be updated. In order to send the right event data, we have to remember it
			// So, we'll store it in a new fresh mode object : oldVanished.
			Mode oldVanished = new Mode(oldMode.getName(), oldMode.getReceiptVdc(), oldMode.getExpenseVdc(), oldMode.isUseCheckBook());
			account.replace(oldMode, newMode);
			event = new ModePropertyChangedEvent(this, account, oldVanished, oldMode);
			this.fireEvent(event);
			this.setChanged();
		}
	}

	/** Adds a checkbook to an account.
	 * @param account the account
	 * @param book the checkbook to add to the account
	 */
	public void add(Account account, Checkbook book) {
		account.add(book);
		this.fireEvent(new CheckbookAddedEvent(this, account, book));
		this.setChanged();
	}
	
	public void remove(Account account, Checkbook book) {
		int index = account.indexOf(book);
		if (index>=0){
			account.remove(book);
			this.fireEvent(new CheckbookRemovedEvent(this, index, account, book));
			this.setChanged();
		}
	}

	/** Updates a checkbook with the data of another checkbook.
	 * @param account The account that contains the checkbook.
	 * @param old The checkbook we want to update
	 * @param checkbook The checkbook that contains new data 
	 */
	public void setCheckbook(Account account, Checkbook old, Checkbook checkbook) {
		CheckbookPropertyChangedEvent event = new CheckbookPropertyChangedEvent(this, account, old, checkbook);
		if (event.getChanges()!=0) {
			// old object will be updated. In order to send the right event data, we have to remember it
			// So, we'll store it in a new fresh mode object : oldVanished.
			Checkbook oldVanished = new Checkbook(old.getPrefix(), old.getFirst(), old.size(), old.getNext());
			old.copy (checkbook);
			event = new CheckbookPropertyChangedEvent(this, account, oldVanished, old);
			this.fireEvent(event);
			this.setChanged();
		}
	}


}