package net.yapbam.ihm.administration;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.GlobalData;

@SuppressWarnings("serial")
public class DeletePeriodicalTransactionAction extends AbstractAction {//LOCAL
	private PeriodicalTransactionListPanel panel;
	
	public DeletePeriodicalTransactionAction(PeriodicalTransactionListPanel panel) {
		super("Supprimer");
        putValue(SHORT_DESCRIPTION, "Supprime l'opération périodique sélectionnée");
        this.panel = panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransactionsTable table = (PeriodicalTransactionsTable) panel.getJTable();
		GlobalData data = ((PeriodicalTransactionTableModel)table.getModel()).getGlobalData();
		data.remove(table.getSelectedTransaction());
	}
}