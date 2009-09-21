package net.yapbam.ihm.administration;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.ihm.dialogs.AbstractDialog;
import net.yapbam.ihm.dialogs.PeriodicalTransactionDialog;

@SuppressWarnings("serial")
public class EditPeriodicalTransactionAction extends AbstractAction {//LOCAL
	private PeriodicalTransactionsTable table;
	
	public EditPeriodicalTransactionAction(PeriodicalTransactionsTable table) {
		super("Editer");
        putValue(SHORT_DESCRIPTION, "Ouvre le dialogue d'édition de l'opération périodique sélectionnée");
        this.table = table;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		PeriodicalTransaction transaction = table.getSelectedTransaction();
		PeriodicalTransactionDialog.open(table.getGlobalData(), AbstractDialog.getOwnerWindow((Component) e.getSource()), transaction);
	}
}