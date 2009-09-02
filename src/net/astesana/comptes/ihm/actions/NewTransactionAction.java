package net.astesana.comptes.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.astesana.comptes.ihm.IconManager;
import net.astesana.comptes.ihm.LocalizationData;
import net.astesana.comptes.ihm.MainFrame;
import net.astesana.comptes.ihm.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class NewTransactionAction extends AbstractAction {
	private MainFrame frame;
	
	public NewTransactionAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Transactions.New"), IconManager.NEW_TRANSACTION);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.New.ToolTip"));
        this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionDialog.open(frame.getData(), frame, null);
	}
}