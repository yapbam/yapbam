package net.yapbam.gui.statementview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.Printable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable.PrintMode;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.JTable;
import com.fathzer.soft.ajlib.swing.table.JTableListener;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.AbstractTransactionUpdater;
import net.yapbam.data.Account;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.data.Transaction;
import net.yapbam.export.Exporter;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.actions.DeleteTransactionAction;
import net.yapbam.gui.actions.DuplicateTransactionAction;
import net.yapbam.gui.actions.EditTransactionAction;
import net.yapbam.gui.dialogs.export.ExportComponent;
import net.yapbam.gui.dialogs.export.ExporterParameters;
import net.yapbam.gui.dialogs.export.TableExporter;
import net.yapbam.gui.transactiontable.TransactionTableUtils;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.SplitPane;
import net.yapbam.util.DateUtils;

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
	
	private transient FilteredData data;
	CheckTransactionAction checkAction;
	private JPanel menuPanel;
	private JLabel lblNewLabel;
	private SplitPane splitPane;
	private JLabel notCheckedColumns;
	
	private boolean checkModeReady = false;

	private JPanel notCheckedPanel;
	private ChangeValueDatePanel changeValueDatePanel;
	private JLabel summaryLabel;
	private JButton btnRename;
	private JButton btnExport;
	private JPanel northPanel;
	private JPanel panel1;
	
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
			statementSelectionPanel.addPropertyChangeListener(StatementSelectionPanel.SELECTED_STATEMENT_PROPERTY_NAME, new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Statement selectedStatement = getStatementSelectionPanel().getSelectedStatement();
					if ((selectedStatement==null) || (selectedStatement.getId()!=null)) {
						getCheckModeChkbx().setSelected(false);
					}
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

			GridBagConstraints gbcBalance = new GridBagConstraints();
			gbcBalance.fill = GridBagConstraints.HORIZONTAL;
			gbcBalance.weightx = 1.0;
			statementPanel.add(getBalancePanel(), gbcBalance);

			GridBagConstraints gbcMenuPanel = new GridBagConstraints();
			gbcMenuPanel.anchor = GridBagConstraints.NORTH;
			gbcMenuPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcMenuPanel.gridx = 0;
			gbcMenuPanel.gridy = 1;
			statementPanel.add(getColumnsMenuPanel(), gbcMenuPanel);

			GridBagConstraints gbcJScrollPane = new GridBagConstraints();
			gbcJScrollPane.weighty = 1.0;
			gbcJScrollPane.weightx = 1.0;
			gbcJScrollPane.fill = GridBagConstraints.BOTH;
			gbcJScrollPane.gridx = 0;
			gbcJScrollPane.gridy = 2;
			JScrollPane jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTransactionsTable());
			statementPanel.add(jScrollPane, gbcJScrollPane);

			GridBagConstraints gbcDetail = new GridBagConstraints();
			gbcDetail.insets = new Insets(0, 0, 5, 0);
			gbcDetail.weightx = 1.0;
			gbcDetail.fill = GridBagConstraints.HORIZONTAL;
			gbcDetail.gridx = 0;
			gbcDetail.gridy = 3;
			statementPanel.add(getDetail(), gbcDetail);
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
			Action uncheckAction = new CheckTransactionAction(this, transactionsTable, getUncheckedTransactionsTable(), false);
			transactionsTable.addMouseListener(new MyListener(new Action[]{edit, duplicate, delete}, edit, uncheckAction));
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
			GridBagLayout gblMenuPanel = new GridBagLayout();
			menuPanel.setLayout(gblMenuPanel);
			GridBagConstraints gbcColumnsMenu = new GridBagConstraints();
			gbcColumnsMenu.weightx = 1.0;
			gbcColumnsMenu.insets = new Insets(0, 0, 0, 5);
			gbcColumnsMenu.anchor = GridBagConstraints.EAST;
			gbcColumnsMenu.gridx = 0;
			gbcColumnsMenu.gridy = 0;
			menuPanel.add(getColumnsMenu(), gbcColumnsMenu);
		}
		return menuPanel;
	}
	
	private JLabel getColumnsMenu() {
		if (lblNewLabel == null) {
			lblNewLabel = new FriendlyTable.ShowHideColumsMenu(getTransactionsTable(), LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
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

			GridBagConstraints gbcPanel = new GridBagConstraints();
			gbcPanel.insets = new Insets(0, 0, 5, 0);
			gbcPanel.gridwidth = 0;
			gbcPanel.weightx = 1.0;
			gbcPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcPanel.gridx = 0;
			gbcPanel.gridy = 0;
			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			panel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.GRAY));
			GridBagLayout gblPanel = new GridBagLayout();
			panel.setLayout(gblPanel);
			notCheckedPanel.add(panel, gbcPanel);
			JLabel label = new JLabel(LocalizationData.get("CheckModePanel.notChecked.title")); //$NON-NLS-1$
			label.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbcLabel = new GridBagConstraints();
			gbcLabel.weightx = 1.0;
			gbcLabel.insets = new Insets(0, 5, 0, 0);
			gbcLabel.anchor = GridBagConstraints.WEST;
			gbcLabel.gridx = 0;
			gbcLabel.gridy = 0;
			label.setFont(label.getFont().deriveFont(14*label.getFont().getSize()/12));
			panel.add(label, gbcLabel);

			GridBagConstraints gbcNotCheckedColumns = new GridBagConstraints();
			gbcNotCheckedColumns.anchor = GridBagConstraints.EAST;
			gbcNotCheckedColumns.gridx = 0;
			gbcNotCheckedColumns.gridy = 1;
			gbcNotCheckedColumns.gridwidth = 0;
			notCheckedPanel.add(getNotCheckedColumns(), gbcNotCheckedColumns);
	
			JScrollPane notCheckedJScrollPane = new JScrollPane();
			notCheckedJScrollPane.setViewportView(getUncheckedTransactionsTable());
			GridBagConstraints gbcNotCheckedJScrollPane = new GridBagConstraints();
			gbcNotCheckedJScrollPane.gridwidth = 0;
			gbcNotCheckedJScrollPane.weighty = 1.0;
			gbcNotCheckedJScrollPane.weightx = 1.0;
			gbcNotCheckedJScrollPane.fill = GridBagConstraints.BOTH;
			gbcNotCheckedJScrollPane.gridy = 2;
			gbcNotCheckedJScrollPane.gridx = 0;
			notCheckedPanel.add(notCheckedJScrollPane, gbcNotCheckedJScrollPane);

			GridBagConstraints gbcChangeValueDatePanel = new GridBagConstraints();
			gbcChangeValueDatePanel.anchor = GridBagConstraints.WEST;
			gbcChangeValueDatePanel.insets = new Insets(0, 5, 0, 0);
			gbcChangeValueDatePanel.gridx = 0;
			gbcChangeValueDatePanel.gridy = 3;
			notCheckedPanel.add(getChangeValueDatePanel(), gbcChangeValueDatePanel);

			GridBagConstraints gbcSummaryLabel = new GridBagConstraints();
			gbcSummaryLabel.anchor = GridBagConstraints.EAST;
			gbcSummaryLabel.insets = new Insets(0, 0, 0, 5);
			gbcSummaryLabel.gridx = 1;
			gbcSummaryLabel.gridy = 3;
			notCheckedPanel.add(getSummaryLabel(), gbcSummaryLabel);
		}
		return notCheckedPanel;
	}
	
	private JLabel getNotCheckedColumns() {
		if (notCheckedColumns == null) {
			notCheckedColumns = new FriendlyTable.ShowHideColumsMenu(getUncheckedTransactionsTable(), LocalizationData.get("MainFrame.showColumns")); //$NON-NLS-1$
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
							message = Formatter.format(LocalizationData.get("CheckModePanel.selectedSummarySingular"),LocalizationData.getCurrencyInstance().format(transactions[0].getAmount())); //$NON-NLS-1$
						} else {
							double total = 0.0;
							for (Transaction transaction : transactions) {
								total += transaction.getAmount();
							}
							message = Formatter.format(LocalizationData.get("CheckModePanel.selectedSummaryPlural"), transactions.length, LocalizationData.getCurrencyInstance().format(total)); //$NON-NLS-1$
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
		
		boolean visibility = statementSelected && (statement.getId()!=null);
		
		getBtnRename().setVisible(visibility);
		getBtnExport().setVisible(visibility);
		
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
				showWarningMessage = getStatementSelectionPanel().isThereANewerStatement(statementId);
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
		getDetail().setText(Formatter.format(LocalizationData.get("StatementView.statementSummary"), statementSelected?statement.getNbTransactions():0, //$NON-NLS-1$
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
				if (result == 0) {
					result = DateUtils.dateToInteger(o1.getDate())-DateUtils.dateToInteger(o2.getDate());
				}
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
	
	private boolean hasStatement(GlobalData gData, String accountName, String statement) {
		for (int i = 0; i < gData.getTransactionsNumber(); i++) {
			Transaction transaction = gData.getTransaction(i);
			if (accountName.equals(transaction.getAccount().getName()) && statement.equals(transaction.getStatement())) {
				return true;
			}
		}
		return false;
	}
	
	private void renameStatement(final GlobalData gData, final String accountName, final String newStatement, final String currentStatement) {
		AbstractTransactionUpdater updater = new AbstractTransactionUpdater(gData) {
			/* (non-Javadoc)
			 * @see net.yapbam.data.AbstractTransactionUpdater#change(net.yapbam.data.Transaction)
			 */
			@Override
			protected Transaction change(Transaction t) {
				if (accountName.equals(t.getAccount().getName()) && currentStatement.equals(t.getStatement())) {
					return new Transaction(t.getDate(), t.getNumber(), t.getDescription(), t.getComment(), t.getAmount(),
							t.getAccount(), t.getMode(), t.getCategory(), t.getValueDate(), newStatement, Arrays.asList(t.getSubTransactions()));
				} else {
					return null;
				}
			}
		};
		updater.doIt();
	}
	
	private JButton getBtnRename() {
		if (btnRename == null) {
			btnRename = new JButton(LocalizationData.get("StatementDialog.button.name")); //$NON-NLS-1$
			btnRename.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GlobalData gData = data.getGlobalData();
					Window owner = Utils.getOwnerWindow(btnRename);
					StatementRenameDialog dialog = new StatementRenameDialog(owner, gData);
					dialog.setVisible(true);
					final String newStatement = dialog.getResult();
					final String currentStatement = getStatementSelectionPanel().getSelectedStatement().getId();
					if ((newStatement!=null) && (!newStatement.equals(currentStatement))) {
						String accountName = statementSelectionPanel.getAccount().getName();
						if (hasStatement(gData, accountName, newStatement)) {
							String message = Formatter.format(LocalizationData.get("StatementDialog.existing.message"), newStatement, currentStatement); //$NON-NLS-1$
							int choice = JOptionPane.showConfirmDialog(owner, message,
									LocalizationData.get("StatementDialog.existing.title"),  JOptionPane.OK_CANCEL_OPTION,  JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
							if (choice==2) {
								return;
							}
						}
						renameStatement(gData, accountName, newStatement, currentStatement);
						getStatementSelectionPanel().select(newStatement);
					}
				}
			});
			btnRename.setToolTipText(LocalizationData.get("StatementDialog.button.tooltip")); //$NON-NLS-1$
		}
		return btnRename;
	}
	
	@SuppressWarnings("serial")
	private JButton getBtnExport() {
		if(btnExport == null) {
			ExportComponent<ExporterParameters, FriendlyTable> exportC = new ExportComponent<ExporterParameters, FriendlyTable>() {
				@Override
				public Exporter<ExporterParameters, FriendlyTable> buildExporter() {
					return new TableExporter() {
						@Override
						protected Object getValueAt(JTable table, int modelRowIndex, int modelColIndex) {
							// Warning, in the table model, the description is already html encoded. It would lead to the export
							// containing html tags or escape sequences. So we will rebuild the description as text
							if (StatementTableModel.DESCRIPTION_COLUMN==modelColIndex) {
								final Transaction transaction = ((StatementTableModel)table.getModel()).getTransactions()[modelRowIndex];
								return TransactionTableUtils.getDescriptionAsText(transaction, true);
							} else {
								return super.getValueAt(table, modelRowIndex, modelColIndex);
							}
						}
					};
				}
			};
			exportC.setContent(getTransactionsTable());
			btnExport = exportC;
		}
		return btnExport;
	}
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new JPanel();
			GridBagLayout gblNorthPanel = new GridBagLayout();
			northPanel.setLayout(gblNorthPanel);
			GridBagConstraints gbcPanel1 = new GridBagConstraints();
			gbcPanel1.anchor = GridBagConstraints.WEST;
			gbcPanel1.weightx = 1.0;
			gbcPanel1.fill = GridBagConstraints.VERTICAL;
			gbcPanel1.gridx = 1;
			gbcPanel1.gridy = 0;
			northPanel.add(getPanel1(), gbcPanel1);
			GridBagConstraints gbcCheckModeChkbx = new GridBagConstraints();
			gbcCheckModeChkbx.insets = new Insets(0, 0, 0, 5);
			gbcCheckModeChkbx.weightx = 1.0;
			gbcCheckModeChkbx.anchor = GridBagConstraints.EAST;
			gbcCheckModeChkbx.gridx = 2;
			gbcCheckModeChkbx.gridy = 0;
			northPanel.add(getCheckModeChkbx(), gbcCheckModeChkbx);
		}
		return northPanel;
	}
	private JPanel getPanel1() {
		if (panel1 == null) {
			panel1 = new JPanel();
			panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panel1.add(getStatementSelectionPanel());
			panel1.add(getBtnRename());
			panel1.add(getBtnExport());
		}
		return panel1;
	}

	Account getSelectedAccount() {
		return getStatementSelectionPanel().getAccount();
	}
}
