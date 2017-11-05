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
	
	void add (Summary summary) {
		this.debts += summary.debts;
		this.receipts += summary.receipts;
	}

	double getDebts() {
		return debts;
	}

	double getReceipts() {
		return receipts;
	}

	@Override
	public String toString() {
		return "{"+LocalizationData.getCurrencyInstance().format(this.receipts)+":"+LocalizationData.getCurrencyInstance().format(this.debts)+"}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}