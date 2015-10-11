package net.yapbam.gui.administration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.MessageFormat;

import javax.swing.JComponent;

import net.yapbam.data.FilteredData;
import net.yapbam.data.PeriodicalTransactionSimulationData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AbstractStatPanel;

@SuppressWarnings("serial")
public class PeriodicalTransactionsStatPanel extends AbstractStatPanel<PeriodicalTransactionSimulationData, FilteredData> {

	public PeriodicalTransactionsStatPanel(FilteredData data) {
		super(data);
		getShowButton().setIcon(IconManager.get(Name.SETTINGS));
	}

	@Override
	protected PeriodicalTransactionSimulationData buildData(FilteredData data) {
		return new PeriodicalTransactionSimulationData(data);
	}

	@Override
	protected JComponent buildDetails() {
		return new PeriodicalTransactionDetailedStatPanel(getData());
	}

	public void setIgnoreFilter(boolean ignore) {
		getData().setIgnoreFilter(ignore);
	}
	
	@Override
	protected void setDeployed(boolean deployed) {
		super.setDeployed(deployed);
		getShowButton().setIcon(IconManager.get(deployed?Name.CLOSE:Name.SETTINGS));
	}

	@Override
	protected void initialize() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints cShow = new GridBagConstraints();
		cShow.gridx = 0;
		cShow.anchor = GridBagConstraints.NORTHWEST;
		add(getShowButton(), cShow);
		GridBagConstraints gbcSummaryLabel = new GridBagConstraints();
		gbcSummaryLabel.insets = new Insets(0, 5, 0, 0);
		gbcSummaryLabel.weightx = 1.0;
		gbcSummaryLabel.anchor = GridBagConstraints.WEST;
		gbcSummaryLabel.gridx = 2;
		add(getSummary(), gbcSummaryLabel);
		GridBagConstraints cContent = new GridBagConstraints();
		cContent.anchor = GridBagConstraints.WEST;
		cContent.insets = new Insets(0, 5, 0, 0);
		cContent.gridx = 1;
		add(getDetails(), cContent);
	}

	private String getPeriodWording() {
		PeriodicalTransactionDetailedStatPanel settings = (PeriodicalTransactionDetailedStatPanel)getDetails();
		if (settings.getNextMonth().isSelected()) {
			return LocalizationData.get("PeriodicalTransactionManager.nextMonth"); //$NON-NLS-1$
		} else if (settings.getNext3months().isSelected()) {
			return LocalizationData.get("PeriodicalTransactionManager.nextQuarter"); //$NON-NLS-1$
		} else if (settings.getNextYear().isSelected()) {
			return LocalizationData.get("PeriodicalTransactionManager.nextYear"); //$NON-NLS-1$
		} else {
			return "?"; //$NON-NLS-1$
		}
	}
	
	protected void doUpdate() {
		DecimalFormat ci = LocalizationData.getCurrencyInstance();
		String format = LocalizationData.get("PeriodicalTransactionManager.summary"); //$NON-NLS-1$
		getSummary().setText(MessageFormat.format(format, getPeriodWording(), getData().getNbTransactions(),
				ci.format(-getData().getTotalExpenses()), ci.format(getData().getTotalReceips()), ci.format(getData().getTotalExpenses()+getData().getTotalReceips())));
	}
}