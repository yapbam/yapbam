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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.Filter;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.gui.LocalizationData;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import net.yapbam.util.NullUtils;
import javax.swing.JButton;

public class CustomFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String INCONSISTENCY_CAUSE_PROPERTY = "InconsistencyCause"; //$NON-NLS-1$
	
	private JPanel accountPanel = null;
	private JList accountList = null;
	private AmountPanel amountPanel = null;
	private JPanel categoryPanel = null;
	private JList categoryList = null;
	private TextMatcherFilterPanel descriptionPanel = null;
	private JPanel statementPanel = null;
	private JCheckBox checked = null;
	private JCheckBox notChecked = null;
	private DateFilterPanel valueDatePanel = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private TextMatcherFilterPanel jPanel11 = null;
	private JButton clear = null;
	private JPanel modePanel = null;
	private JScrollPane jScrollPane2 = null;
	private JList modes = null;
		
	private String oldInconsistencyCause;
	private FilteredData data;
	
	private ListSelectionListener CONSISTENCY_CHECKER_LIST = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			checkConsistency();  //  @jve:decl-index=0:
		}
	};
	
	private TextMatcherFilterPanel numberPanel = null;
	private NatureFilterPanel receipts_expensesPanel;
	private JPanel panel;
	private DateFilterPanel datePanel;
	private PropertyChangeListener inconsistencyListener;
	private TextMatcherFilterPanel commentPanel;

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
		gridBagConstraints28.gridy = 2;
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.anchor = GridBagConstraints.NORTH;
		gridBagConstraints41.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints41.weightx = 1.0D;
		gridBagConstraints41.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 0.0D;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridy = 0;
		this.setSize(800, 700);
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 0;
		gbc_panel.gridwidth = 2;
		gbc_panel.weighty = 1.0;
		gbc_panel.weightx = 1.0;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		add(getPanel(), gbc_panel);
		this.add(getAccountPanel(), gridBagConstraints);
		GridBagConstraints gbc_clear = new GridBagConstraints();
		gbc_clear.insets = new Insets(0, 0, 0, 5);
		gbc_clear.gridx = 0;
		gbc_clear.gridy = 4;
		add(getClear(), gbc_clear);
		this.add(getValueDatePanel(), gridBagConstraints41);
		this.add(getModePanel(), gridBagConstraints28);
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
				if (data.getFilter().isOk(data.getGlobalData().getAccount(i))) indices.add(i);
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
			amountPanel.setAmounts(data.getFilter().getMinAmount(), data.getFilter().getMaxAmount());
		}
		return amountPanel;
	}
	
	private DateFilterPanel getDatePanel() {
		if (datePanel==null) {
			datePanel = new DateFilterPanel(DateFilterPanel.TRANSACTION_DATE);
			datePanel.addPropertyChangeListener(DateFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
			datePanel.setDates(data.getFilter().getDateFrom(), data.getFilter().getDateTo());
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
				if (data.getFilter().isOk(data.getGlobalData().getCategory(i))) indices.add(i);
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
			List<String> validModes = data.getFilter().getValidModes();
			if (validModes!=null) {
				for (int i = 0; i < validModes.size(); i++) {
					String name = validModes.get(i);
					if (name.isEmpty()) name = Mode.UNDEFINED.getName();
					int index = Arrays.binarySearch(arrayModes, name);
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
	public boolean apply() {
		Filter filter = this.data.getFilter();
		filter.setSuspended(true);
		// build the account and mode filter
		Object[] selectedModes = getModes().getSelectedValues();
		ArrayList<String> modes = new ArrayList<String>();
		boolean all = true;
		int[] accountIndices = this.accountList.getSelectedIndices();
		Account[] accounts = new Account[accountIndices.length];
		for (int i = 0; i < accounts.length; i++) {
			accounts[i] = data.getGlobalData().getAccount(accountIndices[i]);
			for (int j=0; j<accounts[i].getModesNumber(); j++) {
				Mode mode = accounts[i].getMode(j);
				if (Arrays.binarySearch(selectedModes,mode.getName())<0) {
					all = false;
				} else {
					modes.add(mode.equals(Mode.UNDEFINED)?"":mode.getName());
				}
			}
		}
		filter.setValidAccounts(accounts.length==data.getGlobalData().getAccountsNumber()?null:Arrays.asList(accounts));
		// set the mode filter
		filter.setValidModes(all?null:modes);
		// build the category filter
		int[] categoryIndices = this.categoryList.getSelectedIndices();
		if (categoryIndices.length==data.getGlobalData().getCategoriesNumber()) {
			filter.setValidCategories(null);
		} else {
			List<Category> categories = new ArrayList<Category>(categoryIndices.length);
			for (int i = 0; i < categoryIndices.length; i++) {
				categories.add(data.getGlobalData().getCategory(categoryIndices[i]));
			}
			filter.setValidCategories(categories);
		}
		// build the expense/receipt and amount filter
		Double min = getAmountPanel().getMinAmount();
		Double max = getAmountPanel().getMaxAmount();
		int mask = 0;
		if (getReceipts_expensesPanel().isReceiptsSelected()) mask += Filter.RECEIPTS;
		if (getReceipts_expensesPanel().isExpensesSelected()) mask += Filter.EXPENSES;
		filter.setAmountFilter(mask, min, max);
		// build the date filter
		filter.setDateFilter(getDatePanel().getDateFrom(), getDatePanel().getDateTo());
		// build the value date filter
		filter.setValueDateFilter(getValueDatePanel().getDateFrom(), getValueDatePanel().getDateTo());
		// build the description filter
		filter.setDescriptionMatcher(getDescriptionPanel().getTextMatcher());
		// build the comment filter
		filter.setCommentMatcher(getCommentPanel().getTextMatcher());
		// Build the statement filter
		mask = 0;
		if (getChecked().isSelected()) mask += Filter.CHECKED;
		if (getNotChecked().isSelected()) mask += Filter.NOT_CHECKED;
		filter.setStatementFilter(mask, getJPanel11().getTextMatcher());
		// Build the number filter
		filter.setNumberMatcher(this.getNumberPanel().getTextMatcher());
		boolean result = filter.hasChanged();
		filter.setSuspended(false);
		return result;
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
		if (getReceipts_expensesPanel().getInconsistencyCause()!=null) return getReceipts_expensesPanel().getInconsistencyCause();
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
	private TextMatcherFilterPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
			descriptionPanel = new TextMatcherFilterPanel(TextMatcherFilterPanel.DESCRIPTION_WORDING);
			descriptionPanel.setTextMatcher(data.getFilter().getDescriptionMatcher());
		}
		return descriptionPanel;
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
			checked.setSelected(data.getFilter().isOk(Filter.CHECKED));
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
		getJPanel11().setEnabled(checked.isSelected());
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
			notChecked.setSelected(data.getFilter().isOk(Filter.NOT_CHECKED));
			notChecked.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					checkConsistency();  //  @jve:decl-index=0:
				}
			});
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
			valueDatePanel.setDates(data.getFilter().getDateFrom(), data.getFilter().getDateTo());
		}
		return valueDatePanel;
	}

	/**
	 * This method initializes jPanel11	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private TextMatcherFilterPanel getJPanel11() {
		if (jPanel11 == null) {
			jPanel11 = new TextMatcherFilterPanel(TextMatcherFilterPanel.STATEMENT_WORDING);
			if (getChecked().isSelected()) {
				jPanel11.setTextMatcher(data.getFilter().getStatementMatcher());
				jPanel11.setCheckBoxesVisible(false);
			}
		}
		return jPanel11;
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
					getDescriptionPanel().clear();
					getReceipts_expensesPanel().clear();
					getAmountPanel().clear();
					getDatePanel().clear();
					getNumberPanel().clear();
					getValueDatePanel().clear();
					getChecked().setSelected(true);
					getNotChecked().setSelected(true);
					getJPanel11().clear();
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
	private TextMatcherFilterPanel getNumberPanel() {
		if (numberPanel == null) {
			numberPanel = new TextMatcherFilterPanel(TextMatcherFilterPanel.NUMBER_WORDING);
			numberPanel.setTextMatcher(data.getFilter().getNumberMatcher());
			numberPanel.setCheckBoxesVisible(false);
		}
		return numberPanel;
	}

	private NatureFilterPanel getReceipts_expensesPanel() {
		if (receipts_expensesPanel == null) {
			receipts_expensesPanel = new NatureFilterPanel();
			receipts_expensesPanel.addPropertyChangeListener(inconsistencyListener);
			receipts_expensesPanel.setSelected(data.getFilter().isOk(Filter.RECEIPTS), data.getFilter().isOk(Filter.EXPENSES));
		}
		return receipts_expensesPanel;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gbl_panel = new GridBagLayout();
			panel.setLayout(gbl_panel);
			GridBagConstraints gbc_commentPanel = new GridBagConstraints();
			gbc_commentPanel.weightx = 1.0;
			gbc_commentPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_commentPanel.gridwidth = 2;
			gbc_commentPanel.insets = new Insets(0, 0, 5, 5);
			gbc_commentPanel.gridx = 0;
			gbc_commentPanel.gridy = 1;
			panel.add(getCommentPanel(), gbc_commentPanel);
			GridBagConstraints gbc_amountPanel = new GridBagConstraints();
			gbc_amountPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_amountPanel.insets = new Insets(0, 0, 5, 5);
			gbc_amountPanel.gridx = 0;
			gbc_amountPanel.gridy = 3;
			panel.add(getAmountPanel(), gbc_amountPanel);
			GridBagConstraints gbc_Receipts_expensesPanel = new GridBagConstraints();
			gbc_Receipts_expensesPanel.insets = new Insets(0, 0, 5, 5);
			gbc_Receipts_expensesPanel.weightx = 1.0;
			gbc_Receipts_expensesPanel.anchor = GridBagConstraints.NORTH;
			gbc_Receipts_expensesPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_Receipts_expensesPanel.gridx = 0;
			gbc_Receipts_expensesPanel.gridy = 2;
			panel.add(getReceipts_expensesPanel(), gbc_Receipts_expensesPanel);
			GridBagConstraints gbc_descriptionPanel = new GridBagConstraints();
			gbc_descriptionPanel.gridwidth = 2;
			gbc_descriptionPanel.insets = new Insets(0, 0, 5, 0);
			gbc_descriptionPanel.weightx = 1.0;
			gbc_descriptionPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_descriptionPanel.gridx = 0;
			gbc_descriptionPanel.gridy = 0;
			panel.add(getDescriptionPanel(), gbc_descriptionPanel);
			GridBagConstraints gbc_categoryPanel = new GridBagConstraints();
			gbc_categoryPanel.fill = GridBagConstraints.BOTH;
			gbc_categoryPanel.gridheight = 4;
			gbc_categoryPanel.insets = new Insets(0, 0, 5, 0);
			gbc_categoryPanel.gridx = 1;
			gbc_categoryPanel.gridy = 2;
			panel.add(getCategoryPanel(), gbc_categoryPanel);
			GridBagConstraints gbc_numberPanel = new GridBagConstraints();
			gbc_numberPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_numberPanel.insets = new Insets(0, 0, 5, 5);
			gbc_numberPanel.gridx = 0;
			gbc_numberPanel.gridy = 4;
			panel.add(getNumberPanel(), gbc_numberPanel);
			GridBagConstraints gbc_statementPanel = new GridBagConstraints();
			gbc_statementPanel.insets = new Insets(0, 0, 5, 5);
			gbc_statementPanel.anchor = GridBagConstraints.NORTH;
			gbc_statementPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_statementPanel.gridx = 0;
			gbc_statementPanel.gridy = 5;
			panel.add(getStatementPanel(), gbc_statementPanel);
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.insets = new Insets(0, 0, 5, 5);
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.weightx = 1.0D;
			gridBagConstraints21.gridy = 1;
			this.add(getDatePanel(), gridBagConstraints21);
		}
		return panel;
	}
	private TextMatcherFilterPanel getCommentPanel() {
		if (commentPanel == null) {
			commentPanel = new TextMatcherFilterPanel(TextMatcherFilterPanel.COMMENT_WORDING);
			commentPanel.setTextMatcher(data.getFilter().getCommentMatcher());
		}
		return commentPanel;
	}
}
