package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;

public class AccountFilteredData extends AccountFilter {
	private static final long serialVersionUID = 1L;

	private double finalBalance;
	private double checkedBalance;
	private double currentBalance;
	private BalanceHistory balanceHistory;
	
	public AccountFilteredData(GlobalData data) {
	    super(data);
	    this.data.addListener(new DataListener() {		
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof TransactionAddedEvent) {
					int index = ((TransactionAddedEvent)event).getTransactionIndex();
					Transaction transaction = ((GlobalData)event.getSource()).getTransaction(index);
					if (isOk(transaction)) { // If the added transaction match with the filter
						updateBalance(new Date(), transaction, true);
						balanceHistory.add(transaction.getAmount(), transaction.getValueDate());
						fireEvent(new TransactionAddedEvent(AccountFilteredData.this, index));
					}
				} else if (event instanceof TransactionRemovedEvent) {
					Transaction transaction = ((TransactionRemovedEvent)event).getRemoved();
					if (isOk(transaction)) {
						updateBalance(new Date(), transaction, false);
						balanceHistory.add(-transaction.getAmount(), transaction.getValueDate());
						fireEvent(new TransactionRemovedEvent(AccountFilteredData.this, -1, transaction));
					}
				} else if (event instanceof AccountAddedEvent) {
					int index = ((AccountAddedEvent)event).getAccountIndex();
					Account account = ((GlobalData)event.getSource()).getAccount(index);
					if (isOk(account)) {
						updateBalance(account.getInitialBalance(), true);
						balanceHistory.add(account.getInitialBalance(), null);
						fireEvent(new AccountAddedEvent(AccountFilteredData.this, index));
					}
				} else if (event instanceof AccountRemovedEvent) {
					System.out.println ("Do something with this AccountRemovedEvent"); //FIXME
				}
			}
		});
	}
	
	protected void filter() {
		double initialBalance = getInitialBalance();
		this.balanceHistory = new BalanceHistory(initialBalance);
		this.finalBalance = initialBalance;
		this.currentBalance = initialBalance;
		this.checkedBalance = initialBalance;
	    Date today = new Date();
	    for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			if (this.isOk(transaction)) {
				this.balanceHistory.add(transaction.getAmount(), transaction.getValueDate());
				updateBalance(today, transaction, true);
			}
		}
	}

	private double getInitialBalance() {
		double balance = 0;
		for (int i = 0; i < this.data.getAccountsNumber(); i++) {
			Account account = this.data.getAccount(i);
			if (this.isOk(account)) balance += account.getInitialBalance();
		}
		return balance;
	}

	private void updateBalance(Date today, Transaction transaction, boolean add) {
		double amount = transaction.getAmount();
		if (!add) amount = -amount;
		this.finalBalance += amount;
		if (transaction.isChecked()) this.checkedBalance += amount;
		if (!transaction.getValueDate().after(today)) this.currentBalance += amount;
	}

	private void updateBalance(double amount, boolean add) {
		if (!add) amount = -amount;
		this.finalBalance += amount;
		this.checkedBalance += amount;
		this.currentBalance += amount;
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

	public GlobalData getGlobalData() {
		return this.data;
	}
	
	public boolean isOk(Transaction transaction) {
		return isOk(transaction.getAccount());
	}
}