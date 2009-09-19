package net.yapbam.ihm.transactiontable;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumnModel;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.ihm.AbstractPlugIn;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.YapbamState;

public class TransactionsPlugIn extends AbstractPlugIn {
	private static final String COLUMN_WIDTH = "net.yapbam.transactionTable.column.width."; //$NON-NLS-1$
	private static final String COLUMN_INDEX = "net.yapbam.transactionTable.column.index."; //$NON-NLS-1$
//	private static final String SELECTED_ROW = "net.yapbam.transactionTable.selectedRow"; //$NON-NLS-1$
	private static final String SCROLL_POSITION = "net.yapbam.transactionTable.scrollPosition"; //$NON-NLS-1$
	
	private TransactionsPlugInPanel panel;
	private JMenu transactionMenu;
	private JMenu filterMenu;

	public TransactionsPlugIn(AccountFilteredData acFilter, FilteredData data) {
		this.panel = new TransactionsPlugInPanel(acFilter, data);
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent) || (event instanceof AccountAddedEvent) || (event instanceof AccountRemovedEvent)) {
					updateFilterMenu();
				}
			}
		});
	}
	
	public JMenu[] getPlugInMenu() {
        //Build the transactions menu
        transactionMenu = new JMenu(LocalizationData.get("MainMenu.Transactions"));
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
        
        //Build the filter menu
        filterMenu = new JMenu(LocalizationData.get("MainMenuBar.Filter")); //$NON-NLS-1$
        filterMenu.setToolTipText(LocalizationData.get("MainMenuBar.Filter.Tooltip")); //$NON-NLS-1$
        filterMenu.setMnemonic(LocalizationData.getChar("MainMenuBar.Filter.Mnemonic")); //$NON-NLS-1$
        updateFilterMenu();

        return new JMenu[]{transactionMenu, filterMenu};
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

	@Override
	public void setDisplayed(boolean displayed) {
		this.transactionMenu.setVisible(displayed);
		this.filterMenu.setVisible(displayed);
	}
	
	private void updateFilterMenu() {
		filterMenu.removeAll();
		GlobalData data = this.panel.getTransactionTable().getGlobalData();
		if (data!=null) {
        	buildBooleanFilterChoiceMenu(new String[]{LocalizationData.get("MainMenuBar.checked"), //$NON-NLS-1$
        			LocalizationData.get("MainMenuBar.notChecked")}, new int[]{FilteredData.CHECKED, FilteredData.NOT_CHECKED}); //$NON-NLS-1$
        	filterMenu.addSeparator();
        	buildBooleanFilterChoiceMenu(new String[]{LocalizationData.get("MainMenuBar.Expenses"), LocalizationData.get("MainMenuBar.Receipts")}, //$NON-NLS-1$ //$NON-NLS-2$
        			new int[]{FilteredData.EXPENSE, FilteredData.RECEIPT});
			
        	//TODO filterMenu.addSeparator();
	        //JMenuItem perso = new JCheckBoxMenuItem(LocalizationData.get("MainMenuBar.customizedFilter")); //$NON-NLS-1$
			//filterMenu.add(perso);
		}
	}
	
	private void buildBooleanFilterChoiceMenu(String[] texts, int[] properties) {
        FilteredData filter = this.panel.getTransactionTable().getFilteredData();
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(texts[0]);
        menuItem.setSelected(filter.isOk(properties[0]) && !filter.isOk(properties[1]));
		filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(properties[0]));
		menuItem = new JRadioButtonMenuItem(texts[1]);
        menuItem.setSelected(!filter.isOk(properties[0]) && filter.isOk(properties[1]));
        filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(properties[1]));
		menuItem = new JRadioButtonMenuItem(LocalizationData.get("MainMenuBar.NoFilter")); //$NON-NLS-1$
        menuItem.setSelected(filter.isOk(properties[0]) && filter.isOk(properties[1]));
        filterMenu.add(menuItem);
		group.add(menuItem);
		menuItem.addActionListener(new FilterActionItem(properties[0] | properties[1]));
	}

	class FilterActionItem implements ActionListener {
		private int property;
		FilterActionItem (int property) {
			this.property = property;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			panel.getTransactionTable().getFilteredData().setFilter(property);
		}
	}
}
