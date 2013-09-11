package net.yapbam.gui.transactiontable;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.border.EmptyBorder;

import com.fathzer.soft.ajlib.swing.table.JTableListener;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.ConvertToPeriodicalTransactionAction;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.actions.NewTransactionAction;
import net.yapbam.gui.widget.JLabelMenu;

public class TransactionsPlugInPanel extends JPanel {
	private static final long serialVersionUID = 1L;
		
	private TransactionTable transactionTable;
	private BalanceReportPanel balances;

	@SuppressWarnings("serial")
	public TransactionsPlugInPanel(FilteredData data) {
		super(new BorderLayout());

		balances = new BalanceReportPanel(data==null?null:data.getBalanceData());

		transactionTable = new TransactionTable(data);
		JScrollPane scrollPane = new JScrollPane(transactionTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel buttons = new JPanel(new GridBagLayout());
		EditTransactionAction editTransactionAction = new EditTransactionAction(transactionTable);
		DuplicateTransactionAction duplicateTransactionAction = new DuplicateTransactionAction(transactionTable);
		DeleteTransactionAction deleteTransactionAction = new DeleteTransactionAction(transactionTable);
		transactionTable.addMouseListener(new JTableListener(new Action[] { editTransactionAction, duplicateTransactionAction,
				deleteTransactionAction, null, new ConvertToPeriodicalTransactionAction(transactionTable) }, editTransactionAction));
       
		final JButton newTransactionButton = new JButton(new NewTransactionAction(data, transactionTable,false));
		newTransactionButton.setText(LocalizationData.get("GenericButton.new")); //$NON-NLS-1$
		final JButton massNewTransactionButton = new JButton(new NewTransactionAction(data, transactionTable,true));
		massNewTransactionButton.setText(LocalizationData.get("MainMenu.Transactions.NewMultiple")); //$NON-NLS-1$
		final JButton editTransactionButton = new JButton(editTransactionAction);
		editTransactionButton.setText(LocalizationData.get("GenericButton.edit")); //$NON-NLS-1$
		final JButton duplicateTransactionButton = new JButton(duplicateTransactionAction);
		duplicateTransactionButton.setText(LocalizationData.get("GenericButton.duplicate")); //$NON-NLS-1$
		final JButton deleteTransactionButton = new JButton(deleteTransactionAction);
		deleteTransactionButton.setText(LocalizationData.get("GenericButton.delete")); //$NON-NLS-1$
		GridBagConstraints c = new GridBagConstraints();
		buttons.add(newTransactionButton, c);
		c.gridx = 1;
		buttons.add(massNewTransactionButton, c);
		c.gridx = 2;
		buttons.add(editTransactionButton, c);
		c.gridx = 3;
		buttons.add(duplicateTransactionButton, c);
		c.gridx = 4;
		c.anchor = GridBagConstraints.WEST;
		buttons.add(deleteTransactionButton, c);
		final JButton periodicalTransactionsButton = new JButton(new GeneratePeriodicalTransactionsAction(data, false));
		c.gridx = 5;
		c.weightx = 1;
		c.anchor = GridBagConstraints.EAST;
		buttons.add(periodicalTransactionsButton, c);

		JPanel menus = new JPanel(new BorderLayout());
		menus.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		menus.setBackground(new Color(240, 240, 240));
		JLabel deploy = new JLabelMenu(LocalizationData.get("MainFrame.ShowSubtransactions")) { //$NON-NLS-1$
			@Override
			protected void fillPopUp(JPopupMenu popup) {
				popup.add(new DeploySubTransactionsAction(LocalizationData.get("MainFrame.ShowSubtransactions.All"), true)); //$NON-NLS-1$
				popup.add(new DeploySubTransactionsAction(LocalizationData.get("MainFrame.ShowSubtransactions.None"), false)); //$NON-NLS-1$
			}
		};
		deploy.setToolTipText(LocalizationData.get("MainFrame.ShowSubtransactions.ToolTip")); //$NON-NLS-1$
		EmptyBorder border = new EmptyBorder(0, 5, 0, 5);
		deploy.setBorder(border);
		menus.add(deploy, BorderLayout.WEST);
		JLabel columns = transactionTable.getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		columns.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip")); //$NON-NLS-1$
		columns.setBorder(border);
		menus.add(columns, BorderLayout.EAST);

		JPanel extraPane = new JPanel(new BorderLayout());
		extraPane.add(buttons, BorderLayout.NORTH);
		extraPane.add(balances, BorderLayout.SOUTH);
		add(menus, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(extraPane, BorderLayout.SOUTH);
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
	
	BalanceReportPanel getBalanceReportPanel() {
		return balances;
	}
}
