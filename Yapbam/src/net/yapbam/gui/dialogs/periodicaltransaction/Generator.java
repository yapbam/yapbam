package net.yapbam.gui.dialogs.periodicaltransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;

/** A class that generates transactions accordingly to the periodical transactions in a data set. 
 * @author Jean-Marc Astesana
 */
public class Generator {
	private ExtendedPeriodicalTransaction[] pTransactions;
	private List<GeneratedTransaction> transactions;
	private int transactionNumber;
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
		this.transactionNumber = 0;
		this.date = null;
	}
	
	public int getNbTransactions() {
		return transactionNumber;
	}
	
	public Transaction getTransaction(int index) {
		if (index>=transactionNumber) throw new IndexOutOfBoundsException();
		return transactions.get(index).getTransaction();
	}

	public boolean isCancelled(int index) {
		if (index>=transactionNumber) throw new IndexOutOfBoundsException();
		return transactions.get(index).isCancelled();
	}

	public boolean isPostponed(int index) {
		if (index>=transactionNumber) throw new IndexOutOfBoundsException();
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
		if (NullUtils.areEquals(date, this.date)) return false;
		//TODO Here is a simplified version of the generation
		// As transactions can be edited after this method is called, we should not change transactions that were already there before
		// the date change (or modifications would be lost).
		// We also need to consider that some transactions may be postponed
		// The best way to have a user friendly behavior seems to be to never forget a generated transaction. When the date is set to null,
		// or changed to a previous date, we should keep the previous transactions, but simply mark them "not used".
		// To achieve that, we will sort the transaction by date and remember how much transactions are available accordingly with the new date. 
		this.date = date;
		if (date==null) {
			this.transactionNumber=0;
		} else {
			this.transactions.clear();
			for (int i=0; i<pTransactions.length; i++) {
				List<Transaction> generated = pTransactions[i].getPeriodicalTransaction().generate(date, null);
				for (Transaction t : generated) {
					this.transactions.add(new GeneratedTransaction(t, pTransactions[i], t.getDate()));
				}
			}
			this.transactionNumber=this.transactions.size();
		}
		//TODO end
		return true;
	}

	public void setTransaction(int index, Transaction transaction) {
		if (index>=transactionNumber) throw new IndexOutOfBoundsException();
		this.transactions.get(index).setTransaction(transaction);
	}

	public void setCancelled(int index, boolean cancelled) {
		if (index>=transactionNumber) throw new IndexOutOfBoundsException();
		this.transactions.get(index).setCancelled(cancelled);
	}

	public void setPostponed(int index, boolean postponed) {
		if (index>=transactionNumber) throw new IndexOutOfBoundsException();
		GeneratedTransaction t = this.transactions.get(index);
		Date pDate = t.getSource().getPosponedDate();
		Date tDate = t.getDate();
		//TODO
		if (postponed) {
			// Set to postponed
			
		} else {
			// Set to not postponed
		}
	}
}
