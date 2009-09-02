package net.astesana.comptes.data;

import java.util.Comparator;

class TransactionComparator implements Comparator<Transaction> {
	static final TransactionComparator INSTANCE = new TransactionComparator();
	
	public int compare(Transaction o1, Transaction o2) {
		int result = o1.getDate().compareTo(o2.getDate());
		if (result == 0) result = o1.getValueDate().compareTo(o2.getValueDate());
		if (result == 0) result = Long.signum(o1.getId() - o2.getId());
		return result;
	}
}
