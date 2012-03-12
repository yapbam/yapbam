package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.EditingOptions;
import net.yapbam.gui.util.AbstractDialog;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsDialog extends AbstractDialog<FilteredData, Void> {
	private PeriodicalTransactionGeneratorPanel panel;

	public GeneratePeriodicalTransactionsDialog(Window owner, FilteredData data) {
		super(owner, LocalizationData.get("GeneratePeriodicalTransactionsDialog.title"), data); //$NON-NLS-1$
	}

	@Override
	protected Void buildResult() {
		Transaction[] transactions = panel.getValidTransactions();
		EditingOptions editingOptions = Preferences.INSTANCE.getEditingOptions();
		if (editingOptions.isAutoFillStatement()) {
			for (int i = 0; i < transactions.length; i++) {
				Transaction t = transactions[i];
				Date date = editingOptions.isDateBasedAutoStatement()?t.getDate():t.getValueDate();
				transactions[i] = new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getComment(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
						t.getValueDate(), editingOptions.getStatementId(date), Arrays.asList(t.getSubTransactions()));
			}
		}
		GlobalData globalData = data.getGlobalData();
		globalData.add(transactions);
		PeriodicalTransaction[] wholeTransactions = new PeriodicalTransaction[globalData.getPeriodicalTransactionsNumber()];
		for (int i = 0; i < wholeTransactions.length; i++) wholeTransactions[i] = globalData.getPeriodicalTransaction(i);
		globalData.setPeriodicalTransactionNextDate(wholeTransactions, panel.getDate());
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new PeriodicalTransactionGeneratorPanel(data);
		panel.addPropertyChangeListener(new AutoUpdateOkButtonPropertyListener(this));
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		if (panel.getDate()==null) return LocalizationData.get("GeneratePeriodicalTransactionsDialog.error.date"); //$NON-NLS-1$
		if (panel.getTransactions().length==0) return LocalizationData.get("GeneratePeriodicalTransactionsDialog.error.noTransaction"); //$NON-NLS-1$
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			panel.restoreState();
		} else {
			panel.saveState();
		}
		super.setVisible(visible);
	}
	
	
}
