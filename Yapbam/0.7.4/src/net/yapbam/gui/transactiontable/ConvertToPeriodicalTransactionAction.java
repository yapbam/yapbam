package net.yapbam.gui.transactiontable;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.dialogs.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
class ConvertToPeriodicalTransactionAction extends AbstractAction {
	private TransactionTable table;

	public ConvertToPeriodicalTransactionAction(TransactionTable table) {
		super(LocalizationData.get("MainMenu.Transactions.convertToPeriodical")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("MainMenu.Transactions.convertToPeriodical.ToolTip")); //$NON-NLS-1$
        this.table = table;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = table.getSelectedTransaction();
		List<SubTransaction> list = new ArrayList<SubTransaction>(transaction.getSubTransactionSize());
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			list.add(transaction.getSubTransaction(i));
		}
		PeriodicalTransaction model = new PeriodicalTransaction(transaction.getDescription(), transaction.getAmount(),
				transaction.getAccount(), transaction.getMode(), transaction.getCategory(), list, null, false, null);
		PeriodicalTransactionDialog.open(table.getGlobalData(), AbstractDialog.getOwnerWindow(table), model, false);
	}

}
