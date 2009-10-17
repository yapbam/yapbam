package net.yapbam.data.event;

import net.yapbam.data.Account;
import net.yapbam.data.Mode;

public class ModeRemovedEvent extends DataEvent {
	private int index;
	private Account account;
	private Mode mode;
	
	public ModeRemovedEvent(Object source, int index, Account account, Mode mode) {
		super(source);
	}

	public int getIndex() {
		return index;
	}

	public Account getAccount() {
		return account;
	}

	public Mode getMode() {
		return mode;
	}
}
