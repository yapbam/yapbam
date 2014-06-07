package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Date;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class DuplicateTransactionAction extends AbstractTransactionAction {
	public DuplicateTransactionAction(TransactionSelector selector) {
		super(selector, LocalizationData.get("MainMenu.Transactions.Duplicate"), IconManager.get(Name.DUPLICATE_TRANSACTION), //$NON-NLS-1$
				LocalizationData.get("MainMenu.Transactions.Duplicate.ToolTip")); //$NON-NLS-1$
		putValue(MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Duplicate.Mnemonic")); //$NON-NLS-1$
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionDialog dialog = new TransactionDialog(Utils.getOwnerWindow((Component) e.getSource()), selector.getFilteredData().getGlobalData(),
				selector.getSelectedTransactions()[0], false);
		if (Preferences.INSTANCE.getEditionSettings().isDuplicateTransactionDateToCurrent()) {
			dialog.setTransactionDate(new Date());
		}
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