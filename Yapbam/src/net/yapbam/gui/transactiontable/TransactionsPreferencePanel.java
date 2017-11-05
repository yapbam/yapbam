package net.yapbam.gui.transactiontable;

import java.awt.GridBagLayout;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dialogs.preferences.PreferencePanel;
import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

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

/*Le GenericTransactionModel semble utilisé un peu partout, à voir ...
 */

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
	private Color expenseColor;
	private Color receiptColor;
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
		Color[] colors = getBackgroundColors();
		this.expenseColor = colors[0]!=null?colors[0]:DEFAULT_CASHOUT;
		this.receiptColor = colors[1]!=null?colors[1]:DEFAULT_CASHIN;

		initialize();
	}

	public static boolean isCommentSeparatedFromDescription() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(SEPARATE_COMMENT));
	}
	
	public static boolean isReceiptSeparatedFromExpense() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(SEPARATE_RECEIPT_EXPENSE));
	}
	
	public static boolean isCustomBackgroundColors() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(TransactionsPreferencePanel.CUSTOMIZED_BACKGROUND_KEY, "true")); //$NON-NLS-1$
	}
	
	/** Gets the transaction's table background colors.
	 * @return an array of two colors. The first one is the expense color, the second is the receipt color.
	 * Both are null if the default background should be used. 
	 */
	public static Color[] getBackgroundColors() {
		Color receipt = null;
		Color expense = null;
		if (isCustomBackgroundColors()) { 
			try {
				receipt = new Color(Integer.parseInt(Preferences.INSTANCE.getProperty(RECEIPT_BACKGROUND_COLOR_KEY)));
				expense = new Color(Integer.parseInt(Preferences.INSTANCE.getProperty(EXPENSE_BACKGROUND_COLOR_KEY)));
			} catch (NumberFormatException e) {
				receipt = DEFAULT_CASHIN;
				expense = DEFAULT_CASHOUT;
			}
		}
		return new Color[]{expense, receipt};
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
		GridBagConstraints gbcSetTodefault = new GridBagConstraints();
		gbcSetTodefault.anchor = GridBagConstraints.SOUTHEAST;
		gbcSetTodefault.weighty = 1.0;
		gbcSetTodefault.weightx = 1.0;
		gbcSetTodefault.insets = new Insets(0, 0, 5, 5);
		gbcSetTodefault.gridx = 0;
		gbcSetTodefault.gridy = 2;
		add(getSetTodefault(), gbcSetTodefault);
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
		Color[] oldColors = getBackgroundColors();
		boolean bckHasChange = (oldColors[0]!=null) && !(oldColors[1].equals(receiptColor) && oldColors[0].equals(expenseColor));
		if (positive.equals(BalanceReportField.POSITIVE_COLOR) && negative.equals(BalanceReportField.NEGATIVE_COLOR)
				&& (separeCommentChkBx.isSelected()==initialSeparateCommentState) && (getSeparateExpenseReceiptChckbx().isSelected()==initialSeparateReceiptExpense) &&
				(getChckBxCustomBackground().isSelected()==(oldColors[0]!=null)) && !bckHasChange) {
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
			GridBagLayout gblJPanel1 = new GridBagLayout();
			jPanel1.setLayout(gblJPanel1);
			GridBagConstraints gbcSepareCommentChkBx = new GridBagConstraints();
			gbcSepareCommentChkBx.weightx = 1.0;
			gbcSepareCommentChkBx.anchor = GridBagConstraints.WEST;
			gbcSepareCommentChkBx.gridwidth = 0;
			gbcSepareCommentChkBx.insets = new Insets(5, 5, 5, 0);
			gbcSepareCommentChkBx.gridx = 0;
			gbcSepareCommentChkBx.gridy = 0;
			jPanel1.add(getSeparateCommentChkBx(), gbcSepareCommentChkBx);
			GridBagConstraints gbcSeparateExpenseReceiptChckbx = new GridBagConstraints();
			gbcSeparateExpenseReceiptChckbx.gridwidth = 0;
			gbcSeparateExpenseReceiptChckbx.anchor = GridBagConstraints.WEST;
			gbcSeparateExpenseReceiptChckbx.insets = new Insets(0, 5, 0, 5);
			gbcSeparateExpenseReceiptChckbx.gridx = 0;
			gbcSeparateExpenseReceiptChckbx.gridy = 1;
			jPanel1.add(getSeparateExpenseReceiptChckbx(), gbcSeparateExpenseReceiptChckbx);
			GridBagConstraints gbcChckBxCustomBackground = new GridBagConstraints();
			gbcChckBxCustomBackground.gridwidth = 0;
			gbcChckBxCustomBackground.anchor = GridBagConstraints.WEST;
			gbcChckBxCustomBackground.insets = new Insets(5, 5, 5, 0);
			gbcChckBxCustomBackground.gridx = 0;
			gbcChckBxCustomBackground.gridy = 2;
			jPanel1.add(getChckBxCustomBackground(), gbcChckBxCustomBackground);
			GridBagConstraints gbcScrollPane = new GridBagConstraints();
			gbcScrollPane.weightx = 1.0;
			gbcScrollPane.fill = GridBagConstraints.HORIZONTAL;
			gbcScrollPane.gridheight = 2;
			gbcScrollPane.insets = new Insets(0, 0, 0, 5);
			gbcScrollPane.gridx = 0;
			gbcScrollPane.gridy = 3;
			jPanel1.add(getScrollPane(), gbcScrollPane);
			GridBagConstraints gbcBtnReceipt = new GridBagConstraints();
			gbcBtnReceipt.fill = GridBagConstraints.HORIZONTAL;
			gbcBtnReceipt.anchor = GridBagConstraints.WEST;
			gbcBtnReceipt.insets = new Insets(0, 0, 0, 5);
			gbcBtnReceipt.gridx = 1;
			gbcBtnReceipt.gridy = 4;
			jPanel1.add(getBtnReceipt(), gbcBtnReceipt);
			GridBagConstraints gbcBtnExpense = new GridBagConstraints();
			gbcBtnExpense.fill = GridBagConstraints.HORIZONTAL;
			gbcBtnExpense.insets = new Insets(0, 0, 5, 5);
			gbcBtnExpense.anchor = GridBagConstraints.WEST;
			gbcBtnExpense.gridx = 1;
			gbcBtnExpense.gridy = 3;
			jPanel1.add(getBtnExpense(), gbcBtnExpense);
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
		return JColorChooser.showDialog(jButton, LocalizationData.get("MainFrame.Transactions.Preferences.ChooseColorDialog.title"), initialColor); //$NON-NLS-1$
	}
	
	private JTable getTable() {
		if (table == null) {
			table = new MyTable();
		}
		return table;
	}
	
	private MyTableModel getTableModel() {
		if (tableModel==null) {
			tableModel = new MyTableModel();
		}
		return tableModel;
	}
	
	class MyTable extends JTable implements PaintedTable {
		private static final long serialVersionUID = 1L;
		private TablePainter painter = new TablePainter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setRowLook(Component renderer, javax.swing.JTable table, int row, boolean isSelected) {
				renderer.setForeground(table.getForeground());
				if (getChckBxCustomBackground().isSelected()) {
					renderer.setBackground(((MyTableModel)table.getModel()).isExpense(row) ? expenseColor : receiptColor);
				} else {
					renderer.setBackground(table.getBackground());
				}
			}
			
			@Override
			public int getAlignment(int column) {
				int firstNumberColumn = getSeparateCommentChkBx().isSelected()?2:1;
				return column<firstNumberColumn?SwingConstants.LEFT:SwingConstants.RIGHT;
			}
		};
		MyTable() {
			super(getTableModel());
			setPreferredScrollableViewportSize (new Dimension(500, getRowCount() * getRowHeight()));
			setDefaultRenderer(Object.class, new ObjectRenderer());
		}

		@Override
		public TablePainter getPainter() {
			return painter;
		}
	}
	
	@SuppressWarnings("serial")
	class MyTableModel extends AbstractTableModel {
		private int getCommentColumnIndex() {
			return getSeparateCommentChkBx().isSelected() ? 1 : -1;
		}
		private int getReceiptColumnIndex() {
			if (!getSeparateExpenseReceiptChckbx().isSelected()) {
				return -1;
			}
			return getSeparateCommentChkBx().isSelected() ? 2:1;
		}
		private int getExpenseColumnIndex() {
			if (!getSeparateExpenseReceiptChckbx().isSelected()) {
				return -1;
			}
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
			if (getSeparateCommentChkBx().isSelected()) {
				nb++;
			}
			if (getSeparateExpenseReceiptChckbx().isSelected()) {
				nb++;
			}
			return nb;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column==0) {
				String result = isExpense(row)?LocalizationData.get("MainFrame.Transactions.Preferences.expenseSample"):LocalizationData.get("MainFrame.Transactions.Preferences.receiptSample"); //$NON-NLS-1$ //$NON-NLS-2$
				if (!getSeparateCommentChkBx().isSelected()) {
					result = result+" ("+LocalizationData.get("Transaction.comment")+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				return result;
			} else if (column==getCommentColumnIndex()) {
				return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
			} else if (column==getReceiptColumnIndex()) {
				return isExpense(row) ? "" : "100"; //$NON-NLS-1$
			} else if (column==getExpenseColumnIndex()) {
				return isExpense(row) ? "-100" : ""; //$NON-NLS-2$
			} else {
				return isExpense(row) ? "-100" : "100";
			}
		}
		
		public void refresh() {
			fireTableStructureChanged();
		}

		@Override
		public String getColumnName(int column) {
			if (column==0) {
				return LocalizationData.get("Transaction.description"); //$NON-NLS-1$
			} else if (column==getCommentColumnIndex()) {
				return LocalizationData.get("Transaction.comment"); //$NON-NLS-1$
			} else if (column==getReceiptColumnIndex()) {
				return LocalizationData.get("StatementView.receipt"); //$NON-NLS-1$
			} else if (column==getExpenseColumnIndex()) {
				return LocalizationData.get("StatementView.debt"); //$NON-NLS-1$
			} else {
				return LocalizationData.get("Transaction.amount"); //$NON-NLS-1$;
			}
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
}

