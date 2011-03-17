package net.yapbam.gui.administration;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTable;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
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

	protected Action getNewButtonAction() {
		return new NewPeriodicalTransactionAction(super.data);
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
	@Override
	public void restoreState() {
		YapbamState.restoreState(getJTable(), STATE_PREFIX);
	}
	@Override
	public void saveState() {
		YapbamState.saveState(getJTable(), STATE_PREFIX);
	}

	@Override
	public JComponent getPanel() {
		return this;
	}
}
