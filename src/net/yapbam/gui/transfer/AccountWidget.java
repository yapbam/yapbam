package net.yapbam.gui.transfer;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

public class AccountWidget extends AbstractSelector<Account> {
	private static final long serialVersionUID = 1L;
	

	public AccountWidget(GlobalData data) {
		super(LocalizationData.get("AccountDialog.account"), null, LocalizationData.get("TransactionDialog.account.new.tooltip"));
	}

}
