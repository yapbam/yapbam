package net.yapbam.gui.transactiontable;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.PeriodicalTransactionAddedEvent;
import net.yapbam.data.event.PeriodicalTransactionRemovedEvent;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.YapbamState;

public class TransactionsPlugIn extends AbstractPlugIn {
	private static final String STATE_PREFIX = "net.yapbam.transactionTable."; //$NON-NLS-1$
	
	private TransactionsPlugInPanel panel;
	private FilteredData data;

	public TransactionsPlugIn(FilteredData filteredData, Object restoreData) {
		data = filteredData;
		this.panel = new TransactionsPlugInPanel(data);
		setPanelTitle(LocalizationData.get("MainFrame.Transactions"));
		setPanelToolTip(LocalizationData.get("MainFrame.Transactions.toolTip"));
		testAlert();
		// Add a listener in order to be able to display an alert if some periodical transactions need to be generated
		// We will listen to the global data because, events may not be thrown by the filtered data (for example if the
		// account of a new peridiocal transaction is not ok wiht the filter) and we want to be able to display alerts
		// event if it concern a hidden data.
		data.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (eventMayChangePeridiocalTranscationToGenerate(event)) {
					testAlert();
				}
			}
		});
	}
	
	private boolean eventMayChangePeridiocalTranscationToGenerate (DataEvent event) {
		return (event instanceof EverythingChangedEvent) || (event instanceof PeriodicalTransactionAddedEvent) || (event instanceof PeriodicalTransactionRemovedEvent);
	}
	
	@Override
	public JMenuItem[] getMenuItem(int part) {
		if (part==TRANSACTIONS_PART) {
			List<JMenuItem> result = new ArrayList<JMenuItem>();
			result.add(new JMenuItem(panel.editTransactionAction)); 
	        result.add(new JMenuItem(panel.duplicateTransactionAction));
	        JMenuItem item = new JMenuItem(panel.deleteTransactionAction); //$NON-NLS-1$
	        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.Delete.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
	        result.add(item);
			return result.toArray(new JMenuItem[result.size()]);
		} else if (part==PERIODIC_TRANSACTIONS_PART) {
			return new JMenuItem[]{new JMenuItem(panel.convertToPericalTransactionAction)};
		} else {
			return null;
		}
	}

	public void restoreState() {
		YapbamState.restoreState(panel.getTransactionTable(), STATE_PREFIX);
	}

	public void saveState() {
		YapbamState.saveState(panel.getTransactionTable(), STATE_PREFIX);
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void setDisplayed(boolean displayed) {
		boolean rowIsSelected = panel.transactionTable.getSelectedRow()>0;
		panel.editTransactionAction.setEnabled(displayed && rowIsSelected); 
        panel.duplicateTransactionAction.setEnabled(displayed && rowIsSelected);
        panel.deleteTransactionAction.setEnabled(displayed && rowIsSelected);
        panel.convertToPericalTransactionAction.setEnabled(displayed && rowIsSelected);
	}

	@Override
	public PreferencePanel getPreferencePanel() {
		return new TransactionsPreferencePanel();
	}

	@Override
	public boolean isPrintingSupported() {
		return true;
	}

	@Override
	public void print() throws PrinterException {
		panel.transactionTable.print();
	}
	
	private void testAlert() {
		//TODO Preferences may say if we want alert on all data, or just on filtered data.
		Transaction[] transactions = data.getGlobalData().generateTransactionsFromPeriodicals(new Date());
		String tooltip;
		tooltip = LocalizationData.get("BalanceHistory.toolTip");
		if (transactions.length!=0) {
			tooltip = "<html>"+tooltip+"<br>"+LocalizationData.get("GeneratePeriodicalTransactionsDialog.alert")+"</html>";
		}
		setPanelIcon((transactions.length>0?IconManager.ALERT:null));
		setPanelToolTip(tooltip);
	}
}
