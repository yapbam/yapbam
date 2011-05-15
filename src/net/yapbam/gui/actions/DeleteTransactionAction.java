package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.Transaction;
import net.yapbam.gui.EditingOptions;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dialogs.AlertDialog;
import net.yapbam.gui.util.AbstractDialog;

/**
 * An action that deletes a selected transaction.
 * 
 * @see TransactionSelector
 */
@SuppressWarnings("serial")
public class DeleteTransactionAction extends AbstractAction {
	private TransactionSelector selector;

	public DeleteTransactionAction(TransactionSelector selector) {
		super(LocalizationData.get("MainMenu.Transactions.Delete"), IconManager.DELETE_TRANSACTION); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Delete.ToolTip")); //$NON-NLS-1$
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.Delete.Mnemonic")); //$NON-NLS-1$
		this.selector = selector;
		this.setEnabled(selector.getSelectedTransaction()!=null);
		this.selector.addPropertyChangeListener(TransactionSelector.SELECTED_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setEnabled(evt.getNewValue()!=null);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = selector.getSelectedTransaction();
		EditingOptions editingOptions = Preferences.INSTANCE.getEditingOptions();
		if (editingOptions.isAlertOnModifyChecked() && (transaction.getStatement() != null)) {
			AlertDialog alert = new AlertDialog(
					AbstractDialog.getOwnerWindow((Component) e.getSource()),
					LocalizationData.get("DeleteCheckedTransactionAlert.title"), LocalizationData.get("DeleteCheckedTransactionAlert.message")); //$NON-NLS-1$ //$NON-NLS-2$
			alert.setVisible(true);
			if (alert.getResult() == null) return;
			if (alert.getResult()) {
				editingOptions.setAlertOnModifyChecked(false);
				Preferences.INSTANCE.setEditingOptions(editingOptions);
			}
		} else if (editingOptions.isAlertOnDelete()) {
			AlertDialog alert = new AlertDialog(AbstractDialog.getOwnerWindow((Component) e.getSource()),
					LocalizationData.get("DeleteTransactionAlert.title"), LocalizationData.get("DeleteTransactionAlert.message")); //$NON-NLS-1$ //$NON-NLS-2$
			alert.setVisible(true);
			if (alert.getResult() == null) return;
			if (alert.getResult()) {
				editingOptions.setAlertOnDelete(false);
				Preferences.INSTANCE.setEditingOptions(editingOptions);
			}
		}
		selector.getFilteredData().getGlobalData().remove(transaction);
	}
}