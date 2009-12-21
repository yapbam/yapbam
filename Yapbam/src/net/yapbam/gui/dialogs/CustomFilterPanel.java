package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.Insets;

import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AmountWidget;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JTextField;
import net.yapbam.gui.widget.DateWidgetPanel;

public class CustomFilterPanel extends JPanel { //LOCAL

	private static final long serialVersionUID = 1L;
	public static final String CONSISTENCY_PROPERTY = "CONSISTENCY";
	private JPanel accountPanel = null;
	private JList accountList = null;
	private JPanel amountPanel = null;
	private JPanel categoryPanel = null;
	private JList categoryList = null;
	private JCheckBox receipt = null;
	private JCheckBox expense = null;
	private JPanel jPanel3 = null;
	private JRadioButton amountEquals = null;
	private JRadioButton amountBetween = null;
	private AmountWidget minAmount = null;
	private JLabel jLabel = null;
	private AmountWidget maxAmount = null;
	private JRadioButton amountAll = null;
	
	private FilteredData data;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	/**
	 * This is the default constructor
	 */
	public CustomFilterPanel() {
		this(new FilteredData(new GlobalData()));
	}
	
	public CustomFilterPanel(FilteredData data) {
		super();
		this.data = data;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.gridy = 2;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.fill = GridBagConstraints.BOTH;
		gridBagConstraints15.gridy = 1;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.weightx = 1.0D;
		gridBagConstraints3.weighty = 2.0D;
		gridBagConstraints3.gridy = 4;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints2.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 400);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(300, 400));
		this.add(getAccountPanel(), gridBagConstraints);
		this.add(getAmountPanel(), gridBagConstraints2);
		this.add(getCategoryPanel(), gridBagConstraints3);
		this.add(getDescriptionPanel(), gridBagConstraints15);
		this.add(getDatePanel(), gridBagConstraints21);
	}

	/**
	 * This method initializes accountPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAccountPanel() {
		if (accountPanel == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.BOTH;
			gridBagConstraints14.weighty = 1.0;
			gridBagConstraints14.gridx = 2;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.weightx = 1.0;
			accountPanel = new JPanel();
			accountPanel.setLayout(new GridBagLayout());
			accountPanel.setBorder(BorderFactory.createTitledBorder(null, "Comptes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			accountPanel.add(getJScrollPane(), gridBagConstraints14);
		}
		return accountPanel;
	}

	/**
	 * This method initializes accountList	
	 * 	
	 * @return javax.swing.JList	
	 */
	@SuppressWarnings("serial")
	private JList getAccountList() {
		if (accountList == null) {
			accountList = new JList();
			accountList.setModel(new AbstractListModel(){
				public Object getElementAt(int index) {
					return data.getGlobalData().getAccount(index).getName();
				}
				public int getSize() {
					return data.getGlobalData().getAccountsNumber();
				}
			});
			ArrayList<Integer> indices = new ArrayList<Integer>(data.getGlobalData().getAccountsNumber()); 
			for (int i=0;i<data.getGlobalData().getAccountsNumber();i++) {
				if (data.isOk(data.getGlobalData().getAccount(i))) indices.add(i);
			}
			int[] selection = new int[indices.size()];
			for (int i = 0; i < indices.size(); i++) {
				selection[i] = indices.get(i);
			}
			accountList.setSelectedIndices(selection);
		}
		return accountList;
	}

	/**
	 * This method initializes amountPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAmountPanel() {
		if (amountPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 5;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 0.0D;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints7.gridy = 1;
			amountPanel = new JPanel();
			amountPanel.setLayout(new GridBagLayout());
			amountPanel.setBorder(BorderFactory.createTitledBorder(null, "Montant", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			amountPanel.add(getJPanel3(), gridBagConstraints7);
			amountPanel.add(getExpense(), gridBagConstraints6);
			amountPanel.add(getReceipt(), gridBagConstraints5);
		}
		return amountPanel;
	}

	/**
	 * This method initializes categoryPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCategoryPanel() {
		if (categoryPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			categoryPanel = new JPanel();
			categoryPanel.setLayout(new GridBagLayout());
			categoryPanel.setBorder(BorderFactory.createTitledBorder(null, "Cat�gories", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			categoryPanel.add(getJScrollPane1(), gridBagConstraints1);
		}
		return categoryPanel;
	}

	/**
	 * This method initializes categoryList	
	 * 	
	 * @return javax.swing.JList	
	 */
	@SuppressWarnings("serial")
	private JList getCategoryList() {
		if (categoryList == null) {
			categoryList = new JList();
			categoryList.setModel(new AbstractListModel(){
				public Object getElementAt(int index) {
					return data.getGlobalData().getCategory(index).getName();
				}
				public int getSize() {
					return data.getGlobalData().getCategoriesNumber();
				}
			});
			ArrayList<Integer> indices = new ArrayList<Integer>(data.getGlobalData().getCategoriesNumber()); 
			for (int i=0;i<data.getGlobalData().getCategoriesNumber();i++) {
				if (data.isOk(data.getGlobalData().getCategory(i))) indices.add(i);
			}
			int[] selection = new int[indices.size()];
			for (int i = 0; i < indices.size(); i++) {
				selection[i] = indices.get(i);
			}
			categoryList.setSelectedIndices(selection);
		}
		return categoryList;
	}

	/**
	 * This method initializes receipt	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getReceipt() {
		if (receipt == null) {
			receipt = new JCheckBox();
			receipt.setText("Recettes");
			receipt.setSelected(data.isOk(FilteredData.RECEIPT));
			receipt.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					checkConsistency();
				}
			});
		}
		return receipt;
	}

	/**
	 * This method initializes expense	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getExpense() {
		if (expense == null) {
			expense = new JCheckBox();
			expense.setText("D�penses");
			expense.setSelected(data.isOk(FilteredData.EXPENSE));
			expense.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					checkConsistency();
				}
			});
		}
		return expense;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridheight = 3;
			gridBagConstraints12.gridx = 3;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.gridheight = 3;
			gridBagConstraints11.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints11.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("et");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.gridheight = 3;
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 1;
			jPanel3 = new JPanel();
			jPanel3.setLayout(new GridBagLayout());
			jPanel3.add(getAmountEquals(), gridBagConstraints9);
			jPanel3.add(getAmountBetween(), gridBagConstraints10);
			jPanel3.add(getMinAmount(), gridBagConstraints8);
			jPanel3.add(jLabel, gridBagConstraints11);
			jPanel3.add(getMaxAmount(), gridBagConstraints12);
			jPanel3.add(getAmountAll(), gridBagConstraints13);
			ButtonGroup group = new ButtonGroup();
			group.add(getAmountAll());
			group.add(getAmountEquals());
			group.add(getAmountBetween());
		}
		return jPanel3;
	}

	/**
	 * This method initializes amountEquals	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAmountEquals() {
		if (amountEquals == null) {
			amountEquals = new JRadioButton();
			amountEquals.setText("Egal �");
			amountEquals.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountEquals.isSelected()) {
						getMinAmount().setEnabled(true);
						getMaxAmount().setEnabled(false);
					}
				}
			});
		}
		return amountEquals;
	}

	/**
	 * This method initializes amountBetween	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAmountBetween() {
		if (amountBetween == null) {
			amountBetween = new JRadioButton();
			amountBetween.setText("Compris entre");
			amountBetween.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountBetween.isSelected()) {
						getMinAmount().setEnabled(true);
						getMaxAmount().setEnabled(true);
					}
				}
			});
		}
		return amountBetween;
	}

	/**
	 * This method initializes minAmount	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private AmountWidget getMinAmount() {
		if (minAmount == null) {
			minAmount = new AmountWidget(LocalizationData.getLocale());
			minAmount.setColumns(6);
		}
		return minAmount;
	}

	/**
	 * This method initializes maxAmount	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private AmountWidget getMaxAmount() {
		if (maxAmount == null) {
			maxAmount = new AmountWidget(LocalizationData.getLocale());
			maxAmount.setColumns(6);
		}
		return maxAmount;
	}

	/**
	 * This method initializes amountAll	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAmountAll() {
		if (amountAll == null) {
			amountAll = new JRadioButton();
			amountAll.setText("Tous");
			amountAll.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountAll.isSelected()) {
						getMinAmount().setEnabled(false);
						getMaxAmount().setEnabled(false);
					}
				}
			});
		}
		return amountAll;
	}

	/** Apply the filter currently defined in this panel to the FilteredData.
	 */
	public void apply() {
		int[] accountIndices = this.accountList.getSelectedIndices();
		Account[] accounts = new Account[accountIndices.length];
		for (int i = 0; i < accounts.length; i++) {
			accounts[i] = data.getGlobalData().getAccount(accountIndices[i]);
		}
		this.data.setAccounts(accounts);
		int[] categoryIndices = this.categoryList.getSelectedIndices();
		Category[] categories = new Category[categoryIndices.length];
		for (int i = 0; i < categories.length; i++) {
			categories[i] = data.getGlobalData().getCategory(categoryIndices[i]);
		}
		this.data.setCategories(categories);
		int filter = 0;
		if (getExpense().isSelected()) filter += FilteredData.EXPENSE;
		if (getReceipt().isSelected()) filter += FilteredData.RECEIPT;
		this.data.setFilter(filter);
		// TODO Auto-generated method stub
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getAccountList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getCategoryList());
		}
		return jScrollPane1;
	}

	/** Gets the reason why the filter defined by this panel current state is inconsitent.
	 * @return A string that explains the problem, or null if the state is consistent.
	 */
	public String getInconsistencyCause() {
		if (!getExpense().isSelected() && !getReceipt().isSelected()) return "Interdire � la fois toutes les recettes et toutes les d�penses n'est pas autoris�";
		return null;
	}

	public boolean isConsistent() {
		return (getExpense().isSelected() || getReceipt().isSelected());
	}
	
	private boolean oldConsistency;
	private JPanel descriptionPanel = null;
	private JCheckBox ignoreCase = null;
	private JCheckBox regularExpression = null;
	private JTextField description = null;
	private JPanel datePanel = null;
	private JRadioButton dateAll = null;
	private JRadioButton dateEquals = null;
	private JRadioButton dateBetween = null;
	private DateWidgetPanel dateFrom = null;
	private JLabel jLabel1 = null;
	private DateWidgetPanel dateTo = null;
	private void checkConsistency() {
		boolean ok = isConsistent();
		if (ok!=oldConsistency) {
			oldConsistency = ok;
			firePropertyChange(CONSISTENCY_PROPERTY, !ok, ok);
		}
	}

	/**
	 * This method initializes descriptionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints18.gridy = 1;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.gridwidth = 0;
			gridBagConstraints18.gridx = 0;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.gridy = 0;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.gridy = 0;
			descriptionPanel = new JPanel();
			descriptionPanel.setLayout(new GridBagLayout());
			descriptionPanel.setBorder(BorderFactory.createTitledBorder(null, "Libell�", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			descriptionPanel.add(getIgnoreCase(), gridBagConstraints16);
			descriptionPanel.add(getRegularExpression(), gridBagConstraints17);
			descriptionPanel.add(getDescription(), gridBagConstraints18);
		}
		return descriptionPanel;
	}

	/**
	 * This method initializes ignoreCase	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIgnoreCase() {
		if (ignoreCase == null) {
			ignoreCase = new JCheckBox();
			ignoreCase.setText("Ignorer la casse");
		}
		return ignoreCase;
	}

	/**
	 * This method initializes regularExpression	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getRegularExpression() {
		if (regularExpression == null) {
			regularExpression = new JCheckBox();
			regularExpression.setText("Expression r�guli�re");
		}
		return regularExpression;
	}

	/**
	 * This method initializes description	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDescription() {
		if (description == null) {
			description = new JTextField();
		}
		return description;
	}

	/**
	 * This method initializes datePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDatePanel() {
		if (datePanel == null) {
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 3;
			gridBagConstraints25.gridheight = 3;
			gridBagConstraints25.weightx = 1.0D;
			gridBagConstraints25.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints25.gridy = 0;
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 2;
			gridBagConstraints24.gridheight = 3;
			gridBagConstraints24.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints24.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText("et");
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 1;
			gridBagConstraints23.gridheight = 3;
			gridBagConstraints23.weightx = 1.0D;
			gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.gridy = 0;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.gridy = 2;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 1;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.gridy = 0;
			datePanel = new JPanel();
			datePanel.setLayout(new GridBagLayout());
			datePanel.setBorder(BorderFactory.createTitledBorder(null, "Date", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			datePanel.setVisible(true);
			datePanel.add(getDateAll(), gridBagConstraints19);
			datePanel.add(getDateEquals(), gridBagConstraints20);
			datePanel.add(getDateBetween(), gridBagConstraints22);
			datePanel.add(getDateFrom(), gridBagConstraints23);
			datePanel.add(jLabel1, gridBagConstraints24);
			datePanel.add(getDateTo(), gridBagConstraints25);
			ButtonGroup group = new ButtonGroup();
			group.add(getDateAll());
			group.add(getDateEquals());
			group.add(getDateBetween());
		}
		return datePanel;
	}

	/**
	 * This method initializes dateAll	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDateAll() {
		if (dateAll == null) {
			dateAll = new JRadioButton();
			dateAll.setText("Toutes");
			dateAll.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (dateAll.isSelected()) {
						getDateFrom().setEnabled(false);
						getDateFrom().setDate(null);
						getDateTo().setEnabled(false);
						getDateTo().setDate(null);
					}
				}
			});
			dateAll.setSelected(true);
		}
		return dateAll;
	}

	/**
	 * This method initializes dateEquals	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDateEquals() {
		if (dateEquals == null) {
			dateEquals = new JRadioButton();
			dateEquals.setText("Egale �");
			dateEquals.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (dateEquals.isSelected()) {
						getDateFrom().setEnabled(true);
						getDateTo().setEnabled(false);
						getDateTo().setDate(null);
					}
				}
			});
		}
		return dateEquals;
	}

	/**
	 * This method initializes dateBetween	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDateBetween() {
		if (dateBetween == null) {
			dateBetween = new JRadioButton();
			dateBetween.setText("Comprise entre");
			dateBetween.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (dateBetween.isSelected()) {
						getDateFrom().setEnabled(true);
						getDateTo().setEnabled(true);
					}
				}
			});
		}
		return dateBetween;
	}

	/**
	 * This method initializes dateFrom	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidgetPanel	
	 */
	private DateWidgetPanel getDateFrom() {
		if (dateFrom == null) {
			dateFrom = new DateWidgetPanel();
		}
		return dateFrom;
	}

	/**
	 * This method initializes dateTo	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidgetPanel	
	 */
	private DateWidgetPanel getDateTo() {
		if (dateTo == null) {
			dateTo = new DateWidgetPanel();
		}
		return dateTo;
	}
}
