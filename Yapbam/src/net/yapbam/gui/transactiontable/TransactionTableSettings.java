package net.yapbam.gui.transactiontable;

import java.io.Serializable;

public class TransactionTableSettings implements Serializable {
	protected boolean isCommentSeparatedFromDescription;
	protected boolean isReceiptsSeparatedFromExpenses;
	
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
