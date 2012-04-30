package net.yapbam.gui.statementview;

import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.util.JTableListener;
import net.yapbam.util.DateUtils;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Insets;
import javax.swing.JTable.PrintMode;

import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JSplitPane;
import javax.swing.border.Border;

public class StatementViewPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Cursor CHECK_CURSOR;
	private static final Cursor UNCHECK_CURSOR;

	private StatementSelectionPanel statementSelectionPanel;
	private JPanel statementPanel = null;
	private BalancePanel balancePanel = null;
	private JLabel detail = null;
	private StatementTable uncheckedTransactionsTable;
	private StatementTable transactionsTable = null;
	private CheckModePanel checkModePanel;
	
	private FilteredData data;
	CheckTransactionAction checkAction;
	private JPanel menuPanel;
	private JLabel lblNewLabel;
	private SplitPane splitPane;
	private JScrollPane notCheckedJScrollPane;
	private JLabel notCheckedColumns;
	
	static {
		URL imgURL = LocalizationData.class.getResource("images/checkCursor.png"); //$NON-NLS-1$
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		CHECK_CURSOR = toolkit.createCustomCursor(toolkit.getImage(imgURL), new Point(5, 13), "checked"); //$NON-NLS-1$
		imgURL = LocalizationData.class.getResource("images/uncheckCursor.png"); //$NON-NLS-1$
		UNCHECK_CURSOR = toolkit.createCustomCursor(toolkit.getImage(imgURL), new Point(8, 8), "unchecked"); //$NON-NLS-1$
	}
	
	/**
	 * This is the default constructor
	 */
	public StatementViewPanel(FilteredData data) {
		this.data = data;
		initialize();
		setTables();
	}
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weightx = 1.0;
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints.weightx = 1.0;
		add(getStatementSelectionPanel(), gridBagConstraints);
		GridBagConstraints gbc_checkModePanel = new GridBagConstraints();
		gbc_checkModePanel.insets = new Insets(0, 0, 5, 0);
		gbc_checkModePanel.weightx = 1.0;
		gbc_checkModePanel.anchor = GridBagConstraints.EAST;
		gbc_checkModePanel.gridx = 1;
		gbc_checkModePanel.gridy = 0;
		add(getCheckModePanel(), gbc_checkModePanel);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.weighty = 1.0;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridwidth = 0;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		add(getSplitPane(), gbc_splitPane);
	}

	private StatementSelectionPanel getStatementSelectionPanel() {
		if (statementSelectionPanel==null) {
			statementSelectionPanel = new StatementSelectionPanel(data);
			statementSelectionPanel.getStatementMenu().addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getStatementSelectionPanel().getSelectedStatement().getId()!=null) getCheckModePanel().setSelected(false);
					setTables();
				}
			});
		}
		return statementSelectionPanel;
	}
	
	/**
	 * This method initializes statementPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStatementPanel() {
		if (statementPanel == null) {
			statementPanel = new JPanel();
			Border border = BorderFactory.createLineBorder(Color.gray, 3);
			border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), border);
			statementPanel.setBorder(border);

			statementPanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc_balance = new GridBagConstraints();
			gbc_balance.fill = GridBagConstraints.HORIZONTAL;
			gbc_balance.weightx = 1.0;
			statementPanel.add(getBalancePanel(), gbc_balance);

			GridBagConstraints gbc_menuPanel = new GridBagConstraints();
			gbc_menuPanel.anchor = GridBagConstraints.NORTH;
			gbc_menuPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_menuPanel.gridx = 0;
			gbc_menuPanel.gridy = 1;
			statementPanel.add(getColumnsMenuPanel(), gbc_menuPanel);

			GridBagConstraints gbc_jScrollPane = new GridBagConstraints();
			gbc_jScrollPane.weighty = 1.0;
			gbc_jScrollPane.weightx = 1.0;
			gbc_jScrollPane.fill = GridBagConstraints.BOTH;
			gbc_jScrollPane.gridx = 0;
			gbc_jScrollPane.gridy = 2;
			JScrollPane jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTransactionsTable());
			statementPanel.add(jScrollPane, gbc_jScrollPane);

			GridBagConstraints gbc_detail = new GridBagConstraints();
			gbc_detail.insets = new Insets(0, 0, 5, 0);
			gbc_detail.weightx = 1.0;
			gbc_detail.fill = GridBagConstraints.HORIZONTAL;
			gbc_detail.gridx = 0;
			gbc_detail.gridy = 3;
			statementPanel.add(getDetail(), gbc_detail);
		}
		return statementPanel;
	}

	/**
	 * This method initializes topPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private BalancePanel getBalancePanel() {
		if (balancePanel == null) {
			balancePanel = new BalancePanel();
		}
		return balancePanel;
	}

	/**
	 * This method initializes bottomPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JLabel getDetail() {
		if (detail == null) {
			detail = new JLabel();
			detail.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return detail;
	}

	/**
	 * This method initializes transactionsTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	StatementTable getTransactionsTable() {
		if (transactionsTable == null) {
			transactionsTable = new StatementTable(data);
			Action edit = new EditTransactionAction(transactionsTable);
			Action delete = new DeleteTransactionAction(transactionsTable);
			Action duplicate = new DuplicateTransactionAction(transactionsTable);
			checkAction = new CheckTransactionAction(getCheckModePanel(), transactionsTable, false);
			new MyListener(getTransactionsTable(), new Action[]{edit, duplicate, delete}, edit, checkAction);
		}
		return transactionsTable;
	}
	
	private StatementTable getUncheckedTransactionsTable() {
		if (uncheckedTransactionsTable==null) {
			uncheckedTransactionsTable = new StatementTable(data);
			Action edit = new EditTransactionAction(uncheckedTransactionsTable);
			Action delete = new DeleteTransactionAction(uncheckedTransactionsTable);
			Action duplicate = new DuplicateTransactionAction(uncheckedTransactionsTable);
			checkAction = new CheckTransactionAction(getCheckModePanel(), uncheckedTransactionsTable, true);
			new MyListener(getUncheckedTransactionsTable(), new Action[]{edit, duplicate, delete}, edit, checkAction);
		}
		return uncheckedTransactionsTable;
	}

	private CheckModePanel getCheckModePanel() {
		if (checkModePanel == null) {
			checkModePanel = new CheckModePanel();
			checkModePanel.setBackground(Color.YELLOW);
			checkModePanel.addPropertyChangeListener(CheckModePanel.IS_OK_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					getUncheckedTransactionsTable().setCursor(checkModePanel.isOk() ? CHECK_CURSOR : Cursor.getDefaultCursor());
				}
			});
			checkModePanel.addPropertyChangeListener(CheckModePanel.EDITED_STATEMENT_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					setTables(); 
					getTransactionsTable().setCursor(checkModePanel.getEditedStatement()!=null ? UNCHECK_CURSOR : Cursor.getDefaultCursor());
				}
			});
		}
		return checkModePanel;
	}
	
	private static class MyListener extends JTableListener {
		private Action checkAction;
		public MyListener(JTable jTable, Action[] actions, Action defaultAction, Action checkAction) {
			super(jTable, actions, defaultAction);
			this.checkAction = checkAction;
		}

		@Override
		protected void fillPopUp(JPopupMenu popup) {
			if (checkAction.isEnabled()) {
				popup.add(new JMenuItem(checkAction));
				popup.addSeparator();
			}
			super.fillPopUp(popup);
		}

		@Override
		protected Action getDoubleClickAction() {
			if (checkAction.isEnabled()) {
				return checkAction;
			} else {
				return super.getDoubleClickAction();
			}
		}
	}

	public Printable getPrintable() {
		return transactionsTable.getPrintable(PrintMode.FIT_WIDTH, null, null);
	}
	
	private JPanel getColumnsMenuPanel() {
		if (menuPanel == null) {
			menuPanel = new JPanel();
			menuPanel.setBorder(null);
			GridBagLayout gbl_menuPanel = new GridBagLayout();
			menuPanel.setLayout(gbl_menuPanel);
			GridBagConstraints gbc_ColumnsMenu = new GridBagConstraints();
			gbc_ColumnsMenu.weightx = 1.0;
			gbc_ColumnsMenu.insets = new Insets(0, 0, 0, 5);
			gbc_ColumnsMenu.anchor = GridBagConstraints.EAST;
			gbc_ColumnsMenu.gridx = 0;
			gbc_ColumnsMenu.gridy = 0;
			menuPanel.add(getColumnsMenu(), gbc_ColumnsMenu);
		}
		return menuPanel;
	}
	
	private JLabel getColumnsMenu() {
		if (lblNewLabel == null) {
			lblNewLabel = getTransactionsTable().getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		}
		return lblNewLabel;
	}

	private SplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new SplitPane(JSplitPane.VERTICAL_SPLIT, true);
			
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc_notCheckedColumns = new GridBagConstraints();
			gbc_notCheckedColumns.anchor = GridBagConstraints.EAST;
			gbc_notCheckedColumns.gridx = 0;
			gbc_notCheckedColumns.gridy = 0;
			panel.add(getNotCheckedColumns(), gbc_notCheckedColumns);

			notCheckedJScrollPane = new JScrollPane();
			notCheckedJScrollPane.setViewportView(getUncheckedTransactionsTable());
			GridBagConstraints gbc_notCheckedJScrollPane = new GridBagConstraints();
			gbc_notCheckedJScrollPane.weighty = 1.0;
			gbc_notCheckedJScrollPane.weightx = 1.0;
			gbc_notCheckedJScrollPane.fill = GridBagConstraints.BOTH;
			gbc_notCheckedJScrollPane.gridy = 1;
			gbc_notCheckedJScrollPane.gridx = 0;
			panel.add(notCheckedJScrollPane, gbc_notCheckedJScrollPane);
			splitPane.setTopComponent(panel);
			splitPane.setBottomComponent(getStatementPanel());
			setTables();
		}
		return splitPane;
	}
	
	private JLabel getNotCheckedColumns() {
		if (notCheckedColumns == null) {
			notCheckedColumns = getUncheckedTransactionsTable().getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		}
		return notCheckedColumns;
	}
	
	private void setTables() {
		boolean checkMode = getCheckModePanel().getStatement()!=null;
		getNotCheckedColumns().setVisible(checkMode);
		notCheckedJScrollPane.setVisible(checkMode);
		getUncheckedTransactionsTable().setVisible(checkMode);
		if (checkMode) {
			if (!getSplitPane().isDividerVisible()) {
				getSplitPane().setDividerVisible(true);
				getSplitPane().setDividerLocation(0.33);
			}
		} else {
			getSplitPane().setDividerLocation(0.0);
			getSplitPane().setDividerVisible(false);
		}
		setStatements();
	}

	private void setStatements() {
		Statement statement = getStatementSelectionPanel().getSelectedStatement();
		boolean visible = statement!=null;
		boolean checkModeAvailable = false;
		if (visible) {
			DecimalFormat ci = LocalizationData.getCurrencyInstance();
			checkModeAvailable = statement.getId()==null;
			if (getCheckModePanel().isSelected()) {
				getUncheckedTransactionsTable().setTransactions(getTransactions(statementSelectionPanel.getAccount(), null));
				double uncheckedStart = statement.getStartBalance();
				statement = getStatementSelectionPanel().getStatement(getCheckModePanel().getStatement());
				if (statement==null) {
					statement = new Statement(getCheckModePanel().getStatement());
					statement.setStartBalance(uncheckedStart);
				}
			}
			getBalancePanel().setStatementId(checkModePanel.getStatement());
			getBalancePanel().setStart(MessageFormat.format(LocalizationData.get("StatementView.startBalance"), ci.format(statement.getStartBalance()))); //$NON-NLS-1$
			getBalancePanel().setEnd(MessageFormat.format(LocalizationData.get("StatementView.endBalance"), ci.format(statement.getEndBalance()))); //$NON-NLS-1$
			getDetail().setText(MessageFormat.format(LocalizationData.get("StatementView.statementSummary"), statement.getNbTransactions(), //$NON-NLS-1$
					ci.format(statement.getNegativeBalance()), ci.format(statement.getPositiveBalance())));
			getTransactionsTable().setTransactions(getTransactions(statementSelectionPanel.getAccount(), statement.getId()));
		}
		getBalancePanel().setVisible(visible);
		getDetail().setVisible(visible);
		getTransactionsTable().setVisible(visible);
		getCheckModePanel().setVisible(checkModeAvailable);
	}
 
	private Transaction[] getTransactions(Account account, String statementId) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		for (int i = 0; i < data.getGlobalData().getTransactionsNumber(); i++) {
			Transaction transaction = data.getGlobalData().getTransaction(i);
			if (transaction.getAccount().equals(account) && NullUtils.areEquals(statementId, transaction.getStatement())) {
				transactions.add(transaction);
			}
		}
		Collections.sort(transactions, new Comparator<Transaction>() {
			@Override
			public int compare(Transaction o1, Transaction o2) {
				int result = DateUtils.dateToInteger(o1.getValueDate())-DateUtils.dateToInteger(o2.getValueDate());
				if (result == 0) result = DateUtils.dateToInteger(o1.getDate())-DateUtils.dateToInteger(o2.getDate());
				return result;
			}
		});
		return transactions.toArray(new Transaction[transactions.size()]);
	}
}
