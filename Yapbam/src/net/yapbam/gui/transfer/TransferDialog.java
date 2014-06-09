package net.yapbam.gui.transfer;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.data.comparator.AccountComparator;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

public class TransferDialog extends AbstractDialog<GlobalData, Transaction[]> {
	private static final long serialVersionUID = 1L;
	
	private TransferPanel transferPanel;

	public TransferDialog(Window owner, String title, GlobalData data) {
		super(owner, title, data);
	}

	@Override
	protected JPanel createCenterPane() {
		transferPanel = new TransferPanel(data);
		transferPanel.addPropertyChangeListener(TransferPanel.OK_DISABLED_CAUSE_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		return transferPanel;
	}

	@Override
	protected Transaction[] buildResult() {
		return transferPanel.getTransactions();
	}

	@Override
	protected String getOkDisabledCause() {
		return transferPanel.getOkDisabledCause();
	}

	public void setFromAccount(Account selectedAccount) {
		Account[] accounts = AccountComparator.getSortedAccounts(data, getLocale());
		if (selectedAccount==null) {
			selectedAccount = accounts[0];
		}
		transferPanel.getFromPane().setAccount(selectedAccount);
		// Find which destination is the most probable
		Account dest = new DestinationAccountWizard(data, selectedAccount).get();
		if (dest==null) {
			for (Account account : accounts) {
				if (!account.equals(selectedAccount)) {
					dest = account;
					break;
				}
			}
		}
		transferPanel.getToPane().setAccount(dest);
	}
}
