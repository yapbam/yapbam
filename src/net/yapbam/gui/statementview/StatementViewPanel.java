package net.yapbam.gui.statementview;

import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
import net.yapbam.data.event.TransactionsRemovedEvent;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.actions.TransactionSelector;
import net.yapbam.gui.util.JTableListener;
import net.yapbam.gui.widget.CoolJComboBox;
import net.yapbam.util.DateUtils;
import net.yapbam.util.NullUtils;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Insets;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTable.PrintMode;

import java.awt.Font;
import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class StatementViewPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Cursor CHECK_CURSOR;

	private JPanel selectionPanel = null;
	private CoolJComboBox accountMenu = null;
	private CoolJComboBox statementMenu = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JPanel statementPanel = null;
	private JLabel startBalance = null;
	private JPanel topPanel = null;
	private JPanel bottomPanel = null;
	private JLabel endBalance = null;
	private JLabel detail = null;
	private JScrollPane jScrollPane = null;
	private StatementTable transactionsTable = null;
	private CheckModePanel checkModePanel;
	
	private FilteredData data;
	private Statement[] statements;
	private JLabel label;
	private JLabel columnsMenu;
	CheckTransactionAction checkAction;
	
	static {
		URL imgURL = LocalizationData.class.getResource("images/checkCursor.png"); //$NON-NLS-1$
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		CHECK_CURSOR = toolkit.createCustomCursor(toolkit.getImage(imgURL), new Point(5, 13), "checked"); //$NON-NLS-1$
	}
	
	/**
	 * This is the default constructor
	 */
	public StatementViewPanel(FilteredData data) {
		this.data = data;
		initialize();
		this.data.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				GlobalData global = StatementViewPanel.this.data.getGlobalData();
				if (event instanceof EverythingChangedEvent) {
					init();
				} else if (event instanceof AccountAddedEvent) {
					if (global.getAccountsNumber()==1) {
						// If there was no account before this one
						init();
					} else {
						getAccountMenu().addItem(((AccountAddedEvent)event).getAccount().getName());
					}
				} else if (event instanceof AccountRemovedEvent) {
					String accountName = ((AccountRemovedEvent)event).getRemoved().getName();
					if (NullUtils.areEquals(getAccountMenu().getSelectedItem(), accountName)) {
						// If the removed account is the current one, reset default settings
						init();
					} else {
						// simply remove the account in the menu
						getAccountMenu().removeItem(accountName);
					}
				} else if (event instanceof AccountPropertyChangedEvent) {
					String property = ((AccountPropertyChangedEvent)event).getProperty();
					if (property.equals(AccountPropertyChangedEvent.INITIAL_BALANCE)) {
						Account account = ((AccountPropertyChangedEvent)event).getAccount();
						if (account.getName().equals(getAccountMenu().getSelectedItem())) {
							refresh();
						}
					} else if (property.equals(AccountPropertyChangedEvent.NAME)) {
						// An account has changed its name
						// Change it in the menu
						getAccountMenu().setActionEnabled(false);
						String old = (String) ((AccountPropertyChangedEvent)event).getOldValue();
						int index = getAccountMenu().getSelectedIndex();
						for (int i = 0; i < getAccountMenu().getItemCount(); i++) {
							if (getAccountMenu().getItemAt(i).equals(old)) {
								getAccountMenu().removeItemAt(i);
								getAccountMenu().insertItemAt(((AccountPropertyChangedEvent)event).getNewValue(), i);
								break;
							}
						}
						// Restore the selected index
						getAccountMenu().setSelectedIndex(index);
						getAccountMenu().setActionEnabled(true);
					}
				} else if (event instanceof TransactionsAddedEvent) {
					Transaction[] ts = ((TransactionsAddedEvent)event).getTransactions();
					refreshIfNeeded(ts);
				} else if (event instanceof TransactionsRemovedEvent) {
					Transaction[] t = ((TransactionsRemovedEvent)event).getRemoved();
					refreshIfNeeded(t);
				}
			}

			private void refreshIfNeeded(Transaction[] ts) {
				boolean refresh = false;
				for (int i = 0; i < ts.length; i++) {
					if (ts[i].getAccount().getName().equals(getAccountMenu().getSelectedItem())) {
						refresh = true;
						break;
					}
				}
				if (refresh) {
					refresh();
				}
			}
		});
		init();
	}
	
	private void init() {
		GlobalData global = data.getGlobalData();
		if (global.getAccountsNumber()==0) {
			accountMenu.setSelectedIndex(-1);
			accountMenu.removeAllItems();
			accountMenu.setEnabled(false);
		} else {
			accountMenu.setEnabled(true);
			this.accountMenu.setActionEnabled(false);
			this.accountMenu.removeAllItems();
			for (int i = 0; i < global.getAccountsNumber(); i++) {
				String accountName = global.getAccount(i).getName();
				accountMenu.addItem(accountName);
			}
			this.accountMenu.setActionEnabled(true);
			accountMenu.setSelectedIndex(0);
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getSelectionPanel(), BorderLayout.NORTH);
		this.add(getStatementPanel(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes selectionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSelectionPanel() {
		if (selectionPanel == null) {
			GridBagConstraints gbc_checkModePanel = new GridBagConstraints();
			gbc_checkModePanel.anchor = GridBagConstraints.EAST;
			gbc_checkModePanel.gridx = 5;
			gbc_checkModePanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_checkModePanel.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText(LocalizationData.get("TransactionDialog.statement")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 3;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 0.0D;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.weightx = 0.0D;
			selectionPanel = new JPanel();
			selectionPanel.setLayout(new GridBagLayout());
			selectionPanel.add(jLabel, gridBagConstraints2);
			selectionPanel.add(getAccountMenu(), gridBagConstraints1);
			selectionPanel.add(jLabel1, gridBagConstraints3);
			selectionPanel.add(getStatementMenu(), gridBagConstraints);
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.weightx = 1.0;
			gbc_label.insets = new Insets(0, 0, 0, 0);
			gbc_label.gridx = 4;
			gbc_label.gridy = 0;
			selectionPanel.add(getLabel(), gbc_label);
			selectionPanel.add(getCheckModePanel(), gbc_checkModePanel);
		}
		return selectionPanel;
	}

	/**
	 * This method initializes accountMenu	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private CoolJComboBox getAccountMenu() {
		if (accountMenu == null) {
			accountMenu = new CoolJComboBox();
			accountMenu.setToolTipText(LocalizationData.get("StatementView.accountMenu.tooltip")); //$NON-NLS-1$
			accountMenu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					refresh();
				}
			});
		}
		return accountMenu;
	}
	
	private void refresh() {
		int accountIndex = accountMenu.getSelectedIndex();
		statementMenu.setActionEnabled(false);
//		String lastSelectedStatement = (String) (statementMenu.getSelectedIndex()==0?null:statementMenu.getSelectedItem());
		String lastSelectedStatement = (String) ((statements==null)||(statementMenu.getSelectedIndex()<0)?null:statementMenu.getSelectedItem());
		statementMenu.removeAllItems();
		if (accountIndex < 0) {
			statements = null;
			statementMenu.setActionEnabled(true);
			statementMenu.setSelectedIndex(-1);
			statementMenu.setEnabled(false);
		} else {
			statements = new StatementBuilder(data.getGlobalData(), data.getGlobalData().getAccount(accountIndex)).getStatements();
			for (int i = 0; i < statements.length; i++) {
				String id = statements[statements.length - 1 - i].getId();
				statementMenu.addItem(id == null ? LocalizationData.get("StatementView.notChecked") : id); //$NON-NLS-1$
			}
			statementMenu.setActionEnabled(true);
			statementMenu.setEnabled(statements.length > 0);
			if ((lastSelectedStatement!=null) && (statementMenu.contains(lastSelectedStatement))){
				statementMenu.setSelectedItem(lastSelectedStatement);
			} else {
				statementMenu.setSelectedIndex(statements.length > 0 ? 0 : -1);
			}
		}
	}
	
	/**
	 * This method initializes statementMenu	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getStatementMenu() {
		if (statementMenu == null) {
			statementMenu = new CoolJComboBox();
			statementMenu.setToolTipText(LocalizationData.get("StatementView.statementMenu.statementMenu.tooltip")); //$NON-NLS-1$
			statementMenu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					boolean visible = (statements!=null) && (statementMenu.getSelectedIndex()>=0);
					boolean checkModeAvailable = false;
					if (visible) {
						Statement statement = statements[statements.length-1-statementMenu.getSelectedIndex()];
						DecimalFormat ci = LocalizationData.getCurrencyInstance();
						startBalance.setText(MessageFormat.format(LocalizationData.get("StatementView.startBalance"), ci.format(statement.getStartBalance()))); //$NON-NLS-1$
						endBalance.setText(MessageFormat.format(LocalizationData.get("StatementView.endBalance"), ci.format(statement.getEndBalance()))); //$NON-NLS-1$
						detail.setText(MessageFormat.format(LocalizationData.get("StatementView.statementSummary"), statement.getNbTransactions(), //$NON-NLS-1$
								ci.format(statement.getNegativeBalance()), ci.format(statement.getPositiveBalance())));
						getTransactionsTable().setTransactions(getTransactions(data.getGlobalData().getAccount(accountMenu.getSelectedIndex()), statement.getId()));
						checkModeAvailable = statement.getId()==null;
					}
					startBalance.setVisible(visible);
					endBalance.setVisible(visible);
					detail.setVisible(visible);
					transactionsTable.setVisible(visible);
					getCheckModePanel().setVisible(checkModeAvailable);
				}
			});
		}
		return statementMenu;
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

	/**
	 * This method initializes statementPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStatementPanel() {
		if (statementPanel == null) {
			statementPanel = new JPanel();
			statementPanel.setLayout(new BorderLayout());
			statementPanel.add(getTopPanel(), BorderLayout.NORTH);
			statementPanel.add(getBottomPanel(), BorderLayout.SOUTH);
			statementPanel.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return statementPanel;
	}

	/**
	 * This method initializes topPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTopPanel() {
		if (topPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.anchor = GridBagConstraints.EAST;
			gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridy = 0;
			topPanel = new JPanel();
			startBalance = new JLabel();
			startBalance.setHorizontalTextPosition(SwingConstants.LEADING);
			startBalance.setFont(new Font("Dialog", Font.PLAIN, 14)); //$NON-NLS-1$
			endBalance = new JLabel();
			endBalance.setHorizontalAlignment(SwingConstants.RIGHT);
			endBalance.setFont(new Font("Dialog", Font.PLAIN, 14)); //$NON-NLS-1$
			topPanel.setLayout(new GridBagLayout());
			topPanel.setBackground(Color.white);
			topPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
			topPanel.add(startBalance, gridBagConstraints4);
			topPanel.add(endBalance, gridBagConstraints6);
		}
		return topPanel;
	}

	/**
	 * This method initializes bottomPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.insets = new Insets(5, 0, 5, 0);
			gridBagConstraints5.gridy = 0;
			detail = new JLabel();
			GridBagConstraints gbc_columnsMenu = new GridBagConstraints();
			gbc_columnsMenu.anchor = GridBagConstraints.WEST;
			gbc_columnsMenu.gridx = 0;
			gbc_columnsMenu.gridy = 0;
			gbc_columnsMenu.insets = new Insets(0, 5, 0, 0);
			bottomPanel.add(getColumnsMenu(), gbc_columnsMenu);
			bottomPanel.add(detail, gridBagConstraints5);
		}
		return bottomPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTransactionsTable());
		}
		return jScrollPane;
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
			checkAction = new CheckTransactionAction(getCheckModePanel(), transactionsTable);
			new MyListener(getTransactionsTable(), new Action[]{edit, duplicate, delete}, edit, checkAction);
		}
		return transactionsTable;
	}
	
	private CheckModePanel getCheckModePanel() {
		if (checkModePanel == null) {
			checkModePanel = new CheckModePanel();
			checkModePanel.addPropertyChangeListener(CheckModePanel.IS_OK_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Cursor cursor = checkModePanel.isOk() ? CHECK_CURSOR : Cursor.getDefaultCursor();
					getTransactionsTable().setCursor(cursor);
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
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel(" "); //$NON-NLS-1$
		}
		return label;
	}
	private JLabel getColumnsMenu() {
		if (columnsMenu == null) {
			columnsMenu = getTransactionsTable().getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		}
		return columnsMenu;
	}
}
