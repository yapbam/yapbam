package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractAction;

import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class DuplicateTransactionAction extends AbstractAction {
	private TransactionSelector selector;
	
	public DuplicateTransactionAction(TransactionSelector selector) {
		super(LocalizationData.get("MainMenu.Transactions.Duplicate"), IconManager.DUPLICATE_TRANSACTION);
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Duplicate.ToolTip"));
		this.selector = selector;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionDialog dialog = new TransactionDialog(AbstractDialog.getOwnerWindow((Component) e.getSource()), selector.getGlobalData(),
				selector.getSelectedTransaction(), false);
		dialog.setTransactionDate(new Date());
		dialog.setVisible(true);
		Transaction newTransaction = dialog.getTransaction();
		if (newTransaction != null) {
			selector.getGlobalData().add(newTransaction);
		}
	}
}