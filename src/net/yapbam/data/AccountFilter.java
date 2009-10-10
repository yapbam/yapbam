package net.yapbam.data;

import java.util.*;

import net.yapbam.data.event.*;

/** The global data class represents the whole data of a Yapbam running instance.
 * This class represents a filter on the data (retaining only the account(s) the user choose to view).
 * The only event sent is the FilterChanged event.
 */
public abstract class AccountFilter extends DefaultListenable implements DataListener {
	private static final long serialVersionUID = 1L;

	private HashSet<Account> validAccounts;
	protected GlobalData data;
	
	/** Constructor. */
	public AccountFilter(GlobalData data) {
	    super();
	    this.data = data;
	    this.setAccounts(null);
	    this.data.addListener(this);
	}
	
	/** Process event that occurs on the globalData.
	 * This implementation only deals with changing the filter state and doesn't forward any event.
	 * For instance if a account is removed, this method will update the filter (changing to "All" is the
	 * removed account was the selected account), but will not throw any AccountRemovedEvent.
	 * @param event The event.
	 */
	public void processEvent(DataEvent event) {
		if (event instanceof EverythingChangedEvent) {
			setAccounts(null);
		} else if (event instanceof AccountRemovedEvent) {
			process((AccountRemovedEvent) event);
		}
	}
	
	/** Process the AccountRemovedEvent.
	 * This implementation only ensure that the account is no more allowed by the filter.
	 * As, the transaction are deleted from the GlobalData when the account is removed, this method does
	 * nothing to deal with the transactions. It doesn't call the filter method nor fire any event
	 * If a subclass wants to perform something when an account is removed, I advise to override this method
	 * and not to register with the GlobalData event; When the listener will receive the event, this method
	 * will already been called and there will be no way for the listener to know if the account was allowed
	 * by the filter or not.
	 * @param event the event
	 * @return true if the removed account was allowed by the filter.
	 */
	protected boolean process(AccountRemovedEvent event) {
		Account account = ((AccountRemovedEvent)event).getRemoved();
		if (validAccounts==null) return true;
		return validAccounts.remove(account);
	}
	
	/** Returns the filtered event : the one which seems to happen to someone listening at the filtered data.
	 * @param event The original global data event
	 * @return null if the event would not occurs on the filtered data or the event on filtered data.
	 */
	/*public boolean getFilteredEvent(DataEvent event) {
		if (event instanceof AccountAddedEvent) {
			return isOk(((AccountAddedEvent)event).getAccount());
		} else if (event instanceof AccountPropertyChangedEvent) {
			return isOk(((AccountPropertyChangedEvent)event).getAccount());
		} else if (event instanceof AccountRemovedEvent) {
			return isOk(((AccountRemovedEvent)event).getRemoved());			
		} else if (event instanceof CategoryAddedEvent) {
			return true; //TODO
		} else if (event instanceof EverythingChangedEvent) {
			return true;
		} else if (event instanceof FileChangedEvent) {
			return true;
		} else if (event instanceof FilterUpdatedEvent) {
			return false;
		} else if (event instanceof ModeAddedEvent) {
			return true; //TODO
		} else if (event instanceof NeedToBeSavedChangedEvent) {
			return true;
		} else if ((event instanceof PeriodicalTransactionAddedEvent) ||
				(event instanceof PeriodicalTransactionRemovedEvent)) {
			return true; //Filters never applies to periodicalTransactions
		} else if (event instanceof TransactionAddedEvent) {
			return isOk(((TransactionAddedEvent)event).getTransaction());
		} else if (event instanceof TransactionRemovedEvent) {
			return isOk(((TransactionRemovedEvent)event).getRemoved());			
		} else {
			throw new IllegalArgumentException("event "+event+" not supported");
		}
	}*/
		
	/** Set the valid accounts for this filter.
	 * There's no side effect between this instance and the returned array.
	 * @param accounts the accounts (null to allow every accounts).
	 */
	public void setAccounts(Account[] accounts) {
		if ((accounts==null) || (accounts.length==data.getAccountsNumber())) {
			this.validAccounts=null;
		} else {
			this.validAccounts = new HashSet<Account>(accounts.length);
			for (int i = 0; i < accounts.length; i++) {
				this.validAccounts.add(accounts[i]);
			}
		}
		this.filter();
		fireEvent(new FilterUpdatedEvent(this));
	}

	/** Returns the valid accounts for this filter.
	 * There's no side effect between this instance and the returned array.
	 * @return the valid accounts (null means, all accounts are ok).
	 */
	public Account[] getAccounts() {
		if (this.validAccounts==null) return null;
		Account[] result = new Account[validAccounts.size()];
		Iterator<Account> iterator = validAccounts.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iterator.next();
		}
		return result;
	}

	protected abstract void filter();

	public GlobalData getGlobalData() {
		return this.data;
	}
	
	public boolean isOk(Transaction transaction) {
		return isOk(transaction.getAccount());
	}

	public boolean isOk(Account account) {
		return (this.validAccounts==null) || (this.validAccounts.contains(account));
	}

	public boolean hasFilterAccount() {
		return this.validAccounts != null;
	}
}