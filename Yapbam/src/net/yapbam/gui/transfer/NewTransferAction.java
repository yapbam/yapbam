package net.yapbam.gui.transfer;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AccountDialog;

public class NewTransferAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private GlobalData data;
	
	NewTransferAction(GlobalData data) {
		super(LocalizationData.get("TransferDialog.menu")); //$NON-NLS-1$
		this.data = data;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		while (data.getAccountsNumber()<2) {
			// Need to create two accounts first
			Account account = AccountDialog.open(data, AbstractDialog.getOwnerWindow((Component) e.getSource()), LocalizationData.get("TransferDialog.needsTwoAccounts")); //$NON-NLS-1$
			if (account == null) return;
		}
		TransferDialog dialog = new TransferDialog(AbstractDialog.getOwnerWindow((Component) e.getSource()), LocalizationData.get("TransferDialog.title"), data); //$NON-NLS-1$
		dialog.setVisible(true);
	}
}
