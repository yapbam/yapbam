package net.yapbam.ihm.administration;

import javax.swing.Action;
import javax.swing.JTable;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.actions.NewPeriodicalTransactionAction;

import java.lang.Object;

public class PeriodicalTransactionListPanel extends AbstractListAdministrationPanel { //LOCAL
	private static final long serialVersionUID = 1L;

	public PeriodicalTransactionListPanel(Object data) {
		super(data);
	}
	
	public String getPanelToolTip() {
		return "Cet onglet permet de g�rer les op�rations p�riodiques";
	}
	protected Action getNewButtonAction() {
		return new NewPeriodicalTransactionAction((GlobalData) super.data);
	}
	protected Action getEditButtonAction() {
		return new EditPeriodicalTransactionAction((PeriodicalTransactionsTable) getJTable());
	}
	protected Action getDeleteButtonAction() {
		return new DeletePeriodicalTransactionAction(this);
	}	
	@Override
	protected JTable instantiateJTable() {
		return new PeriodicalTransactionsTable(new PeriodicalTransactionTableModel(this));
	}
}
