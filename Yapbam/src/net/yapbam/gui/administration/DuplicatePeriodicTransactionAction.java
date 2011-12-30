package net.yapbam.gui.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractTransactionDialog;
import net.yapbam.gui.dialogs.PeriodicalTransactionDialog;

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
		PeriodicalTransactionDialog.open(table.getFilteredData(),AbstractTransactionDialog.getOwnerWindow(table),
				(PeriodicalTransaction) table.getSelectedTransaction().clone(), false);
	}
}