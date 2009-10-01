package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class EditTransactionAction extends AbstractAction {
	private TransactionTable table;
	
	public EditTransactionAction(TransactionTable table) {
		super(LocalizationData.get("MainMenu.Transactions.Edit"), IconManager.EDIT_TRANSACTION); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Edit.ToolTip")); //$NON-NLS-1$
        putValue(Action.MNEMONIC_KEY, (int)LocalizationData.getChar("MainMenu.Transactions.Edit.Mnemonic")); //$NON-NLS-1$
        this.table = table;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = table.getSelectedTransaction();
		TransactionDialog.open(table.getGlobalData(), AbstractDialog.getOwnerWindow((Component) e.getSource()), transaction, true, true);
	}
}