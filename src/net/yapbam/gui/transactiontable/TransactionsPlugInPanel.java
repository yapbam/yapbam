package net.yapbam.gui.transactiontable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.NewTransactionAction;
import net.yapbam.gui.util.JTableListener;

public class TransactionsPlugInPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Action newTransactionAction;
	Action editTransactionAction;
	Action duplicateTransactionAction;
	Action deleteTransactionAction;
	Action generatePeriodical;
	Action checkTransactionAction;
	Action convertToPericalTransactionAction;

	
	private AccountFilteredData acFilter;
	
	private CheckModePanel checkModePane;
	private TransactionTable transactionTable;
	private BalanceReportField currentBalance;
	private BalanceReportField finalBalance;
	private BalanceReportField checkedBalance;

	public TransactionsPlugInPanel(AccountFilteredData acFilter, FilteredData data) {
		super(new BorderLayout());
		setOpaque(true);

		transactionTable = new TransactionTable(data);
		this.acFilter = acFilter;
		
        this.newTransactionAction = new NewTransactionAction(transactionTable.getGlobalData());
        this.editTransactionAction = new EditTransactionAction(transactionTable);
        this.duplicateTransactionAction = new DuplicateTransactionAction(transactionTable);
        this.deleteTransactionAction = new DeleteTransactionAction(transactionTable);
        this.checkTransactionAction = new CheckTransactionAction(this);
        this.convertToPericalTransactionAction = new ConvertToPeriodicalTransactionAction(transactionTable);
        this.generatePeriodical = new GeneratePeriodicalTransactionsAction(transactionTable);
                
        new MyListener(transactionTable, new Action[]{editTransactionAction, duplicateTransactionAction, deleteTransactionAction,
        		null, convertToPericalTransactionAction}, editTransactionAction);
        
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
		currentBalance.setToolTipText(LocalizationData.get("MainFrame.CurrentBalance.ToolTip"));
		finalBalance = new BalanceReportField(LocalizationData.get("MainFrame.FinalBalance"));
		finalBalance.setToolTipText(LocalizationData.get("MainFrame.FinalBalance.ToolTip"));
		checkedBalance = new BalanceReportField(LocalizationData.get("MainFrame.CheckedBalance"));
		checkedBalance.setToolTipText(LocalizationData.get("MainFrame.CheckedBalance.ToolTip"));
		bottomPane.add(currentBalance);
		bottomPane.add(finalBalance);
		bottomPane.add(checkedBalance);
		add(bottomPane, BorderLayout.SOUTH);
		
		acFilter.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				updateBalances();
				if (event instanceof EverythingChangedEvent) {//TODO must be in FilteredData
					if (TransactionsPlugInPanel.this.acFilter.hasFilterAccount()) {
						transactionTable.getFilteredData().setAccounts(TransactionsPlugInPanel.this.acFilter.getAccounts());
					} else {
						transactionTable.getFilteredData().clearAccounts();
					}
				}
			}
		});
		updateBalances();
	}
	
	class MyListener extends JTableListener {

		public MyListener(JTable jTable, Action[] actions, Action defaultAction) {
			super(jTable, actions, defaultAction);
		}

		@Override
		protected void fillPopUp(JPopupMenu popup) {
        	if (checkModePane.isOk()) {
        		popup.add(new JMenuItem(checkTransactionAction));
        		popup.addSeparator();
        	}
			super.fillPopUp(popup);
		}

		@Override
		protected Action getDoubleClickAction() {
      	  if (checkModePane.isOk()) {
    		  return checkTransactionAction;
    	  } else {
    		  return super.getDoubleClickAction();
    	  }
		}
		
	}
	
	public void updateBalances() {
		currentBalance.setValue(acFilter.getCurrentBalance());
		finalBalance.setValue(acFilter.getFinalBalance());
	    checkedBalance.setValue(acFilter.getCheckedBalance());
	}
	
	CheckModePanel getCheckModePane() {
		return checkModePane;
	}

	TransactionTable getTransactionTable() {
		return transactionTable;
	}
}
