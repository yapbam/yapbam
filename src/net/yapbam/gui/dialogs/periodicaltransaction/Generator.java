package net.yapbam.gui.dialogs.periodicaltransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;

/** A class that generates transactions accordingly to the periodical transactions in a data set. 
 * @author Jean-Marc Astesana
 */
public class Generator {
	private ExtendedPeriodicalTransaction[] pTransactions;
	private List<GeneratedTransaction> transactions;
	private Date date;

	/** Constructor.
	 * @param data The data set
	 */
	public Generator(GlobalData data) {
		this.pTransactions = new ExtendedPeriodicalTransaction[data.getPeriodicalTransactionsNumber()];
		for (int i = 0; i < this.pTransactions.length; i++) {
			this.pTransactions[i] = new ExtendedPeriodicalTransaction(data.getPeriodicalTransaction(i));
		}
		this.transactions = new ArrayList<GeneratedTransaction>();
		this.date = null;
	}
	
	public int getNbTransactions() {
		return transactions.size();
	}
	
	public Transaction getTransaction(int index) {
		return transactions.get(index).getTransaction();
	}

	public boolean isCancelled(int index) {
		return transactions.get(index).isCancelled();
	}

	public boolean isPostponed(int index) {
		return transactions.get(index).isPostponed();
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/** Sets the date.
	 * @param date The new date
	 * @return true if the date change adds or removes transactions to the transaction list
	 */
	public boolean setDate(Date date) {
		if (NullUtils.areEquals(date, this.date)) {
			return false;
		}
		// As transactions can be edited after this method is called, we should not change transactions that were already there before
		// the date change (or modifications would be lost).
		// We also need to consider that some transactions may be postponed
		// The best way to have a user friendly behavior seems to be to never forget a generated transaction. When the date is set to null,
		// or changed to a previous date, we should keep the previous transactions, but simply mark them "not used".
		// To achieve that, ExtendedPeriodicalTransaction remembers all the transactions it generated/edited. 
		this.date = date;
		refreshTransactions();
		return true;
	}

	private void refreshTransactions() {
		this.transactions.clear();
		if (this.date!=null) {
			for (int i=0; i<pTransactions.length; i++) {
				this.transactions.addAll(pTransactions[i].getTransactions(this.date));
			}
		}
	}

	/** Updates a transaction.
	 * @param index The transaction index
	 * @param transaction The updated transaction
	 */
	public void setTransaction(int index, Transaction transaction) {
		this.transactions.get(index).setTransaction(transaction);
	}

	/** Marks a transaction as cancelled or not.
	 * @param index The transaction index.
	 * @param cancelled true to mark the transaction cancelled
	 */
	public void setCancelled(int index, boolean cancelled) {
		this.transactions.get(index).setCancelled(cancelled);
	}

	/** Marks a transaction as postponed or not.
	 * @param index The transaction index.
	 * @param postponed true to mark the transaction postponed
	 */
	public void setPostponed(int index, boolean postponed) {
		GeneratedTransaction t = this.transactions.get(index);
		Date tDate = t.getDate();
		if (postponed) {
			// Set to postponed
			t.getSource().setPosponedDate(tDate);
		} else {
			// Set to not postponed
			t.getSource().setPosponedDate(null);
		}
		refreshTransactions();
	}
	
	/** Gets the postponed date of a periodical transaction.
	 * @param indexPeriodical The index of the periodical transaction in the global data.
	 * @return The postponed date, or null if the transaction is not postponed
	 */
	public Date getPostponedDate(int indexPeriodical) {
		return pTransactions[indexPeriodical].getPosponedDate();
	}
}
