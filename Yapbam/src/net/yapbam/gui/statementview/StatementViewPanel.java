package net.yapbam.gui.statementview;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;

import net.yapbam.data.AbstractTransactionUpdater;
import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.data.Transaction;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.actions.TransactionSelector;
import net.yapbam.gui.util.SplitPane;
import net.yapbam.util.DateUtils;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import java.awt.Insets;
import javax.swing.JTable.PrintMode;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.JButton;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.JTableListener;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class StatementViewPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final String CHECK_MODE_READY_PROPERTY = "CheckModeReady"; //$NON-NLS-1$
	private static final Cursor CHECK_CURSOR;
	private static final Cursor UNCHECK_CURSOR;

	private StatementSelectionPanel statementSelectionPanel;
	private JPanel statementPanel = null;
	private BalancePanel balancePanel;
	private JLabel detail;
	private StatementTable uncheckedTransactionsTable;
	private StatementTable transactionsTable;
	private JCheckBox checkModeChkbx;
	
	private FilteredData data;
	CheckTransactionAction checkAction;
	private JPanel menuPanel;
	private JLabel lblNewLabel;
	private SplitPane splitPane;
	private JScrollPane notCheckedJScrollPane;
	private JLabel notCheckedColumns;
	
	private boolean checkModeReady = false;

	private JPanel notCheckedPanel;
	private ChangeValueDatePanel changeValueDatePanel;
	private JLabel summaryLabel;
	private JButton btnRename;
	private JPanel northPanel;
	private JPanel panel_1;
	
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
		setLayout(new BorderLayout(0, 0));
		add(getNorthPanel(), BorderLayout.NORTH);
		add(getSplitPane());
	}

	private StatementSelectionPanel getStatementSelectionPanel() {
		if (statementSelectionPanel==null) {
			statementSelectionPanel = new StatementSelectionPanel(data);
			statementSelectionPanel.getStatementMenu().addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Statement selectedStatement = getStatementSelectionPanel().getSelectedStatement();
					if ((selectedStatement==null) || (selectedStatement.getId()!=null)) getCheckModeChkbx().setSelected(false);
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
			balancePanel.addPropertyChangeListener(BalancePanel.EDITED_STATEMENT_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					setTables(); 
					getTransactionsTable().setCursor(balancePanel.getEditedStatement()!=null ? UNCHECK_CURSOR : Cursor.getDefaultCursor());
				}
			});
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
			Action checkAction = new CheckTransactionAction(this, transactionsTable, getUncheckedTransactionsTable(), false);
			transactionsTable.addMouseListener(new MyListener(new Action[]{edit, duplicate, delete}, edit, checkAction));
		}
		return transactionsTable;
	}
	
	StatementTable getUncheckedTransactionsTable() {
		if (uncheckedTransactionsTable==null) {
			uncheckedTransactionsTable = new StatementTable(data);
			Action edit = new EditTransactionAction(uncheckedTransactionsTable);
			Action delete = new DeleteTransactionAction(uncheckedTransactionsTable);
			Action duplicate = new DuplicateTransactionAction(uncheckedTransactionsTable);
			checkAction = new CheckTransactionAction(this, uncheckedTransactionsTable, getTransactionsTable(), true);
			uncheckedTransactionsTable.addMouseListener(new MyListener(new Action[]{edit, duplicate, delete}, edit, checkAction));
		}
		return uncheckedTransactionsTable;
	}

	private JCheckBox getCheckModeChkbx() {
		if (checkModeChkbx == null) {
			checkModeChkbx = new JCheckBox(LocalizationData.get("CheckModePanel.title")); //$NON-NLS-1$
			checkModeChkbx.setToolTipText(LocalizationData.get("CheckModePanel.title.tooltip")); //$NON-NLS-1$
			checkModeChkbx.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					setTables();
				}
			});
		}
		return checkModeChkbx;
	}
	
	private static class MyListener extends JTableListener {
		private Action checkAction;
		public MyListener(Action[] actions, Action defaultAction, Action checkAction) {
			super(actions, defaultAction);
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
			splitPane.setTopComponent(getNotCheckedPanel());
			splitPane.setBottomComponent(getStatementPanel());
			setTables();
		}
		return splitPane;
	}

	protected JPanel getNotCheckedPanel() {
		if (notCheckedPanel==null) {
			notCheckedPanel = new JPanel(new GridBagLayout());
			Border border = BorderFactory.createLineBorder(Color.gray, 3);
			border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), border);
			notCheckedPanel.setBorder(border);

			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.gridwidth = 0;
			gbc_panel.weightx = 1.0;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			panel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.GRAY));
			GridBagLayout gbl_panel = new GridBagLayout();
			panel.setLayout(gbl_panel);
			notCheckedPanel.add(panel, gbc_panel);
			JLabel label = new JLabel(LocalizationData.get("CheckModePanel.notChecked.title")); //$NON-NLS-1$
			label.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.weightx = 1.0;
			gbc_label.insets = new Insets(0, 5, 0, 0);
			gbc_label.anchor = GridBagConstraints.WEST;
			gbc_label.gridx = 0;
			gbc_label.gridy = 0;
			Font font = label.getFont();
			font = label.getFont().deriveFont(14*label.getFont().getSize()/12);
			label.setFont(font);
			panel.add(label, gbc_label);

			GridBagConstraints gbc_notCheckedColumns = new GridBagConstraints();
			gbc_notCheckedColumns.anchor = GridBagConstraints.EAST;
			gbc_notCheckedColumns.gridx = 0;
			gbc_notCheckedColumns.gridy = 1;
			gbc_notCheckedColumns.gridwidth = 0;
			notCheckedPanel.add(getNotCheckedColumns(), gbc_notCheckedColumns);
	
			notCheckedJScrollPane = new JScrollPane();
			notCheckedJScrollPane.setViewportView(getUncheckedTransactionsTable());
			GridBagConstraints gbc_notCheckedJScrollPane = new GridBagConstraints();
			gbc_notCheckedJScrollPane.gridwidth = 0;
			gbc_notCheckedJScrollPane.weighty = 1.0;
			gbc_notCheckedJScrollPane.weightx = 1.0;
			gbc_notCheckedJScrollPane.fill = GridBagConstraints.BOTH;
			gbc_notCheckedJScrollPane.gridy = 2;
			gbc_notCheckedJScrollPane.gridx = 0;
			notCheckedPanel.add(notCheckedJScrollPane, gbc_notCheckedJScrollPane);

			GridBagConstraints gbc_changeValueDatePanel = new GridBagConstraints();
			gbc_changeValueDatePanel.anchor = GridBagConstraints.WEST;
			gbc_changeValueDatePanel.insets = new Insets(0, 5, 0, 0);
			gbc_changeValueDatePanel.gridx = 0;
			gbc_changeValueDatePanel.gridy = 3;
			notCheckedPanel.add(getChangeValueDatePanel(), gbc_changeValueDatePanel);

			GridBagConstraints gbc_summaryLabel = new GridBagConstraints();
			gbc_summaryLabel.anchor = GridBagConstraints.EAST;
			gbc_summaryLabel.insets = new Insets(0, 0, 0, 5);
			gbc_summaryLabel.gridx = 1;
			gbc_summaryLabel.gridy = 3;
			notCheckedPanel.add(getSummaryLabel(), gbc_summaryLabel);
		}
		return notCheckedPanel;
	}
	
	private JLabel getNotCheckedColumns() {
		if (notCheckedColumns == null) {
			notCheckedColumns = getUncheckedTransactionsTable().getShowHideColumnsMenu(LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
		}
		return notCheckedColumns;
	}
	
	private JLabel getSummaryLabel() {
		if (summaryLabel == null) {
			summaryLabel = new JLabel();
			getUncheckedTransactionsTable().addPropertyChangeListener(TransactionSelector.SELECTED_PROPERTY, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Transaction[] transactions = getUncheckedTransactionsTable().getSelectedTransactions();
					summaryLabel.setVisible(transactions.length>0);
					if (transactions.length>0) {
						String message;
						if (transactions.length==1) {
							message = MessageFormat.format(LocalizationData.get("CheckModePanel.selectedSummarySingular"),LocalizationData.getCurrencyInstance().format(transactions[0].getAmount())); //$NON-NLS-1$
						} else {
							double total = 0.0;
							for (Transaction transaction : transactions) {
								total += transaction.getAmount();
							}
							message = MessageFormat.format(LocalizationData.get("CheckModePanel.selectedSummaryPlural"), transactions.length, LocalizationData.getCurrencyInstance().format(total)); //$NON-NLS-1$
						}
						summaryLabel.setText(message);
					}
				}
			});
		}
		return summaryLabel;
	}
	
	private void setTables() {
		// Gets the currently selected statement
		Statement statement = getStatementSelectionPanel().getSelectedStatement();
		// If no statement is selected, everything but the statement selection panel should be invisible
		boolean statementSelected = statement!=null;
		// The check mode is available only if the selected statement is the "not checked" pseudo-statement
		boolean checkModeAvailable = statementSelected && (statement.getId()==null);
		// The check mode is activated if it is available and the check box is selected
		boolean checkMode = checkModeAvailable && getCheckModeChkbx().isSelected();
		
		// Show/hide the check mode widget
		getCheckModeChkbx().setVisible(checkModeAvailable);
		getBtnRename().setVisible(statementSelected && (statement.getId()!=null));
		
		// Show hide the widgets of the check mode
		getNotCheckedPanel().setVisible(checkMode);
		if (checkMode) {
			if (!getSplitPane().isDividerVisible()) {
				getSplitPane().setDividerLocation(0.5);
			}
		} else {
			getSplitPane().setDividerLocation(0.0);
		}
		getSplitPane().setDividerVisible(checkMode);
		
		DecimalFormat ci = LocalizationData.getCurrencyInstance();
		Transaction[] transactions;
		if (checkMode) { // If check mode is on
			// Fill the top table with not checked transactions
			getUncheckedTransactionsTable().setTransactions(getTransactions(statementSelectionPanel.getAccount(), null));
			// Fill the bottom table with the edited statement.
			String statementId = getBalancePanel().getEditedStatement();
			double uncheckedStart = statement.getStartBalance();
			// If a statement is entered, take this one 
			statement = statementId==null ? null : getStatementSelectionPanel().getStatement(statementId);
			boolean showWarningMessage = false;
			if (statement==null) {
				// If the statement doesn't exist (or no statement id is entered), create a fake one.
				statement = new Statement(statementId, uncheckedStart);
				transactions = new Transaction[0];
			} else {
				// Verify that this statement is the last one
				showWarningMessage = getStatementSelectionPanel().IsThereANewerStatement(statementId);
				transactions = getTransactions(statementSelectionPanel.getAccount(), statementId);
			}
			getBalancePanel().setAlertVisible(showWarningMessage);
		} else {
			transactions = statementSelected ? getTransactions(statementSelectionPanel.getAccount(), statement.getId()) : new Transaction[0];
		}
		getTransactionsTable().setTransactions(transactions);

		// Set up the balance panel
		getBalancePanel().setCheckMode(checkMode);
		getBalancePanel().setStatement(statementSelected?statement:null);
		// Set up the details
		getDetail().setText(MessageFormat.format(LocalizationData.get("StatementView.statementSummary"), statementSelected?statement.getNbTransactions():0, //$NON-NLS-1$
				statementSelected?ci.format(statement.getNegativeBalance()):0.0, statementSelected?ci.format(statement.getPositiveBalance()):0.0));
		
		// Sets the cursors
		getUncheckedTransactionsTable().setCursor(isCheckModeReady() ? CHECK_CURSOR : Cursor.getDefaultCursor());
		getTransactionsTable().setCursor(isCheckModeReady() ? UNCHECK_CURSOR : Cursor.getDefaultCursor());
		
		if (isCheckModeReady()!=this.checkModeReady) {
			this.checkModeReady = !this.checkModeReady;
			firePropertyChange(CHECK_MODE_READY_PROPERTY, !this.isCheckModeReady(), this.isCheckModeReady());
		}
	}
	
	boolean isCheckModeReady() {
		return getCheckModeChkbx().isSelected() && (getBalancePanel().getEditedStatement()!=null);
	}
	
	String getEditedStatement() {
		return getBalancePanel().getEditedStatement();
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
	private ChangeValueDatePanel getChangeValueDatePanel() {
		if (changeValueDatePanel == null) {
			changeValueDatePanel = new ChangeValueDatePanel(getUncheckedTransactionsTable());
		}
		return changeValueDatePanel;
	}
	
	FilteredData getFilteredData() {
		return this.data;
	}

	public boolean isCheckMode() {
		return getCheckModeChkbx().isSelected();
	}
	private JButton getBtnRename() {
		if (btnRename == null) {
			btnRename = new JButton(LocalizationData.get("StatementDialog.button.name"));
			btnRename.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GlobalData gData = data.getGlobalData();
					Window owner = Utils.getOwnerWindow(btnRename);
					StatementRenameDialog dialog = new StatementRenameDialog(owner, gData);
					dialog.setVisible(true);
					final String result = dialog.getResult();
					final String current = getStatementSelectionPanel().getSelectedStatement().getId();
					if ((result!=null) && (!result.equals(current))) {
						for (int i = 0; i < gData.getTransactionsNumber(); i++) {
							if (result.equals(gData.getTransaction(i).getStatement())) {
								String message = MessageFormat.format(LocalizationData.get("StatementDialog.existing.message"), result, current);
								int choice = JOptionPane.showConfirmDialog(owner, message,
										LocalizationData.get("StatementDialog.existing.title"),  JOptionPane.OK_CANCEL_OPTION,  JOptionPane.WARNING_MESSAGE);
								if (choice==2) return;
								break;
							}
						}
						AbstractTransactionUpdater updater = new AbstractTransactionUpdater(gData) {
							/* (non-Javadoc)
							 * @see net.yapbam.data.AbstractTransactionUpdater#change(net.yapbam.data.Transaction)
							 */
							@Override
							protected Transaction change(Transaction t) {
								if (current.equals(t.getStatement())) {
									return new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getComment(), t.getAmount(),
											t.getAccount(), t.getMode(), t.getCategory(), t.getValueDate(), result, Arrays.asList(t.getSubTransactions()));
								} else {
									return null;
								}
							}
						};
						updater.doIt();
						getStatementSelectionPanel().select(result);
					}
				}
			});
			btnRename.setToolTipText(LocalizationData.get("StatementDialog.button.tooltip"));
		}
		return btnRename;
	}
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
			GridBagLayout gbl_northPanel = new GridBagLayout();
			northPanel.setLayout(gbl_northPanel);
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.anchor = GridBagConstraints.WEST;
			gbc_panel_1.weightx = 1.0;
			gbc_panel_1.fill = GridBagConstraints.VERTICAL;
			gbc_panel_1.gridx = 1;
			gbc_panel_1.gridy = 0;
			northPanel.add(getPanel_1(), gbc_panel_1);
			GridBagConstraints gbc_checkModeChkbx = new GridBagConstraints();
			gbc_checkModeChkbx.insets = new Insets(0, 0, 0, 5);
			gbc_checkModeChkbx.weightx = 1.0;
			gbc_checkModeChkbx.anchor = GridBagConstraints.EAST;
			gbc_checkModeChkbx.gridx = 2;
			gbc_checkModeChkbx.gridy = 0;
			northPanel.add(getCheckModeChkbx(), gbc_checkModeChkbx);
		}
		return northPanel;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panel_1.add(getStatementSelectionPanel());
			panel_1.add(getBtnRename());
		}
		return panel_1;
	}
}
