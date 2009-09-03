package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;

public abstract class AccountFilter extends DefaultListenable {
	private static final long serialVersionUID = 1L;

	private HashSet<String> validAccounts;
	protected GlobalData data;
	
	public AccountFilter(GlobalData data) {
	    super();
	    this.data = data;
	    this.clear();
	    this.data.addListener(new DataListener() {		
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof EverythingChangedEvent) {
					clear();
					fireEvent(new EverythingChangedEvent(this));
				}
			}
		});
	}
	
	public void clear() {
		this.validAccounts=null;
		this.filter();
		fireEvent(new EverythingChangedEvent(this));
	}
	
	public void setAccounts(Account[] accounts) {
		if (accounts.length==data.getAccountsNumber()) {
			this.clear();
		} else {
			this.validAccounts = new HashSet<String>(accounts.length);
			for (int i = 0; i < accounts.length; i++) {
				this.validAccounts.add(accounts[i].getName());
			}
		}
		this.filter();
		fireEvent(new EverythingChangedEvent(this));
	}

	protected abstract void filter();

	public GlobalData getGlobalData() {
		return this.data;
	}
	
	public boolean isOk(Transaction transaction) {
		return isOk(transaction.getAccount());
	}

	public boolean isOk(Account account) {
		return (this.validAccounts==null) || (this.validAccounts.contains(account.getName()));
	}

	public boolean hasFilterAccount() {
		return this.validAccounts != null;
	}
}