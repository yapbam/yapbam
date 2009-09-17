package net.yapbam.ihm.transactiontable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.data.FilteredData;
import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.YapbamState;
import net.yapbam.ihm.actions.NewTransactionAction;

public class TransactionsPlugIn extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String COLUMN_WIDTH = "net.yapbam.transactionTable.column.width."; //$NON-NLS-1$
	private static final String COLUMN_INDEX = "net.yapbam.transactionTable.column.index."; //$NON-NLS-1$
//	private static final String SELECTED_ROW = "net.yapbam.transactionTable.selectedRow"; //$NON-NLS-1$
	private static final String SCROLL_POSITION = "net.yapbam.transactionTable.scrollPosition"; //$NON-NLS-1$
	
	Action newTransactionAction;
	Action editTransactionAction;
	Action duplicateTransactionAction;
	Action deleteTransactionAction;
	Action generatePeriodical;
	Action checkTransactionAction;
	
	private AccountFilteredData acFilter;
	
	private CheckModePanel checkModePane;
	private TransactionTable transactionTable;
	private BalanceReportField currentBalance;
	private BalanceReportField finalBalance;
	private BalanceReportField checkedBalance;

	public TransactionsPlugIn(AccountFilteredData acFilter, FilteredData data) {
		super(new BorderLayout());
		setOpaque(true);

		transactionTable = new TransactionTable(data);
		this.acFilter = acFilter;
		
        this.newTransactionAction = new NewTransactionAction(transactionTable.getGlobalData());
        this.editTransactionAction = new EditTransactionAction(transactionTable);
        this.duplicateTransactionAction = new DuplicateTransactionAction(transactionTable);
        this.deleteTransactionAction = new DeleteTransactionAction(transactionTable);
        this.checkTransactionAction = new CheckTransactionAction(this);
        this.generatePeriodical = new GeneratePeriodicalTransactionsAction(transactionTable);
        
        transactionTable.addMouseListener(new MouseAdapter() {
        	public void mouseReleased(MouseEvent e) {
        		maybeShowPopup(e);
        	}
            public void mousePressed(MouseEvent e) {
           		if ((e.getClickCount()==2) && (e.getButton()==MouseEvent.BUTTON1)) {
                  Point p = e.getPoint();
                  int row = transactionTable.rowAtPoint(p);
                  if (row >= 0) {
                	  if (checkModePane.isSelected()) {
                		  checkTransactionAction.actionPerformed(new ActionEvent(transactionTable, 0, null));
                	  } else {
                		  editTransactionAction.actionPerformed(new ActionEvent(transactionTable, 0, null));
                	  }
                  }
                } else {
                	maybeShowPopup(e);
                }
            }
            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    Point p = e.getPoint();
                    int row = transactionTable.rowAtPoint(p);
                    transactionTable.getSelectionModel().setSelectionInterval(row, row);
                	JPopupMenu popup = new JPopupMenu();
                	if (checkModePane.isSelected()) {
                		popup.add(new JMenuItem(checkTransactionAction));
                		popup.addSeparator();
                	}
                    popup.add(new JMenuItem(editTransactionAction));
                    popup.add(new JMenuItem(duplicateTransactionAction));
                    popup.add(new JMenuItem(deleteTransactionAction));
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
		});
		ListSelectionModel selModel = transactionTable.getSelectionModel();
		selModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel m = (javax.swing.ListSelectionModel) e.getSource();
				if (!e.getValueIsAdjusting()) {
					boolean ok = m.getMinSelectionIndex()>=0;
					duplicateTransactionAction.setEnabled(ok);
					editTransactionAction.setEnabled(ok);
					deleteTransactionAction.setEnabled(ok);
				}
			}
		});


		JPanel topPanel = new JPanel(new GridBagLayout());
		String noText = ""; //$NON-NLS-1$
		JButton newTransactionButton = new JButton(newTransactionAction);
		newTransactionButton.setText(noText);
		Dimension dimension = newTransactionButton.getPreferredSize();
		dimension.width = dimension.height;
		newTransactionButton.setPreferredSize(dimension);
		final JButton editTransactionButton = new JButton(editTransactionAction);
		editTransactionButton.setText(noText);
		editTransactionButton.setPreferredSize(dimension);
		final JButton duplicateTransactionButton = new JButton(duplicateTransactionAction);
		duplicateTransactionButton.setText(noText);
		duplicateTransactionButton.setPreferredSize(dimension);
		final JButton deleteTransactionButton = new JButton(deleteTransactionAction);
		deleteTransactionButton.setText(noText);
		deleteTransactionButton.setPreferredSize(dimension);
		GridBagConstraints c = new GridBagConstraints();
		topPanel.add(newTransactionButton, c);
		c.gridx = 1;
		topPanel.add(editTransactionButton, c);
		c.gridx = 2;
		topPanel.add(duplicateTransactionButton, c);
		c.gridx = 3;
		topPanel.add(deleteTransactionButton, c);

		checkModePane = new CheckModePanel(transactionTable);
		c.gridx = 4;
		c.anchor = GridBagConstraints.EAST;
		c.weightx = 1;
		topPanel.add(checkModePane, c);

		add(topPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(transactionTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		JPanel bottomPane = new JPanel(new GridLayout(1, 3));
		currentBalance = new BalanceReportField(LocalizationData.get("MainFrame.CurrentBalance"));
		finalBalance = new BalanceReportField(LocalizationData.get("MainFrame.FinalBalance"));
		checkedBalance = new BalanceReportField(LocalizationData.get("MainFrame.CheckedBalance"));
		bottomPane.add(currentBalance);
		bottomPane.add(finalBalance);
		bottomPane.add(checkedBalance);
		add(bottomPane, BorderLayout.SOUTH);
	}
	
	public void updateBalances() {
		currentBalance.setValue(acFilter.getCurrentBalance());
		finalBalance.setValue(acFilter.getFinalBalance());
	    checkedBalance.setValue(acFilter.getCheckedBalance());
	}
	
	public JMenu getPlugInMenu() {
        JMenu transactionMenu = new JMenu(LocalizationData.get("MainMenu.Transactions")); //$NON-NLS-1$
        transactionMenu.setMnemonic(LocalizationData.getChar("MainMenu.Transactions.Mnemonic")); //$NON-NLS-1$
        transactionMenu.setToolTipText(LocalizationData.get("MainMenu.Transactions.ToolTip")); //$NON-NLS-1$
        
        JMenuItem item = new JMenuItem(newTransactionAction);
        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.New.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        transactionMenu.add(item);
        item = new JMenuItem(editTransactionAction);
        transactionMenu.add(item);
        item = new JMenuItem(duplicateTransactionAction);
        transactionMenu.add(item);
        item = new JMenuItem(deleteTransactionAction); //$NON-NLS-1$
        item.setAccelerator(KeyStroke.getKeyStroke(LocalizationData.getChar("MainMenu.Transactions.Delete.Accelerator"), ActionEvent.CTRL_MASK)); //$NON-NLS-1$
        transactionMenu.add(item);
        transactionMenu.addSeparator();
        transactionMenu.add(new JMenuItem(generatePeriodical));
        return transactionMenu;
	}

	public void restoreState(Properties properties) {
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

	public void saveTransactionTableState(Properties properties) {
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

	CheckModePanel getCheckModePane() {
		return checkModePane;
	}

	TransactionTable getTransactionTable() {
		return transactionTable;
	}
		
//TODO	 else if ((event instanceof PeriodicalTransactionAddedEvent) || (event instanceof PeriodicalTransactionRemovedEvent)) {
//			this.generatePeriodical.setEnabled(data.getPeriodicalTransactionsNumber()>0);
//	 }

}
