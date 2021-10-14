package net.yapbam.gui.transactiontable;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.TransactionUtils;
import net.yapbam.util.TransactionUtils.WordingProvider;

public abstract class TransactionTableUtils {
	private static final TransactionUtils UTILS = new TransactionUtils(new WordingProvider() {
		@Override
		public String getComplementWording() {
			return LocalizationData.get("Transaction.14"); //$NON-NLS-1$
		}
	});
	
	private TransactionTableUtils() {
		super();
	}
	
	public static String getDescription (AbstractTransaction transaction, boolean spread, boolean mergeComment, boolean withHtmlTags) {
		return UTILS.getDescription(transaction, spread, mergeComment, withHtmlTags);
	}
	
	public static String getDescriptionAsText(AbstractTransaction transaction, boolean mergeComment) {
		if (mergeComment) {
			final StringBuilder buf = new StringBuilder();
			buf.append (transaction.getDescription());
			if (transaction.getComment()!=null) {
				buf.append(" ("); //$NON-NLS-1$
				buf.append(transaction.getComment());
				buf.append(")"); //$NON-NLS-1$
			}
			return buf.toString();
		} else {
			return transaction.getDescription();
		}
	}

	public static String getComment(AbstractTransaction transaction) {
		return UTILS.getComment(transaction);
	}

	public static double[] getAmount(AbstractTransaction transaction, boolean spread) {
		return UTILS.getAmount(transaction, spread);
	}
	
	public static double[] getExpenseReceipt(AbstractTransaction transaction, boolean spread, boolean expense) {
		return UTILS.getExpenseReceipt(transaction, spread, expense);
	}

	public static Object getCategory(AbstractTransaction transaction, boolean spread) {
		return UTILS.getCategory(transaction, spread);
	}
}
