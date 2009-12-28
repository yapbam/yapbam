package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JCheckBox;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.ListModel;

import net.yapbam.data.Account;
import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Mode;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.widget.AmountWidget;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeSet;

import javax.swing.JTextField;
import net.yapbam.gui.widget.DateWidgetPanel;
import net.yapbam.util.NullUtils;
import net.yapbam.util.TextMatcher;
import javax.swing.JButton;

public class CustomFilterPanel extends JPanel { //LOCAL
	private static final long serialVersionUID = 1L;
	public static final String CONSISTENCY_PROPERTY = "CONSISTENCY";
	private JPanel accountPanel = null;
	private JList accountList = null;
	private JPanel amountPanel = null;
	private JPanel categoryPanel = null;
	private JList categoryList = null;
	private JPanel jPanel3 = null;
	private JRadioButton amountEquals = null;
	private JRadioButton amountBetween = null;
	private AmountWidget minAmount = null;
	private JLabel jLabel = null;
	private AmountWidget maxAmount = null;
	private JRadioButton amountAll = null;
	private boolean oldConsistency;
	private JPanel descriptionPanel = null;
	private JCheckBox ignoreCase = null;
	private JTextField description = null;
	private JPanel datePanel = null;
	private JRadioButton dateAll = null;
	private JRadioButton dateEquals = null;
	private JRadioButton dateBetween = null;
	private DateWidgetPanel dateFrom = null;
	private JLabel jLabel1 = null;
	private DateWidgetPanel dateTo = null;
	private JPanel statementPanel = null;
	private JCheckBox checked = null;
	private JCheckBox notChecked = null;
	private JPanel valueDatePanel = null;
	private JRadioButton valueDateAll = null;
	private JRadioButton valueDateEquals = null;
	private JRadioButton valueDateBetween = null;
	private DateWidgetPanel valueDateFrom = null;
	private JLabel jLabel11 = null;
	private DateWidgetPanel valueDateTo = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private JTextField statement = null;
	private JPanel jPanel = null;
	private JCheckBox ignoreDiacritics = null;
	private JLabel regexpHelp = null;
	private JPanel jPanel1 = null;
	private JRadioButton descriptionEqualsTo = null;
	private JRadioButton descriptionContains = null;
	private JRadioButton descriptionRegular = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel11 = null;
	private JLabel regexpStatement = null;
	private JRadioButton statementEqualsTo = null;
	private JRadioButton statementContains = null;
	private JRadioButton statementRegular = null;
	private JButton clear = null;
	private JPanel modePanel = null;
	private JScrollPane jScrollPane2 = null;
	private JList modes = null;
		
	private FilteredData data;
	
