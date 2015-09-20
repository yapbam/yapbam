package net.yapbam.gui.transactiontable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import com.fathzer.soft.ajlib.swing.table.JTableListener;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AccountSelector;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.ConvertToPeriodicalTransactionAction;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.widget.JLabelMenu;

@SuppressWarnings("serial")
public class TransactionsPlugInPanel extends JPanel {
	private static final long serialVersionUID = 1L;
		
	private FilteredData data;
	private AccountSelector accountSelector;

	private TransactionTable transactionTable;
	private TransactionEditionButtonsPanel buttons;
	private BalanceReportPanel balances;
	
	public TransactionsPlugInPanel(FilteredData data, AccountSelector accountSelector) {
		super();
		this.data = data;
		this.accountSelector = accountSelector;
		this.initialize();
	}
	
	private void initialize() {
		setLayout(new BorderLayout());
       
		JScrollPane scrollPane = new JScrollPane(getTransactionTable(), ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(getMenusPanel(), BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(getSouthPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel getSouthPanel() {
		JPanel result = new JPanel();
		result.setLayout(new GridBagLayout());
		GridBagConstraints cButtons = new GridBagConstraints();
		cButtons.fill = GridBagConstraints.HORIZONTAL;
		cButtons.gridy = 1;
		cButtons.weightx = 1.0;
		result.add(getButtons(), cButtons);
		GridBagConstraints cBalance = new GridBagConstraints();
		cBalance.fill = GridBagConstraints.HORIZONTAL;
		cBalance.weightx = 1.0;
		cBalance.gridy = 2;
		result.add(getBalanceReportPanel(),cBalance);
		GridBagConstraints cStat = new GridBagConstraints();
		cStat.fill = GridBagConstraints.HORIZONTAL;
		cStat.weightx = 1.0;
		cStat.gridy = 0;
		result.add(new StatPanel(data), cStat);
		return result;
	}
	
	private JPanel getMenusPanel() {
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
		JLabel columns = new FriendlyTable.ShowHideColumsMenu(getTransactionTable(), LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		columns.setToolTipText(LocalizationData.get("MainFrame.showColumns.ToolTip")); //$NON-NLS-1$
		columns.setBorder(border);
		menus.add(columns, BorderLayout.EAST);
		return menus;
	}
	
	private TransactionEditionButtonsPanel getButtons() {
		if (buttons==null) {
			buttons = new TransactionEditionButtonsPanel(getTransactionTable(), data, accountSelector);
		}
		return buttons;
	}

	private final class DeploySubTransactionsAction extends AbstractAction {
		private boolean spread;
		private DeploySubTransactionsAction(String name, boolean spread) {
			super(name);
			this.spread = spread;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			SpreadableTableModel model = (SpreadableTableModel)getTransactionTable().getModel();
			for (int i = 0; i < getTransactionTable().getRowCount(); i++) {
				if (model.isSpreadable(i)) {
					model.setSpread(i, spread);
					int viewRow = getTransactionTable().convertRowIndexToView(i);
					if (spread) {
						getTransactionTable().setRowHeight(viewRow, getTransactionTable().getRowHeight() * model.getSpreadLines(i));
					} else {
						getTransactionTable().setRowHeight(viewRow, getTransactionTable().getRowHeight());
					}
				}
			}
		}
	}

	TransactionTable getTransactionTable() {
		if (transactionTable==null) {
			transactionTable = new TransactionTable(data);
			Action editAction = getButtons().getEditButton().getAction();
			JTableListener listener = new JTableListener(new Action[] {
					editAction,
					getButtons().getDuplicateButton().getAction(),
					getButtons().getDeleteButton().getAction(),
					null,
					new ConvertToPeriodicalTransactionAction(transactionTable) }, editAction);
			transactionTable.addMouseListener(listener);
		}
		return transactionTable;
	}
	
	BalanceReportPanel getBalanceReportPanel() {
		if (balances==null) {
			balances = new BalanceReportPanel(data==null?null:data.getBalanceData());
		}
		return balances;
	}
}
