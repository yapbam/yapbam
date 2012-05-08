package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Date;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class DuplicateTransactionAction extends AbstractTransactionAction {
	public DuplicateTransactionAction(TransactionSelector selector) {
		super(selector, LocalizationData.get("MainMenu.Transactions.Duplicate"), IconManager.DUPLICATE_TRANSACTION,
				LocalizationData.get("MainMenu.Transactions.Duplicate.ToolTip"));
		putValue(MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Duplicate.Mnemonic")); //$NON-NLS-1$
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionDialog dialog = new TransactionDialog(AbstractDialog.getOwnerWindow((Component) e.getSource()), selector.getFilteredData(),
				selector.getSelectedTransactions()[0], false);
		if (Preferences.INSTANCE.getEditingOptions().isDuplicateTransactionDateToCurrent()) dialog.setTransactionDate(new Date());
		dialog.setStatement(null);
		dialog.autoFillStatement();
		dialog.setVisible(true);
		Transaction newTransaction = dialog.getTransaction();
		if (newTransaction != null) {
			selector.getFilteredData().getGlobalData().add(newTransaction);
			selector.setSelectedTransactions(new Transaction[]{newTransaction});
		}
	}
}