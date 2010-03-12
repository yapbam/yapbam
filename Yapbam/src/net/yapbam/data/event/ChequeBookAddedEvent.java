package net.yapbam.data.event;

import net.yapbam.data.Account;
import net.yapbam.data.ChequeBook;

public class ChequeBookAddedEvent extends DataEvent {
	private ChequeBook book;
	private Account account;
	
	public ChequeBookAddedEvent(Object source, Account account, ChequeBook newBook) {
		super(source);
		this.book = newBook;
		this.account = account;
	}
	
	public ChequeBook getChequeBook() {
		return this.book;
	}

	public Account getAccount() {
		return account;
	}
}
