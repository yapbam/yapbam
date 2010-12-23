package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsDialog extends AbstractDialog<GlobalData> {
	private PeriodicalTransactionGeneratorPanel panel;

	public GeneratePeriodicalTransactionsDialog(Window owner, GlobalData data) {
		super(owner, LocalizationData.get("GeneratePeriodicalTransactionsDialog.title"), data); //$NON-NLS-1$
	}

	@Override
	protected Object buildResult() {
		panel.saveState();
		Transaction[] transactions = panel.getValidTransactions();
		GlobalData globalData = (GlobalData)data;
		globalData.add(transactions);
		PeriodicalTransaction[] wholeTransactions = new PeriodicalTransaction[globalData.getPeriodicalTransactionsNumber()];
		for (int i = 0; i < wholeTransactions.length; i++) wholeTransactions[i] = globalData.getPeriodicalTransaction(i);
		globalData.setPeriodicalTransactionNextDate(wholeTransactions, panel.getDate());
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new PeriodicalTransactionGeneratorPanel(data);
		panel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkButtonEnabled();
			}
		});
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		if (panel.getDate()==null) return LocalizationData.get("GeneratePeriodicalTransactionsDialog.error.date"); //$NON-NLS-1$
		if (panel.getTransactions().length==0) return LocalizationData.get("GeneratePeriodicalTransactionsDialog.error.noTransaction"); //$NON-NLS-1$
		return null;
	}
}
