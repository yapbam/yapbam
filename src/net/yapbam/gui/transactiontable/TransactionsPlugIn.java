package net.yapbam.gui.transactiontable;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.YapbamState;

public class TransactionsPlugIn extends AbstractPlugIn {
	private static final String STATE_PREFIX = "net.yapbam.transactionTable."; //$NON-NLS-1$
	
	private TransactionsPlugInPanel panel;

	public TransactionsPlugIn(FilteredData filteredData, Object restoreData) {
		FilteredData data = filteredData;
		this.panel = new TransactionsPlugInPanel(data);
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

	public String getPanelTitle() {
		return LocalizationData.get("MainFrame.Transactions");
	}

	@Override
	public String getPanelToolTip() {
		return LocalizationData.get("MainFrame.Transactions.toolTip");
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
}
