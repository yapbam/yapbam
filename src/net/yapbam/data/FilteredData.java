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
	
	public FilteredData(GlobalData data) {
	    super(data);
	    this.data.addListener(new DataListener() {		
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof TransactionAddedEvent) {
					int index = ((TransactionAddedEvent)event).getTransactionIndex();
					Transaction transaction = ((GlobalData)event.getSource()).getTransaction(index);
					if (isOk(transaction)) { // If the added transaction match with the filter
						index = -Collections.binarySearch(transactions, transaction, TransactionComparator.INSTANCE)-1;
						transactions.add(index, transaction);
						fireEvent(new TransactionAddedEvent(FilteredData.this, index));
					}
				} else if (event instanceof TransactionRemovedEvent) {
					Transaction transaction = ((TransactionRemovedEvent)event).getRemoved();
					int index = Collections.binarySearch(transactions, transaction, TransactionComparator.INSTANCE);
					if (index>=0) {
						transactions.remove(index);
						fireEvent(new TransactionRemovedEvent(FilteredData.this, index, transaction));
					}
				} else if (event instanceof AccountAddedEvent) {
					int index = ((AccountAddedEvent)event).getAccountIndex();
					Account account = ((GlobalData)event.getSource()).getAccount(index);
					if (isOk(account) && isOk(CHECKED)) {
						fireEvent(new AccountAddedEvent(FilteredData.this, index));
					}
				}
			}

		});
	}
		
	@Override
	public void clear() {
		this.filter = ALL;
		super.clear();
	}

	public boolean isOk(Transaction transaction) {
		return isOk(transaction.getAccount()) && isOk((transaction.getStatement()==null)?NOT_CHECKED:CHECKED) &&
		isOk((transaction.getAmount()>0)?RECEIPT:EXPENSE);
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
		fireEvent(new EverythingChangedEvent(this));
	}

	protected void filter() {
	    this.transactions = new ArrayList<Transaction>(0);
	    for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			if (isOk(transaction)) {
				this.transactions.add(transaction);				
			}
		}
	}

	public int getTransactionsNumber() {
		return this.transactions.size();
	}

	public Transaction getTransaction(int index) {
		return this.transactions.get(index);
	}

	public GlobalData getGlobalData() {
		return this.data;
	}

	public void clearAccounts() {
		super.clear();
	}	
}