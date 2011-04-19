package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.TransactionDialog;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class EditTransactionAction extends AbstractAction {
	private TransactionSelector selector;
	
	public EditTransactionAction(TransactionSelector selector) {
		super(LocalizationData.get("MainMenu.Transactions.Edit"), IconManager.EDIT_TRANSACTION); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Edit.ToolTip")); //$NON-NLS-1$
        putValue(Action.MNEMONIC_KEY, (int)LocalizationData.getChar("MainMenu.Transactions.Edit.Mnemonic")); //$NON-NLS-1$
        this.selector = selector;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = selector.getSelectedTransaction();
		TransactionDialog.open(selector.getFilteredData(), AbstractDialog.getOwnerWindow((Component) e.getSource()), transaction, true, true);
	}
}