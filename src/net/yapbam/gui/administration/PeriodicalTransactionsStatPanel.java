package net.yapbam.gui.administration;

import javax.swing.JComponent;

import net.yapbam.data.FilteredData;
import net.yapbam.data.PeriodicalTransactionSimulationData;
import net.yapbam.gui.widget.AbstractStatPanel;

@SuppressWarnings("serial")
public class PeriodicalTransactionsStatPanel extends AbstractStatPanel<PeriodicalTransactionSimulationData, FilteredData> {

	public PeriodicalTransactionsStatPanel(FilteredData data) {
		super(data);
	}

	@Override
	protected PeriodicalTransactionSimulationData buildData(FilteredData data) {
		return new PeriodicalTransactionSimulationData(data);
	}

	@Override
	protected JComponent buildDetails() {
		return new PeriodicalTransactionDetailedStatPanel(getData());
	}

	protected void doUpdate() {
/*		if (data.getNbExpenses()+data.getNbReceipts()==0) {
			this.setVisible(false);
		} else {
			DecimalFormat format = LocalizationData.getCurrencyInstance();
			String message = Formatter.format(LocalizationData.get("MainFrame.stat.summary"), //$NON-NLS-1$
					data.getNbExpenses(),
					data.getNbReceipts(),
					format.format(data.getReceipts()+data.getExpenses())
					);
			getContent().setText(message);
			String summary = Formatter.format(LocalizationData.get("StatementView.statementSummary"), data.getNbExpenses()+data.getNbReceipts(), //$NON-NLS-1$
					format.format(data.getExpenses()), format.format(data.getReceipts()));
			getSummaryLabel().setText(summary);
			this.setVisible(true);
		}*/
	}
}