	private final class RegexprListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				Desktop.getDesktop().browse(new URI("http://www.yapbam.net/"));
			} catch (Exception exception) {
				String message = MessageFormat.format("Erreur {0} lors de l'ouverture de l'aide", exception.toString());
				JOptionPane.showMessageDialog(AbstractDialog.getOwnerWindow(regexpHelp), message, "Impossible d'ouvrir l'aide", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	private PropertyChangeListener CONSISTENCY_CHECKER = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			checkConsistency();  //  @jve:decl-index=0:
		}
	};

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
		GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
		gridBagConstraints28.gridx = 1;
		gridBagConstraints28.fill = GridBagConstraints.BOTH;
		gridBagConstraints28.gridy = 0;
		GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
		gridBagConstraints110.gridx = 0;
		gridBagConstraints110.gridy = 4;
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.fill = GridBagConstraints.BOTH;
		gridBagConstraints41.weightx = 1.0D;
		gridBagConstraints41.gridy = 3;
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 1;
		gridBagConstraints31.fill = GridBagConstraints.BOTH;
		gridBagConstraints31.gridwidth = 1;
		gridBagConstraints31.weightx = 1.0D;
		gridBagConstraints31.gridheight = 0;
		gridBagConstraints31.gridy = 3;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.weightx = 1.0D;
		gridBagConstraints21.gridy = 2;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.fill = GridBagConstraints.BOTH;
		gridBagConstraints15.gridwidth = 2;
		gridBagConstraints15.gridy = 1;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.weightx = 4.0D;
		gridBagConstraints3.weighty = 2.0D;
		gridBagConstraints3.gridwidth = 1;
		gridBagConstraints3.gridheight = 0;
		gridBagConstraints3.gridy = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints2.gridy = 2;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridy = 0;
		this.setSize(800, 400);
		this.setLayout(new GridBagLayout());
		this.add(getAccountPanel(), gridBagConstraints);
		this.add(getAmountPanel(), gridBagConstraints2);
		this.add(getCategoryPanel(), gridBagConstraints3);
		this.add(getDescriptionPanel(), gridBagConstraints15);
		this.add(getDatePanel(), gridBagConstraints21);
		this.add(getStatementPanel(), gridBagConstraints31);
		this.add(getValueDatePanel(), gridBagConstraints41);
		this.add(getClear(), gridBagConstraints110);
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
			accountList.setToolTipText("Sélectionner ici les comptes autorisés");
			ArrayList<Integer> indices = new ArrayList<Integer>(data.getGlobalData().getAccountsNumber()); 
			for (int i=0;i<data.getGlobalData().getAccountsNumber();i++) {
				if (data.isOk(data.getGlobalData().getAccount(i))) indices.add(i);
			}
			int[] selection = new int[indices.size()];
			for (int i = 0; i < indices.size(); i++) {
				selection[i] = indices.get(i);
			}
			accountList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					updateModesList();
				}
			});
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
			categoryPanel.setBorder(BorderFactory.createTitledBorder(null, "Catégories", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
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
			categoryList.setToolTipText("Sélectionnez ici les catégories autorisées");
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
			amountEquals.setText("Egal à");
			amountEquals.setToolTipText("Sélectionnez ce bouton pour ne retenir que les opérations dont le montant est saisi ci-contre");
			amountEquals.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountEquals.isSelected()) {
						getMinAmount().setEnabled(true);
						getMaxAmount().setEnabled(false);
						getMaxAmount().setValue(getMinAmount().getValue());
					}
				}
			});
			amountEquals.setSelected(data.getMinimumAmount()==data.getMaximumAmount());
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
			amountBetween.setToolTipText("Sélectionnez ce bouton pour ne retenir que les opérations dont le montant est compris entre les montants ci-contre");
			amountBetween.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountBetween.isSelected()) {
						getMinAmount().setEnabled(true);
						getMaxAmount().setEnabled(true);
					}
				}
			});
			amountBetween.setSelected(((data.getMinimumAmount()!=Double.NEGATIVE_INFINITY) ||
					(data.getMaximumAmount()!=Double.POSITIVE_INFINITY)) &&
					(data.getMaximumAmount()!=data.getMinimumAmount()));
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
			minAmount.setEmptyAllowed(true);
			minAmount.setToolTipText("Montant minimum");
			minAmount.setValue(data.getMinimumAmount()==Double.NEGATIVE_INFINITY?null:data.getMinimumAmount());
			minAmount.addPropertyChangeListener(AmountWidget.VALUE_PROPERTY, CONSISTENCY_CHECKER);
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
			maxAmount.setEmptyAllowed(true);
			maxAmount.setToolTipText("Montant maximum");
			maxAmount.setValue(data.getMaximumAmount()==Double.POSITIVE_INFINITY?null:data.getMaximumAmount());
			maxAmount.addPropertyChangeListener(AmountWidget.VALUE_PROPERTY, CONSISTENCY_CHECKER);
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
			amountAll.setToolTipText("Sélectionnez ce bouton pour ne pas filtrer sur le montant de l'opération");
			amountAll.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (amountAll.isSelected()) {
						getMinAmount().setEnabled(false);
						getMaxAmount().setEnabled(false);
					}
				}
			});
			amountAll.setSelected((data.getMinimumAmount()==Double.NEGATIVE_INFINITY) && (data.getMaximumAmount()==Double.POSITIVE_INFINITY));
		}
		return amountAll;
	}

	@SuppressWarnings("serial")
	private void updateModesList() {
		// Remember what modes are currently selected.
		// This will allow us to re-select them after updating the list.
		int[] selectedIndices = getModes().getSelectedIndices();
		String[] currentSelectedModes = new String[selectedIndices.length];
		for (int i = 0; i < currentSelectedModes.length; i++) {
			currentSelectedModes[i] = (String) getModes().getModel().getElementAt(selectedIndices[i]);
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
		// Restore the selection of items that have been deleted
		ArrayList<Integer> newSelection = new ArrayList<Integer>();
		for (int i = 0; i < currentSelectedModes.length; i++) {
			int index = Arrays.binarySearch(arrayModes, currentSelectedModes[i]);
			if (index>=0) newSelection.add(index);
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
		long time = System.currentTimeMillis(); //TODO
		// build the account filter 
		int[] accountIndices = this.accountList.getSelectedIndices();
		Account[] accounts = new Account[accountIndices.length];
		for (int i = 0; i < accounts.length; i++) {
			accounts[i] = data.getGlobalData().getAccount(accountIndices[i]);
		}
		this.data.setAccounts(accounts);
		// build the category filter
		int[] categoryIndices = this.categoryList.getSelectedIndices();
		Category[] categories = new Category[categoryIndices.length];
		for (int i = 0; i < categories.length; i++) {
			categories[i] = data.getGlobalData().getCategory(categoryIndices[i]);
		}
		this.data.setCategories(categories);
		// build the expense/receipt filter
		double min = getMinAmount().getValue()==null?Double.NEGATIVE_INFINITY:getMinAmount().getValue();
		double max = getMaxAmount().getValue()==null?Double.POSITIVE_INFINITY:getMaxAmount().getValue();
		this.data.setAmountFilter(min, max);
		// build the date filter
		Date from = getDateFrom().getDate();
		Date to = getDateTo().getDate();
		if (getDateAll().isSelected()) {
			this.data.setDateFilter(null, null);
		} else {
			if (getDateEquals().isSelected()) to = from;
			this.data.setDateFilter(from, to);
		}
		// build the value date filter
		Date vfrom = getValueDateFrom().getDate();
		Date vto = getValueDateTo().getDate();
		if (getValueDateAll().isSelected()) {
			this.data.setValueDateFilter(null, null);
		} else {
			if (getValueDateEquals().isSelected()) vto = vfrom;
			this.data.setValueDateFilter(vfrom, vto);
		}
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
		int filter = 0;
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
		// TODO Auto-generated method stub
		time = System.currentTimeMillis()-time;
		System.out.println ("filtering done in "+time+"ms"); //TODO
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
		if ((getDateFrom().getDate()!=null) && (getDateTo().getDate()!=null)
				&& (getDateFrom().getDate().compareTo(getDateTo().getDate())>0)) {
			return "La date de début doit être antérieure à la date de fin";
		}
		if ((getValueDateFrom().getDate()!=null) && (getValueDateTo().getDate()!=null)
				&& (getValueDateFrom().getDate().compareTo(getValueDateTo().getDate())>0)) {
			return "La date de valeur de début doit être antérieure à la date de valeur de fin";
		}
		if ((getMinAmount().getValue()!=null) && (getMaxAmount().getValue()!=null)
				&& (getMinAmount().getValue()>getMaxAmount().getValue())) {
			return "Le montant minimum doit être inférieur au montant maxi";
		}
		return null;
	}

	public boolean isConsistent() {
		return getInconsistencyCause()!=null;
	}
		
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
			regexpHelp.setText("");
			regexpHelp.setToolTipText("Cliquez ici pour obtenir de l'aide sur les expressions régulières");
			regexpHelp.setIcon(IconManager.HELP);
			regexpHelp.addMouseListener(new RegexprListener());
			descriptionPanel = new JPanel();
			descriptionPanel.setLayout(new GridBagLayout());
			descriptionPanel.setBorder(BorderFactory.createTitledBorder(null, "Libellé", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
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
			ignoreCase.setText("Ignorer la casse");
			ignoreCase.setToolTipText("Cochez cette case si la comparaison ne doit pas faire la distinction majuscule/minuscules");
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
			description.setText(data.getDescriptionFilter()==null?"":data.getDescriptionFilter().getFilter());
			description.setToolTipText("Texte à comparer au libellé des opérations");
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
			dateAll.setToolTipText("Sélectionnez ce bouton pour ne pas filtrer sur la date d'opération");
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
			dateAll.setSelected(data.getDateFrom()==null && data.getDateTo()==null);
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
			dateEquals.setText("Egale à");
			dateEquals.setToolTipText("Sélectionnez ce bouton pour ne retenir que les opérations dont la date est saisie ci-contre");
			dateEquals.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (dateEquals.isSelected()) {
						getDateFrom().setEnabled(true);
						getDateTo().setEnabled(false);
						getDateTo().setDate(getDateFrom().getDate());
					}
				}
			});
			boolean areEquals = (data.getDateFrom()!=null) && NullUtils.areEquals(data.getDateFrom(), data.getDateTo());
			dateEquals.setSelected(areEquals);
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
			dateBetween.setToolTipText("Sélectionnez ce bouton pour ne retenir que les opérations dont la date est comprise entre les dates ci-contre");
			dateBetween.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (dateBetween.isSelected()) {
						getDateFrom().setEnabled(true);
						getDateTo().setEnabled(true);
					}
				}
			});
		}
		dateBetween.setSelected(!NullUtils.areEquals(data.getDateFrom(), data.getDateTo()));
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
			dateFrom.setToolTipText("Plus ancienne date d'opération autorisée");
			dateFrom.setDate(data.getDateFrom());
			dateFrom.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, CONSISTENCY_CHECKER);
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
			dateTo.setToolTipText("Plus récente date d'opération autorisée");
			dateTo.setDate(data.getDateTo());
			dateTo.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, CONSISTENCY_CHECKER);
		}
		return dateTo;
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
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints29.gridwidth = 0;
			gridBagConstraints29.gridy = 1;
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
			statementPanel.setBorder(BorderFactory.createTitledBorder(null, "Pointage", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			statementPanel.add(getChecked(), gridBagConstraints26);
			statementPanel.add(getNotChecked(), gridBagConstraints27);
			statementPanel.add(getJPanel(), gridBagConstraints29);
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
			checked.setText("Pointées");
			checked.setToolTipText("Cochez cette case pour autoriser les opérations pointées");
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
			notChecked.setText("Non pointées");
			notChecked.setToolTipText("Cochez cette case pour autoriser les opérations non pointées");
			notChecked.setSelected(data.isOk(FilteredData.NOT_CHECKED));
			notChecked.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					checkConsistency();
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
	private JPanel getValueDatePanel() {
		if (valueDatePanel == null) {
			TitledBorder titledBorder = BorderFactory.createTitledBorder(null, "Date", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51));
			titledBorder.setTitle("Date de valeur");
			GridBagConstraints gridBagConstraints251 = new GridBagConstraints();
			gridBagConstraints251.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints251.gridx = 3;
			gridBagConstraints251.gridy = 0;
			gridBagConstraints251.weightx = 1.0D;
			gridBagConstraints251.gridheight = 3;
			GridBagConstraints gridBagConstraints241 = new GridBagConstraints();
			gridBagConstraints241.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints241.gridx = 2;
			gridBagConstraints241.gridy = 0;
			gridBagConstraints241.gridheight = 3;
			jLabel11 = new JLabel();
			jLabel11.setText("et");
			GridBagConstraints gridBagConstraints231 = new GridBagConstraints();
			gridBagConstraints231.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints231.gridx = 1;
			gridBagConstraints231.gridy = 0;
			gridBagConstraints231.weightx = 1.0D;
			gridBagConstraints231.gridheight = 3;
			GridBagConstraints gridBagConstraints221 = new GridBagConstraints();
			gridBagConstraints221.anchor = GridBagConstraints.WEST;
			gridBagConstraints221.gridy = 2;
			gridBagConstraints221.gridx = 0;
			GridBagConstraints gridBagConstraints201 = new GridBagConstraints();
			gridBagConstraints201.anchor = GridBagConstraints.WEST;
			gridBagConstraints201.gridy = 1;
			gridBagConstraints201.gridx = 0;
			GridBagConstraints gridBagConstraints191 = new GridBagConstraints();
			gridBagConstraints191.anchor = GridBagConstraints.WEST;
			gridBagConstraints191.gridy = 0;
			gridBagConstraints191.gridx = 0;
			valueDatePanel = new JPanel();
			valueDatePanel.setLayout(new GridBagLayout());
			valueDatePanel.setBorder(titledBorder);
			valueDatePanel.add(getValueDateAll(), gridBagConstraints191);
			valueDatePanel.add(getValueDateEquals(), gridBagConstraints201);
			valueDatePanel.add(getValueDateBetween(), gridBagConstraints221);
			valueDatePanel.add(getValueDateFrom(), gridBagConstraints231);
			valueDatePanel.add(jLabel11, gridBagConstraints241);
			valueDatePanel.add(getValueDateTo(), gridBagConstraints251);
			valueDatePanel.setVisible(true);
			ButtonGroup group = new ButtonGroup();
			group.add(getValueDateAll());
			group.add(getValueDateEquals());
			group.add(getValueDateBetween());
		}
		return valueDatePanel;
	}

	/**
	 * This method initializes valueDateAll	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getValueDateAll() {
		if (valueDateAll == null) {
			valueDateAll = new JRadioButton();
			valueDateAll.setText("Toutes");
			valueDateAll.setToolTipText("Sélectionnez ce bouton pour ne pas filtrer sur la date de valeur");
			valueDateAll.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (valueDateAll.isSelected()) {
						getValueDateFrom().setEnabled(false);
						getValueDateFrom().setDate(null);
						getValueDateTo().setEnabled(false);
						getValueDateTo().setDate(null);
					}
				}
			});
			valueDateAll.setSelected(data.getValueDateFrom()==null && data.getValueDateTo()==null);
		}
		return valueDateAll;
	}

	/**
	 * This method initializes valueDateEquals	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getValueDateEquals() {
		if (valueDateEquals == null) {
			valueDateEquals = new JRadioButton();
			valueDateEquals.setText("Egale à");
			valueDateEquals.setToolTipText("Sélectionnez ce bouton pour ne retenir que les opérations dont la date de valeur est saisie ci-contre");
			valueDateEquals.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (valueDateEquals.isSelected()) {
						getValueDateFrom().setEnabled(true);
						getValueDateTo().setEnabled(false);
						getValueDateTo().setDate(getValueDateFrom().getDate());
					}
				}
			});
			boolean areEquals = (data.getValueDateFrom()!=null) && NullUtils.areEquals(data.getValueDateFrom(), data.getValueDateTo());
			valueDateEquals.setSelected(areEquals);
		}
		return valueDateEquals;
	}

	/**
	 * This method initializes valueDateBetween	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getValueDateBetween() {
		if (valueDateBetween == null) {
			valueDateBetween = new JRadioButton();
			valueDateBetween.setText("Comprise entre");
			valueDateBetween.setToolTipText("Sélectionnez ce bouton pour ne retenir que les opérations dont la date de valeur est comprise entre les dates ci-contre");
			valueDateBetween.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (valueDateBetween.isSelected()) {
						getValueDateFrom().setEnabled(true);
						getValueDateTo().setEnabled(true);
					}
				}
			});
			valueDateBetween.setSelected(!NullUtils.areEquals(data.getValueDateFrom(), data.getValueDateTo()));
		}
		return valueDateBetween;
	}

	/**
	 * This method initializes valueDateFrom	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidgetPanel	
	 */
	private DateWidgetPanel getValueDateFrom() {
		if (valueDateFrom == null) {
			valueDateFrom = new DateWidgetPanel();
			valueDateFrom.setToolTipText("Plus ancienne date de valeur autorisée");
			valueDateFrom.setDate(data.getValueDateFrom());
			valueDateFrom.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, CONSISTENCY_CHECKER);
		}
		return valueDateFrom;
	}

	/**
	 * This method initializes valueDateTo	
	 * 	
	 * @return net.yapbam.gui.widget.DateWidgetPanel	
	 */
	private DateWidgetPanel getValueDateTo() {
		if (valueDateTo == null) {
			valueDateTo = new DateWidgetPanel();
			valueDateTo.setToolTipText("Plus récente date de valeur autorisée");
			valueDateTo.setDate(data.getValueDateTo());
			valueDateTo.addPropertyChangeListener(DateWidgetPanel.DATE_PROPERTY, CONSISTENCY_CHECKER);
		}
		return valueDateTo;
	}

	/**
	 * This method initializes statement	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStatement() {
		if (statement == null) {
			statement = new JTextField();
			statement.setText(data.getStatementFilter()==null?"":data.getStatementFilter().getFilter());
			statement.setToolTipText("Texte à comparer au relevé des opérations");
		}
		return statement;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
		}
		return jPanel;
	}

	/**
	 * This method initializes ignoreDiacritics	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIgnoreDiacritics() {
		if (ignoreDiacritics == null) {
			ignoreDiacritics = new JCheckBox();
			ignoreDiacritics.setText("Ignorer les accents");
			ignoreDiacritics.setToolTipText("Cochez cette case si la comparaison ne doit pas tenir compte des accents");
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
			jPanel1.setToolTipText("Cliquez ici pour obtenir de l'aide sur les expressions régulières");
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
			descriptionEqualsTo.setText("Egal à");
			descriptionEqualsTo.setToolTipText("Sélectionner ce bouton si le libellé doit être égal au champ ci-contre");
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
			descriptionContains.setText("Contient");
			descriptionContains.setToolTipText("Sélectionner ce bouton si le libellé doit contenir champ ci-contre");
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
			descriptionRegular.setText("Expression régulière");
			descriptionRegular.setToolTipText("Sélectionner ce bouton si le libellé doit satisfaire l'expression régulière contenue dans champ ci-contre");
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
			regexpStatement.setToolTipText("Cliquez ici pour obtenir de l\'aide sur les expressions régulières");
			regexpStatement.setText("");
			regexpStatement.setIcon(IconManager.HELP);
			regexpStatement.addMouseListener(new RegexprListener());

			jPanel11 = new JPanel();
			jPanel11.setLayout(new GridBagLayout());
			jPanel11.setBorder(BorderFactory.createTitledBorder(null, "Relevé", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
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
			statementEqualsTo.setText("Egal à");
			statementEqualsTo.setToolTipText("Sélectionner ce bouton si le relevé doit être égal au champ ci-contre");
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
			statementContains.setText("Contient");
			statementContains.setToolTipText("Sélectionner ce bouton si le relevé doit contenir champ de droite");
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
			statementRegular.setText("Expression régulière");
			statementRegular.setToolTipText("Sélectionner ce bouton si le relevé doit satisfaire l'expression régulière contenue dans champ de droite");
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
			clear.setText("Effacer tous les filtres");
			clear.setToolTipText("Cliquez ici pour effacer tous les filtres");
			clear.setEnabled(false);
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
			modePanel.setBorder(BorderFactory.createTitledBorder(null, "Mode de paiement", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
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
			jScrollPane2.setToolTipText("");
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
			modes.setToolTipText("Sélectionnez ici les modes de paiement autorisés");
		}
		return modes;
	}
}
