package net.yapbam.data;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.SwingUtilities;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.event.*;
import net.yapbam.data.xml.Serializer;
import net.yapbam.date.helpers.DateStepper;

/** The whole Yapbam data.
 *  <br>You can also have a look at FilteredData which presents a filtered view of Yapbam data.
 *  @see FilteredData
 */
public class GlobalData extends DefaultListenable {
	private static final boolean CHECK_EVENT_ON_EDT = Boolean.getBoolean("CheckGlobalDataEventAreOnEDT");
	
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
	 * <br>Builds a new empty instance.
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
	 * @see #save(URI, ProgressReport)
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
	 * @param report A progress report where to report the progress of the operation or null, to not report anything.
	 * @throws IOException if a problem occurs while saving the data.
	 * @see #read(URI, String, ProgressReport)
	 */
	public void save(URI uri, ProgressReport report) throws IOException {
		Serializer.write(this, uri, report);
		this.somethingChanged = false;
		setURI(uri);
		fireEvent(new NeedToBeSavedChangedEvent(this));
	}

	/** Sets the URI attached to the data.
	 * @param uri The new URI.
	 */
	public void setURI(URI uri) {
		URI old = this.uri;
		this.uri = uri;
		if (!this.uri.equals(old)) fireEvent(new URIChangedEvent(this));
	}

	/** Reads the data from an URI.
	 * The only DataEvent sent during the read is EverythingChangedEvent, where the read is successfully finished.
	 * @param uri The URI we want to read the data from.
	 * @param password the password that protects the data or null if there's no password.
	 * @param report A progress report where to report the progress of the operation or null, to not report anything.
	 * @throws IOException if a problem occurs while reading the data.
	 * @throws AccessControlException if the password is wrong
	 * @see EverythingChangedEvent
	 */	
	public void read(URI uri, String password, ProgressReport report) throws IOException, AccessControlException {
		this.setEventsEnabled(false);
		try {
//System.out.println ("Start reading");
//long st = System.currentTimeMillis();
			Serializer.read (this, uri, password, report);
//System.out.println ("Reading in "+(System.currentTimeMillis()-st)+"ms");
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
			if (CHECK_EVENT_ON_EDT && !SwingUtilities.isEventDispatchThread()) {
				RuntimeException e = new RuntimeException("WARNING: a GlobalData event is thrown in a thread different from the event dispatch thread !");
				e.fillInStackTrace();
				e.printStackTrace();
			}
			super.fireEvent(event);
		} else {
			eventsPending = true;
		}
	}

	/** Gets an account by its name.
	 * @param name The account's name
	 * @return an Account or null if no account with this name exists
	 */
	public Account getAccount(String name) {
		for (Account account : this.accounts) {
			if (account.getName().equalsIgnoreCase(name)) return account;
		}
		return null;
	}
	
	/** Gets an account by its index.
	 * @param index The account's index
	 * @return an Account
	 */
	public Account getAccount(int index) {
		return this.accounts.get(index);
	}

	/** Gets the number of accounts.
	 * @return a positive or null int
	 */
	public int getAccountsNumber() {
		return this.accounts.size();
	}

	public int indexOf(Account account) {
		return this.accounts.indexOf(account);
	}

