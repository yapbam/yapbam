package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.TransactionDialog;

@SuppressWarnings("serial")
public class NewTransactionAction extends AbstractAction {
	private FilteredData data;
	private TransactionSelector selector;
	private boolean massMode;
	
	/** Constructor.
	 * @param data FilteredData (mandatory, even if TransactionSelector has a getFilteredData method, because selector may be null)
	 * @param selector An optional selector (it is used to selected the created transactions) 
	 * @param massMode true to select mass modification
	 */
	public NewTransactionAction(FilteredData data, TransactionSelector selector, boolean massMode) {
		super(LocalizationData.get("MainMenu.Transactions.New"), massMode?IconManager.NEW_BULK_TRANSACTION:IconManager.NEW_TRANSACTION);
		putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.New.ToolTip"));
		putValue(Action.MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.New.Mnemonic")); //$NON-NLS-1$
		this.data = data;
		this.selector = selector;
		this.massMode = massMode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = TransactionDialog.open(data, AbstractDialog.getOwnerWindow((Component) e.getSource()), null, false, true, this.massMode);
		if (transaction!=null) this.selector.setSelectedTransactions(new Transaction[]{transaction});
	}
}