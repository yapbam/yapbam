package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.FilteredData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class NewTransactionAction extends AbstractAction {
	private FilteredData data;
	private boolean massMode;
	
	public NewTransactionAction(FilteredData data, boolean massMode) {
		super(LocalizationData.get("MainMenu.Transactions.New"), massMode?IconManager.NEW_BULK_TRANSACTION:IconManager.NEW_TRANSACTION);
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.New.ToolTip"));
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.New.Mnemonic")); //$NON-NLS-1$
		this.data = data;
		this.massMode = massMode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionDialog.open(data, AbstractDialog.getOwnerWindow((Component) e.getSource()), null, false, true, this.massMode);
	}
}