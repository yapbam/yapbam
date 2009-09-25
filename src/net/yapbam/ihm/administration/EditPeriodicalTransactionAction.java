package net.yapbam.ihm.administration;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.ihm.IconManager;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class EditPeriodicalTransactionAction extends AbstractAction {
	private PeriodicalTransactionsTable table;
	
	public EditPeriodicalTransactionAction(PeriodicalTransactionsTable table) {
		super(LocalizationData.get("GenericButton.edit"), IconManager.EDIT); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("PeriodicalTransactionManager.edit.toolTip")); //$NON-NLS-1$
        this.table = table;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransaction transaction = table.getSelectedTransaction();
		PeriodicalTransactionDialog.open(table.getGlobalData(), AbstractDialog.getOwnerWindow((Component) e.getSource()), transaction, true);
	}
}