package net.yapbam.gui.transactiontable;

class TableSettings extends TransactionTableSettings {
	private static final long serialVersionUID = 1L;

	int getColumnCount() {
		int nb = 10;
		if (isCommentSeparatedFromDescription()) {
			nb++;
		}
		if (isReceiptsSeparatedFromExpenses()) {
			nb++;
		}
		return nb;
	}
	
	int getSpreadColumn() {
		return 0;
	}
	
	int getAccountColumn() {
		return 1;
	}
	
	int getDateColumn() {
		return 2;
	}
	
	int getDescriptionColumn() {
		return 3;
	}
	
	int getAmountColumn() {
		return isReceiptsSeparatedFromExpenses() ? -1 : 4;
	}
	
	int getReceiptColumn() {
		return isReceiptsSeparatedFromExpenses() ? 4 : -1;
	}
	
	int getExpenseColumn() {
		return isReceiptsSeparatedFromExpenses() ? 5 : -1;
	}

	int getCategoryColumn() {
		return isReceiptsSeparatedFromExpenses() ? 6 : 5;
	}
	
	int getModeColumn() {
		return isReceiptsSeparatedFromExpenses() ? 7 : 6;
	}
	
	int getNumberColumn() {
		return isReceiptsSeparatedFromExpenses() ? 8 : 7;
	}
	
	int getValueDateColumn() {
		return isReceiptsSeparatedFromExpenses() ? 9 : 8;
	}
	
	int getStatementColumn() {
		return isReceiptsSeparatedFromExpenses() ? 10 : 9;
	}
	
	int getCommentColumn() {
		if (!isCommentSeparatedFromDescription()) {
			return -1;
		} else {
			return isReceiptsSeparatedFromExpenses() ? 11 : 10;
		}
	}
}
