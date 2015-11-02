package net.yapbam.gui.transactiontable;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringEscapeUtils;

import net.yapbam.data.AbstractTransaction;
import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

public abstract class TransactionTableUtils {
	private TransactionTableUtils() {}
	
	public static String getDescription (AbstractTransaction transaction, boolean spread, boolean mergeComment, boolean withHtmlTags) {
		StringBuilder buf = new StringBuilder();
		if (withHtmlTags) {
			buf.append("<html><body>");
		}
		if (spread) {
			buf.append(getDescription(transaction, false, mergeComment, false)); //$NON-NLS-1$
			for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
				buf.append("<BR>&nbsp;&nbsp;").append(StringEscapeUtils.escapeHtml3(transaction.getSubTransaction(i).getDescription())); //$NON-NLS-1$
			}
			if (transaction.getComplement()!=0) {
				buf.append("<BR>&nbsp;&nbsp;").append(StringEscapeUtils.escapeHtml3(LocalizationData.get("Transaction.14"))); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			buf.append (StringEscapeUtils.escapeHtml3(transaction.getDescription()));
			if (mergeComment && (transaction.getComment()!=null)) {
				buf.append(" (");
				buf.append(getComment(transaction));
				buf.append(")");
			}
		}
		if (withHtmlTags) {
			buf.append("</body></html>"); //$NON-NLS-1$
		}
		return buf.toString().replace(" ", "&nbsp;");
	}

	public static String getComment(AbstractTransaction transaction) {
		String comment = transaction.getComment();
		try {
			new URL(comment);
			return "<a href=\""+comment+"\">"+StringEscapeUtils.escapeHtml3(comment)+"</a>";
		} catch (MalformedURLException e) {
			// Comment is not a valid URL
			return StringEscapeUtils.escapeHtml3(comment);
		}
	}
	
	private static boolean isExpense (double amount) {
		return GlobalData.AMOUNT_COMPARATOR.compare(amount, 0.0)<=0;
	}

	private static boolean isZero (double amount) {
		return GlobalData.AMOUNT_COMPARATOR.compare(amount, 0.0)==0;
	}

	private static String getName(Category category) {
		return category.equals(Category.UNDEFINED) ? "" : category.getName(); //$NON-NLS-1$
	}

	public static double[] getAmount(AbstractTransaction transaction, boolean spread) {
		if (spread) {
			double complement = transaction.getComplement();
			int numberOfLines = transaction.getSubTransactionSize()+1;
			if (complement!=0) {
				numberOfLines++;
			}
			double[] result = new double[numberOfLines];
			result[0] = transaction.getAmount();
			for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
				result[i+1] = transaction.getSubTransaction(i).getAmount();
			}
			if (complement!=0) {
				result[result.length-1] = complement;
			}
			return result;
		} else {
			return new double[]{transaction.getAmount()};
		}
	}
	
	public static double[] getExpenseReceipt(AbstractTransaction transaction, boolean spread, boolean expense) {
		if (spread) {
			double complement = transaction.getComplement();
			int numberOfLines = transaction.getSubTransactionSize()+1;
			if (!isZero(complement)) {
				numberOfLines++;
			}
			double[] result = new double[numberOfLines];
			result[0] = expense==isExpense(transaction.getAmount()) ? transaction.getAmount() : Double.NaN;
			for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
				double amount = transaction.getSubTransaction(i).getAmount();
				result[i+1] = expense==isExpense(amount) ? amount : Double.NaN;
			}
			if (!isZero(complement)) {
				result[result.length-1] = expense==isExpense(complement) ? complement : Double.NaN;
			}
			return result;
		} else {
			return (expense==isExpense(transaction.getAmount())) ? new double[]{transaction.getAmount()} : null;
		}
	}

	public static Object getCategory(AbstractTransaction transaction, boolean spread) {
		if (spread) {
			StringBuilder buf = new StringBuilder("<html><body>").append(StringEscapeUtils.escapeHtml3(getName(transaction.getCategory()))); //$NON-NLS-1$
			for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
				buf.append("<BR>&nbsp;&nbsp;").append(StringEscapeUtils.escapeHtml3(getName(transaction.getSubTransaction(i).getCategory()))); //$NON-NLS-1$
			}
			if (transaction.getComplement()!=0) {
				buf.append("<BR>&nbsp;&nbsp;").append(StringEscapeUtils.escapeHtml3(getName(transaction.getCategory()))); //$NON-NLS-1$
			}
			buf.append("</body></html>"); //$NON-NLS-1$
			return buf.toString().replace(" ", "&nbsp;");
		} else {
			return getName(transaction.getCategory());
		}
	}
}
