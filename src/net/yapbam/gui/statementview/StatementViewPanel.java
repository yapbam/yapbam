package net.yapbam.gui.statementview;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;

import net.yapbam.data.Account;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.Insets;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.BorderFactory;

import java.awt.Font;

public class StatementViewPanel extends JPanel {
	private static final long serialVersionUID = 1L;
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
	private JTable transactionsTable = null;
	private TransactionsTableModel model;
	private JLabel jLabel2 = null;
	
	private GlobalData data;
	private Statement[] statements;
	
	/**
	 * This is the default constructor
	 */
	public StatementViewPanel() {
		super();
		initialize();
	}

	/**
	 * This is the default constructor
	 */
	public StatementViewPanel(GlobalData data) {
		this();
		this.data = data;
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (event instanceof EverythingChangedEvent) {
					init();
				} else if (event instanceof AccountAddedEvent) {
					if (StatementViewPanel.this.data.getAccountsNumber()==1) {
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
					System.out.println ("Account removed");
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
					for (int i = 0; i < ts.length; i++) {
						if (ts[i].getAccount().getName().equals(getAccountMenu().getSelectedItem())) {
							refresh();
							break;
						}
					}
				} else if (event instanceof TransactionsRemovedEvent) {
					boolean refresh = false;
					Transaction[] t = ((TransactionsRemovedEvent)event).getRemoved();
					for (int i = 0; i < t.length; i++) {
						if (t[i].getAccount().getName().equals(getAccountMenu().getSelectedItem())) {
							refresh = true;
							break;
						}
					}
					if (refresh) {
						refresh();
					}
				}
			}
		});
		init();
	}
	
	private void init() {
		if (data.getAccountsNumber()==0) {
			accountMenu.setSelectedIndex(-1);
			accountMenu.removeAllItems();
			accountMenu.setEnabled(false);
		} else {
			accountMenu.setEnabled(true);
			this.accountMenu.setActionEnabled(false);
			this.accountMenu.removeAllItems();
			for (int i = 0; i < data.getAccountsNumber(); i++) {
				String accountName = data.getAccount(i).getName();
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
		this.setSize(300, 200);
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
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 4;
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.gridy = 0;
			jLabel2 = new JLabel();
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
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridx = 3;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 0.0D;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new Insets(5, 0, 5, 5);
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.weightx = 0.0D;
			selectionPanel = new JPanel();
			selectionPanel.setLayout(new GridBagLayout());
			selectionPanel.add(jLabel, gridBagConstraints2);
			selectionPanel.add(getAccountMenu(), gridBagConstraints1);
			selectionPanel.add(jLabel1, gridBagConstraints3);
			selectionPanel.add(getStatementMenu(), gridBagConstraints);
			selectionPanel.add(jLabel2, gridBagConstraints11);
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
		String lastSelectedStatement = (String) (statementMenu.getSelectedIndex()==0?null:statementMenu.getSelectedItem());
		statementMenu.removeAllItems();
		if (accountIndex < 0) {
			statements = null;
			statementMenu.setActionEnabled(true);
			statementMenu.setSelectedIndex(-1);
			statementMenu.setEnabled(false);
		} else {
			statements = new StatementBuilder(data, data.getAccount(accountIndex)).getStatements();
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
					if (visible) {
						Statement statement = statements[statements.length-1-statementMenu.getSelectedIndex()];
						DecimalFormat ci = LocalizationData.getCurrencyInstance();
						startBalance.setText(MessageFormat.format(LocalizationData.get("StatementView.startBalance"), ci.format(statement.getStartBalance()))); //$NON-NLS-1$
						endBalance.setText(MessageFormat.format(LocalizationData.get("StatementView.endBalance"), ci.format(statement.getEndBalance()))); //$NON-NLS-1$
						detail.setText(MessageFormat.format(LocalizationData.get("StatementView.statementSummary"), statement.getNbTransactions(), //$NON-NLS-1$
								ci.format(statement.getNegativeBalance()), ci.format(statement.getPositiveBalance())));
						model.setTransactions(getTransactions(data.getAccount(accountMenu.getSelectedIndex()), statement.getId()));
					}
					startBalance.setVisible(visible);
					endBalance.setVisible(visible);
					detail.setVisible(visible);
					transactionsTable.setVisible(visible);
				}
			});
		}
		return statementMenu;
	}

	private Transaction[] getTransactions(Account account, String statementId) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		for (int i = 0; i < data.getTransactionsNumber(); i++) {
			Transaction transaction = data.getTransaction(i);
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
			startBalance.setFont(new Font("Dialog", Font.PLAIN, 14));
			endBalance = new JLabel();
			endBalance.setHorizontalAlignment(SwingConstants.RIGHT);
			endBalance.setFont(new Font("Dialog", Font.PLAIN, 14));
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
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints5.gridy = 1;
			detail = new JLabel();
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new GridBagLayout());
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
	JTable getTransactionsTable() {
		if (transactionsTable == null) {
			transactionsTable = new JTable();
			this.model = new TransactionsTableModel(transactionsTable, new Transaction[0]);
			transactionsTable.setModel(this.model);
			transactionsTable.setDefaultRenderer(Object.class, new CellRenderer());
			transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			TransactionSelector selector = new TransactionSelector() {
				@Override
				public Transaction getSelectedTransaction() {
					int index = getTransactionsTable().getSelectedRow();
					return (index>=0) ? model.getTransactions()[index]:null;
				}
				
				@Override
				public GlobalData getGlobalData() {
					return data;
				}
			};
			Action edit = new EditTransactionAction(selector);
			Action delete = new DeleteTransactionAction(selector);
			Action duplicate = new DuplicateTransactionAction(selector);
			new JTableListener(transactionsTable, new Action[]{edit, duplicate, delete}, edit);
		}
		return transactionsTable;
	}
}
