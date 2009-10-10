package net.yapbam.data;

import java.util.Comparator;


class TransactionComparator implements Comparator<Transaction> {
	static final TransactionComparator INSTANCE = new TransactionComparator();
	
	public int compare(Transaction o1, Transaction o2) {
		int result = o1.getDate().compareTo(o2.getDate());
		if (result == 0) result = o1.getValueDate().compareTo(o2.getValueDate());
		if (result == 0) result = nullCompare(o1.getStatement(),o2.getStatement());
		if (result == 0) result = o1.getAccount().getName().compareToIgnoreCase(o2.getAccount().getName());
		if (result == 0) result = nullCompare(o1.getNumber(),o2.getNumber());
		if (result == 0) result = (int) Math.signum(o1.getAmount() - o2.getAmount());
		if (result == 0) result = o1.getDescription().compareTo(o2.getDescription());
		if (result == 0) result = o1.getCategory().getName().compareToIgnoreCase(o2.getCategory().getName());
		if (result == 0) result = o1.getMode().getName().compareToIgnoreCase(o2.getMode().getName());
		if (result == 0) result = Long.signum(o1.getId() - o2.getId());
		return result;
	}
	
	private int nullCompare(String str1, String str2) {
		if (str1==null) {
			if (str2 == null) return 0;
			else return -1;
		} else if (str2==null) {
			return 1;
		} else {
			return str1.compareToIgnoreCase(str2);
		}
	}
}
