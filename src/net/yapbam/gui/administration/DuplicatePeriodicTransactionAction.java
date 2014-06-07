package net.yapbam.gui.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.fathzer.soft.ajlib.swing.Utils;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.periodicaltransaction.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class DuplicatePeriodicTransactionAction extends AbstractAction {
	private PeriodicalTransactionsTable table;
	
	public DuplicatePeriodicTransactionAction(PeriodicalTransactionsTable table) {
		super(LocalizationData.get("GenericButton.duplicate"), IconManager.get(Name.DUPLICATE_TRANSACTION)); //$NON-NLS-1$
		putValue(SHORT_DESCRIPTION, LocalizationData.get("PeriodicalTransactionManager.duplicate.toolTip")); //$NON-NLS-1$
		this.table = table;
		this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransactionDialog.open(table.getGlobalData(),Utils.getOwnerWindow(table),
				(PeriodicalTransaction) table.getSelectedTransaction().clone(), false);
	}
}