package net.yapbam.data.event;

import net.yapbam.data.Account;
import net.yapbam.data.Mode;

public class ModeAddedEvent extends DataEvent {
	private Mode mode;
	private Account account;
	
	public ModeAddedEvent(Object source, Account account, Mode newMode) {
		super(source);
		this.mode = newMode;
		this.account = account;
	}
	
	public Mode getMode() {
		return this.mode;
	}

	public Account getAccount() {
		return account;
	}
}
