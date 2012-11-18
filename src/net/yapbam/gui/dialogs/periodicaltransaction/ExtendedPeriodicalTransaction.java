package net.yapbam.gui.dialogs.periodicaltransaction;

import java.util.Date;

import net.yapbam.data.PeriodicalTransaction;

class ExtendedPeriodicalTransaction {
	private PeriodicalTransaction transaction;
	private Date posponedDate;
	
	/** Constructor.
	 * <br>Creates a new periodical transaction with no posponed date. 
	 * @param transaction
	 */
	ExtendedPeriodicalTransaction(PeriodicalTransaction transaction) {
		this.transaction = transaction;
		this.posponedDate = null;
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
}