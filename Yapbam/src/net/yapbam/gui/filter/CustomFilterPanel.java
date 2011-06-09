package net.yapbam.gui.filter;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.gui.HelpManager;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AutoSelectFocusListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import javax.swing.JTextField;
import net.yapbam.util.NullUtils;
import net.yapbam.util.TextMatcher;
import javax.swing.JButton;

public class CustomFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String INCONSISTENCY_CAUSE_PROPERTY = "InconsistencyCause"; //$NON-NLS-1$
	
	private JPanel accountPanel = null;
	private JList accountList = null;
	private AmountPanel amountPanel = null;
	private JPanel categoryPanel = null;
	private JList categoryList = null;
	private JPanel descriptionPanel = null;
	private JCheckBox ignoreCase = null;
	private JTextField description = null;
	private JPanel statementPanel = null;
	private JCheckBox checked = null;
	private JCheckBox notChecked = null;
	private DateFilterPanel valueDatePanel = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JTextField statement = null;
	private JCheckBox ignoreDiacritics = null;
	private JLabel regexpHelp = null;
	private JRadioButton descriptionEqualsTo = null;
	private JRadioButton descriptionContains = null;
	private JRadioButton descriptionRegular = null;
	private JPanel jPanel11 = null;
	private JLabel regexpStatement = null;
	private JRadioButton statementEqualsTo = null;
	private JRadioButton statementContains = null;
	private JRadioButton statementRegular = null;
	private JButton clear = null;
	private JPanel modePanel = null;
	private JScrollPane jScrollPane2 = null;
	private JList modes = null;
		
	private String oldInconsistencyCause;
	private FilteredData data;
	
	private final static class RegexprListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			HelpManager.show(e.getComponent(), HelpManager.REGULAR_EXPRESSIONS);
		}
	}
	
	private ListSelectionListener CONSISTENCY_CHECKER_LIST = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			checkConsistency();  //  @jve:decl-index=0:
		}
	};
	
	private ItemListener CONSISTENCY_CHECKER_ITEM_LISTENER = new ItemListener() {
		@Override
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			checkConsistency();  //  @jve:decl-index=0:
		}
	};

	private JPanel jPanel111 = null;
	private JLabel regexpNumber = null;
	private JRadioButton numberEqualsTo = null;
	private JRadioButton numberContains = null;
	private JRadioButton numberRegular = null;
	private JTextField number = null;
	private JCheckBox receipts;
	private JCheckBox expenses;
	private JPanel Receipts_expensesPanel;
	private JPanel panel_3;
	private JPanel panel;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private DateFilterPanel datePanel;
	private PropertyChangeListener inconsistencyListener;

	public CustomFilterPanel() {
		this(new FilteredData(new GlobalData()));
	}
	
	
	public CustomFilterPanel(FilteredData data) {
		super();
		this.data = data;
		inconsistencyListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				checkConsistency();
			}
		};
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
		gridBagConstraints28.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints28.gridx = 0;
		gridBagConstraints28.fill = GridBagConstraints.BOTH;
		gridBagConstraints28.gridy = 3;
		GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
		gridBagConstraints110.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints110.gridx = 0;
		gridBagConstraints110.anchor = GridBagConstraints.CENTER;
		gridBagConstraints110.weighty = 1.0D;
		gridBagConstraints110.gridy = 6;
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.fill = GridBagConstraints.BOTH;
		gridBagConstraints41.weightx = 1.0D;
		gridBagConstraints41.gridy = 4;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.weightx = 1.0D;
		gridBagConstraints21.gridy = 2;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 0.0D;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridy = 1;
		this.setSize(800, 500);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[]{0, 155, 0, 0, 0, 0, 0, 0};
		this.setLayout(gridBagLayout);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.weighty = 1.0;
		gbc_panel.weightx = 1.0;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		add(getPanel(), gbc_panel);
		this.add(getAccountPanel(), gridBagConstraints);
		GridBagConstraints gbc_amountPanel = new GridBagConstraints();
		gbc_amountPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_amountPanel.insets = new Insets(0, 0, 5, 5);
		gbc_amountPanel.gridx = 1;
		gbc_amountPanel.gridy = 2;
		add(getAmountPanel(), gbc_amountPanel);
		this.add(getDatePanel(), gridBagConstraints21);
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.weightx = 4.0D;
		gridBagConstraints3.weighty = 1.0D;
		gridBagConstraints3.gridwidth = 1;
		gridBagConstraints3.gridheight = 0;
		gridBagConstraints3.gridy = 2;
		this.add(getCategoryPanel(), gridBagConstraints3);
		GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
		gridBagConstraints111.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints111.gridx = 1;
		gridBagConstraints111.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints111.gridy = 3;
		this.add(getJPanel111(), gridBagConstraints111);
		this.add(getValueDatePanel(), gridBagConstraints41);
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints31.gridx = 1;
		gridBagConstraints31.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints31.gridwidth = 1;
		gridBagConstraints31.weightx = 1.0D;
		gridBagConstraints31.gridheight = 0;
		gridBagConstraints31.anchor = GridBagConstraints.NORTH;
		gridBagConstraints31.weighty = 1.0D;
		gridBagConstraints31.gridy = 4;
		this.add(getStatementPanel(), gridBagConstraints31);
		this.add(getClear(), gridBagConstraints110);
		this.add(getModePanel(), gridBagConstraints28);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 0, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 7;
		add(getPanel_3(), gbc_panel_3);
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
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.weightx = 1.0;
			accountPanel = new JPanel();
			accountPanel.setLayout(new GridBagLayout());
			accountPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.account"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
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
			accountList.setToolTipText(LocalizationData.get("CustomFilterPanel.account.toolTip")); //$NON-NLS-1$
			ArrayList<Integer> indices = new ArrayList<Integer>(data.getGlobalData().getAccountsNumber()); 
			for (int i=0;i<data.getGlobalData().getAccountsNumber();i++) {
				if (data.isOk(data.getGlobalData().getAccount(i))) indices.add(i);
			}
			int[] selection = new int[indices.size()];
			for (int i = 0; i < indices.size(); i++) {
				selection[i] = indices.get(i);
			}
			accountList.setSelectedIndices(selection);
			updateModesList(true);
			accountList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					checkConsistency();
					updateModesList(false);
				}
			});
			accountList.setVisibleRowCount(7);
		}
		return accountList;
	}

	/**
	 * This method initializes amountPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private AmountPanel getAmountPanel() {
		if (amountPanel == null) {
			amountPanel = new AmountPanel();
			amountPanel.addPropertyChangeListener(AmountPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
			amountPanel.setAmounts(data.getMinimumAmount(), data.getMaximumAmount());
		}
		return amountPanel;
	}
	
	private DateFilterPanel getDatePanel() {
		if (datePanel==null) {
			datePanel = new DateFilterPanel(DateFilterPanel.TRANSACTION_DATE);
			datePanel.addPropertyChangeListener(DateFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
			datePanel.setDates(data.getDateFrom(), data.getDateTo());
		}
		return datePanel;
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
			categoryPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.category"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
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
			categoryList.setToolTipText(LocalizationData.get("CustomFilterPanel.category.toolTip")); //$NON-NLS-1$
			ArrayList<Integer> indices = new ArrayList<Integer>(data.getGlobalData().getCategoriesNumber()); 
			for (int i=0;i<data.getGlobalData().getCategoriesNumber();i++) {
				if (data.isOk(data.getGlobalData().getCategory(i))) indices.add(i);
			}
			int[] selection = new int[indices.size()];
			for (int i = 0; i < indices.size(); i++) {
				selection[i] = indices.get(i);
			}
			categoryList.setSelectedIndices(selection);
			categoryList.addListSelectionListener(CONSISTENCY_CHECKER_LIST);
		}
		return categoryList;
	}

	@SuppressWarnings("serial")
	private void updateModesList(boolean first) {
		// Remember what modes are currently selected.
		// This will allow us to re-select them after updating the list.
		String[] currentSelectedModes = null;
		if (!first) {
			int[] selectedIndices = getModes().getSelectedIndices();
			currentSelectedModes = new String[selectedIndices.length];
			for (int i = 0; i < currentSelectedModes.length; i++) {
				currentSelectedModes[i] = (String) getModes().getModel().getElementAt(selectedIndices[i]);
			}
		}
		// Update the list content
		// We have to merge all the names of the modes of the currently selected accounts.
		int[] accountIndices =	this.accountList.getSelectedIndices();
		TreeSet<String> modes = new TreeSet<String>();
		for (int i = 0; i < accountIndices.length; i++) {
			Account account = data.getGlobalData().getAccount(accountIndices[i]);
			int nb = account.getModesNumber();
			for (int j = 0; j < nb; j++) {
				modes.add(account.getMode(j).getName());
			}
		}
		final String[] arrayModes = modes.toArray(new String[modes.size()]);
		getModes().setModel(new AbstractListModel() {
			@Override
			public int getSize() {
				return arrayModes.length;
			}
			@Override
			public Object getElementAt(int index) {
				return arrayModes[index];
			}
		});
		ArrayList<Integer> newSelection = new ArrayList<Integer>();
		if (first) {
			// set the selection to the content of the filter
			Mode[] validModes = data.getModes();
			if (validModes!=null) {
				for (int i = 0; i < validModes.length; i++) {
					int index = Arrays.binarySearch(arrayModes, validModes[i].getName());
					if (index>=0) newSelection.add(index);
				}
			} else {
				for (int i = 0; i < arrayModes.length; i++) {
					newSelection.add(i);
				}
			}
		} else {
			// Restore the selection of items that have been deleted
			for (int i = 0; i < currentSelectedModes.length; i++) {
				int index = Arrays.binarySearch(arrayModes, currentSelectedModes[i]);
				if (index>=0) newSelection.add(index);
			}
		}
		int[] indices = new int[newSelection.size()];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = newSelection.get(i);
		}
		getModes().setSelectedIndices(indices);
	}
	
	/** Apply the filter currently defined in this panel to the FilteredData.
	 */
	public void apply() {
		data.setSuspended(true);
		// build the account and mode filter
		Object[] selectedModes = getModes().getSelectedValues();
		ArrayList<Mode> modes = new ArrayList<Mode>();
		boolean all = true;
		int[] accountIndices = this.accountList.getSelectedIndices();
		Account[] accounts = new Account[accountIndices.length];
		for (int i = 0; i < accounts.length; i++) {
			accounts[i] = data.getGlobalData().getAccount(accountIndices[i]);
			for (int j=0; j<accounts[i].getModesNumber(); j++) {
				if (Arrays.binarySearch(selectedModes,accounts[i].getMode(j).getName())<0) {
					all = false;
				} else {
					modes.add(accounts[i].getMode(j));
				}
			}
		}
		this.data.setAccounts(accounts);
		// set the mode filter
		this.data.setModes(all?null:modes.toArray(new Mode[modes.size()]));
		// build the category filter
		int[] categoryIndices = this.categoryList.getSelectedIndices();
		Category[] categories = new Category[categoryIndices.length];
		for (int i = 0; i < categories.length; i++) {
			categories[i] = data.getGlobalData().getCategory(categoryIndices[i]);
		}
		this.data.setCategories(categories);
		// build the expense/receipt and amount filter
		Double min = getAmountPanel().getMinAmount();
		Double max = getAmountPanel().getMaxAmount();
		int filter = 0;
		if (getReceipts().isSelected()) filter += FilteredData.RECEIPTS;
		if (getExpenses().isSelected()) filter += FilteredData.EXPENSES;
		this.data.setAmountFilter(filter, min, max);
		// build the date filter
		this.data.setDateFilter(getDatePanel().getDateFrom(), getDatePanel().getDateTo());
		// build the value date filter
		this.data.setValueDateFilter(getValueDatePanel().getDateFrom(), getValueDatePanel().getDateTo());
		// build the description filter
		String text = getDescription().getText().trim();
		if (text.length()==0) {
			this.data.setDescriptionFilter(null);
		} else {
			TextMatcher.Kind kind = null;
			if (getDescriptionEqualsTo().isSelected()) {
				kind = TextMatcher.EQUALS;
			} else if (getDescriptionContains().isSelected()) {
				kind = TextMatcher.CONTAINS;
			} else if (getDescriptionRegular().isSelected()) {
				kind = TextMatcher.REGULAR;
			}
			this.data.setDescriptionFilter(new TextMatcher(kind, text, !getIgnoreCase().isSelected(), !getIgnoreDiacritics().isSelected()));
		}
		// Build the statement filter
		filter = 0;
		if (getChecked().isSelected()) filter += FilteredData.CHECKED;
		if (getNotChecked().isSelected()) filter += FilteredData.NOT_CHECKED;
		text = getStatement().getText().trim();
		if (text.length()==0) {
			this.data.setStatementFilter(filter, null);
		} else {
			TextMatcher.Kind kind = null;
			if (getStatementEqualsTo().isSelected()) {
				kind = TextMatcher.EQUALS;
			} else if (getStatementContains().isSelected()) {
				kind = TextMatcher.CONTAINS;
			} else if (getStatementRegular().isSelected()) {
				kind = TextMatcher.REGULAR;
			}
			this.data.setStatementFilter(filter, new TextMatcher(kind, text, true, true));
		}
		// Build the number filter
		text = getNumber().getText().trim();
		if (text.length()==0) {
			this.data.setNumberFilter(null);
		} else {
			TextMatcher.Kind kind = null;
			if (getNumberEqualsTo().isSelected()) {
				kind = TextMatcher.EQUALS;
			} else if (getNumberContains().isSelected()) {
				kind = TextMatcher.CONTAINS;
			} else if (getNumberRegular().isSelected()) {
				kind = TextMatcher.REGULAR;
			}
			this.data.setNumberFilter(new TextMatcher(kind, text, true, true));
		}
		data.setSuspended(false);
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
		if (!getExpenses().isSelected() && !getReceipts().isSelected()) {
			return MessageFormat.format(LocalizationData.get("CustomFilterPanel.error.natureStatus"), //$NON-NLS-1$
					LocalizationData.get("MainMenuBar.Expenses"), LocalizationData.get("MainMenuBar.Receipts")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (!getChecked().isSelected() && !getNotChecked().isSelected()) {
			return MessageFormat.format(LocalizationData.get("CustomFilterPanel.error.checkStatus"), //$NON-NLS-1$
					LocalizationData.get("MainMenuBar.checked"), LocalizationData.get("MainMenuBar.notChecked")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (getValueDatePanel().getInconsistencyCause()!=null) return getValueDatePanel().getInconsistencyCause();
		if (getAmountPanel().getInconsistencyCause()!=null) return getAmountPanel().getInconsistencyCause();
		if (getDatePanel().getInconsistencyCause()!=null) return getDatePanel().getInconsistencyCause();
		if (getAccountList().getSelectedIndices().length==0) {
			return LocalizationData.get("CustomFilterPanel.error.noAccount"); //$NON-NLS-1$
		}
		if (getModes().getSelectedIndices().length==0) {
			return LocalizationData.get("CustomFilterPanel.error.noMode"); //$NON-NLS-1$
		}
		if (getCategoryList().getSelectedIndices().length==0) {
			return LocalizationData.get("CustomFilterPanel.error.noCategory"); //$NON-NLS-1$
		}
		return null;
	}

	public boolean isConsistent() {
		return getInconsistencyCause()!=null;
	}
		
	private void checkConsistency() {
		String cause = getInconsistencyCause();
		if (!NullUtils.areEquals(cause,oldInconsistencyCause)) {
			firePropertyChange(INCONSISTENCY_CAUSE_PROPERTY, oldInconsistencyCause, cause);
			oldInconsistencyCause = cause;
		}
	}

	/**
	 * This method initializes descriptionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.gridx = 1;
			gridBagConstraints30.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.weightx = 1.0D;
			gridBagConstraints30.gridy = 0;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridheight = 0;
			gridBagConstraints33.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints33.gridy = 0;
			regexpHelp = new JLabel();
			regexpHelp.setText(""); //$NON-NLS-1$
			regexpHelp.setToolTipText(LocalizationData.get("CustomFilterPanel.regexprHelp.toolTip")); //$NON-NLS-1$
			regexpHelp.setIcon(IconManager.HELP);
			regexpHelp.addMouseListener(new RegexprListener());
			descriptionPanel = new JPanel();
			descriptionPanel.setLayout(new GridBagLayout());
			descriptionPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.description"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
			descriptionPanel.add(getJPanel1(), gridBagConstraints33);
			descriptionPanel.add(getJPanel2(), gridBagConstraints30);
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
			ignoreCase.setText(LocalizationData.get("CustomFilterPanel.description.ignoreCase")); //$NON-NLS-1$
			ignoreCase.setToolTipText(LocalizationData.get("CustomFilterPanel.description.ignoreCase.toolTip")); //$NON-NLS-1$
			ignoreCase.setSelected((data.getDescriptionFilter()==null)||!data.getDescriptionFilter().isCaseSensitive());
		}
		return ignoreCase;
	}

	/**
	 * This method initializes description	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDescription() {
		if (description == null) {
			description = new JTextField();
			description.setText(data.getDescriptionFilter()==null?"":data.getDescriptionFilter().getFilter()); //$NON-NLS-1$
			description.setToolTipText(LocalizationData.get("CustomFilterPanel.description.toolTip")); //$NON-NLS-1$
			description.addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return description;
	}

	/**
	 * This method initializes statementPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStatementPanel() {
		if (statementPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridwidth = 0;
			gridBagConstraints6.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.weighty = 1.0D;
			gridBagConstraints5.gridheight = 0;
			gridBagConstraints5.gridx = 2;
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 2;
			gridBagConstraints27.anchor = GridBagConstraints.WEST;
			gridBagConstraints27.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints27.weightx = 1.0D;
			gridBagConstraints27.gridy = 0;
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 0;
			gridBagConstraints26.anchor = GridBagConstraints.WEST;
			gridBagConstraints26.gridy = 0;
			statementPanel = new JPanel();
			statementPanel.setLayout(new GridBagLayout());
			statementPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("CustomFilterPanel.check"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));  //$NON-NLS-1$//$NON-NLS-2$
			statementPanel.add(getChecked(), gridBagConstraints26);
			statementPanel.add(getNotChecked(), gridBagConstraints27);
			statementPanel.add(getJPanel11(), gridBagConstraints6);
		}
		return statementPanel;
	}

	/**
	 * This method initializes checked	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getChecked() {
		if (checked == null) {
			checked = new JCheckBox();
			checked.setText(LocalizationData.get("MainMenuBar.checked")); //$NON-NLS-1$
			checked.setToolTipText(LocalizationData.get("CustomFilterPanel.checked.toolTip")); //$NON-NLS-1$
			checked.setSelected(data.isOk(FilteredData.CHECKED));
			setStatementFilterEnabled();
			checked.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					checkConsistency();
					setStatementFilterEnabled();
				}
			});
		}
		return checked;
	}
	
	private void setStatementFilterEnabled() {
		boolean ok = checked.isSelected();
		getStatementContains().setEnabled(ok);
		getStatementEqualsTo().setEnabled(ok);
		getStatementRegular().setEnabled(ok);
		getStatement().setEnabled(ok);		
	}

	/**
	 * This method initializes notChecked	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNotChecked() {
		if (notChecked == null) {
			notChecked = new JCheckBox();
			notChecked.setText(LocalizationData.get("MainMenuBar.notChecked")); //$NON-NLS-1$
			notChecked.setToolTipText(LocalizationData.get("CustomFilterPanel.unchecked.toolTip")); //$NON-NLS-1$
			notChecked.setSelected(data.isOk(FilteredData.NOT_CHECKED));
			notChecked.addItemListener(CONSISTENCY_CHECKER_ITEM_LISTENER);
		}
		return notChecked;
	}

	/**
	 * This method initializes valueDatePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private DateFilterPanel getValueDatePanel() {
		if (valueDatePanel == null) {
			valueDatePanel = new DateFilterPanel(DateFilterPanel.VALUE_DATE);
			valueDatePanel.addPropertyChangeListener(DateFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
			valueDatePanel.setDates(data.getDateFrom(), data.getDateTo());
		}
		return valueDatePanel;
	}

	/**
	 * This method initializes statement	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStatement() {
		if (statement == null) {
			statement = new JTextField();
			statement.setText(data.getStatementFilter()==null?"":data.getStatementFilter().getFilter()); //$NON-NLS-1$
			statement.setToolTipText(LocalizationData.get("CustomFilterPanel.statement.toolTip")); //$NON-NLS-1$
			statement.addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return statement;
	}

	/**
	 * This method initializes ignoreDiacritics	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIgnoreDiacritics() {
		if (ignoreDiacritics == null) {
			ignoreDiacritics = new JCheckBox();
			ignoreDiacritics.setText(LocalizationData.get("CustomFilterPanel.description.ignoreDiacriticals")); //$NON-NLS-1$
			ignoreDiacritics.setToolTipText(LocalizationData.get("CustomFilterPanel.description.ignoreDiacriticals.toolTip")); //$NON-NLS-1$
			ignoreDiacritics.setSelected((data.getDescriptionFilter()==null)||!data.getDescriptionFilter().isDiacriticalSensitive());
		}
		return ignoreDiacritics;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.gridx = 0;
			gridBagConstraints36.gridy = 2;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.anchor = GridBagConstraints.WEST;
			gridBagConstraints35.gridheight = 1;
			gridBagConstraints35.gridwidth = 1;
			gridBagConstraints35.weightx = 0.0D;
			gridBagConstraints35.gridy = 1;
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.gridx = 0;
			gridBagConstraints34.anchor = GridBagConstraints.WEST;
			gridBagConstraints34.gridy = 0;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.anchor = GridBagConstraints.WEST;
			gridBagConstraints32.gridy = 2;
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.gridx = 1;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.setToolTipText(LocalizationData.get("CustomFilterPanel.regexprHelp.toolTip")); //$NON-NLS-1$
			jPanel1.add(regexpHelp, gridBagConstraints32);
			jPanel1.add(getDescriptionEqualsTo(), gridBagConstraints34);
			jPanel1.add(getDescriptionContains(), gridBagConstraints35);
			jPanel1.add(getDescriptionRegular(), gridBagConstraints36);
			ButtonGroup group = new ButtonGroup();
			group.add(getDescriptionEqualsTo());
			group.add(getDescriptionContains());
			group.add(getDescriptionRegular());
			if ((data.getDescriptionFilter()==null) || (data.getDescriptionFilter().getKind()==TextMatcher.CONTAINS)) {
				getDescriptionContains().setSelected(true);
			} else if (data.getDescriptionFilter().getKind()==TextMatcher.EQUALS) {
				getDescriptionEqualsTo().setSelected(true);
			} else {
				getDescriptionRegular().setSelected(true);
			}
		}
		return jPanel1;
	}

	/**
	 * This method initializes descriptionEqualsTo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDescriptionEqualsTo() {
		if (descriptionEqualsTo == null) {
			descriptionEqualsTo = new JRadioButton();
			descriptionEqualsTo.setText(LocalizationData.get("CustomFilterPanel.description.equals")); //$NON-NLS-1$
			descriptionEqualsTo.setToolTipText(LocalizationData.get("CustomFilterPanel.description.equals.toolTip")); //$NON-NLS-1$
		}
		return descriptionEqualsTo;
	}

	/**
	 * This method initializes descriptionContains	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDescriptionContains() {
		if (descriptionContains == null) {
			descriptionContains = new JRadioButton();
			descriptionContains.setText(LocalizationData.get("CustomFilterPanel.description.contains")); //$NON-NLS-1$
			descriptionContains.setToolTipText(LocalizationData.get("CustomFilterPanel.description.contains.toolTip")); //$NON-NLS-1$
		}
		return descriptionContains;
	}

	/**
	 * This method initializes descriptionRegular	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDescriptionRegular() {
		if (descriptionRegular == null) {
			descriptionRegular = new JRadioButton();
			descriptionRegular.setText(LocalizationData.get("CustomFilterPanel.description.regularExpression")); //$NON-NLS-1$
			descriptionRegular.setToolTipText(LocalizationData.get("CustomFilterPanel.description.regularExpression.toolTip")); //$NON-NLS-1$
		}
		return descriptionRegular;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.NORTH;
			gridBagConstraints18.gridwidth = 0;
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.gridy = 1;
			gridBagConstraints18.weightx = 1.0;
			gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.gridy = 0;
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.gridy = -1;
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.add(getIgnoreCase(), gridBagConstraints16);
			jPanel2.add(getIgnoreDiacritics(), gridBagConstraints17);
			jPanel2.add(getDescription(), gridBagConstraints18);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel11	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			GridBagConstraints gridBagConstraints361 = new GridBagConstraints();
			gridBagConstraints361.gridx = 0;
			gridBagConstraints361.gridy = 2;
			GridBagConstraints gridBagConstraints351 = new GridBagConstraints();
			gridBagConstraints351.anchor = GridBagConstraints.WEST;
			gridBagConstraints351.gridwidth = 1;
			gridBagConstraints351.gridx = 0;
			gridBagConstraints351.gridy = 1;
			gridBagConstraints351.weightx = 0.0D;
			gridBagConstraints351.gridheight = 1;
			GridBagConstraints gridBagConstraints341 = new GridBagConstraints();
			gridBagConstraints341.anchor = GridBagConstraints.WEST;
			gridBagConstraints341.gridy = 0;
			gridBagConstraints341.gridx = 0;
			GridBagConstraints gridBagConstraints321 = new GridBagConstraints();
			gridBagConstraints321.anchor = GridBagConstraints.WEST;
			gridBagConstraints321.gridy = 2;
			gridBagConstraints321.weightx = 0.0D;
			gridBagConstraints321.gridx = 1;
			regexpStatement = new JLabel();
			regexpStatement.setToolTipText(LocalizationData.get("CustomFilterPanel.regexprHelp.toolTip")); //$NON-NLS-1$
			regexpStatement.setText(""); //$NON-NLS-1$
			regexpStatement.setIcon(IconManager.HELP);
			regexpStatement.addMouseListener(new RegexprListener());

			jPanel11 = new JPanel();
			jPanel11.setLayout(new GridBagLayout());
			jPanel11.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.statement"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
			jPanel11.add(regexpStatement, gridBagConstraints321);
			jPanel11.add(getStatementEqualsTo(), gridBagConstraints341);
			jPanel11.add(getStatementContains(), gridBagConstraints351);
			jPanel11.add(getStatementRegular(), gridBagConstraints361);
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.gridheight = 0;
			gridBagConstraints5.gridy = 0;
			jPanel11.add(getStatement(), gridBagConstraints5);
			ButtonGroup group = new ButtonGroup();
			group.add(getStatementContains());
			group.add(getStatementEqualsTo());
			group.add(getStatementRegular());
			if (getChecked().isSelected()) {
				if ((data.getStatementFilter()==null) || (data.getStatementFilter().getKind()==TextMatcher.CONTAINS)) {
					getStatementContains().setSelected(true);
				} else if (data.getStatementFilter().getKind()==TextMatcher.EQUALS) {
					getStatementEqualsTo().setSelected(true);
				} else getStatementRegular().setSelected(true);
			}
		}
		return jPanel11;
	}

	/**
	 * This method initializes statementEqualsTo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getStatementEqualsTo() {
		if (statementEqualsTo == null) {
			statementEqualsTo = new JRadioButton();
			statementEqualsTo.setText(LocalizationData.get("CustomFilterPanel.statement.equals")); //$NON-NLS-1$
			statementEqualsTo.setToolTipText(LocalizationData.get("CustomFilterPanel.statement.equals.toolTip")); //$NON-NLS-1$
		}
		return statementEqualsTo;
	}

	/**
	 * This method initializes statementContains	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getStatementContains() {
		if (statementContains == null) {
			statementContains = new JRadioButton();
			statementContains.setText(LocalizationData.get("CustomFilterPanel.statement.contains")); //$NON-NLS-1$
			statementContains.setToolTipText(LocalizationData.get("CustomFilterPanel.statement.contains.toolTip")); //$NON-NLS-1$
		}
		return statementContains;
	}

	/**
	 * This method initializes statementRegular	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getStatementRegular() {
		if (statementRegular == null) {
			statementRegular = new JRadioButton();
			statementRegular.setText(LocalizationData.get("CustomFilterPanel.statement.regularExpression")); //$NON-NLS-1$
			statementRegular.setToolTipText(LocalizationData.get("CustomFilterPanel.statement.regularExpression.toolTip")); //$NON-NLS-1$
		}
		return statementRegular;
	}

	/**
	 * This method initializes clear	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClear() {
		if (clear == null) {
			clear = new JButton();
			clear.setText(LocalizationData.get("CustomFilterPanel.clearAll")); //$NON-NLS-1$
			clear.setToolTipText(LocalizationData.get("CustomFilterPanel.clearAll.toolTip")); //$NON-NLS-1$
			clear.setEnabled(true);
			clear.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectAll(getAccountList());
					selectAll(getModes());
					selectAll(getCategoryList());
					getDescriptionContains().setSelected(true);
					getDescription().setText(""); //$NON-NLS-1$
					getAmountPanel().clear();
					getDatePanel().clear();
					getNumberContains().setSelected(true);
					getNumber().setText(""); //$NON-NLS-1$
					getValueDatePanel().clear();
					getChecked().setSelected(true);
					getNotChecked().setSelected(true);
					getStatementContains().setSelected(true);
					getStatement().setText(""); //$NON-NLS-1$
				}

				private void selectAll(JList list) {
					list.getSelectionModel().addSelectionInterval(0, list.getModel().getSize()-1);
				}
			});
		}
		return clear;
	}

	/**
	 * This method initializes modePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getModePanel() {
		if (modePanel == null) {
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.fill = GridBagConstraints.BOTH;
			gridBagConstraints37.gridy = 0;
			gridBagConstraints37.weightx = 1.0;
			gridBagConstraints37.weighty = 1.0;
			gridBagConstraints37.gridx = 0;
			modePanel = new JPanel();
			modePanel.setLayout(new GridBagLayout());
			modePanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.mode"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
			modePanel.add(getJScrollPane2(), gridBagConstraints37);
		}
		return modePanel;
	}

	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setToolTipText(""); //$NON-NLS-1$
			jScrollPane2.setViewportView(getModes());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes modes	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getModes() {
		if (modes == null) {
			modes = new JList();
			modes.setToolTipText(LocalizationData.get("CustomFilterPanel.mode.toolTip")); //$NON-NLS-1$
			modes.addListSelectionListener(CONSISTENCY_CHECKER_LIST);
			modes.setVisibleRowCount(7);
		}
		return modes;
	}

	/**
	 * This method initializes jPanel111	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel111() {
		if (jPanel111 == null) {
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.gridx = 2;
			gridBagConstraints51.gridy = 0;
			gridBagConstraints51.weightx = 1.0D;
			gridBagConstraints51.gridheight = 0;
			GridBagConstraints gridBagConstraints3611 = new GridBagConstraints();
			gridBagConstraints3611.gridx = 0;
			gridBagConstraints3611.gridy = 2;
			GridBagConstraints gridBagConstraints3511 = new GridBagConstraints();
			gridBagConstraints3511.anchor = GridBagConstraints.WEST;
			gridBagConstraints3511.gridwidth = 1;
			gridBagConstraints3511.gridx = 0;
			gridBagConstraints3511.gridy = 1;
			gridBagConstraints3511.weightx = 0.0D;
			gridBagConstraints3511.gridheight = 1;
			GridBagConstraints gridBagConstraints3411 = new GridBagConstraints();
			gridBagConstraints3411.anchor = GridBagConstraints.WEST;
			gridBagConstraints3411.gridy = 0;
			gridBagConstraints3411.fill = GridBagConstraints.NONE;
			gridBagConstraints3411.gridx = 0;
			GridBagConstraints gridBagConstraints3211 = new GridBagConstraints();
			gridBagConstraints3211.anchor = GridBagConstraints.WEST;
			gridBagConstraints3211.gridy = 2;
			gridBagConstraints3211.weightx = 0.0D;
			gridBagConstraints3211.gridx = 1;
			regexpNumber = new JLabel();
			regexpNumber.setToolTipText(LocalizationData.get("CustomFilterPanel.regexprHelp.toolTip")); //$NON-NLS-1$
			regexpNumber.setText(""); //$NON-NLS-1$
			regexpNumber.setIcon(IconManager.HELP);
			jPanel111 = new JPanel();
			jPanel111.setLayout(new GridBagLayout());
			jPanel111.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.number"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
			jPanel111.add(regexpNumber, gridBagConstraints3211);
			jPanel111.add(getNumberEqualsTo(), gridBagConstraints3411);
			jPanel111.add(getNumberContains(), gridBagConstraints3511);
			jPanel111.add(getNumberRegular(), gridBagConstraints3611);
			jPanel111.add(getNumber(), gridBagConstraints51);
			ButtonGroup group = new ButtonGroup();
			group.add(getNumberContains());
			group.add(getNumberEqualsTo());
			group.add(getNumberRegular());
			if ((data.getNumberFilter()==null) || (data.getNumberFilter().getKind()==TextMatcher.CONTAINS)) {
				getNumberContains().setSelected(true);
			} else if (data.getNumberFilter().getKind()==TextMatcher.EQUALS) {
				getNumberEqualsTo().setSelected(true);
			} else getNumberRegular().setSelected(true);
		}
		return jPanel111;
	}

	/**
	 * This method initializes numberEqualsTo	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getNumberEqualsTo() {
		if (numberEqualsTo == null) {
			numberEqualsTo = new JRadioButton();
			numberEqualsTo.setToolTipText(LocalizationData.get("CustomFilterPanel.number.equals.toolTip")); //$NON-NLS-1$
			numberEqualsTo.setText(LocalizationData.get("CustomFilterPanel.number.equals")); //$NON-NLS-1$
		}
		return numberEqualsTo;
	}

	/**
	 * This method initializes numberContains	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getNumberContains() {
		if (numberContains == null) {
			numberContains = new JRadioButton();
			numberContains.setToolTipText(LocalizationData.get("CustomFilterPanel.number.contains.toolTip")); //$NON-NLS-1$
			numberContains.setText(LocalizationData.get("CustomFilterPanel.number.contains")); //$NON-NLS-1$
		}
		return numberContains;
	}

	/**
	 * This method initializes numberRegular	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getNumberRegular() {
		if (numberRegular == null) {
			numberRegular = new JRadioButton();
			numberRegular.setToolTipText(LocalizationData.get("CustomFilterPanel.number.regularExpression.toolTip")); //$NON-NLS-1$
			numberRegular.setText(LocalizationData.get("CustomFilterPanel.number.regularExpression")); //$NON-NLS-1$
		}
		return numberRegular;
	}

	/**
	 * This method initializes number	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getNumber() {
		if (number == null) {
			number = new JTextField();
			number.setToolTipText(LocalizationData.get("CustomFilterPanel.number.toolTip")); //$NON-NLS-1$
			number.setText(data.getNumberFilter()==null?"":data.getNumberFilter().getFilter()); //$NON-NLS-1$
			number.addFocusListener(AutoSelectFocusListener.INSTANCE);
		}
		return number;
	}
	private JCheckBox getReceipts() {
		if (receipts == null) {
			receipts = new JCheckBox(LocalizationData.get("MainMenuBar.Receipts")); //$NON-NLS-1$
			receipts.setToolTipText(LocalizationData.get("CustomFilterPanel.receipts.toolTip")); //$NON-NLS-1$
			receipts.setSelected(data.isOk(FilteredData.RECEIPTS));
			receipts.addItemListener(CONSISTENCY_CHECKER_ITEM_LISTENER);
		}
		return receipts;
	}
	private JCheckBox getExpenses() {
		if (expenses == null) {
			expenses = new JCheckBox(LocalizationData.get("MainMenuBar.Expenses")); //$NON-NLS-1$
			expenses.setToolTipText(LocalizationData.get("CustomFilterPanel.expenses.toolTip")); //$NON-NLS-1$
			expenses.setSelected(data.isOk(FilteredData.EXPENSES));
			expenses.addItemListener(CONSISTENCY_CHECKER_ITEM_LISTENER);
		}
		return expenses;
	}
	private JPanel getReceipts_expensesPanel() {
		if (Receipts_expensesPanel == null) {
			Receipts_expensesPanel = new JPanel();
			Receipts_expensesPanel.setBorder(new TitledBorder(null, LocalizationData.get("CustomFilterPanel.nature"), //$NON-NLS-1$
					TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, null));
			GridBagLayout gbl_Receipts_expensesPanel = new GridBagLayout();
			gbl_Receipts_expensesPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_Receipts_expensesPanel.rowHeights = new int[]{0, 0};
			gbl_Receipts_expensesPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_Receipts_expensesPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			Receipts_expensesPanel.setLayout(gbl_Receipts_expensesPanel);
			GridBagConstraints gbc_receipts = new GridBagConstraints();
			gbc_receipts.insets = new Insets(0, 0, 0, 5);
			gbc_receipts.gridx = 0;
			gbc_receipts.gridy = 0;
			Receipts_expensesPanel.add(getReceipts(), gbc_receipts);
			GridBagConstraints gbc_expenses = new GridBagConstraints();
			gbc_expenses.gridx = 1;
			gbc_expenses.gridy = 0;
			Receipts_expensesPanel.add(getExpenses(), gbc_expenses);
		}
		return Receipts_expensesPanel;
	}
	private JPanel getPanel_3() {
		if (panel_3 == null) {
			panel_3 = new JPanel();
		}
		return panel_3;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{551, 0};
			gbl_panel.rowHeights = new int[]{50, 85, 0, 0};
			gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			GridBagConstraints gbc_Receipts_expensesPanel = new GridBagConstraints();
			gbc_Receipts_expensesPanel.weightx = 1.0;
			gbc_Receipts_expensesPanel.anchor = GridBagConstraints.NORTH;
			gbc_Receipts_expensesPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_Receipts_expensesPanel.insets = new Insets(0, 0, 5, 0);
			gbc_Receipts_expensesPanel.gridx = 0;
			gbc_Receipts_expensesPanel.gridy = 0;
			panel.add(getReceipts_expensesPanel(), gbc_Receipts_expensesPanel);
			GridBagConstraints gbc_descriptionPanel = new GridBagConstraints();
			gbc_descriptionPanel.insets = new Insets(0, 0, 5, 0);
			gbc_descriptionPanel.weightx = 1.0;
			gbc_descriptionPanel.weighty = 1.0;
			gbc_descriptionPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_descriptionPanel.gridx = 0;
			gbc_descriptionPanel.gridy = 1;
			panel.add(getDescriptionPanel(), gbc_descriptionPanel);
		}
		return panel;
	}
}
