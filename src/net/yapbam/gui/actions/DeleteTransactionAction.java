package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.dialogs.AlertDialog;
import net.yapbam.gui.preferences.EditingSettings;

/**
 * An action that deletes a selected transaction.
 * 
 * @see TransactionSelector
 */
@SuppressWarnings("serial")
public class DeleteTransactionAction extends AbstractTransactionAction {
	public DeleteTransactionAction(TransactionSelector selector) {
		super(selector, LocalizationData.get("MainMenu.Transactions.Delete"), IconManager.get(Name.DELETE_TRANSACTION), //$NON-NLS-1$
				LocalizationData.get("MainMenu.Transactions.Delete.ToolTip")); //$NON-NLS-1$
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Delete.Mnemonic")); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = selector.getSelectedTransactions()[0];
		EditingSettings editingOptions = Preferences.INSTANCE.getEditionSettings();
		if (editingOptions.isAlertOnModifyChecked() && (transaction.getStatement() != null)) {
			AlertDialog alert = new AlertDialog(Utils.getOwnerWindow((Component) e.getSource()),
					LocalizationData.get("DeleteCheckedTransactionAlert.title"), LocalizationData.get("DeleteCheckedTransactionAlert.message")); //$NON-NLS-1$ //$NON-NLS-2$
			alert.setVisible(true);
			if (alert.getResult() == null) {
				return;
			}
			if (alert.getResult()) {
				editingOptions.setAlertOnModifyChecked(false);
				Preferences.INSTANCE.setEditingOptions(editingOptions);
			}
		} else if (editingOptions.isAlertOnDelete()) {
			AlertDialog alert = new AlertDialog(Utils.getOwnerWindow((Component) e.getSource()),
					LocalizationData.get("DeleteTransactionAlert.title"), LocalizationData.get("DeleteTransactionAlert.message")); //$NON-NLS-1$ //$NON-NLS-2$
			alert.setVisible(true);
			if (alert.getResult() == null) {
				return;
			}
			if (alert.getResult()) {
				editingOptions.setAlertOnDelete(false);
				Preferences.INSTANCE.setEditingOptions(editingOptions);
			}
		}
		selector.getFilteredData().getGlobalData().remove(transaction);
	}
}