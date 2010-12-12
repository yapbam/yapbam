package net.yapbam.data.event;

import net.yapbam.data.Account;

public class AccountAddedEvent extends DataEvent {
	private Account account;

	public AccountAddedEvent(Object source, Account account) {
		super(source);
		this.account = account;
	}

	public Account getAccount() {
		return this.account;
	}
}
