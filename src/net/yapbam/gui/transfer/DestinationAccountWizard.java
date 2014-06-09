package net.yapbam.gui.transfer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.yapbam.data.AbstractTransactionWizard;
import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;

public class DestinationAccountWizard extends AbstractTransactionWizard<Account> {
	private Account sourceAccount;
	private Map<DateAmount,List<Account>> destReceipts;
	private DateAmount key;
	
	protected DestinationAccountWizard(GlobalData data, Account sourceAccount) {
		super(data);
		this.sourceAccount = sourceAccount;
		// First we will search all the receipts for accounts that are not the selected one (these receipts can be transfer destination).
		destReceipts = new HashMap<DateAmount, List<Account>>();
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
			if ((transaction.getAmount()>0) && !transaction.getAccount().equals(sourceAccount)) {
				DateAmount dateAmount = new DateAmount(transaction.getDateAsInteger(), transaction.getAmount());
				List<Account> accounts = destReceipts.get(dateAmount);
				if (accounts==null) {
					accounts = new LinkedList<Account>();
					destReceipts.put(dateAmount, accounts);
				}
				accounts.add(transaction.getAccount());
			}
		}
		key = new DateAmount(0, 0.0);
	}

	@Override
	protected Account getValue(Transaction transaction) {
		throw new UnsupportedOperationException();
	}
	
	private List<Account> getValues(Transaction transaction) {
		key.date = transaction.getDateAsInteger();
		key.amount = -transaction.getAmount();
		return destReceipts.get(key);
	}
	
	@Override
	public Account get() {
		if (!inited) {
			HashMap<Account, Double> toProbability = new HashMap<Account, Double>();
			for (int i = 0; i < data.getTransactionsNumber(); i++) {
				Transaction transaction = data.getTransaction(i);
				if (isValid(transaction)) {
					List<Account> transactionValues = getValues(transaction);
					if (transactionValues != null) {
						for (Account account:transactionValues) {
							Double weight = toProbability.get(account);
							double transactionWeight = getRanking(transaction);
							toProbability.put(account, transactionWeight + (weight == null ? 0 : weight));
						}
					}
				}
			}
			value = getHeaviest(toProbability);
		}
		return value;
	}


	@Override
	protected boolean isValid(Transaction transaction) {
		return (transaction.getAmount()<0) && transaction.getAccount().equals(sourceAccount);
	}
	
	private static class DateAmount {
		private int date;
		private double amount;
		public DateAmount(int date, double amount) {
			this.date = date;
			this.amount = amount;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			long temp;
			temp = Double.doubleToLongBits(amount);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + date;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof DateAmount)) {
				return false;
			}
			DateAmount other = (DateAmount) obj;
			if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount)) {
				return false;
			}
			if (date != other.date) {
				return false;
			}
			return true;
		}
	}

}
