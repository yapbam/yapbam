package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class EditTransactionAction extends AbstractTransactionAction {
	public EditTransactionAction(TransactionSelector selector) {
		super(selector, LocalizationData.get("MainMenu.Transactions.Edit"), IconManager.get(Name.EDIT_TRANSACTION), //$NON-NLS-1$
				LocalizationData.get("MainMenu.Transactions.Edit.ToolTip")); //$NON-NLS-1$
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Edit.Mnemonic")); //$NON-NLS-1$
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction[] transactions = selector.getSelectedTransactions();
		if (transactions.length==1) {
			Transaction transaction = TransactionDialog.open(selector.getFilteredData().getGlobalData(), Utils.getOwnerWindow((Component) e.getSource()), transactions[0], true, true, false);
			if (transaction!=null) {
				selector.setSelectedTransactions(new Transaction[]{transaction});
			}
		}
	}
}