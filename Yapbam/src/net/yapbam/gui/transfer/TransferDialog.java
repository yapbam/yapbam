package net.yapbam.gui.transfer;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
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
		if (selectedAccount==null) {
			selectedAccount = data.getAccount(0);
		}
		transferPanel.getFromPane().setAccount(selectedAccount);
		// Find which destination is the most probable
		Account dest = new DestinationAccountWizard(data, selectedAccount).get();
		if (dest==null) {
			for (int i = 0; i < data.getAccountsNumber(); i++) {
				if (!data.getAccount(i).equals(selectedAccount)) {
					dest = data.getAccount(i);
					break;
				}
			}
		}
		transferPanel.getToPane().setAccount(dest);
	}
}
