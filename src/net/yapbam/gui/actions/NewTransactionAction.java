package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.utilities.StringUtils;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Mode;
import net.yapbam.data.Transaction;
import net.yapbam.gui.AccountSelector;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class NewTransactionAction extends AbstractAction {
	private FilteredData data;
	private TransactionSelector selector;
	private boolean massMode;
	private AccountSelector accountSelector;
	
	/** Constructor.
	 * @param data FilteredData (mandatory, even if TransactionSelector has a getFilteredData method, because selector may be null)
	 * @param selector An optional selector (it is used to select the created transactions once they are created) 
	 * @param massMode true to select mass modification
	 * @param accountSelector an accountSelector that will define the default account when this instance's action will be performed.
	 */
	public NewTransactionAction(FilteredData data, TransactionSelector selector, boolean massMode, AccountSelector accountSelector) {
		super(LocalizationData.get("MainMenu.Transactions.New"), IconManager.get(massMode?Name.NEW_BULK_TRANSACTION:Name.NEW_TRANSACTION)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.New.ToolTip")); //$NON-NLS-1$
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.New.Mnemonic")); //$NON-NLS-1$
		this.data = data;
		this.selector = selector;
		this.massMode = massMode;
		this.accountSelector = accountSelector;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = null;
		Account defaultAccount = accountSelector.getSelectedAccount();
		if (defaultAccount!=null) {
			// If there's a default account, select it in the created dialog
			Date today = new Date();
			transaction = new Transaction(today, null, StringUtils.EMPTY, null, 0.0, defaultAccount, Mode.UNDEFINED,
					Category.UNDEFINED, today, null, null);
		}

		Window owner = Utils.getOwnerWindow((Component) e.getSource());
		transaction = TransactionDialog.open(data.getGlobalData(), owner, transaction, false, true, this.massMode);
		if ((transaction!=null) && (selector!=null)) {
			this.selector.setSelectedTransactions(new Transaction[]{transaction});
		}
	}
}