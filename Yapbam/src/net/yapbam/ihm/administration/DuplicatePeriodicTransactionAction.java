package net.yapbam.ihm.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.dialogs.AbstractTransactionDialog;
import net.yapbam.ihm.dialogs.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class DuplicatePeriodicTransactionAction extends AbstractAction {
	private PeriodicalTransactionsTable table;
	
	public DuplicatePeriodicTransactionAction(PeriodicalTransactionsTable table) {
		super(LocalizationData.get("GenericButton.duplicate"), IconManager.DUPLICATE);
        putValue(SHORT_DESCRIPTION, LocalizationData.get("PeriodicalTransactionManager.duplicate.toolTip"));
        this.table = table;
        this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransactionDialog.open(table.getGlobalData(),
				AbstractTransactionDialog.getOwnerWindow(table),
				(PeriodicalTransaction)table.getSelectedTransaction().clone(), false);
	}
}