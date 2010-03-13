package net.yapbam.data.event;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;

public class CheckbookAddedEvent extends DataEvent {
	private Checkbook book;
	private Account account;
	
	public CheckbookAddedEvent(Object source, Account account, Checkbook newBook) {
		super(source);
		this.book = newBook;
		this.account = account;
	}
	
	public Checkbook getCheckbook() {
		return this.book;
	}

	public Account getAccount() {
		return account;
	}
}
