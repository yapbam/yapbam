package net.yapbam.gui.transactiontable;

public class TransactionTableSettings {
	private boolean isCommentSeparatedFromDescription;
	private boolean isReceiptsSeparatedFromExpenses;
	
	public TransactionTableSettings() {
		super();
		this.isCommentSeparatedFromDescription = TransactionsPreferencePanel.isCommentSeparatedFromDescription();
		this.isReceiptsSeparatedFromExpenses = TransactionsPreferencePanel.isReceiptSeparatedFromExpense();
	}

	public boolean isCommentSeparatedFromDescription() {
		return isCommentSeparatedFromDescription;
	}

	public boolean isReceiptsSeparatedFromExpenses() {
		return isReceiptsSeparatedFromExpenses;
	}
}
