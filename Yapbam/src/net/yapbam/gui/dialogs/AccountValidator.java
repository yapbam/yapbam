package net.yapbam.gui.dialogs;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

public class AccountValidator {
	private GlobalData data;
	private int accountNumber;

	public AccountValidator(GlobalData data, int accountNumber) {
		this.data = data;
		this.accountNumber = accountNumber;
	}

	public boolean isNameOk(String name) {
		// Unfortunately, before Yapbam version 0.9.8, it was possible to define account names starting or ending with a space
		// We chose not to trim the old account names because it was very hard to merge accounts with the same name except the spaces
		// (because modes could not be the same).
		// So, we have to test if the name is equivalent to a trimmed previously entered name
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			if ((i!=accountNumber) && name.equalsIgnoreCase(data.getAccount(i).getName().trim())) {
				return false;
			}
		}
		return true;
	}
	
	public String getOkDisabledCause(String name, Double initBalance) {
		if (name.length()==0) {
			return LocalizationData.get("AccountDialog.err1"); //$NON-NLS-1$
		} else if (!isNameOk(name)) {
			return LocalizationData.get("AccountDialog.err2"); //$NON-NLS-1$
		} else if (initBalance==null) {
			return LocalizationData.get("AccountDialog.err3"); //$NON-NLS-1$
		}
		return null;
	}

}
