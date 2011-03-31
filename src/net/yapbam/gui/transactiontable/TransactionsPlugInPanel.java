package net.yapbam.gui.transactiontable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import net.yapbam.data.BalanceData;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.actions.NewTransactionAction;
import net.yapbam.gui.util.JTableListener;
import net.yapbam.gui.widget.JLabelMenu;

public class TransactionsPlugInPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Action editTransactionAction;
	Action duplicateTransactionAction;
	Action deleteTransactionAction;
	Action checkTransactionAction;
	Action convertToPericalTransactionAction;
	
	private BalanceData acFilter;
	
	private CheckModePanel checkModePane;
	TransactionTable transactionTable;
	private BalanceReportField currentBalance;
	private BalanceReportField finalBalance;
	private BalanceReportField checkedBalance;

	@SuppressWarnings("serial")
	public TransactionsPlugInPanel(FilteredData data) {
		super(new BorderLayout());
		setOpaque(true);

		transactionTable = new TransactionTable(data);
		this.acFilter = data.getBalanceData();

		this.editTransactionAction = new EditTransactionAction(transactionTable);
		this.duplicateTransactionAction = new DuplicateTransactionAction(transactionTable);
		this.deleteTransactionAction = new DeleteTransactionAction(transactionTable);
		this.checkTransactionAction = new CheckTransactionAction(this);
		this.convertToPericalTransactionAction = new ConvertToPeriodicalTransactionAction(transactionTable);

		new MyListener(transactionTable, new Action[] { editTransactionAction, duplicateTransactionAction,
				deleteTransactionAction, null, convertToPericalTransactionAction }, editTransactionAction);
        
		JPanel topPanel = new JPanel(new GridBagLayout());
		String noText = ""; //$NON-NLS-1$
		JButton newTransactionButton = new JButton(new NewTransactionAction(transactionTable.getFilteredData()));
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

		JScrollPane scrollPane = new JScrollPane(transactionTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		JPanel bottomPane = new JPanel(new BorderLayout());
		JPanel menus = new JPanel(new BorderLayout());
		JLabel deploy = new JLabelMenu(LocalizationData.get("MainFrame.ShowSubtransactions")) { //$NON-NLS-1$
			@Override
			protected void fillPopUp(JPopupMenu popup) {
				popup.add(new DeploySubTransactionsAction(LocalizationData.get("MainFrame.ShowSubtransactions.All"), true)); //$NON-NLS-1$
				popup.add(new DeploySubTransactionsAction(LocalizationData.get("MainFrame.ShowSubtransactions.None"), false)); //$NON-NLS-1$
			}
		};
		Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.gray, 1), new EmptyBorder(0, 5, 0, 0));
		deploy.setBorder(border);
		deploy.setToolTipText(LocalizationData.get("MainFrame.ShowSubtransactions.ToolTip")); //$NON-NLS-1$
		menus.add(deploy, BorderLayout.NORTH);
		JLabel columns = new JLabelMenu(LocalizationData.get("MainFrame.showColumns")) { //$NON-NLS-1$
			@Override
			protected void fillPopUp(JPopupMenu popup) {
				//TODO Fill the popup in the order of the column in the view ... not in the model
				for (int i = 0; i < transactionTable.getColumnCount(false); i++) {
					JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(new ShowHideColumnAction(i));
					menuItem.setSelected(transactionTable.isColumnVisible(i));
					popup.add(menuItem);
				}
			}
		};
		columns.setBorder(border);
		columns.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip")); //$NON-NLS-1$
		menus.add(columns, BorderLayout.SOUTH);
		bottomPane.add(menus, BorderLayout.WEST);
		JPanel balancePane = new JPanel(new GridLayout(1, 3));
		currentBalance = new BalanceReportField(LocalizationData.get("MainFrame.CurrentBalance")); //$NON-NLS-1$
		currentBalance.setToolTipText(LocalizationData.get("MainFrame.CurrentBalance.ToolTip")); //$NON-NLS-1$
		finalBalance = new BalanceReportField(LocalizationData.get("MainFrame.FinalBalance")); //$NON-NLS-1$
		finalBalance.setToolTipText(LocalizationData.get("MainFrame.FinalBalance.ToolTip")); //$NON-NLS-1$
		checkedBalance = new BalanceReportField(LocalizationData.get("MainFrame.CheckedBalance")); //$NON-NLS-1$
		checkedBalance.setToolTipText(LocalizationData.get("MainFrame.CheckedBalance.ToolTip")); //$NON-NLS-1$
		balancePane.add(currentBalance);
		balancePane.add(finalBalance);
		balancePane.add(checkedBalance);
		bottomPane.add(balancePane, BorderLayout.CENTER);
		add(bottomPane, BorderLayout.SOUTH);
		
		acFilter.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				updateBalances();
			}
		});
		updateBalances();
	}
	
	@SuppressWarnings("serial")
	private final class ShowHideColumnAction extends AbstractAction {
		private int index;
		
		public ShowHideColumnAction(int i) {
			super(transactionTable.getModel().getColumnName(i));
			this.index = i;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean visible = !transactionTable.isColumnVisible(index);
			transactionTable.setColumnVisible(index, visible);
		}
	}

	@SuppressWarnings("serial")
	private final class DeploySubTransactionsAction extends AbstractAction {
		private boolean spread;
		private DeploySubTransactionsAction(String name, boolean spread) {
			super(name);
			this.spread = spread;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			SpreadableTableModel model = (SpreadableTableModel)transactionTable.getModel();
			for (int i = 0; i < transactionTable.getRowCount(); i++) {
				if (model.isSpreadable(i)) {
					model.setSpread(i, spread);
					int viewRow = transactionTable.convertRowIndexToView(i);
					if (spread) {
						transactionTable.setRowHeight(viewRow, transactionTable.getRowHeight() * model.getSpreadLines(i));
					} else {
						transactionTable.setRowHeight(viewRow, transactionTable.getRowHeight());
					}
				}
			}
		}
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
	
	private void updateBalances() {
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
