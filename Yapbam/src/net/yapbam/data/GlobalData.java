package net.yapbam.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.yapbam.data.event.*;
import net.yapbam.data.xml.Serializer;

public class GlobalData extends DefaultListenable {
	private static final long serialVersionUID = 1L;

	private List<Account> accounts;
	private List<PeriodicalTransaction> periodicals;
	private List<Transaction> transactions;
	private File path;
	private boolean somethingChanged;
	private List<Category> categories;
	
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
		fireEvent(new AccountAddedEvent(this, this.accounts.size()-1));
		this.setChanged();
	}

	public int getTransactionsNumber() {
		return this.transactions.size();
	}

	public Transaction getTransaction(int index) {
		return this.transactions.get(index);
	}

	public void add(Transaction transaction) {
		int index = -Collections.binarySearch(this.transactions, transaction, TransactionComparator.INSTANCE)-1;
		this.transactions.add(index, transaction);
		transaction.getAccount().add(transaction);
		fireEvent(new TransactionAddedEvent(this, index));
		this.setChanged();
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
		return this.categories.indexOf(category);
	}

	public void add(Category category) {
		if (category.getName()==null) throw new IllegalArgumentException();
		int index = -Collections.binarySearch(categories, category)-1;
		this.categories.add(index, category);
		fireEvent(new CategoryAddedEvent(this, index));
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

	public boolean removeTransaction(Transaction transaction) {
		int index = Collections.binarySearch(this.transactions, transaction, TransactionComparator.INSTANCE);
		if (index>=0) {
			this.removeTransaction(index);
			return true;
		} else {
			return false;
		}
	}
	
	public void removeTransaction(int index) {
		Transaction removed = this.transactions.remove(index);
		this.fireEvent(new TransactionRemovedEvent(this, index, removed));
		setChanged();
	}
	
	public void add(PeriodicalTransaction periodical) {
		int index = -Collections.binarySearch(this.periodicals, periodical, new Comparator<PeriodicalTransaction>() {
			@Override
			public int compare(PeriodicalTransaction o1, PeriodicalTransaction o2) {
				return o1.getDescription().compareToIgnoreCase(o2.getDescription());
			}
		})-1;
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

	private void setChanged() {
		if (!this.somethingChanged) {
			this.somethingChanged = true;
			this.fireEvent(new NeedToBeSavedChangedEvent(this));
		}
	}
}