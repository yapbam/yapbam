package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.TransactionDialog;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class NewTransactionAction extends AbstractAction {
	private FilteredData data;
	
	public NewTransactionAction(FilteredData data) {
		super(LocalizationData.get("MainMenu.Transactions.New"), IconManager.NEW_TRANSACTION);
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.New.ToolTip"));
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.New.Mnemonic")); //$NON-NLS-1$
		this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TransactionDialog.open(data, AbstractDialog.getOwnerWindow((Component) e.getSource()), null, false, true, ((e.getModifiers()&ActionEvent.SHIFT_MASK)!=0));
	}
}