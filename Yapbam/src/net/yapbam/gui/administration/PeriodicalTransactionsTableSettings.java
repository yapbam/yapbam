package net.yapbam.gui.administration;

import net.yapbam.gui.transactiontable.TransactionTableSettings;

class PeriodicalTransactionsTableSettings extends TransactionTableSettings {
	private static final long serialVersionUID = 1L;

	PeriodicalTransactionsTableSettings() {
		super();
	}

	int getColumnCount() {
		return getActiveColumn()+1;
	}
	
	int getSpreadColumn() {
		return 0;
	}
	
	int getAccountColumn() {
		return 1;
	}
	
	int getDescriptionColumn() {
		return 2;
	}

	int getCommentColumn() {
		return isCommentSeparatedFromDescription() ? 3 : -1;
	}

	int getAmountColumn() {
		if (isReceiptsSeparatedFromExpenses()) {
			return -1;
		} else {
			return isCommentSeparatedFromDescription() ? 4 : 3;
		}
	}
	
	int getReceiptColumn() {
		if (isReceiptsSeparatedFromExpenses()) {
			return isCommentSeparatedFromDescription() ? 4 : 3;
		} else {
			return -1;
		}
	}
	
	int getExpenseColumn() {
		if (isReceiptsSeparatedFromExpenses()) {
			return getReceiptColumn()+1;
		} else {
			return -1;
		}
	}

	int getCategoryColumn() {
		int result = 4;
		if (isCommentSeparatedFromDescription()) {
			result++;
		}
		if (isReceiptsSeparatedFromExpenses()) {
			result++;
		}
		return result;
	}
	
	int getModeColumn() {
		return getCategoryColumn()+1;
	}
	
	
	int getNextDateColumn() {
		return getCategoryColumn()+2;
	}
	
	
	int getPeriodColumn() {
		return getCategoryColumn()+3;
	}
	
	int getActiveColumn() {
		return getCategoryColumn()+4;
	}
}
