package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.MainFrame;
import net.yapbam.ihm.dialogs.TransactionDialog;

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