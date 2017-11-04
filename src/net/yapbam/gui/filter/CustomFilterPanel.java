package net.yapbam.gui.filter;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;

import java.awt.GridBagConstraints;

import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.Scrollable;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.data.comparator.AccountComparator;
import net.yapbam.data.comparator.CategoryComparator;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.TextMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JButton;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.utilities.NullUtils;

public class CustomFilterPanel extends JPanel implements Scrollable {
	private static final long serialVersionUID = 1L;
	public static final String INCONSISTENCY_CAUSE_PROPERTY = "InconsistencyCause"; //$NON-NLS-1$
	private static final String CATEGORY_UNDEFINED_RESOURCE = "Category.undefined"; //$NON-NLS-1$
	private static final String MODE_UNDEFINED_RESOURCE = "Mode.undefined"; //$NON-NLS-1$
	
	private JPanel accountPanel;
	private JList accountList;
	private AmountPanel amountPanel;
	private JPanel categoryPanel;
	private JList categoryList;
	private TextMatcherFilterPanel descriptionPanel;
	private JPanel statementPanel;
	private JCheckBox checked;
	private JCheckBox notChecked;
	private DateFilterPanel valueDatePanel;
	private JScrollPane jScrollPane;
	private JScrollPane jScrollPane1;
	private TextMatcherFilterPanel jPanel11;
	private JButton clear;
	private JPanel modePanel;
	private JScrollPane jScrollPane2;
	private JList modes;
		
	private String oldInconsistencyCause;
	private Filter filter;
	private GlobalData gData;
	
