package net.yapbam.data.event;

import net.yapbam.data.Account;

public class AccountPropertyChangedEvent extends DataEvent {
	public static final String NAME = "name"; //$NON-NLS-1$
	public static final String INITIAL_BALANCE = "initialBalance"; //$NON-NLS-1$
	public static final String ALERT_THRESHOLD = "alertThreshold"; //$NON-NLS-1$
	public static final String COMMENT = "comment"; //$NON-NLS-1$
	
	private Account account;
	private String property;
	private Object oldValue;
	private Object newValue;

	public AccountPropertyChangedEvent(Object source, String propertyName, Account account, Object oldValue, Object newValue) {
		super (source);
		this.account = account;
		this.property = propertyName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Account getAccount() {
		return account;
	}

	public String getProperty() {
		return property;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}
}
