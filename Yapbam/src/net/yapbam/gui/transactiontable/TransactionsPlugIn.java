package net.yapbam.gui.transactiontable;

import java.awt.print.Printable;
import java.io.Serializable;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTable.PrintMode;

import net.yapbam.data.FilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionsAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionsRemovedEvent;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.dialogs.preferences.PreferencePanel;
import net.yapbam.gui.transactiontable.BalanceReportPanel.Selection;
import net.yapbam.gui.util.JTableUtils;

public class TransactionsPlugIn extends AbstractPlugIn {
	private static final String STATE_PREFIX = "net.yapbam.transactionTable."; //$NON-NLS-1$
	private static final String BALANCE_REF = STATE_PREFIX+"balanceReport"; //$NON-NLS-1$
	
	private TransactionsPlugInPanel panel;
	private FilteredData data;

	public TransactionsPlugIn(FilteredData filteredData, Object restoreData) {
		data = filteredData;
		this.panel = new TransactionsPlugInPanel(data, this);
		setPanelTitle(LocalizationData.get("MainFrame.Transactions")); //$NON-NLS-1$
		setPanelToolTip(LocalizationData.get("MainFrame.Transactions.toolTip")); //$NON-NLS-1$
		testAlert();
		// Add a listener in order to be able to display an alert if some periodical transactions need to be generated
		// We will listen to the global data because, events may not be thrown by the filtered data (for example if the
		// account of a new peridiocal transaction is not ok with the filter) and we want to be able to display alerts
		// event if it concern a hidden data.
		data.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (eventMayChangePeridiocalTranscationToGenerate(event)) {
					testAlert();
				}
			}
		});
		this.setPrintingSupported(true);
	}
	
	private boolean eventMayChangePeridiocalTranscationToGenerate (DataEvent event) {
		return (event instanceof EverythingChangedEvent) || (event instanceof PeriodicalTransactionsAddedEvent) || (event instanceof PeriodicalTransactionsRemovedEvent);
	}
	
	@Override
	public void restoreState() {
		TransactionTable transactionTable = panel.getTransactionTable();
		YapbamState.INSTANCE.restoreState(transactionTable, STATE_PREFIX);
		transactionTable.scrollToLastLine();
		Serializable restoredSelected = YapbamState.INSTANCE.restore(BALANCE_REF);
		if ((restoredSelected!=null) && (restoredSelected!=BalanceReportPanel.Selection.NONE)) {
			panel.getBalanceReportPanel().setSelected((Selection) restoredSelected);
		}
		// transactionTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); //TODO This mode seems better than the default one, but should be done for all tables or none !
		// The following lines prevent the open/close subtransactions column from having a size different from the default one
		JTableUtils.fixColumnSize(transactionTable, 0,0);
	}

	@Override
	public void saveState() {
		YapbamState.INSTANCE.saveState(panel.getTransactionTable(), STATE_PREFIX);
		YapbamState.INSTANCE.save(BALANCE_REF, panel.getBalanceReportPanel().getSelected());
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public PreferencePanel getLFPreferencePanel() {
		return new TransactionsPreferencePanel();
	}

	@Override
	protected Printable getPrintable() {
		return panel.getTransactionTable().getPrintable(PrintMode.FIT_WIDTH, null, null);
	}
		
	private void testAlert() {
		//TODO Preferences may say if we want alert on all data, or just on filtered data.
		String tooltip;
		tooltip = LocalizationData.get("MainFrame.Transactions.toolTip"); //$NON-NLS-1$
		boolean hasPendingPeriodicalTransactions = data.getGlobalData().hasPendingPeriodicalTransactions(new Date());
		if (hasPendingPeriodicalTransactions) {
			tooltip = "<html>"+tooltip+"<br>"+LocalizationData.get("GeneratePeriodicalTransactionsDialog.alert")+"</html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		setPanelIcon(hasPendingPeriodicalTransactions?IconManager.get(Name.ALERT):null);
		setPanelToolTip(tooltip);
	}

	@Override
	public TransactionSelector getTransactionSelector() {
		return panel.getTransactionTable();
	}
}
