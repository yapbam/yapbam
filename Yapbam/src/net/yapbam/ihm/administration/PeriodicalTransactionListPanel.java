package net.yapbam.ihm.administration;

import javax.swing.Action;
import javax.swing.JTable;

import net.yapbam.ihm.actions.EditPeriodicalTransactionAction;
import net.yapbam.ihm.actions.NewPeriodicalTransactionAction;

import java.lang.Object;

public class PeriodicalTransactionListPanel extends AbstractListAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	public PeriodicalTransactionListPanel(Object data) {
		super(data);
	}
	
	public String getPanelToolTip() {
		return "Cet onglet permet de gérer les opérations périodiques";
	}
	protected Action getNewButtonAction() {
		return new NewPeriodicalTransactionAction();
	}
	protected Action getEditButtonAction() {
		return new EditPeriodicalTransactionAction();
	}
	protected Action getDeleteButtonAction() {
		return new DeletePeriodicalTransactionAction(this);
	}	
	@Override
	protected JTable instantiateJTable() {
		return new PeriodicalTransactionsTable(new PeriodicalTransactionTableModel(this));
	}
}
