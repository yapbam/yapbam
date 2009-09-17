package net.yapbam.ihm.transactiontable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.Transaction;
import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;

@SuppressWarnings("serial")
public class DeleteTransactionAction extends AbstractAction {
	private TransactionTable table;
	
	public DeleteTransactionAction(TransactionTable table) {
		super(LocalizationData.get("MainMenu.Transactions.Delete"), IconManager.DELETE_TRANSACTION);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Delete.ToolTip"));
        putValue(Action.MNEMONIC_KEY,(int)LocalizationData.getChar("MainMenu.Transactions.Delete.Mnemonic")); //$NON-NLS-1$
        this.table = table;
        this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = table.getSelectedTransaction();
		table.getGlobalData().remove(transaction);
	}
}