package net.yapbam.gui.transactiontable;

import java.awt.GridBagLayout;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JScrollPane;

import com.fathzer.soft.ajlib.swing.table.JTable;

public class TransactionsPreferencePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private BalanceReportField positiveBalanceReport = null;
	private BalanceReportField negativeBalanceReport = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton setTodefault = null;
	private JPanel jPanel1 = null;
	private JCheckBox separeCommentChkBx;
	private JCheckBox separateExpenseReceiptChckbx;
	private JTable table;
	private JCheckBox chckBxCustomBackground;
	private JButton btnExpense;
	private JButton btnReceipt;
	private JScrollPane scrollPane;

	private MyTableModel tableModel;
	private Color expenseColor = GenericTransactionTableModel.CASHOUT!=null?GenericTransactionTableModel.CASHOUT:DEFAULT_CASHOUT;
	private Color receiptColor = GenericTransactionTableModel.CASHIN!=null?GenericTransactionTableModel.CASHIN:DEFAULT_CASHIN;
	private boolean initialSeparateCommentState;
	private boolean initialSeparateReceiptExpense;
	
	static String NEGATIVE_KEY = "net.yapbam.balanceReport.negative"; //$NON-NLS-1$
	static String POSITIVE_KEY = "net.yapbam.balanceReport.positive"; //$NON-NLS-1$
	private static String CUSTOMIZED_BACKGROUND_KEY = "net.yapbam.transactionTable.customized.background"; //$NON-NLS-1$
	static String EXPENSE_BACKGROUND_COLOR_KEY = "net.yapbam.transactionTable.expense.color"; //$NON-NLS-1$
	static String RECEIPT_BACKGROUND_COLOR_KEY = "net.yapbam.transactionTable.receipt.color"; //$NON-NLS-1$
	private static final String SEPARATE_RECEIPT_EXPENSE ="net.yapbam.transactionTable.separateReceiptAndExpense"; //$NON-NLS-1$
	private static final String SEPARATE_COMMENT = "net.yapbam.transactionTable.separateDescriptionAndComment"; //$NON-NLS-1$
	
	static final Color DEFAULT_POSITIVE = new Color(0,200,0);
	static final Color DEFAULT_NEGATIVE = Color.RED;
	static final Color DEFAULT_CASHIN = new Color(240,255,240);
	static final Color DEFAULT_CASHOUT = new Color(255,240,240);
		
	/**
	 * This is the default constructor
	 */
	public TransactionsPreferencePanel() {
		super();
		this.initialSeparateCommentState = isCommentSeparatedFromDescription();
		this.initialSeparateReceiptExpense = isReceiptSeparatedFromExpense();
		initialize();
	}

	public static boolean isCommentSeparatedFromDescription() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(SEPARATE_COMMENT));
	}
	
	public static boolean isReceiptSeparatedFromExpense() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(SEPARATE_RECEIPT_EXPENSE));
	}
	
	public static boolean isCustomBackgroundColors() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(TransactionsPreferencePanel.CUSTOMIZED_BACKGROUND_KEY, "true"));
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.weightx = 1.0D;
		gridBagConstraints6.gridy = 0;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.insets = new Insets(5, 0, 5, 0);
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.anchor = GridBagConstraints.NORTH;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.add(getJPanel(), gridBagConstraints4);
		GridBagConstraints gbc_setTodefault = new GridBagConstraints();
		gbc_setTodefault.anchor = GridBagConstraints.SOUTHEAST;
		gbc_setTodefault.weighty = 1.0;
		gbc_setTodefault.weightx = 1.0;
		gbc_setTodefault.insets = new Insets(0, 0, 5, 5);
		gbc_setTodefault.gridx = 0;
		gbc_setTodefault.gridy = 2;
		add(getSetTodefault(), gbc_setTodefault);
		this.add(getJPanel1(), gridBagConstraints6);
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("MainFrame.Transactions.Preferences.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("MainFrame.Transactions.Preferences.tooltip"); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		Color positive = positiveBalanceReport.getForeground();
		Color negative = negativeBalanceReport.getForeground();
		boolean bckHasChange = (GenericTransactionTableModel.CASHIN!=null) && !(GenericTransactionTableModel.CASHIN.equals(receiptColor) && GenericTransactionTableModel.CASHOUT.equals(expenseColor));
		if (positive.equals(BalanceReportField.POSITIVE_COLOR) && negative.equals(BalanceReportField.NEGATIVE_COLOR)
				&& (separeCommentChkBx.isSelected()==initialSeparateCommentState) && (getSeparateExpenseReceiptChckbx().isSelected()==initialSeparateReceiptExpense) &&
				(getChckBxCustomBackground().isSelected()==(GenericTransactionTableModel.CASHIN!=null)) && !bckHasChange) {
			return false;
		}
		BalanceReportField.POSITIVE_COLOR = positive;
		BalanceReportField.NEGATIVE_COLOR = negative;
		Preferences.INSTANCE.setProperty(POSITIVE_KEY, Integer.toString(positive.getRGB()));
		Preferences.INSTANCE.setProperty(NEGATIVE_KEY, Integer.toString(negative.getRGB()));
		Preferences.INSTANCE.setProperty(SEPARATE_RECEIPT_EXPENSE, Boolean.toString(getSeparateExpenseReceiptChckbx().isSelected()));
		Preferences.INSTANCE.setProperty(SEPARATE_COMMENT, Boolean.toString(separeCommentChkBx.isSelected()));
		Preferences.INSTANCE.setProperty(CUSTOMIZED_BACKGROUND_KEY, Boolean.toString(getChckBxCustomBackground().isSelected()));
		if (getChckBxCustomBackground().isSelected()) {
			Preferences.INSTANCE.setProperty(EXPENSE_BACKGROUND_COLOR_KEY, Integer.toString(expenseColor.getRGB()));
			Preferences.INSTANCE.setProperty(RECEIPT_BACKGROUND_COLOR_KEY, Integer.toString(receiptColor.getRGB()));
		} else {
			Preferences.INSTANCE.removeProperty(EXPENSE_BACKGROUND_COLOR_KEY);
			Preferences.INSTANCE.removeProperty(RECEIPT_BACKGROUND_COLOR_KEY);
		}
		return true;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			negativeBalanceReport = new BalanceReportField(LocalizationData.get("MainFrame.Transactions.Preferences.balanceSummary.negativeSample")); //$NON-NLS-1$
			negativeBalanceReport.setValue(-100, true);
			positiveBalanceReport = new BalanceReportField(LocalizationData.get("MainFrame.Transactions.Preferences.balanceSummary.positiveSample")); //$NON-NLS-1$
			positiveBalanceReport.setValue(100, true);
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("MainFrame.Transactions.Preferences.balanceSummary.title"))); //$NON-NLS-1$
			jPanel.add(positiveBalanceReport, gridBagConstraints);
			jPanel.add(negativeBalanceReport, gridBagConstraints1);
			jPanel.add(getJButton(), gridBagConstraints2);
			jPanel.add(getJButton1(), gridBagConstraints3);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText(LocalizationData.get("MainFrame.Transactions.Preferences.changeColor")); //$NON-NLS-1$
			jButton.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.changeColor.tooltip")); //$NON-NLS-1$
			jButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color c = localizedColorChooser(positiveBalanceReport.getForeground());
					if (c!=null) {
						positiveBalanceReport.setForeground(c);
					}
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText(LocalizationData.get("MainFrame.Transactions.Preferences.changeColor")); //$NON-NLS-1$
			jButton1.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.changeColor.tooltip")); //$NON-NLS-1$
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color c = localizedColorChooser(negativeBalanceReport.getForeground());
					if (c!=null) {
						negativeBalanceReport.setForeground(c);
					}
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes setTodefault	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSetTodefault() {
		if (setTodefault == null) {
			setTodefault = new JButton();
			setTodefault.setText(LocalizationData.get("MainFrame.Transactions.Preferences.setDefault")); //$NON-NLS-1$
			setTodefault.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.setDefault.tooltip")); //$NON-NLS-1$
			setTodefault.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					negativeBalanceReport.setForeground(DEFAULT_NEGATIVE);
					positiveBalanceReport.setForeground(DEFAULT_POSITIVE);
					separeCommentChkBx.setSelected(false);
					getChckBxCustomBackground().setSelected(true);
					expenseColor = DEFAULT_CASHOUT;
					receiptColor = DEFAULT_CASHIN;
					getTableModel().refresh();
				}
			});
		}
		return setTodefault;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setBorder(null);
			GridBagLayout gbl_jPanel1 = new GridBagLayout();
			jPanel1.setLayout(gbl_jPanel1);
			GridBagConstraints gbc_separeCommentChkBx = new GridBagConstraints();
			gbc_separeCommentChkBx.weightx = 1.0;
			gbc_separeCommentChkBx.anchor = GridBagConstraints.WEST;
			gbc_separeCommentChkBx.gridwidth = 0;
			gbc_separeCommentChkBx.insets = new Insets(5, 5, 5, 0);
			gbc_separeCommentChkBx.gridx = 0;
			gbc_separeCommentChkBx.gridy = 0;
			jPanel1.add(getSeparateCommentChkBx(), gbc_separeCommentChkBx);
			GridBagConstraints gbc_separateExpenseReceiptChckbx = new GridBagConstraints();
			gbc_separateExpenseReceiptChckbx.gridwidth = 0;
			gbc_separateExpenseReceiptChckbx.anchor = GridBagConstraints.WEST;
			gbc_separateExpenseReceiptChckbx.insets = new Insets(0, 5, 0, 5);
			gbc_separateExpenseReceiptChckbx.gridx = 0;
			gbc_separateExpenseReceiptChckbx.gridy = 1;
			jPanel1.add(getSeparateExpenseReceiptChckbx(), gbc_separateExpenseReceiptChckbx);
			GridBagConstraints gbc_chckBxCustomBackground = new GridBagConstraints();
			gbc_chckBxCustomBackground.gridwidth = 0;
			gbc_chckBxCustomBackground.anchor = GridBagConstraints.WEST;
			gbc_chckBxCustomBackground.insets = new Insets(5, 5, 5, 0);
			gbc_chckBxCustomBackground.gridx = 0;
			gbc_chckBxCustomBackground.gridy = 2;
			jPanel1.add(getChckBxCustomBackground(), gbc_chckBxCustomBackground);
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.weightx = 1.0;
			gbc_scrollPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_scrollPane.gridheight = 2;
			gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 3;
			jPanel1.add(getScrollPane(), gbc_scrollPane);
			GridBagConstraints gbc_btnReceipt = new GridBagConstraints();
			gbc_btnReceipt.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnReceipt.anchor = GridBagConstraints.WEST;
			gbc_btnReceipt.insets = new Insets(0, 0, 0, 5);
			gbc_btnReceipt.gridx = 1;
			gbc_btnReceipt.gridy = 4;
			jPanel1.add(getBtnReceipt(), gbc_btnReceipt);
			GridBagConstraints gbc_btnExpense = new GridBagConstraints();
			gbc_btnExpense.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnExpense.insets = new Insets(0, 0, 5, 5);
			gbc_btnExpense.anchor = GridBagConstraints.WEST;
			gbc_btnExpense.gridx = 1;
			gbc_btnExpense.gridy = 3;
			jPanel1.add(getBtnExpense(), gbc_btnExpense);
		}
		return jPanel1;
	}

	private JCheckBox getSeparateCommentChkBx() {
		if (separeCommentChkBx == null) {
			separeCommentChkBx = new JCheckBox();
			separeCommentChkBx.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					getTableModel().refresh();
				}
			});
			separeCommentChkBx.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.commentDisplay.tooltip")); //$NON-NLS-1$
			separeCommentChkBx.setText(LocalizationData.get("MainFrame.Transactions.Preferences.commentDisplay")); //$NON-NLS-1$
			separeCommentChkBx.setSelected(initialSeparateCommentState);
		}
		return separeCommentChkBx;
	}

	private Color localizedColorChooser(Color initialColor) {
		//FIXME The JColorChooser locale is wrong, it's always the system default locale
		//This is a JRE known bug fixed in java 7 (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6524757)
		//TODO test with JRE 7
		//TODO probably better to have a customized panel with a BalanceReport field
		Color c = JColorChooser.showDialog(jButton, LocalizationData.get("MainFrame.Transactions.Preferences.ChooseColorDialog.title"), initialColor); //$NON-NLS-1$
		return c;
	}
	
	private JTable getTable() {
		if (table == null) {
			table = new JTable(getTableModel());
			table.setPreferredScrollableViewportSize (new Dimension(500, table.getRowCount() * table.getRowHeight()));
			table.setDefaultRenderer(Object.class, new ObjectRenderer());
		}
		return table;
	}
	
	private MyTableModel getTableModel() {
		if (tableModel==null) {
			tableModel = new MyTableModel();
		}
		return tableModel;
	}
	
	@SuppressWarnings("serial")
	class MyTableModel extends AbstractTableModel implements ColoredModel {
		private int getCommentColumnIndex() {
			return getSeparateCommentChkBx().isSelected() ? 1 : -1;
		}
		private int getReceiptColumnIndex() {
			if (!getSeparateExpenseReceiptChckbx().isSelected()) return -1;
			return getSeparateCommentChkBx().isSelected() ? 2:1;
		}
		private int getExpenseColumnIndex() {
			if (!getSeparateExpenseReceiptChckbx().isSelected()) return -1;
			return getSeparateCommentChkBx().isSelected() ? 3:2;
		}
		
		private boolean isExpense (int rowIndex) {
			return rowIndex==0;
		}

		@Override
		public int getRowCount() {
			return 2;
		}

		@Override
		public int getColumnCount() {
			int nb = 2;
			if (getSeparateCommentChkBx().isSelected()) nb++;
			if (getSeparateExpenseReceiptChckbx().isSelected()) nb++;
			return nb;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column==0) {
				String result = isExpense(row)?LocalizationData.get("MainFrame.Transactions.Preferences.expenseSample"):LocalizationData.get("MainFrame.Transactions.Preferences.receiptSample"); //$NON-NLS-1$ //$NON-NLS-2$
				if (!getSeparateCommentChkBx().isSelected()) result = result+" ("+LocalizationData.get("Transaction.comment")+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return result;
			} else if (column==getCommentColumnIndex()) {
				return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
			} else if (column==getReceiptColumnIndex()) {
				return isExpense(row) ? "" : "100";
			} else if (column==getExpenseColumnIndex()) {
				return isExpense(row) ? "-100" : "";
			} else {
				return isExpense(row) ? "-100" : "100";
			}
		}

		@Override
		public void setRowLook(Component renderer, javax.swing.JTable table, int row, boolean isSelected) {
				renderer.setForeground(table.getForeground());
				if (getChckBxCustomBackground().isSelected()) {
					renderer.setBackground(isExpense(row) ? expenseColor : receiptColor);
				} else {
					renderer.setBackground(table.getBackground());
				}
		}

		@Override
		public int getAlignment(int column) {
			return SwingConstants.CENTER;
		}
		
		public void refresh() {
			fireTableStructureChanged();
		}

		@Override
		public String getColumnName(int column) {
			if (column==0) return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
			if (column==getCommentColumnIndex()) return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
			if (column==getReceiptColumnIndex()) return LocalizationData.get("StatementView.receipt"); //$NON-NLS-1$
			if (column==getExpenseColumnIndex()) return LocalizationData.get("StatementView.debt"); //$NON-NLS-1$
			return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$;
		}
	}
	
	private JCheckBox getChckBxCustomBackground() {
		if (chckBxCustomBackground == null) {
			chckBxCustomBackground = new JCheckBox();
			chckBxCustomBackground.setText(LocalizationData.get("MainFrame.Transactions.Preferences.customBackground.title")); //$NON-NLS-1$
			chckBxCustomBackground.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.customBackground.tooltip")); //$NON-NLS-1$
			chckBxCustomBackground.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					getTableModel().refresh();
					getBtnExpense().setEnabled(getChckBxCustomBackground().isSelected());
					getBtnReceipt().setEnabled(getChckBxCustomBackground().isSelected());
				}
			});
			chckBxCustomBackground.setSelected(isCustomBackgroundColors());
		}
		return chckBxCustomBackground;
	}
	private JButton getBtnExpense() {
		if (btnExpense == null) {
			btnExpense = new JButton(LocalizationData.get("MainFrame.Transactions.Preferences.changeExpenseBackground.title")); //$NON-NLS-1$
			btnExpense.setEnabled(false);
			btnExpense.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.changeExpenseBackground.tooltip")); //$NON-NLS-1$
			btnExpense.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color c = localizedColorChooser(expenseColor);
					if (c!=null) {
						expenseColor = c;
						getTableModel().refresh();
					}
				}
			});
		}
		return btnExpense;
	}
	private JButton getBtnReceipt() {
		if (btnReceipt == null) {
			btnReceipt = new JButton(LocalizationData.get("MainFrame.Transactions.Preferences.changeReceiptBackground.title")); //$NON-NLS-1$
			btnReceipt.setEnabled(false);
			btnReceipt.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.changeReceiptBackground.tooltip")); //$NON-NLS-1$
			btnReceipt.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color c = localizedColorChooser(receiptColor);
					if (c!=null) {
						receiptColor = c;
						getTableModel().refresh();
					}
				}
			});
		}
		return btnReceipt;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getTable());
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		}
		return scrollPane;
	}
	private JCheckBox getSeparateExpenseReceiptChckbx() {
		if (separateExpenseReceiptChckbx == null) {
			separateExpenseReceiptChckbx = new JCheckBox(LocalizationData.get("MainFrame.Transactions.Preferences.separateExpenseReceipt")); //$NON-NLS-1$
			separateExpenseReceiptChckbx.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					getTableModel().refresh();
				}
			});
			separateExpenseReceiptChckbx.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.separateExpenseReceipt.tooltip")); //$NON-NLS-1$
			separateExpenseReceiptChckbx.setSelected(initialSeparateReceiptExpense);

		}
		return separateExpenseReceiptChckbx;
	}
}  //  @jve:decl-index=0:visual-constraint="64,14"
