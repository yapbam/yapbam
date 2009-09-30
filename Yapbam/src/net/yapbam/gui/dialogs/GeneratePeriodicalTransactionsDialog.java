package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsDialog extends AbstractDialog {
	private PeriodicalTransactionGeneratorPanel panel;

	public GeneratePeriodicalTransactionsDialog(Window owner, GlobalData data) {
		super(owner, LocalizationData.get("GeneratePeriodicalTransactionsDialog.title"), data); //$NON-NLS-1$
	}

	@Override
	protected Object buildResult() {
		panel.saveState();
		Transaction[] transactions = panel.getTransactions();
		for (int i = 0; i < transactions.length; i++) {
			((GlobalData)data).add(transactions[i]);
		}
		for (int i=0; i < ((GlobalData)data).getPeriodicalTransactionsNumber(); i++) {
			((GlobalData)data).setPeriodicalTransactionNextDate(i, panel.getDate());
		}
		return null;
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		panel = new PeriodicalTransactionGeneratorPanel((GlobalData) data);
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
