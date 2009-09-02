package net.astesana.comptes.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.astesana.comptes.data.Transaction;
import net.astesana.comptes.ihm.IconManager;
import net.astesana.comptes.ihm.LocalizationData;
import net.astesana.comptes.ihm.MainFrame;
import net.astesana.comptes.ihm.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class EditTransactionAction extends AbstractAction {
	private MainFrame frame;
	
	public EditTransactionAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Transactions.Edit"), IconManager.EDIT_TRANSACTION);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Edit.ToolTip"));
        this.frame = frame;
        this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = frame.getSelectedTransaction();
		TransactionDialog.open(frame.getData(), frame, transaction);
	}
}