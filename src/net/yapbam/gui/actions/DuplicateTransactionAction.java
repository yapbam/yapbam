package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.AbstractAction;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dialogs.TransactionDialog;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class DuplicateTransactionAction extends AbstractAction {
	private TransactionSelector selector;
	
	public DuplicateTransactionAction(TransactionSelector selector) {
		super(LocalizationData.get("MainMenu.Transactions.Duplicate"), IconManager.DUPLICATE_TRANSACTION);
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Duplicate.ToolTip"));
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
		TransactionDialog dialog = new TransactionDialog(AbstractDialog.getOwnerWindow((Component) e.getSource()), selector.getFilteredData(),
				selector.getSelectedTransaction(), false);
		if (Preferences.INSTANCE.getEditingOptions().isDuplicateTransactionDateToCurrent()) dialog.setTransactionDate(new Date());
		dialog.autoFillStatement();
		dialog.setVisible(true);
		Transaction newTransaction = dialog.getTransaction();
		if (newTransaction != null) {
			selector.getFilteredData().getGlobalData().add(newTransaction);
		}
	}
}