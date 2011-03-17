package net.yapbam.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;

/** An action that deletes a selected transaction.
 * @see TransactionSelector
 */
@SuppressWarnings("serial")
public class DeleteTransactionAction extends AbstractAction {
	private TransactionSelector selector;
	
	public DeleteTransactionAction(TransactionSelector selector) {
		super(LocalizationData.get("MainMenu.Transactions.Delete"), IconManager.DELETE_TRANSACTION);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Delete.ToolTip"));
        putValue(Action.MNEMONIC_KEY,(int)LocalizationData.getChar("MainMenu.Transactions.Delete.Mnemonic")); //$NON-NLS-1$
        this.selector = selector;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = selector.getSelectedTransaction();
		selector.getFilteredData().getGlobalData().remove(transaction);
	}
}