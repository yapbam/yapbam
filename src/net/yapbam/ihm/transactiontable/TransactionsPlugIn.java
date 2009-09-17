package net.yapbam.ihm.transactiontable;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumnModel;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.data.FilteredData;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.YapbamState;

public class TransactionsPlugIn {
	private static final long serialVersionUID = 1L;
	private static final String COLUMN_WIDTH = "net.yapbam.transactionTable.column.width."; //$NON-NLS-1$
	private static final String COLUMN_INDEX = "net.yapbam.transactionTable.column.index."; //$NON-NLS-1$
//	private static final String SELECTED_ROW = "net.yapbam.transactionTable.selectedRow"; //$NON-NLS-1$
	private static final String SCROLL_POSITION = "net.yapbam.transactionTable.scrollPosition"; //$NON-NLS-1$
	
	private TransactionsPlugInPanel panel;

	public TransactionsPlugIn(AccountFilteredData acFilter, FilteredData data) {
		this.panel = new TransactionsPlugInPanel(acFilter, data);
	}
	
	public JMenu getPlugInMenu() {
        JMenu transactionMenu = new JMenu(LocalizationData.get("MainMenu.Transactions")); //$NON-NLS-1$
        transactionMenu.setMnemonic(LocalizationData.getChar("MainMenu.Transactions.Mnemonic")); //$NON-NLS-1$
        transactionMenu.setToolTipText(LocalizationData.get("MainMenu.Transactions.ToolTip")); //$NON-NLS-1$
        
        JMenuItem item = new JMenuItem(panel.newTransactionAction);
        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.New.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        transactionMenu.add(item);
        item = new JMenuItem(panel.editTransactionAction);
        transactionMenu.add(item);
        item = new JMenuItem(panel.duplicateTransactionAction);
        transactionMenu.add(item);
        item = new JMenuItem(panel.deleteTransactionAction); //$NON-NLS-1$
        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.Delete.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        transactionMenu.add(item);
        transactionMenu.addSeparator();
        transactionMenu.add(new JMenuItem(panel.generatePeriodical));
        return transactionMenu;
	}

	public void restoreState(Properties properties) {
		JTable transactionTable = panel.getTransactionTable();
		TableColumnModel model = transactionTable.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			String valueString = (String) properties.get(COLUMN_WIDTH+i);
			if (valueString!=null) {
				int width = Integer.parseInt(valueString);
				if (width>0) model.getColumn(i).setPreferredWidth(width);
			}
		}
		// Restore column order
		for (int i = model.getColumnCount()-1; i>=0 ; i--) {
			String valueString = (String) properties.get(COLUMN_INDEX+i);
			if (valueString!=null) {
				int modelIndex = Integer.parseInt(valueString);
				if (modelIndex>=0) transactionTable.moveColumn(transactionTable.convertColumnIndexToView(modelIndex), i);
			}
		}
		// Now the selected row (not a very good idea).
//		String valueString = (String) properties.get(SELECTED_ROW);
//		if (valueString!=null) {
//			int index = Integer.parseInt(valueString);
//			if (index < table.getRowCount()) table.getSelectionModel().setSelectionInterval(index, index);
//		}
		// And the scroll position
		Rectangle visibleRect = YapbamState.getRectangle(properties.getProperty(SCROLL_POSITION));
		if (visibleRect!=null) transactionTable.scrollRectToVisible(visibleRect);
	}

	public void saveState(Properties properties) {
		JTable transactionTable = panel.getTransactionTable();
		TableColumnModel model = transactionTable.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			properties.put(COLUMN_WIDTH+transactionTable.convertColumnIndexToModel(i), Integer.toString(model.getColumn(i).getWidth()));
		}
		// Save the column order (if two or more columns were inverted)
		for (int i = 0; i < model.getColumnCount(); i++) {
			properties.put(COLUMN_INDEX+i, Integer.toString(transactionTable.convertColumnIndexToModel(i)));
		}
//		properties.put(SELECTED_ROW, Integer.toString(table.getSelectedRow()));
		properties.put(SCROLL_POSITION, YapbamState.toString(transactionTable.getVisibleRect()));
	}

	public String getPanelTitle() {
		return LocalizationData.get("MainFrame.Transactions");
	}

	public JPanel getPanel() {
		return panel;
	}
		
//TODO	 else if ((event instanceof PeriodicalTransactionAddedEvent) || (event instanceof PeriodicalTransactionRemovedEvent)) {
//			this.generatePeriodical.setEnabled(data.getPeriodicalTransactionsNumber()>0);
//	 }

}
