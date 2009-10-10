package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;

public class FilteredData extends AccountFilter {
	private static final long serialVersionUID = 1L;
	private static final boolean DEBUG = false;
	
	public static final int CHECKED=1;
	public static final int NOT_CHECKED=2;
	public static final int RECEIPT=4;
	public static final int EXPENSE=8;
	private static final int ALL = -1;
	private static final int CHECKED_MASK = (ALL ^ CHECKED) ^ NOT_CHECKED;
	private static final int EXPENSE_RECEIPT_MASK = (ALL ^ RECEIPT) ^ EXPENSE;

	private ArrayList<Transaction> transactions;
	private int filter;
	private Comparator<Transaction> comparator = TransactionComparator.INSTANCE;
	
	public FilteredData(AccountFilteredData data) {
	    super(data.getGlobalData());
	    this.data.addListener(new DataListener() {		
			@Override
			public void processEvent(DataEvent event) {
				if (eventImplySorting(event)) Collections.sort(transactions, comparator);
				if (event instanceof TransactionAddedEvent) {
					Transaction transaction = ((TransactionAddedEvent)event).getTransaction();
					if (isOk(transaction)) { // If the added transaction match with the filter
						int index = -Collections.binarySearch(transactions, transaction, comparator)-1;
						transactions.add(index, transaction);
						fireEvent(new TransactionAddedEvent(FilteredData.this, transaction));
					}
				} else if (event instanceof TransactionRemovedEvent) {
					Transaction transaction = ((TransactionRemovedEvent)event).getRemoved();
					int index = Collections.binarySearch(transactions, transaction, comparator);
					if (index>=0) {
						transactions.remove(index);
						fireEvent(new TransactionRemovedEvent(FilteredData.this, index, transaction));
					}
				} else if (event instanceof AccountAddedEvent) {
					Account account = ((AccountAddedEvent)event).getAccount();
					if (isOk(account) && isOk(CHECKED)) {
						fireEvent(new AccountAddedEvent(FilteredData.this, account));
					}
				} else if (event instanceof AccountRemovedEvent) {
					// Nothing to do, all the account's transactions are removed when the Account is removed
					Account removed = ((AccountRemovedEvent)event).getRemoved();
					if (isOk(removed)) { // Propagate the event
						fireEvent(new AccountRemovedEvent(FilteredData.this, -1, removed));
					}
				} else if (event instanceof AccountPropertyChangedEvent) {
					AccountPropertyChangedEvent evt = (AccountPropertyChangedEvent) event;
					if (isOk(evt.getAccount())) {
						fireEvent(event);
					}
				}
			}
		});
	    this.clear();
	}
	
	private boolean eventImplySorting (DataEvent event) {
		return ((event instanceof AccountPropertyChangedEvent) &&
				((AccountPropertyChangedEvent)event).getProperty().equals(AccountPropertyChangedEvent.NAME) &&
				isOk(((AccountPropertyChangedEvent)event).getAccount()) ||
				false); //TODO other change events
	}
	
	public void clear() {
		this.filter = ALL;
		super.setAccounts(null);
	}

	public boolean isOk(Transaction transaction) {
		boolean accountOk = isOk(transaction.getAccount());
		boolean checkedOk = isOk((transaction.getStatement()==null)?NOT_CHECKED:CHECKED);
		boolean amountOk = isOk((transaction.getAmount()>0)?RECEIPT:EXPENSE);
		return accountOk && checkedOk && amountOk;
	}

	public boolean isOk(int property) {
		if (DEBUG) {
			System.out.println("---------- isOK("+Integer.toBinaryString(property)+") ----------");
			System.out.println("filter  : "+Integer.toBinaryString(this.filter));
			System.out.println("result  : "+Integer.toBinaryString(property & this.filter));
		}
		return ((property & this.filter) != 0);
	}
	
	public void setFilter(int property) {
		if (DEBUG) System.out.println("---------- setFilter("+Integer.toBinaryString(property)+") ----------");
		int mask = ALL;
		if (((property & CHECKED) != 0) || ((property & NOT_CHECKED) != 0)) mask = mask & CHECKED_MASK;
		if (((property & RECEIPT) != 0) || ((property & EXPENSE) != 0)) mask = mask & EXPENSE_RECEIPT_MASK;
		if (mask == ALL) throw new IllegalArgumentException();
		if (DEBUG) System.out.println(Integer.toBinaryString(mask));//CU
		this.filter = (this.filter & mask) | property;
		if (DEBUG) System.out.println("filter : "+this.filter);
		filter();
	}

	protected void filter() {
	    this.transactions = new ArrayList<Transaction>();
	    for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			if (isOk(transaction)) {
				int index = -Collections.binarySearch(transactions, transaction, comparator)-1;
				transactions.add(index, transaction);
			}
		}
		fireEvent(new EverythingChangedEvent(this));
	}

	public int getTransactionsNumber() {
		return this.transactions.size();
	}

	public Transaction getTransaction(int index) {
		return this.transactions.get(index);
	}
	
	public int indexOf(Transaction transaction) {
		return Collections.binarySearch(transactions, transaction, comparator);
	}

	public GlobalData getGlobalData() {
		return this.data;
	}

	public void clearAccounts() {
		super.setAccounts(null);
	}	
}