	private ListSelectionListener CONSISTENCY_CHECKER_LIST = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			checkConsistency();  //  @jve:decl-index=0:
		}
	};
	
	private TextMatcherFilterPanel numberPanel;
	private NatureFilterPanel receiptsExpensesPanel;
	private JPanel panel;
	private DateFilterPanel datePanel;
	private PropertyChangeListener inconsistencyListener;
	private TextMatcherFilterPanel commentPanel;

	public CustomFilterPanel() {
		this(new Filter(), new GlobalData());
	}
	
	public CustomFilterPanel(Filter filter, GlobalData data) {
		super();
		this.gData = data;
		this.filter = filter;
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
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
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
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.gridheight = 0;
		gbcPanel.gridwidth = 2;
		gbcPanel.weighty = 1.0;
		gbcPanel.weightx = 1.0;
		gbcPanel.fill = GridBagConstraints.BOTH;
		gbcPanel.gridx = 1;
		gbcPanel.gridy = 0;
		add(getPanel(), gbcPanel);
		this.add(getAccountPanel(), gridBagConstraints);
		GridBagConstraints gbcClear = new GridBagConstraints();
		gbcClear.insets = new Insets(0, 0, 0, 5);
		gbcClear.gridx = 0;
		gbcClear.gridy = 4;
		add(getClear(), gbcClear);
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
			accountPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.account"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
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
			final Account[] accounts = AccountComparator.getSortedAccounts(gData, getLocale());
			accountList = new JList();
			accountList.setModel(new AbstractListModel(){
				public Object getElementAt(int index) {
					return accounts[index].getName();
				}
				public int getSize() {
					return accounts.length;
				}
			});
			accountList.setToolTipText(LocalizationData.get("CustomFilterPanel.account.toolTip")); //$NON-NLS-1$
			ArrayList<Integer> indices = new ArrayList<Integer>(accounts.length); 
			for (int i=0;i<accounts.length;i++) {
				if (filter.isOk(accounts[i])) {
					indices.add(i);
				}
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
			amountPanel.setAmounts(filter.getMinAmount(), filter.getMaxAmount());
		}
		return amountPanel;
	}
	
	private DateFilterPanel getDatePanel() {
		if (datePanel==null) {
			datePanel = new DateFilterPanel(DateFilterPanel.TRANSACTION_DATE);
			datePanel.addPropertyChangeListener(DateFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
			datePanel.setDates(filter.getDateFrom(), filter.getDateTo());
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
			categoryPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.category"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
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
			final Category[] categories = CategoryComparator.getSortedCategories(gData, getLocale());
			categoryList = new JList();
			categoryList.setModel(new AbstractListModel(){
				public Object getElementAt(int index) {
					Category category = categories[index];
					return category.equals(Category.UNDEFINED)?LocalizationData.get(CATEGORY_UNDEFINED_RESOURCE):category.getName();
				}
				public int getSize() {
					return categories.length;
				}
			});
			categoryList.setToolTipText(LocalizationData.get("CustomFilterPanel.category.toolTip")); //$NON-NLS-1$
			ArrayList<Integer> indices = new ArrayList<Integer>(gData.getCategoriesNumber()); 
			for (int i=0;i<categories.length;i++) {
				if (filter.isOk(categories[i])) {
					indices.add(i);
				}
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
		Object[] accountNames =	this.accountList.getSelectedValues();
		TreeSet<String> modes = new TreeSet<String>();
		for (int i = 0; i < accountNames.length; i++) {
			Account account = gData.getAccount((String)accountNames[i]);
			int nb = account.getModesNumber();
			for (int j = 0; j < nb; j++) {
				Mode mode = account.getMode(j);
				modes.add(mode.equals(Mode.UNDEFINED)?LocalizationData.get(MODE_UNDEFINED_RESOURCE):mode.getName());
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
			List<String> validModes = filter.getValidModes();
			if (validModes!=null) {
				for (int i = 0; i < validModes.size(); i++) {
					String name = validModes.get(i);
					if (name.isEmpty()) {
						name = LocalizationData.get(MODE_UNDEFINED_RESOURCE);
					}
					int index = Arrays.binarySearch(arrayModes, name);
					if (index>=0) {
						newSelection.add(index);
					}
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
				if (index>=0) {
					newSelection.add(index);
				}
			}
		}
		int[] indices = new int[newSelection.size()];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = newSelection.get(i);
		}
		getModes().setSelectedIndices(indices);
	}
	
	/** Apply the filter currently defined in this panel to the FilteredData.
	 * @return true if the edited filter changed
	 */
	public boolean apply() {
		return apply(filter);
	}

	/** Apply the filter currently defined in this panel to a filter.
	 * @return true if the edited filter changed
	 */
	public boolean apply(Filter filter) {
		filter.setSuspended(true);
		// build the account and mode filter
		Object[] selectedModes = getModes().getSelectedValues();
		ArrayList<String> modes = new ArrayList<String>();
		boolean all = true;
		Object[] accountNames = this.accountList.getSelectedValues();
		Account[] accounts = new Account[accountNames.length];
		for (int i = 0; i < accounts.length; i++) {
			accounts[i] = gData.getAccount((String)accountNames[i]);
			for (int j=0; j<accounts[i].getModesNumber(); j++) {
				Mode mode = accounts[i].getMode(j);
				if (Arrays.binarySearch(selectedModes,mode.equals(Mode.UNDEFINED)?LocalizationData.get(MODE_UNDEFINED_RESOURCE):mode.getName())<0) {
					all = false;
				} else {
					modes.add(mode.equals(Mode.UNDEFINED)?"":mode.getName()); //$NON-NLS-1$
				}
			}
		}
		filter.setValidAccounts(accounts.length==gData.getAccountsNumber()?null:Arrays.asList(accounts));
		// set the mode filter
		filter.setValidModes(all?null:modes);
		// build the category filter
		Object[] categoryNames = this.categoryList.getSelectedValues();
		if (categoryNames.length==gData.getCategoriesNumber()) {
			filter.setValidCategories(null);
		} else {
			List<Category> categories = new ArrayList<Category>(categoryNames.length);
			String undefinedCategoryName = LocalizationData.get(CATEGORY_UNDEFINED_RESOURCE);
			for (int i = 0; i < categoryNames.length; i++) {
				String name = (String)categoryNames[i];
				categories.add(undefinedCategoryName.equals(categoryNames[i])?Category.UNDEFINED:gData.getCategory(name));
			}
			filter.setValidCategories(categories);
		}
		// build the expense/receipt and amount filter
		Double min = getAmountPanel().getMinAmount();
		Double max = getAmountPanel().getMaxAmount();
		int mask = 0;
		if (getReceiptsExpensesPanel().isReceiptsSelected()) {
			mask += Filter.RECEIPTS;
		}
		if (getReceiptsExpensesPanel().isExpensesSelected()) {
			mask += Filter.EXPENSES;
		}
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
		TextMatcher statementFilter = null;
		if (getChecked().isSelected()) {
			mask += Filter.CHECKED;
			statementFilter = getJPanel11().getTextMatcher();
		}
		if (getNotChecked().isSelected()) {
			mask += Filter.NOT_CHECKED;
		}
		filter.setStatementFilter(mask, statementFilter);
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
		if (getDescriptionPanel().getInconsistencyCause()!=null) {
			return getDescriptionPanel().getInconsistencyCause();
		}
		if (getCommentPanel().getInconsistencyCause()!=null) {
			return getCommentPanel().getInconsistencyCause();
		}
		if (getNumberPanel().getInconsistencyCause()!=null) {
			return getNumberPanel().getInconsistencyCause();
		}
		if (getJPanel11().getInconsistencyCause()!=null) {
			return getJPanel11().getInconsistencyCause();
		}
		if (getReceiptsExpensesPanel().getInconsistencyCause()!=null) {
			return getReceiptsExpensesPanel().getInconsistencyCause();
		}
		if (!getChecked().isSelected() && !getNotChecked().isSelected()) {
			return Formatter.format(LocalizationData.get("CustomFilterPanel.error.checkStatus"), //$NON-NLS-1$
					LocalizationData.get("MainMenuBar.checked"), LocalizationData.get("MainMenuBar.notChecked")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (getValueDatePanel().getInconsistencyCause()!=null) {
			return getValueDatePanel().getInconsistencyCause();
		}
		if (getAmountPanel().getInconsistencyCause()!=null) {
			return getAmountPanel().getInconsistencyCause();
		}
		if (getDatePanel().getInconsistencyCause()!=null) {
			return getDatePanel().getInconsistencyCause();
		}
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
			descriptionPanel.addPropertyChangeListener(TextMatcherFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
			descriptionPanel.setTextMatcher(filter.getDescriptionMatcher());
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
			statementPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("CustomFilterPanel.check"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
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
			checked.setSelected(filter.isOk(Filter.CHECKED));
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
			notChecked.setSelected(filter.isOk(Filter.NOT_CHECKED));
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
			valueDatePanel.setDates(filter.getValueDateFrom(), filter.getValueDateTo());
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
				jPanel11.setTextMatcher(filter.getStatementMatcher());
				jPanel11.addPropertyChangeListener(TextMatcherFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
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
					clear();
				}
			});
		}
		return clear;
	}
	
	private void clear() {
		selectAll(getAccountList());
		selectAll(getModes());
		selectAll(getCategoryList());
		getDescriptionPanel().clear();
		getCommentPanel().clear();
		getReceiptsExpensesPanel().clear();
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
			modePanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("Transaction.mode"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
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
			numberPanel.addPropertyChangeListener(TextMatcherFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
			numberPanel.setTextMatcher(filter.getNumberMatcher());
			numberPanel.setCheckBoxesVisible(false);
		}
		return numberPanel;
	}

	private NatureFilterPanel getReceiptsExpensesPanel() {
		if (receiptsExpensesPanel == null) {
			receiptsExpensesPanel = new NatureFilterPanel();
			receiptsExpensesPanel.addPropertyChangeListener(inconsistencyListener);
			receiptsExpensesPanel.setSelected(filter.isOk(Filter.RECEIPTS), filter.isOk(Filter.EXPENSES));
		}
		return receiptsExpensesPanel;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gblPanel = new GridBagLayout();
			panel.setLayout(gblPanel);
			GridBagConstraints gbcCommentPanel = new GridBagConstraints();
			gbcCommentPanel.weightx = 1.0;
			gbcCommentPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcCommentPanel.gridwidth = 2;
			gbcCommentPanel.insets = new Insets(0, 0, 5, 5);
			gbcCommentPanel.gridx = 0;
			gbcCommentPanel.gridy = 1;
			panel.add(getCommentPanel(), gbcCommentPanel);
			GridBagConstraints gbcAmountPanel = new GridBagConstraints();
			gbcAmountPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcAmountPanel.insets = new Insets(0, 0, 5, 5);
			gbcAmountPanel.gridx = 0;
			gbcAmountPanel.gridy = 3;
			panel.add(getAmountPanel(), gbcAmountPanel);
			GridBagConstraints gbcReceiptsExpensesPanel = new GridBagConstraints();
			gbcReceiptsExpensesPanel.insets = new Insets(0, 0, 5, 5);
			gbcReceiptsExpensesPanel.weightx = 1.0;
			gbcReceiptsExpensesPanel.anchor = GridBagConstraints.NORTH;
			gbcReceiptsExpensesPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcReceiptsExpensesPanel.gridx = 0;
			gbcReceiptsExpensesPanel.gridy = 2;
			panel.add(getReceiptsExpensesPanel(), gbcReceiptsExpensesPanel);
			GridBagConstraints gbcDescriptionPanel = new GridBagConstraints();
			gbcDescriptionPanel.gridwidth = 2;
			gbcDescriptionPanel.insets = new Insets(0, 0, 5, 0);
			gbcDescriptionPanel.weightx = 1.0;
			gbcDescriptionPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcDescriptionPanel.gridx = 0;
			gbcDescriptionPanel.gridy = 0;
			panel.add(getDescriptionPanel(), gbcDescriptionPanel);
			GridBagConstraints gbcCategoryPanel = new GridBagConstraints();
			gbcCategoryPanel.fill = GridBagConstraints.BOTH;
			gbcCategoryPanel.gridheight = 4;
			gbcCategoryPanel.insets = new Insets(0, 0, 5, 0);
			gbcCategoryPanel.gridx = 1;
			gbcCategoryPanel.gridy = 2;
			panel.add(getCategoryPanel(), gbcCategoryPanel);
			GridBagConstraints gbcNumberPanel = new GridBagConstraints();
			gbcNumberPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcNumberPanel.insets = new Insets(0, 0, 5, 5);
			gbcNumberPanel.gridx = 0;
			gbcNumberPanel.gridy = 4;
			panel.add(getNumberPanel(), gbcNumberPanel);
			GridBagConstraints gbcStatementPanel = new GridBagConstraints();
			gbcStatementPanel.insets = new Insets(0, 0, 5, 5);
			gbcStatementPanel.anchor = GridBagConstraints.NORTH;
			gbcStatementPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcStatementPanel.gridx = 0;
			gbcStatementPanel.gridy = 5;
			panel.add(getStatementPanel(), gbcStatementPanel);
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
			commentPanel.addPropertyChangeListener(TextMatcherFilterPanel.INCONSISTENCY_CAUSE_PROPERTY, inconsistencyListener);
			commentPanel.setTextMatcher(filter.getCommentMatcher());
		}
		return commentPanel;
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 40;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	/** Gets the edited filter in its original definition
	 * @return a filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/** Gets the global data the filter refers to
	 * @return A global data instance
	 */
	public GlobalData getData() {
		return gData;
	}
}
