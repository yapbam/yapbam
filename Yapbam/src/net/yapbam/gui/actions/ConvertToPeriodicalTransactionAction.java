package net.yapbam.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.dialogs.periodicaltransaction.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class ConvertToPeriodicalTransactionAction extends AbstractTransactionAction {
	public ConvertToPeriodicalTransactionAction(TransactionSelector selector) {
		super(selector, LocalizationData.get("MainMenu.Transactions.convertToPeriodical"), null, //$NON-NLS-1$
				LocalizationData.get("MainMenu.Transactions.convertToPeriodical.ToolTip")); //$NON-NLS-1$
		putValue(MNEMONIC_KEY, (int) LocalizationData.getChar("MainMenu.Transactions.convertToPeriodical.Mnemonic")); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Transaction transaction = selector.getSelectedTransactions()[0];
		List<SubTransaction> list = new ArrayList<SubTransaction>(transaction.getSubTransactionSize());
		for (int i = 0; i < transaction.getSubTransactionSize(); i++) {
			list.add(transaction.getSubTransaction(i));
		}
		PeriodicalTransaction model = new PeriodicalTransaction(transaction.getDescription(), transaction.getComment(), transaction.getAmount(),
				transaction.getAccount(), transaction.getMode(), transaction.getCategory(), list, null, false, null);
		PeriodicalTransactionDialog.open(selector.getFilteredData().getGlobalData(), Utils.getOwnerWindow((Component)e.getSource()), model, false);
	}
}
