package net.astesana.comptes.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.astesana.comptes.data.Transaction;
import net.astesana.comptes.ihm.IconManager;
import net.astesana.comptes.ihm.LocalizationData;
import net.astesana.comptes.ihm.MainFrame;

@SuppressWarnings("serial")
public class DuplicateTransactionAction extends AbstractAction {
	private MainFrame frame;
	
	public DuplicateTransactionAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Transactions.Duplicate"), IconManager.DUPLICATE_TRANSACTION);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Duplicate.ToolTip"));
        this.frame = frame;
        this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = frame.getSelectedTransaction();
		transaction = (Transaction) transaction.clone();
		frame.getData().add(transaction);
	}
}