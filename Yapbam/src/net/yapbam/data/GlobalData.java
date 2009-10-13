package net.yapbam.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.yapbam.data.event.*;
import net.yapbam.data.xml.Serializer;
import net.yapbam.date.helpers.DateStepper;

public class GlobalData extends DefaultListenable {
	private static final long serialVersionUID = 1L;

	private List<Account> accounts;
	private List<PeriodicalTransaction> periodicals;
	private List<Transaction> transactions;
	private File path;
	private boolean somethingChanged;
	private List<Category> categories;
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
	
	public GlobalData() {
		super();
	    this.clear();
	}

	public boolean somethingHasChanged() {
		return this.somethingChanged;
	}
	
	public File getPath() {
		return path;
	}

	public boolean isEmpty() {
		return this.accounts.size()==0;
	}
	
	public void save(File file) throws IOException {
		if (file.exists() && !file.canWrite()) throw new IOException("writing to "+file+" is not allowed");
		// Proceed safely, it means not to erase the old version until the new version is written
		File writed = file.exists()?File.createTempFile("yapbam", "cpt"):file;
		output(writed);
		if (!file.equals(writed)) {
			// Ok, not so safe as I want since we could lost the file between deleting and renaming
			// but I can't find a better way
			if (!file.delete()) {
				writed.delete();
				throw new IOException("Unable to delete old copy of "+file);
			}
			writed.renameTo(file);
		}
		this.somethingChanged = false;
		File old = this.path;
		this.path = file;
		fireEvent(new NeedToBeSavedChangedEvent(this));
		if (!this.path.equals(old)) fireEvent(new FileChangedEvent(this));
	}

	private void output(File writed) throws IOException {
		Serializer.write(this,writed);
	}
	
	public void read(File file) throws IOException {
		this.setEventsEnabled(false);
		Serializer.read(this, file);
		this.path = file;
		this.somethingChanged = false;
		this.setEventsEnabled(true);
		fireEvent(new EverythingChangedEvent(this));
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
		account.addListener(new DataListener(){
			public void processEvent(DataEvent event) {
				setChanged();
			}});
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
		transaction.getAccount().add(transaction);
		fireEvent(new TransactionAddedEvent(this, transaction));
		this.setChanged();
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
		return this.categories.indexOf(category); //TODO use a binary search
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
	    this.path=null;
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
	
	public boolean remove (PeriodicalTransaction periodical) {
		int index = Collections.binarySearch(this.periodicals, periodical, PERIODICAL_COMPARATOR);
		if (index>=0) {
			this.removePeriodicalTransaction(index);
			return true;
		} else {
			return false;
		}
	}

	public void removePeriodicalTransaction(int index) {
		PeriodicalTransaction removed = this.periodicals.remove(index);
		this.fireEvent(new PeriodicalTransactionRemovedEvent(this, index, removed));
		setChanged();
	}
	

	public void setPeriodicalTransactionNextDate(int index, Date date) {
		PeriodicalTransaction pt = getPeriodicalTransaction(index);
		Date nextDate = pt.getNextDate();
		if (nextDate!=null) {
			DateStepper ds = pt.getNextDateBuilder();
			if (ds == null) {
				nextDate = date;
			} else {
				while (nextDate.compareTo(date)<0) {
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
				for (Date tDate = p.getNextDate();tDate.compareTo(date)<=0;tDate=p.getNextDateBuilder().getNextStep(tDate)) {
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
	 * If there's some transactions were attached to the account, all these transactions will be also removed.
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

	/** Removes a category from the data
	 * All the transactions and the subtransactions attached to the deleted category are moved to the "undifined" category.
	 * @param category
	 */
	public void remove(Category category) {
		int index = this.categories.indexOf(category);
		if (index>=0){
			for (int i = 0; i < getTransactionsNumber(); i++) {
				Transaction t = getTransaction(i).change(category, Category.UNDEFINED);
				if (t!=null) {
					remove(getTransaction(i));
					add(t);
				}
			}
			for (int i = 0; i < getPeriodicalTransactionsNumber(); i++) {
				PeriodicalTransaction pt = getPeriodicalTransaction(i).change(category, Category.UNDEFINED);
				if (pt!=null) {
					remove(getPeriodicalTransaction(i));
					add(pt);
				}
			}
			this.categories.remove(index);
			this.fireEvent(new CategoryRemovedEvent(this, index, category));
			this.setChanged();
		}
	}
}