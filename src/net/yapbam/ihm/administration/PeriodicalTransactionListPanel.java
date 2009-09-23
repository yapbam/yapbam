package net.yapbam.ihm.administration;

import javax.swing.Action;
import javax.swing.JTable;

import net.yapbam.data.GlobalData;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.actions.NewPeriodicalTransactionAction;

import java.lang.Object;

public class PeriodicalTransactionListPanel extends AbstractListAdministrationPanel {
	private static final long serialVersionUID = 1L;

	public PeriodicalTransactionListPanel(Object data) {
		super(data);
	}
	@Override
	public String getPanelToolTip() {
		return LocalizationData.get("PeriodicalTransactionManager.toolTip"); //$NON-NLS-1$
	}
	@Override
	public String getTitle() {
		return LocalizationData.get("PeriodicalTransactionManager.title"); //$NON-NLS-1$
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
	protected Action getDuplicateButtonAction() {
		return new DuplicatePeriodicTransactionAction((PeriodicalTransactionsTable) getJTable());
	}
	@Override
	protected JTable instantiateJTable() {
		return new PeriodicalTransactionsTable(new PeriodicalTransactionTableModel(this));
	}
}
