package net.yapbam.gui.dialogs.periodicaltransaction;

import java.util.Date;

import net.yapbam.data.Transaction;

class GeneratedTransaction {
	private Transaction transaction;
	private boolean cancelled;
	private ExtendedPeriodicalTransaction source;
	private Date date;
	
	public GeneratedTransaction(Transaction transaction, ExtendedPeriodicalTransaction source, Date date) {
		super();
		this.transaction = transaction;
		this.source = source;
		this.date = date;
		this.cancelled = false;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public ExtendedPeriodicalTransaction getSource() {
		return source;
	}

	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	public boolean isPostponed() {
		Date pDate = getSource().getPosponedDate();
		return pDate==null?false:(pDate.compareTo(date)<=0);
	}

	public Date getDate() {
		return date;
	}

	/**
	 * @param cancelled the cancelled to set
	 */
	void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}