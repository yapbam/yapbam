package net.yapbam.gui.statistics;

import net.yapbam.gui.LocalizationData;

class Summary {
	private double debts;
	private double receipts;
	
	Summary() {
		this.debts = 0;
		this.receipts = 0;
	}
	
	void add (double amount) {
		if (amount>0) {
			this.receipts += amount;
		} else {
			this.debts += amount;
		}
	}

	double getDebts() {
		return debts;
	}

	double getReceipts() {
		return receipts;
	}

	@Override
	public String toString() {
		return "{"+LocalizationData.getCurrencyInstance().format(this.receipts)+":"+LocalizationData.getCurrencyInstance().format(this.debts)+"}";
	}

}