package net.astesana.comptes.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.astesana.comptes.data.Transaction;
import net.astesana.comptes.ihm.IconManager;
import net.astesana.comptes.ihm.LocalizationData;
import net.astesana.comptes.ihm.MainFrame;

@SuppressWarnings("serial")
public class DeleteTransactionAction extends AbstractAction {
	private MainFrame frame;
	
	public DeleteTransactionAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Transactions.Delete"), IconManager.DELETE_TRANSACTION);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Delete.ToolTip"));
        this.frame = frame;
        this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = frame.getSelectedTransaction();
		frame.getData().removeTransaction(transaction);
	}
}