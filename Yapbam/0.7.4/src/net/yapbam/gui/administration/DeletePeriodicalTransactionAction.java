package net.yapbam.gui.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public class DeletePeriodicalTransactionAction extends AbstractAction {
	private PeriodicalTransactionListPanel panel;
	
	public DeletePeriodicalTransactionAction(PeriodicalTransactionListPanel panel) {
		super(LocalizationData.get("GenericButton.delete"), IconManager.DELETE); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, LocalizationData.get("PeriodicalTransactionManager.delete.toolTip")); //$NON-NLS-1$
        this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransactionsTable table = (PeriodicalTransactionsTable) panel.getJTable();
		GlobalData data = ((PeriodicalTransactionTableModel)table.getModel()).getGlobalData();
		data.remove(table.getSelectedTransaction());
	}
}