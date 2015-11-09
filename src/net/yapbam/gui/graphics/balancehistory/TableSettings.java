package net.yapbam.gui.graphics.balancehistory;

import net.yapbam.gui.transactiontable.TransactionTableSettings;

class TableSettings extends TransactionTableSettings {
	private static final long serialVersionUID = 1L;

	TableSettings() {
		super();
		this.isReceiptsSeparatedFromExpenses = TablePreferencePanel.isReceiptSeparatedFromExpense();
	}

	int getColumnCount() {
		return getRemainingColumn()+1;
	}
	
	int getAccountColumn() {
		return 0;
	}
	
	int getDateColumn() {
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
	
	int getNumberColumn() {
		return getCategoryColumn()+2;
	}
	
	int getValueDateColumn() {
		return getCategoryColumn()+3;
	}
	
	int getStatementColumn() {
		return getCategoryColumn()+4;
	}
	
	int getRemainingColumn() {
		return getCategoryColumn()+5;
	}
}
