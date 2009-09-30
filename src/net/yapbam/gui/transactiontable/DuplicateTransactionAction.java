package net.yapbam.gui.transactiontable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractTransactionDialog;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class DuplicateTransactionAction extends AbstractAction {
	private TransactionTable table;
	
	public DuplicateTransactionAction(TransactionTable table) {
		super(LocalizationData.get("MainMenu.Transactions.Duplicate"), IconManager.DUPLICATE_TRANSACTION);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Duplicate.ToolTip"));
        this.table = table;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionDialog.open(table.getGlobalData(),
				AbstractTransactionDialog.getOwnerWindow(table),
				(Transaction)table.getSelectedTransaction().clone(), false);
	}
}