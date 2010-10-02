package net.yapbam.statementview;

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
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.CoolJComboBox;
import net.yapbam.util.DateUtils;
import net.yapbam.util.NullUtils;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.Insets;

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
	
	private GlobalData data;
	private Statement[] statements;
	private TransactionsTableModel model;
	private JLabel jLabel2 = null;
	
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
				accountMenu.addItem(data.getAccount(i).getName());
			}
			accountMenu.setSelectedIndex(-1);
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
			gridBagConstraints3.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints3.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText(LocalizationData.get("TransactionDialog.statement")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints2.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridx = 3;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints.weightx = 0.0D;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.weightx = 1.0;
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
	private JComboBox getAccountMenu() {
		if (accountMenu == null) {
			accountMenu = new CoolJComboBox();
			accountMenu.setToolTipText(LocalizationData.get("StatementView.accountMenu.tooltip")); //$NON-NLS-1$
			accountMenu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int accountIndex = accountMenu.getSelectedIndex();
					statementMenu.setActionEnabled(false);
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
						statementMenu.setSelectedIndex(statements.length > 0 ? 0 : -1);
					}
				}
			});
		}
		return accountMenu;
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
			startBalance = new JLabel();
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
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 1;
			topPanel = new JPanel();
			topPanel.setLayout(new GridBagLayout());
			topPanel.add(startBalance, gridBagConstraints4);
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
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			detail = new JLabel();
			endBalance = new JLabel();
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new GridBagLayout());
			bottomPanel.add(endBalance, gridBagConstraints6);
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
	private JTable getTransactionsTable() {
		if (transactionsTable == null) {
			transactionsTable = new JTable();
			this.model = new TransactionsTableModel(transactionsTable, new Transaction[0]);
			transactionsTable.setModel(this.model);
			transactionsTable.setDefaultRenderer(Object.class, new CellRenderer());
			transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//			TableColumn column = transactionsTable.getColumnModel().getColumn(0);
//			column.setWidth(20);
//			column.setResizable(false);
		}
		return transactionsTable;
	}
}
