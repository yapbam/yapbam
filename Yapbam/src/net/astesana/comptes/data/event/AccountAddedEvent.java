package net.astesana.comptes.data.event;

public class AccountAddedEvent extends DataEvent {
	private int accountIndex;

	public AccountAddedEvent(Object source, int accountIndex) {
		super(source);
		this.accountIndex = accountIndex;
	}

	public int getAccountIndex() {
		return accountIndex;
	}
}
