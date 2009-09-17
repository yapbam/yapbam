package net.yapbam.ihm.transactiontable;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractAction;

import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;

class CheckTransactionAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private TransactionsPlugIn plugin;
	
	CheckTransactionAction (TransactionsPlugIn plugin) {
		super(LocalizationData.get("MainMenu.Transactions.Check"), IconManager.EDIT_TRANSACTION); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.Check.ToolTip")); //$NON-NLS-1$
		this.plugin = plugin;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction t = plugin.getTransactionTable().getSelectedTransaction();
		ArrayList<SubTransaction> list = new ArrayList<SubTransaction>(t.getSubTransactionSize());
		for (int i = 0; i < t.getSubTransactionSize(); i++) {
			list.add(t.getSubTransaction(i));
		}
		String statementId = null;
		Date date = t.getValueDate();
		if (t.getStatement()==null) {
			Date ckDate = plugin.getCheckModePane().getValueDate();
			if (ckDate!=null) date = ckDate;
			statementId = plugin.getCheckModePane().getStatement();
		}
		Transaction tChecked = new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
				date, statementId, list);
		plugin.getTransactionTable().getGlobalData().remove(t);
		plugin.getTransactionTable().getGlobalData().add(tChecked);
	}
}
