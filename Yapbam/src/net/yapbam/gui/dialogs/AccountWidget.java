package net.yapbam.gui.dialogs;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.CategoryComparator;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AbstractSelector;

public class AccountWidget extends AbstractSelector<Account, GlobalData> {
	private static final long serialVersionUID = 1L;
	public static final String ACCOUNT_PROPERTY = "account"; //$NON-NLS-1$
	
	public AccountWidget(GlobalData data) {
		super(data);
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
			Account[] accounts = getSortedAccounts(getParameters(), getLocale());
			for (int i = 0; i < accounts.length; i++) {
				getCombo().addItem(accounts[i]);
			}
		}
	}

	public static Account[] getSortedAccounts(final GlobalData data, final Locale locale) {
		Account[] accounts = new Account[data.getAccountsNumber()];
		for (int i = 0; i < accounts.length; i++) {
			accounts[i] = data.getAccount(i);
		}
		Arrays.sort(accounts, new Comparator<Account>() {
			Collator c = Collator.getInstance(locale);
			@Override
			public int compare(Account o1, Account o2) {
				return c.compare(o1.getName(), o2.getName());
			}
		});
		return accounts;
	}

	@Override
	protected Object getDefaultRenderedValue(Account account) {
		return account==null ? account : account.getName();
	}

	@Override
	protected Account createNew() {
		if (getParameters()!=null) {
			return EditAccountDialog.open(getParameters(), Utils.getOwnerWindow(this), null);
		} else {
			return null;
		}
	}
}
