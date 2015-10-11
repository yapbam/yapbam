package net.yapbam.gui.administration;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.NewPeriodicalTransactionAction;
import net.yapbam.gui.transactiontable.GeneratePeriodicalTransactionsAction;
import net.yapbam.gui.util.JTableUtils;

public class PeriodicalTransactionListPanel extends AbstractListAdministrationPanel<FilteredData> implements AbstractAdministrationPanel {
	private static final String STATE_PREFIX = "net.yapbam.periodicalTransactionAdministration."; //$NON-NLS-1$
	private static final long serialVersionUID = 1L;
	private PeriodicalTransactionTableModel model;
	private PeriodicalTransactionsStatPanel statPanel;

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
	protected Component getRightComponent() {
		return new JButton(new GeneratePeriodicalTransactionsAction(data.getGlobalData(), false));
	}
	@Override
	protected Component getTopComponent() {
		if (statPanel==null) {
			statPanel = new PeriodicalTransactionsStatPanel(data);
		}
		return statPanel;
	}
	@Override
	protected int getBottomInset() {
		return 5;
	}
	@Override
	protected Action getNewButtonAction() {
		return new NewPeriodicalTransactionAction(data.getGlobalData());
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
		return new PeriodicalTransactionsTable(getModel());
	}
	private PeriodicalTransactionTableModel getModel() {
		if (model ==null) {
			model = new PeriodicalTransactionTableModel(this);
		}
		return model;
	}
	@Override
	protected String getStatePrefix() {
		return STATE_PREFIX;
	}
	@Override
	public JComponent getPanel() {
		return this;
	}
	@Override
	public void restoreState() {
		super.restoreState();
		// The following lines prevent the open/close subtransactions column from having a size different from the default one
		JTableUtils.fixColumnSize(getJTable(), 0, 0);
	}
	public void setIgnoreFilter(boolean ignore) {
		getModel().setIgnoreFilter(ignore);
		((PeriodicalTransactionsStatPanel)getTopComponent()).setIgnoreFilter(ignore);
	}
}
