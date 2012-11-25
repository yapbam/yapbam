package net.yapbam.gui.dialogs.periodicaltransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.Transaction;

class ExtendedPeriodicalTransaction {
	private PeriodicalTransaction transaction;
	private Date posponedDate;
	private Date generationDate;
	private List<GeneratedTransaction> transactions;
	
	/** Constructor.
	 * <br>Creates a new periodical transaction with no postponed date. 
	 * @param transaction
	 */
	ExtendedPeriodicalTransaction(PeriodicalTransaction transaction) {
		this.transaction = transaction;
		this.posponedDate = null;
		this.generationDate = null;
		this.transactions = new ArrayList<GeneratedTransaction>();
	}

	/**
	 * @return the transaction
	 */
	public PeriodicalTransaction getPeriodicalTransaction() {
		return transaction;
	}

	/**
	 * @return the posponedDate
	 */
	public Date getPosponedDate() {
		return posponedDate;
	}

	/**
	 * @param posponedDate the posponedDate to set
	 */
	public void setPosponedDate(Date posponedDate) {
		this.posponedDate = posponedDate;
	}
	
	public List<GeneratedTransaction> getTransactions(Date date) {
		if (date==null) return new ArrayList<GeneratedTransaction>();
		if (generationDate==null || generationDate.compareTo(date)<0) {
			// Some transactions needs to be generated
			List<Transaction> generated = getPeriodicalTransaction().generate(date, null);
			if (generationDate==null) generationDate = new Date(0);
			for (Transaction transaction : generated) {
				if (transaction.getDate().after(generationDate)) transactions.add(new GeneratedTransaction(transaction, this, transaction.getDate()));
			}
			generationDate = date;
			return transactions;
		} else {
			int length;
			// All transactions are generated, we need to find which one are after the wanted date
			for (length = 0; length < transactions.size(); length++) {
				if (transactions.get(length).getDate().compareTo(date)>0) break;
			}
			return length==0 ? new ArrayList<GeneratedTransaction>() : transactions.subList(0, length);
		}
	}
}