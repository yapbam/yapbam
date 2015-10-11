package net.yapbam.gui.transactiontable;

import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.fathzer.jlocal.Formatter;

import net.yapbam.data.FilteredData;
import net.yapbam.data.StatData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AbstractStatPanel;

@SuppressWarnings("serial")
public class StatPanel extends AbstractStatPanel<StatData, FilteredData> {
	public StatPanel(FilteredData data) {
		super(data);
	}
	
	@Override
	protected StatData buildData(FilteredData data) {
		return new StatData(data);
	}

	@Override
	protected JComponent buildDetails() {
		return new JLabel();
	}

	protected void doUpdate() {
		if (getData().getNbExpenses()+getData().getNbReceipts()==0) {
			this.setVisible(false);
		} else {
			DecimalFormat format = LocalizationData.getCurrencyInstance();
			String message = Formatter.format(LocalizationData.get("MainFrame.stat.summary"), //$NON-NLS-1$
					getData().getNbExpenses(),
					getData().getNbReceipts(),
					format.format(getData().getReceipts()+getData().getExpenses())
					);
			((JLabel)getDetails()).setText(message);
			String summary = Formatter.format(LocalizationData.get("StatementView.statementSummary"), getData().getNbExpenses()+getData().getNbReceipts(), //$NON-NLS-1$
					format.format(-getData().getExpenses()), format.format(getData().getReceipts()));
			getSummary().setText(summary);
			this.setVisible(true);
		}
	}
}