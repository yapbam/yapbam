package net.yapbam.gui.transfer;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.EditAccountDialog;

public class NewTransferAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private GlobalData data;
	private AbstractPlugIn plugin;
	
	NewTransferAction(GlobalData data, AbstractPlugIn plugin) {
		super(LocalizationData.get("TransferDialog.menu")); //$NON-NLS-1$
		this.data = data;
		this.plugin = plugin;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Window owner = Utils.getOwnerWindow((Component) e.getSource());
		while (data.getAccountsNumber()<2) {
			// Need to create two accounts first
			Account account = EditAccountDialog.open(data, owner, LocalizationData.get("TransferDialog.needsTwoAccounts")); //$NON-NLS-1$
			if (account == null) {
				return;
			}
		}
		TransferDialog dialog = new TransferDialog(owner, LocalizationData.get("TransferDialog.title"), data); //$NON-NLS-1$
		dialog.setFromAccount(plugin.getContext().getSelectedAccount());
		dialog.setVisible(true);
		Transaction[] transactions = dialog.getResult();
		if (transactions!=null) {
			data.add(transactions);
			if (plugin.getContext().getCurrentTransactionSelector()!=null) {
				plugin.getContext().getCurrentTransactionSelector().setSelectedTransactions(transactions);
			}
		}
	}
}
