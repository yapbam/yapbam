package net.yapbam.gui.dialogs.periodicaltransaction;

import java.awt.Window;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.GlobalData;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.EditingSettings;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;
import net.yapbam.util.DateUtils;

@SuppressWarnings("serial")
public class GeneratePeriodicalTransactionsDialog extends AbstractDialog<GlobalData, Void> {
	private PeriodicalTransactionGeneratorPanel panel;

	public GeneratePeriodicalTransactionsDialog(Window owner, GlobalData data) {
		super(owner, LocalizationData.get("GeneratePeriodicalTransactionsDialog.title"), data); //$NON-NLS-1$
		this.setResizable(true);
		this.pack();
		this.setMinimumSize(getSize());
	}

	@Override
	protected Void buildResult() {
		// Add the generated transactions
		Transaction[] transactions = panel.getValidTransactions();
		EditingSettings editingOptions = Preferences.INSTANCE.getEditionSettings();
		if (editingOptions.isAutoFillStatement()) {
			for (int i = 0; i < transactions.length; i++) {
				Transaction t = transactions[i];
				Date date = editingOptions.isDateBasedAutoStatement()?t.getDate():t.getValueDate();
				transactions[i] = new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getComment(), t.getAmount(), t.getAccount(), t.getMode(), t.getCategory(),
						t.getValueDate(), editingOptions.getStatementId(date), Arrays.asList(t.getSubTransactions()));
			}
		}
		data.add(transactions);
		// Update the next date of periodical transactions
		PeriodicalTransaction[] wholeTransactions = new PeriodicalTransaction[data.getPeriodicalTransactionsNumber()];
		Date[] dates = new Date[wholeTransactions.length];
		for (int i = 0; i < wholeTransactions.length; i++) {
			wholeTransactions[i] = data.getPeriodicalTransaction(i);
			dates[i] = panel.getDate();
			Date pDate = panel.getPostponedDate(i);
			if (pDate!=null) {
				dates[i] = DateUtils.integerToDate(DateUtils.dateToInteger(pDate)-1);
			}
		}
		data.setPeriodicalTransactionNextDate(wholeTransactions, dates);
		return null;
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new PeriodicalTransactionGeneratorPanel(data);
		panel.addPropertyChangeListener(PeriodicalTransactionGeneratorPanel.HAS_IMPACT_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		if (panel.getDate()==null) {
			return LocalizationData.get("GeneratePeriodicalTransactionsDialog.error.date"); //$NON-NLS-1$
		} else if (!panel.hasImpact()) {
			return LocalizationData.get("GeneratePeriodicalTransactionsDialog.error.noTransaction"); //$NON-NLS-1$
		} else {
			return null;
		}
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
