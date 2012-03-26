package net.yapbam.gui.dialogs;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;

public class ModeWidgetParams {
	private GlobalData data;
	private Account account;
	private boolean isExpense;
	
	public GlobalData getGlobalData() {
		return data;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public boolean isExpense() {
		return isExpense;
	}
}
