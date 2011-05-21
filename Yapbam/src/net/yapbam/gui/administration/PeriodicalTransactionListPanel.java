package net.yapbam.gui.administration;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTable;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.NewPeriodicalTransactionAction;

public class PeriodicalTransactionListPanel extends AbstractListAdministrationPanel<FilteredData> implements AbstractAdministrationPanel {
	private static final String STATE_PREFIX = "net.yapbam.periodicalTransactionAdministration.";
	private static final long serialVersionUID = 1L;

	public PeriodicalTransactionListPanel(FilteredData data) {
		super(data);
	}
	@Override
	public String getPanelToolTip() {
		return LocalizationData.get("PeriodicalTransactionManager.toolTip"); //$NON-NLS-1$
	}
	@Override
	public String getPanelTitle() {
		return LocalizationData.get("PeriodicalTransactionManager.title"); //$NON-NLS-1$
	}

	@Override
	protected Action getNewButtonAction() {
		return new NewPeriodicalTransactionAction(super.data);
	}
	@Override
	protected Action getEditButtonAction() {
		return new EditPeriodicalTransactionAction((PeriodicalTransactionsTable) getJTable());
	}
	@Override
	protected Action getDeleteButtonAction() {
		return new DeletePeriodicalTransactionAction(this);
	}
	@Override
	protected Action getDuplicateButtonAction() {
		return new DuplicatePeriodicTransactionAction((PeriodicalTransactionsTable) getJTable());
	}
	@Override
	protected JTable instantiateJTable() {
		return new PeriodicalTransactionsTable(new PeriodicalTransactionTableModel(this));
	}
	@Override
	protected String getStatePrefix() {
		return STATE_PREFIX;
	}
	@Override
	public JComponent getPanel() {
		return this;
	}
}