package net.yapbam.gui.transactiontable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;

public class TransactionsPlugIn extends AbstractPlugIn {
	private static final String STATE_PREFIX = "net.yapbam.transactionTable."; //$NON-NLS-1$
	
	private TransactionsPlugInPanel panel;
	private JMenu filterMenu;

	public TransactionsPlugIn(AccountFilteredData acFilter, Object restoreData) {
		FilteredData data = (FilteredData) restoreData;
		if (data == null) data = new FilteredData(acFilter.getGlobalData());
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
	
	@Override
	public Object getRestartData() {
		return this.panel.getTransactionTable().getFilteredData();
	}

	public JMenu[] getPlugInMenu() {
        //Build the filter menu
        filterMenu = new JMenu(LocalizationData.get("MainMenuBar.Filter")); //$NON-NLS-1$
        filterMenu.setToolTipText(LocalizationData.get("MainMenuBar.Filter.Tooltip")); //$NON-NLS-1$
        filterMenu.setMnemonic(LocalizationData.getChar("MainMenuBar.Filter.Mnemonic")); //$NON-NLS-1$
        updateFilterMenu();

        return new JMenu[]{filterMenu};
	}

	@Override
	public JMenuItem[] getMenuItem(int part) {
		if (part==TRANSACTIONS) {
			List<JMenuItem> result = new ArrayList<JMenuItem>();
			result.add(new JMenuItem(panel.editTransactionAction)); 
	        result.add(new JMenuItem(panel.duplicateTransactionAction));
	        JMenuItem item = new JMenuItem(panel.deleteTransactionAction); //$NON-NLS-1$
	        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.Delete.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
	        result.add(item);
			return result.toArray(new JMenuItem[result.size()]);
		} else if (part==PERIODIC_TRANSACTIONS) {
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
	public String getPanelToolIp() {
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
