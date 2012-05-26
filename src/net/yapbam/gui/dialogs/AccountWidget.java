package net.yapbam.gui.dialogs;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AbstractSelector;

public class AccountWidget extends AbstractSelector<Account, GlobalData> {
	private static final long serialVersionUID = 1L;
	public static final String ACCOUNT_PROPERTY = "account"; //$NON-NLS-1$
	
	public AccountWidget(GlobalData data) {
		super(data);
		if (data!=null) {
			data.addListener(new DataListener() {
				@Override
				public void processEvent(DataEvent event) {
					if ((event instanceof AccountAddedEvent) || (event instanceof AccountRemovedEvent)) {
						refresh();
					} else if ((event instanceof AccountPropertyChangedEvent) && ((AccountPropertyChangedEvent)event).getProperty().equals(AccountPropertyChangedEvent.NAME)) {
						refresh();
					}
				}
			});
		}
	}
	
	@Override
	protected String getLabel() {
		return LocalizationData.get("AccountDialog.account"); //$NON-NLS-1$
	}
	
	@Override
	protected String getNewButtonTip() {
		return LocalizationData.get("TransactionDialog.account.new.tooltip"); //$NON-NLS-1$
	}

	@Override
	protected String getPropertyName() {
		return ACCOUNT_PROPERTY;
	}

	@Override
	protected void populateCombo() {
		if (getParameters()!=null) {
			for (int i = 0; i < getParameters().getAccountsNumber(); i++) {
				getCombo().addItem(getParameters().getAccount(i));
			}
		}
	}

	@Override
	protected Object getDefaultRenderedValue(Account account) {
		return account==null ? account : account.getName();
	}

	@Override
	protected Account createNew() {
		if (getParameters()!=null) {
			return AccountDialog.open(getParameters(), AccountDialog.getOwnerWindow(this), null);
		} else {
			return null;
		}
	}
}
