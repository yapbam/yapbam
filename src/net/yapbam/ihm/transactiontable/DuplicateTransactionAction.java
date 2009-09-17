package net.yapbam.ihm.transactiontable;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.Transaction;
import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;

@SuppressWarnings("serial")
public class DuplicateTransactionAction extends AbstractAction {
	private TransactionTable table;
	
	public DuplicateTransactionAction(TransactionTable table) {
		super(LocalizationData.get("MainMenu.Transactions.Duplicate"), IconManager.DUPLICATE_TRANSACTION);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Duplicate.ToolTip"));
        this.table = table;
        this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = table.getSelectedTransaction();
		Transaction newTransaction = (Transaction) transaction.clone();
		table.getGlobalData().add(newTransaction);
	}
}