package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.PeriodicalTransactionDialog;
import net.yapbam.gui.util.AbstractDialog;

@SuppressWarnings("serial")
public class ConvertToPeriodicalTransactionAction extends AbstractTransactionAction {
	public ConvertToPeriodicalTransactionAction(TransactionSelector selector) {
		super(selector, LocalizationData.get("MainMenu.Transactions.convertToPeriodical"), null, //$NON-NLS-1$
				LocalizationData.get("MainMenu.Transactions.convertToPeriodical.ToolTip")); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = selector.getSelectedTransaction();
		List<SubTransaction> list = new ArrayList<SubTransaction>(transaction.getSubTransactionSize());
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			list.add(transaction.getSubTransaction(i));
		}
		PeriodicalTransaction model = new PeriodicalTransaction(transaction.getDescription(), transaction.getAmount(),
				transaction.getAccount(), transaction.getMode(), transaction.getCategory(), list, null, false, null);
		PeriodicalTransactionDialog.open(selector.getFilteredData(), AbstractDialog.getOwnerWindow((Component)e.getSource()), model, false);
	}
}