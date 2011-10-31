package net.yapbam.gui.transactiontable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.ConvertToPeriodicalTransactionAction;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.DynamicNewTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.util.JTableListener;
import net.yapbam.gui.widget.JLabelMenu;

public class TransactionsPlugInPanel extends JPanel {
	private static final long serialVersionUID = 1L;
		
	TransactionTable transactionTable;
	private BalanceReportPanel balancePanel;

	@SuppressWarnings("serial")
	public TransactionsPlugInPanel(FilteredData data) {
		super(new BorderLayout());
		setOpaque(true);

		transactionTable = new TransactionTable(data);

		EditTransactionAction editTransactionAction = new EditTransactionAction(transactionTable);
		DuplicateTransactionAction duplicateTransactionAction = new DuplicateTransactionAction(transactionTable);
		DeleteTransactionAction deleteTransactionAction = new DeleteTransactionAction(transactionTable);
		new JTableListener(transactionTable, new Action[] { editTransactionAction, duplicateTransactionAction,
				deleteTransactionAction, null, new ConvertToPeriodicalTransactionAction(transactionTable) }, editTransactionAction);
        
		JPanel topPanel = new JPanel(new GridBagLayout());
		String noText = ""; //$NON-NLS-1$
		JButton newTransactionButton = new JButton(new DynamicNewTransactionAction(transactionTable.getFilteredData()));
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
		c.weightx = 1;
		c.anchor = GridBagConstraints.WEST;
		topPanel.add(deleteTransactionButton, c);

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
		JLabel columns = transactionTable.getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		columns.setBorder(border);
		columns.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip")); //$NON-NLS-1$
		menus.add(columns, BorderLayout.SOUTH);
		bottomPane.add(menus, BorderLayout.WEST);
		balancePanel = new BalanceReportPanel(data.getBalanceData());
		bottomPane.add(balancePanel, BorderLayout.CENTER);
		add(bottomPane, BorderLayout.SOUTH);
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

	TransactionTable getTransactionTable() {
		return transactionTable;
	}
}
