package net.yapbam.gui.statementview;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.widget.ComboBox;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.data.Transaction;
import net.yapbam.data.comparator.AccountComparator;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
import net.yapbam.data.event.TransactionsRemovedEvent;
import net.yapbam.gui.LocalizationData;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class StatementSelectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String SELECTED_STATEMENT_PROPERTY_NAME = "SelectedStatement"; //$NON-NLS-1$
	
	private ComboBox accountMenu;
	private ComboBox statementMenu;

	private FilteredData data;
	private Statement[] statements;
	private String lastSelectedStatement;

	public StatementSelectionPanel(FilteredData data) {
		this.data = data;
		initialize();
		if (data!=null) {
			this.data.addListener(new DataListener() {
				@Override
				public void processEvent(DataEvent event) {
					if (event instanceof EverythingChangedEvent) {
						List<Account> validAccounts = StatementSelectionPanel.this.data.getFilter().getValidAccounts();
						if ((validAccounts!=null) && (validAccounts.size()==1)) {
							getAccountMenu().setSelectedItem(validAccounts.get(0).getName());
						}
					}
				}
			});
			this.data.getGlobalData().addListener(new GlobalDataListener());
			init();
		}
	}
	
	private final class GlobalDataListener implements DataListener {
		@Override
		public void processEvent(DataEvent event) {
			GlobalData global = StatementSelectionPanel.this.data.getGlobalData();
			ComboBox accountMenu = getAccountMenu();
			if (event instanceof EverythingChangedEvent) {
				init();
			} else if (event instanceof AccountAddedEvent) {
				if (global.getAccountsNumber()==1) {
					// If there was no account before this one
					init();
				} else {
					// Find the future location of the new account in the menu
					Account[] accounts = AccountComparator.getSortedAccounts(global, getLocale());
					Account account = ((AccountAddedEvent)event).getAccount();
					int index = Arrays.binarySearch(accounts, account, AccountComparator.getInstance(getLocale()));
					accountMenu.insertItemAt(account.getName(), index);
				}
			} else if (event instanceof AccountRemovedEvent) {
				String accountName = ((AccountRemovedEvent)event).getRemoved().getName();
				if (NullUtils.areEquals(accountMenu.getSelectedItem(), accountName)) {
					// If the removed account is the current one, reset default settings
					init();
				} else {
					// simply remove the account in the menu
					accountMenu.removeItem(accountName);
				}
			} else if (event instanceof AccountPropertyChangedEvent) {
				String property = ((AccountPropertyChangedEvent)event).getProperty();
				if (property.equals(AccountPropertyChangedEvent.INITIAL_BALANCE)) {
					Account account = ((AccountPropertyChangedEvent)event).getAccount();
					if (account.getName().equals(accountMenu.getSelectedItem())) {
						refresh();
					}
				} else if (property.equals(AccountPropertyChangedEvent.NAME)) {
					// An account has changed its name
					// Change it in the menu
					accountMenu.setActionEnabled(false);
					String old = (String) ((AccountPropertyChangedEvent)event).getOldValue();
					int index = accountMenu.getSelectedIndex();
					for (int i = 0; i < accountMenu.getItemCount(); i++) {
						if (accountMenu.getItemAt(i).equals(old)) {
							accountMenu.removeItemAt(i);
							accountMenu.insertItemAt(((AccountPropertyChangedEvent)event).getNewValue(), i);
							break;
						}
					}
					// Restore the selected index
					accountMenu.setSelectedIndex(index);
					accountMenu.setActionEnabled(true);
				}
			} else if (event instanceof TransactionsAddedEvent) {
				Transaction[] ts = ((TransactionsAddedEvent)event).getTransactions();
				refreshIfNeeded(ts);
			} else if (event instanceof TransactionsRemovedEvent) {
				Transaction[] t = ((TransactionsRemovedEvent)event).getTransactions();
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
	}

	private void init() {
		GlobalData global = data.getGlobalData();
		ComboBox accountMenu = getAccountMenu();
		if (global.getAccountsNumber()==0) {
			accountMenu .setSelectedIndex(-1);
			accountMenu.removeAllItems();
			accountMenu.setEnabled(false);
		} else {
			accountMenu.setEnabled(true);
			accountMenu.setActionEnabled(false);
			accountMenu.removeAllItems();
			Account[] accounts = AccountComparator.getSortedAccounts(global, getLocale());
			for (Account account : accounts) {
				accountMenu.addItem(account.getName());
			}
			accountMenu.setActionEnabled(true);
			accountMenu.setSelectedIndex(0);
		}
	}


	private void initialize() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbcCheckModePanel = new GridBagConstraints();
		gbcCheckModePanel.anchor = GridBagConstraints.EAST;
		gbcCheckModePanel.gridx = 5;
		gbcCheckModePanel.fill = GridBagConstraints.HORIZONTAL;
		gbcCheckModePanel.gridy = 0;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.insets = new Insets(0, 10, 0, 5);
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.gridy = 0;
		JLabel jLabel1 = new JLabel();
		jLabel1.setText(LocalizationData.get("TransactionDialog.statement")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 0;
		JLabel jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.0D;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.weightx = 0.0D;
		add(jLabel, gridBagConstraints2);
		add(getAccountMenu(), gridBagConstraints1);
		add(jLabel1, gridBagConstraints3);
		add(getStatementMenu(), gridBagConstraints);
	}

	/**
	 * This method initializes accountMenu	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private ComboBox getAccountMenu() {
		if (accountMenu == null) {
			accountMenu = new ComboBox();
			accountMenu.setToolTipText(LocalizationData.get("StatementView.accountMenu.tooltip")); //$NON-NLS-1$
			accountMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refresh();
				}
			});
		}
		return accountMenu;
	}
	
	public Account getAccount() {
		if (data==null) {
			return null;
		}
		Object accountName = getAccountMenu().getSelectedItem();
		return accountName!=null ? data.getGlobalData().getAccount((String)accountName) : null;
	}
	
	/**
	 * This method initializes statementMenu	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private ComboBox getStatementMenu() {
		if (statementMenu == null) {
			statementMenu = new ComboBox();
			statementMenu.setToolTipText(LocalizationData.get("StatementView.statementMenu.statementMenu.tooltip")); //$NON-NLS-1$
			statementMenu.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					statementSelected(true);
				}
			});
		}
		return statementMenu;
	}
	
	private void statementSelected(boolean forcePropertyChange) {
		Statement selectedStatement = getSelectedStatement();
		String statementName = selectedStatement==null?null:selectedStatement.getId();
		if (forcePropertyChange || !NullUtils.areEquals(statementName, lastSelectedStatement)) {
			lastSelectedStatement = statementName;
			firePropertyChange(SELECTED_STATEMENT_PROPERTY_NAME, lastSelectedStatement, selectedStatement);
		}
	}

	private void refresh() {
		String accountName = (String) getAccountMenu().getSelectedItem();
		statementMenu .setActionEnabled(false);
		boolean ignoreLastSelected = (statements==null)||(statementMenu.getSelectedIndex()<0)||(statementMenu.getItemCount()<=2);
		String lastSelectedStatement = (String) (ignoreLastSelected?null:statementMenu.getSelectedItem());
		statementMenu.removeAllItems();
		if (accountName==null) {
			statements = null;
			statementMenu.setActionEnabled(true);
			statementMenu.setSelectedIndex(-1);
			statementMenu.setEnabled(false);
		} else {
			statements = Statement.getStatements(data.getGlobalData().getAccount(accountName));
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

	public Statement getSelectedStatement() {
		if ((statements==null) || (getStatementMenu().getSelectedIndex()<0)) {
			return null;
		}
		return statements[statements.length-1-getStatementMenu().getSelectedIndex()];
	}
	
	public void select (String statementId) {
		getStatementMenu().setSelectedItem(statementId);
	}

	public Statement getStatement(String statementId) {
		if (statements==null) {
			return null;
		}
		for (Statement statement : statements) {
			if (NullUtils.areEquals(statement.getId(), statementId)) {
				return statement;
			}
		}
		return null;
	}
	
	public boolean isThereANewerStatement(String statementId) {
		if (statements==null) {
			return false;
		}
		int index = -1;
		for (int i = 0; i < statements.length; i++) {
			if (NullUtils.areEquals(statements[i].getId(), statementId)) {
				index = i;
				break;
			}
		}
		return index>=0 && (index!=statements.length-1) && ((index!=statements.length-2) || (statements[statements.length-1].getId()!=null));
	}
}
