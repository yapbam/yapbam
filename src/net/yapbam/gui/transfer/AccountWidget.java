package net.yapbam.gui.transfer;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

public class AccountWidget extends AbstractSelector<Account> {
	private static final long serialVersionUID = 1L;
	public static final String ACCOUNT_PROPERTY = "account"; //$NON-NLS-1$
	
	public AccountWidget(GlobalData data) {
		super();
	}
	
	protected String getNewButtonTip() {
		return LocalizationData.get("TransactionDialog.account.new.tooltip");
	}

	@Override
	protected String getPropertyName() {
		return ACCOUNT_PROPERTY;
	}
}
