package net.yapbam.ihm.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.Transaction;
import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.MainFrame;
import net.yapbam.ihm.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class EditTransactionAction extends AbstractAction {
	private MainFrame frame;
	
	public EditTransactionAction(MainFrame frame) {
		super(LocalizationData.get("MainMenu.Transactions.Edit"), IconManager.EDIT_TRANSACTION); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Edit.ToolTip")); //$NON-NLS-1$
        putValue(Action.MNEMONIC_KEY, (int)LocalizationData.getChar("MainMenu.Transactions.Edit.Mnemonic")); //$NON-NLS-1$
        this.frame = frame;
        this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = frame.getSelectedTransaction();
		TransactionDialog.open(frame.getData(), frame, transaction);
	}
}