package net.yapbam.gui.transactiontable;

import java.awt.GridBagLayout;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;

import java.awt.Component;
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
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JLabel;

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
	
	private boolean initialSeparateCommentState;
	
	static String NEGATIVE_KEY = "net.yapbam.balanceReport.negative"; //$NON-NLS-1$
	static String POSITIVE_KEY = "net.yapbam.balanceReport.positive"; //$NON-NLS-1$
	static String SEPARATE_COMMENT = "net.yapbam.transactionTable.separateDescriptionAndComment"; //$NON-NLS-1$
	static Color DEFAULT_POSITIVE = new Color(0,200,0);
	static Color DEFAULT_NEGATIVE = Color.RED;
	private JTable table;
	private JCheckBox chckBxCustomBackground;
	private JButton btnExpense;
	private JButton btnReceipt;
	private JLabel lblNewLabel;
		
	/**
	 * This is the default constructor
	 */
	public TransactionsPreferencePanel() {
		super();
		this.initialSeparateCommentState = isCommentSeparatedFromDescription();
		initialize();
	}

	public static boolean isCommentSeparatedFromDescription() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(SEPARATE_COMMENT));
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
		if (positive.equals(BalanceReportField.POSITIVE_COLOR) && negative.equals(BalanceReportField.NEGATIVE_COLOR)
				&& (separeCommentChkBx.isSelected()==initialSeparateCommentState)) {
			return false;
		}
		BalanceReportField.POSITIVE_COLOR = positive;
		BalanceReportField.NEGATIVE_COLOR = negative;
		Preferences.INSTANCE.setProperty(TransactionsPreferencePanel.POSITIVE_KEY, Integer.toString(positive.getRGB()));
		Preferences.INSTANCE.setProperty(TransactionsPreferencePanel.NEGATIVE_KEY, Integer.toString(negative.getRGB()));
		Preferences.INSTANCE.setProperty(TransactionsPreferencePanel.SEPARATE_COMMENT, Boolean.toString(separeCommentChkBx.isSelected()));
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
					Color c = localizedColorChooser();
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
					Color c = localizedColorChooser();
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
			gbl_jPanel1.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0};
			gbl_jPanel1.columnWeights = new double[]{1.0, 0.0, 0.0};
			jPanel1.setLayout(gbl_jPanel1);
			GridBagConstraints gbc_chckBxCustomBackground = new GridBagConstraints();
			gbc_chckBxCustomBackground.gridwidth = 0;
			gbc_chckBxCustomBackground.anchor = GridBagConstraints.WEST;
			gbc_chckBxCustomBackground.insets = new Insets(5, 5, 5, 0);
			gbc_chckBxCustomBackground.gridx = 0;
			gbc_chckBxCustomBackground.gridy = 0;
			jPanel1.add(getChckBxCustomBackground(), gbc_chckBxCustomBackground);
			GridBagConstraints gbc_table = new GridBagConstraints();
			gbc_table.gridheight = 2;
			gbc_table.fill = GridBagConstraints.HORIZONTAL;
			gbc_table.weightx = 0.2;
			gbc_table.insets = new Insets(0, 10, 5, 10);
			gbc_table.gridx = 0;
			gbc_table.gridy = 1;
			jPanel1.add(getTable(), gbc_table);
			GridBagConstraints gbc_btnReceipt = new GridBagConstraints();
			gbc_btnReceipt.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnReceipt.anchor = GridBagConstraints.WEST;
			gbc_btnReceipt.insets = new Insets(0, 0, 5, 0);
			gbc_btnReceipt.gridx = 1;
			gbc_btnReceipt.gridy = 2;
			jPanel1.add(getBtnReceipt(), gbc_btnReceipt);
			separeCommentChkBx = new JCheckBox();
			GridBagConstraints gbc_separeCommentChkBx = new GridBagConstraints();
			gbc_separeCommentChkBx.weightx = 1.0;
			gbc_separeCommentChkBx.anchor = GridBagConstraints.WEST;
			gbc_separeCommentChkBx.gridwidth = 0;
			gbc_separeCommentChkBx.insets = new Insets(10, 5, 10, 0);
			gbc_separeCommentChkBx.gridx = 0;
			gbc_separeCommentChkBx.gridy = 3;
			jPanel1.add(separeCommentChkBx, gbc_separeCommentChkBx);
			separeCommentChkBx.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.commentDisplay.tooltip")); //$NON-NLS-1$
			separeCommentChkBx.setText(LocalizationData.get("MainFrame.Transactions.Preferences.commentDisplay")); //$NON-NLS-1$
			separeCommentChkBx.setSelected(initialSeparateCommentState);
			GridBagConstraints gbc_btnExpense = new GridBagConstraints();
			gbc_btnExpense.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnExpense.insets = new Insets(0, 0, 5, 0);
			gbc_btnExpense.anchor = GridBagConstraints.WEST;
			gbc_btnExpense.gridx = 1;
			gbc_btnExpense.gridy = 1;
			jPanel1.add(getBtnExpense(), gbc_btnExpense);
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.weightx = 1.0;
			gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
			gbc_lblNewLabel.gridx = 2;
			gbc_lblNewLabel.gridy = 1;
			jPanel1.add(getLblNewLabel(), gbc_lblNewLabel);
		}
		return jPanel1;
	}

	private Color localizedColorChooser() {
		//FIXME The JColorChooser locale is wrong, it's always the system default locale
		//This is a JRE known bug fixed in java 7 (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6524757)
		//TODO test with JRE 7
		//TODO probably better to have a customized panel with a BalanceReport field
		Color c = JColorChooser.showDialog(jButton, LocalizationData.get("MainFrame.Transactions.Preferences.ChooseColorDialog.title"), BalanceReportField.POSITIVE_COLOR); //$NON-NLS-1$
		return c;
	}
	
	private JTable getTable() {
		if (table == null) {
			table = new JTable(new MyTableModel());
			table.setBorder(new LineBorder(new Color(0, 0, 0)));
			table.setDefaultRenderer(Object.class, new ObjectRenderer());
		}
		return table;
	}
	
	@SuppressWarnings("serial")
	class MyTableModel extends AbstractTableModel implements ColoredModel {
		@Override
		public int getRowCount() {
			return 2;
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column==0) {
				return row==0?"Expense":"Receipt";
			} else {
				return null;
			}
		}

		@Override
		public void setRowLook(Component renderer, JTable table, int row, boolean isSelected) {
			if (isSelected) {
				renderer.setBackground(table.getSelectionBackground());
				renderer.setForeground(table.getSelectionForeground());
			} else {
				boolean expense = row==0;
				renderer.setForeground(table.getForeground());
				if (getChckBxCustomBackground().isSelected()) {
					renderer.setBackground(expense ? GenericTransactionTableModel.CASHOUT : GenericTransactionTableModel.CASHIN);
				} else {
					renderer.setBackground(table.getBackground());
				}
			}
		}

		@Override
		public int getAlignment(int column) {
			return SwingConstants.CENTER;
		}
		
		public void refresh() {
			fireTableDataChanged();
		}
	}
	
	private JCheckBox getChckBxCustomBackground() {
		if (chckBxCustomBackground == null) {
			chckBxCustomBackground = new JCheckBox("Use custom background color");
			chckBxCustomBackground.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					((MyTableModel)getTable().getModel()).refresh();
					getBtnExpense().setEnabled(getChckBxCustomBackground().isSelected());
					getBtnReceipt().setEnabled(getChckBxCustomBackground().isSelected());
				}
			});
			chckBxCustomBackground.setToolTipText("Check this box to have custom background colors");
		}
		return chckBxCustomBackground;
	}
	private JButton getBtnExpense() {
		if (btnExpense == null) {
			btnExpense = new JButton("Change expense background");
			btnExpense.setEnabled(false);
			btnExpense.setToolTipText("Click this button to choose the expense background");
		}
		return btnExpense;
	}
	private JButton getBtnReceipt() {
		if (btnReceipt == null) {
			btnReceipt = new JButton("Change receipt background");
			btnReceipt.setEnabled(false);
			btnReceipt.setToolTipText("Click this button to choose the reicept background");
		}
		return btnReceipt;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel();
		}
		return lblNewLabel;
	}
}  //  @jve:decl-index=0:visual-constraint="64,14"
