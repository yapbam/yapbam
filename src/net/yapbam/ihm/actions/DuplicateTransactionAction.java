package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.Transaction;
import net.yapbam.data.TransactionComparator;
import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.MainFrame;

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
		Transaction newTransaction = (Transaction) transaction.clone();
		frame.getData().add(newTransaction);
	}
}