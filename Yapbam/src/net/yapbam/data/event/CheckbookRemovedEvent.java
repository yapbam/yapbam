package net.yapbam.data.event;

import net.yapbam.data.Account;
import net.yapbam.data.Checkbook;

public class CheckbookRemovedEvent extends DataEvent {
	private int index;
	private Account account;
	private Checkbook book;
	
	public CheckbookRemovedEvent(Object source, int index, Account account, Checkbook book) {
		super(source);
		this.index = index;
		this.account = account;
		this.book = book;
	}

	public int getIndex() {
		return index;
	}

	public Account getAccount() {
		return account;
	}

	public Checkbook getCheckbook() {
		return book;
	}
}