	public void add(Account account) {
		if (getAccount(account.getName())!=null) throw new IllegalArgumentException("Duplicate account name : "+account); //$NON-NLS-1$
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

	/** Adds some transactions.
	 * @param transactions The transactions to add
	 */
	public void add(Transaction[] transactions) {
		if (transactions.length==0) return;
		// In order to optimize the number of events fired, we will group transactions by account before
		// removing them from their accounts (so, we will generate a maximum of one event per account).
		// Initialize the lists of transactions per account.
		List<Collection<Transaction>> accountTransactions = new ArrayList<Collection<Transaction>>(this.getAccountsNumber());
		for (int i = 0; i < this.getAccountsNumber(); i++) accountTransactions.add(new ArrayList<Transaction>());
		for (Transaction transaction : transactions) {
			int index = -Collections.binarySearch(this.transactions, transaction, COMPARATOR)-1;
			this.transactions.add(index, transaction);
			accountTransactions.get(indexOf(transaction.getAccount())).add(transaction);
		}
		for (Collection<Transaction> collection : accountTransactions) { // For each account (there's one collection per account)
			if (collection.size()>0) { // If this account has some transactions removed
				Transaction[] addedAccountTransactions = collection.toArray(new Transaction[collection.size()]);
				addedAccountTransactions[0].getAccount().add(addedAccountTransactions);
			}
		}
		fireEvent(new TransactionsAddedEvent(this, transactions));
		this.setChanged();
		for (Transaction transaction : transactions) {
			// Let's examine if this new transaction is a check and has a number behind next check available
			// If so, detach the checks between current "next check" and this one (included).
			if (transaction.getMode().isUseCheckBook() && (transaction.getAmount()<=0)) { // If transaction use checkbook
					// Detach check
					String number = transaction.getNumber();
					if (number!=null) {
						Account account = transaction.getAccount();
						for (int j = 0; j < account.getCheckbooksNumber(); j++) {
							Checkbook checkbook = account.getCheckbook(j);
							BigInteger shortNumber = checkbook.getNumber(number);
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
	}

	/** Adds a transaction.
	 * @param transaction The transaction to add.
	 */
	public void add (Transaction transaction) {
		add(new Transaction[]{transaction});
	}
	
	/** Removes some transactions from this.
	 * <br>Note : that if one or more transactions are not in this, they are ignored.
	 * <br>If one or more transactions are not ignored, a TransactionsRemovedEvent is fired.
	 * @param transactions The transactions to be removed.
	 * @see TransactionsRemovedEvent
	 */
	public void remove(Transaction[] transactions) {
		Collection<Transaction> removed = new ArrayList<Transaction>(transactions.length);
		// In order to optimize the number of events fired, we will group transactions by account before
		// removing them from their accounts (so, we will generate a maximum of one event per account).
		// Initialize the lists of transactions per account.
		List<Collection<Transaction>> accountTransactions = new ArrayList<Collection<Transaction>>(this.getAccountsNumber());
		for (int i = 0; i < this.getAccountsNumber(); i++) accountTransactions.add(new ArrayList<Transaction>());
		for (Transaction transaction: transactions) {
			int index = indexOf(transaction);
			if (index>=0) {
				Transaction removedTransaction = this.transactions.remove(index);
				removed.add(removedTransaction);
				accountTransactions.get(indexOf(transaction.getAccount())).add(removedTransaction);
			}
		}
		if (removed.size()>0) {
			for (Collection<Transaction> collection : accountTransactions) { // For each account (there's one collection per account)
				if (collection.size()>0) { // If this account has some transactions removed
					Transaction[] removedAccountTransactions = collection.toArray(new Transaction[collection.size()]);
					removedAccountTransactions[0].getAccount().remove(removedAccountTransactions);
				}
			}
			this.fireEvent(new TransactionsRemovedEvent(this, removed.toArray(new Transaction[removed.size()])));
			setChanged();
		}
	}

	/** Removes a transaction from this.
	 * If the transaction is not in this, does nothing. 
	 * @param transaction
	 */
	public void remove(Transaction transaction) {
		remove(new Transaction[]{transaction});
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
		this.somethingChanged = false;
		fireEvent(new EverythingChangedEvent(this));
	}

	public void add(PeriodicalTransaction periodical) {
		int index = -Collections.binarySearch(this.periodicals, periodical, PERIODICAL_COMPARATOR)-1;
		this.periodicals.add(index, periodical);
		fireEvent(new PeriodicalTransactionsAddedEvent(this, new PeriodicalTransaction[]{periodical}));
		setChanged();
	}
	
	/** Adds periodical transactions.
	 * @param transactions The transactions to add
	 */
	public void add(PeriodicalTransaction[] transactions) {
		if (transactions.length==0) return;
		for (PeriodicalTransaction transaction : transactions) {
			int index = -Collections.binarySearch(this.periodicals, transaction, PERIODICAL_COMPARATOR)-1;
			this.periodicals.add(index, transaction);
		}
		fireEvent(new PeriodicalTransactionsAddedEvent(this, transactions));
		setChanged();
	}
	
	/** Gets the number of periodicals transactions.
	 * @return an positive or null integer.
	 */
	public int getPeriodicalTransactionsNumber() {
		return this.periodicals.size();
	}
	
	public PeriodicalTransaction getPeriodicalTransaction(int index) {
		return this.periodicals.get(index);
	}
	
	/** Removes some periodical transactions.
	 * <br>Note : that if one or more transactions are not in this, they are ignored.
	 * @param periodicals The periodical transactions to remove
	 */
	public void remove (PeriodicalTransaction[] periodicals) {
		int nb = 0; // The number of effectively removed transactions (the ones found in this).
		int[] indexes = new int[periodicals.length];
		for (PeriodicalTransaction transaction : periodicals) {
			indexes[nb] = Collections.binarySearch(this.periodicals, transaction, PERIODICAL_COMPARATOR);
			if (indexes[nb]>=0) nb++;
		}
		if (nb>0) { // If some were found
			PeriodicalTransaction[] removed = new PeriodicalTransaction[nb];
			int[] removedIndexes = (nb==periodicals.length)?indexes:Arrays.copyOf(indexes, nb);
			Arrays.sort(removedIndexes);
			for (int i = removedIndexes.length-1; i >=0 ; i--) {
				removed[i] = this.periodicals.remove(removedIndexes[i]);
			}
			this.fireEvent(new PeriodicalTransactionsRemovedEvent(this, removedIndexes, removed));
			setChanged();
		}
	}

	/** Removes a periodical transaction.
	 * <br>If the transaction is not in this, makes nothing.
	 * @param periodical The periodical transaction to remove
	 */
	public void remove (PeriodicalTransaction periodical) {
		int index = Collections.binarySearch(this.periodicals, periodical, PERIODICAL_COMPARATOR);
		if (index>=0) this.removePeriodicalTransaction(index);
	}

	/** Removes a periodical transaction identified by its index.
	 * @param index the periodical transaction index
	 */
	private void removePeriodicalTransaction(int index) {
		PeriodicalTransaction removed = this.periodicals.remove(index);
		this.fireEvent(new PeriodicalTransactionsRemovedEvent(this, new int[]{index}, new PeriodicalTransaction[]{removed}));
		setChanged();
	}

	/** Increments some periodical transactions next date until it becomes greater than a date.
	 * If some periodical transactions have no next date, they are ignored. 
	 * @param transactions the periodical transaction to update
	 * @param date the limit date the periodical transaction have to pass
	 */
	public void setPeriodicalTransactionNextDate(PeriodicalTransaction[] transactions, Date date) {
		Collection<PeriodicalTransaction> removed = new ArrayList<PeriodicalTransaction>(transactions.length);
		Collection<PeriodicalTransaction> updated = new ArrayList<PeriodicalTransaction>(transactions.length);
		for (PeriodicalTransaction pt : transactions) {
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
				removed.add(pt);
				updated.add(new PeriodicalTransaction(pt.getDescription(), pt.getComment(), pt.getAmount(), pt.getAccount(), pt.getMode(),
						pt.getCategory(), Arrays.asList(pt.getSubTransactions()), nextDate, pt.isEnabled(), ds));
			}
		}
		this.remove(removed.toArray(new PeriodicalTransaction[removed.size()]));
		this.add(updated.toArray(new PeriodicalTransaction[updated.size()]));
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
				// Be aware that the date stepper may not be available anymore (if the mode is no more usable for this kind of transaction)
				if (vdStepper==null) vdStepper = DateStepper.IMMEDIATE;
				//Be aware, when the transaction has an "end date", and the date is after this "end date", tDate become null
				for (Date tDate = p.getNextDate();((tDate!=null)&&(tDate.compareTo(date)<=0)/*&&(tDate.compareTo(p.getNextDateBuilder().getLastDate())<=0)*/);tDate=p.getNextDateBuilder().getNextStep(tDate)) {
					result.add(new Transaction(tDate, null, p.getDescription(), p.getComment(), amount, p.getAccount(), mode, p.getCategory(),
							vdStepper.getNextStep(tDate), null, Arrays.asList(p.getSubTransactions())));
				}
			}
		}
		return result.toArray(new Transaction[result.size()]);
	}

	private void setChanged() {
		setChanged(true);
	}

	public void setChanged(boolean changed) {
		if (changed!=this.somethingChanged) {
			this.somethingChanged = changed;
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
				List<Transaction> removed = new ArrayList<Transaction>(account.getTransactionsNumber());
				for (Transaction transaction : this.transactions) {
					if (transaction.getAccount()==account) removed.add(transaction);
				}
				this.remove(removed.toArray(new Transaction[removed.size()]));
			}
			List<PeriodicalTransaction> removed = new ArrayList<PeriodicalTransaction>();
			for (PeriodicalTransaction transaction : this.periodicals) {
				if (transaction.getAccount()==account) removed.add(transaction);
			}
			this.remove(removed.toArray(new PeriodicalTransaction[removed.size()]));
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
			if (getAccount(value) != null) throw new IllegalArgumentException("Account name already exists"); //$NON-NLS-1$
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

	/** Removes a category from the data.
	 * All the transactions and the subtransactions attached to the deleted category are moved to the "undefined" category.
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
			if (getCategory(value) != null) throw new IllegalArgumentException("Category name already exists"); //$NON-NLS-1$
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