package net.yapbam.gui.transactiontable;

import java.io.Serializable;

public class TransactionTableSettings implements Serializable {
	private static final long serialVersionUID = -8786461071723495935L;
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
