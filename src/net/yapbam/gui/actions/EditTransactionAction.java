package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class EditTransactionAction extends AbstractTransactionAction {
	public EditTransactionAction(TransactionSelector selector) {
		super(selector, LocalizationData.get("MainMenu.Transactions.Edit"), IconManager.EDIT_TRANSACTION, //$NON-NLS-1$
				LocalizationData.get("MainMenu.Transactions.Edit.ToolTip")); //$NON-NLS-1$
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Edit.Mnemonic")); //$NON-NLS-1$
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = selector.getSelectedTransaction();
		if (transaction != null) {
			TransactionDialog.open(selector.getFilteredData(), AbstractDialog.getOwnerWindow((Component) e.getSource()), transaction, true, true, false);
		}
	}
}