package net.yapbam.gui.transactiontable;

import net.yapbam.data.AbstractTransaction;

public class DescriptionSettings {
	private boolean isCommentSeparatedFromDescription;

	public DescriptionSettings() {
		this.isCommentSeparatedFromDescription = TransactionsPreferencePanel.isCommentSeparatedFromDescription();
	}

	public boolean isCommentSeparatedFromDescription() {
		return isCommentSeparatedFromDescription;
	}

	public String getDescription (AbstractTransaction transaction) {
		if (isCommentSeparatedFromDescription) return transaction.getDescription();
		return getMergedDescriptionAndComment(transaction);
	}

	public static String getMergedDescriptionAndComment(AbstractTransaction transaction) {
		String description = transaction.getDescription();
		if (transaction.getComment()==null) {
			return description;
		} else {
			StringBuilder buffer = new StringBuilder();
			buffer.append(description);
			buffer.append(" (");
			buffer.append(transaction.getComment());
			buffer.append(")");
			return buffer.toString();
		}
	}
}