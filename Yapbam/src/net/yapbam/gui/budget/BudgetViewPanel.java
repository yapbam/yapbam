package net.yapbam.gui.budget;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.dialog.FileChooser;
import net.astesana.ajlib.swing.table.RowHeaderRenderer;
import net.astesana.ajlib.swing.table.Table;
import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.data.BudgetView;
import net.yapbam.data.Category;
import net.yapbam.data.Filter;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

import javax.swing.JCheckBox;

public class BudgetViewPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel topPanel = null;
	private JRadioButton month = null;
	private JRadioButton year = null;
	private JButton export = null;
	private Table budgetTable = null;
	private JButton filter = null;
	
	private BudgetView budget;
	private FilteredData data;
	private JCheckBox chckbxAddSumColumn;
	private JCheckBox chckbxAddSumLine;
	private JCheckBox groupSubCategories;
	
	/**
	 * This is the default constructor
	 */
	public BudgetViewPanel() {
		this(new FilteredData(new GlobalData()));
	}

	public BudgetViewPanel(FilteredData data) {
		this.budget = new BudgetView(data, false);
		this.data = data;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gbc_budgetTable = new GridBagConstraints();
		gbc_budgetTable.insets = new Insets(0, 0, 5, 0);
		gbc_budgetTable.fill = GridBagConstraints.BOTH;
		gbc_budgetTable.gridy = 1;
		gbc_budgetTable.weightx = 1.0;
		gbc_budgetTable.weighty = 1.0;
		gbc_budgetTable.gridx = 0;
		GridBagConstraints gbc_topPanel = new GridBagConstraints();
		gbc_topPanel.insets = new Insets(0, 0, 5, 0);
		gbc_topPanel.gridx = 0;
		gbc_topPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_topPanel.weightx = 1.0D;
		gbc_topPanel.gridy = 0;
		this.setSize(529, 287);
		this.setLayout(new GridBagLayout());
		this.add(getTopPanel(), gbc_topPanel);
		this.add(getTable(), gbc_budgetTable);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTopPanel() {
		if (topPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 3;
			gridBagConstraints5.gridheight = 0;
			gridBagConstraints5.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 4;
			gridBagConstraints3.gridheight = 0;
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			topPanel = new JPanel();
			topPanel.setLayout(new GridBagLayout());
			topPanel.add(getMonth(), gridBagConstraints1);
			GridBagConstraints gbc_chckbxAddSumLine = new GridBagConstraints();
			gbc_chckbxAddSumLine.anchor = GridBagConstraints.WEST;
			gbc_chckbxAddSumLine.weightx = 1.0;
			gbc_chckbxAddSumLine.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxAddSumLine.gridx = 2;
			gbc_chckbxAddSumLine.gridy = 0;
			topPanel.add(getChckbxAddSumLine(), gbc_chckbxAddSumLine);
			topPanel.add(getYear(), gridBagConstraints2);
			topPanel.add(getExport(), gridBagConstraints3);
			topPanel.add(getFilter(), gridBagConstraints5);
			ButtonGroup group = new ButtonGroup();
			group.add(getMonth());
			group.add(getYear());
			GridBagConstraints gbc_chckbxAddSumColumn = new GridBagConstraints();
			gbc_chckbxAddSumColumn.anchor = GridBagConstraints.WEST;
			gbc_chckbxAddSumColumn.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxAddSumColumn.gridx = 2;
			gbc_chckbxAddSumColumn.gridy = 1;
			topPanel.add(getChckbxAddSumColumn(), gbc_chckbxAddSumColumn);
			GridBagConstraints gbc_groupSubCategories = new GridBagConstraints();
			gbc_groupSubCategories.weightx = 1.0;
			gbc_groupSubCategories.insets = new Insets(0, 0, 0, 5);
			gbc_groupSubCategories.anchor = GridBagConstraints.WEST;
			gbc_groupSubCategories.gridx = 1;
			gbc_groupSubCategories.gridy = 0;
			topPanel.add(getGroupSubCategories(), gbc_groupSubCategories);
		}
		return topPanel;
	}

	/**
	 * This method initializes month	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	JRadioButton getMonth() {
		if (month == null) {
			month = new JRadioButton();
			month.setText(LocalizationData.get("BudgetPanel.perMonth")); //$NON-NLS-1$
			month.setSelected(true);
			month.setToolTipText(LocalizationData.get("BudgetPanel.perMonth.tooltip")); //$NON-NLS-1$
		}
		return month;
	}

	/**
	 * This method initializes year	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	JRadioButton getYear() {
		if (year == null) {
			year = new JRadioButton();
			year.setToolTipText(LocalizationData.get("BudgetPanel.perYear.tooltip")); //$NON-NLS-1$
			year.setText(LocalizationData.get("BudgetPanel.perYear")); //$NON-NLS-1$
			year.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					budget.setYear(year.isSelected());
				}
			});
		}
		return year;
	}

	/**
	 * This method initializes export	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getExport() {
		if (export == null) {
			export = new JButton();
			export.setText(LocalizationData.get("BudgetPanel.export")); //$NON-NLS-1$
			export.setToolTipText(LocalizationData.get("BudgetPanel.export.toolTip")); //$NON-NLS-1$
			export.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new FileChooser();
					chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
					File result = chooser.showSaveDialog(Utils.getOwnerWindow(export))==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null; //$NON-NLS-1$
					if (result!=null) {
						try {
							result = FileUtils.getCanonical(result);
							String sumColumnName = getChckbxAddSumColumn().isSelected()?LocalizationData.get("BudgetPanel.sum"):null; //$NON-NLS-1$
							String sumLineName = getChckbxAddSumLine().isSelected()?LocalizationData.get("BudgetPanel.sum"):null; //$NON-NLS-1$
							budget.export(result, '\t', LocalizationData.getLocale(), sumLineName, sumColumnName);
						} catch (IOException e1) {
							ErrorManager.INSTANCE.display(BudgetViewPanel.this, e1);
						}
					}
				}
			});
		}
		return export;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private Table getTable() {
		if (budgetTable == null) {
			budgetTable = new Table();
			BudgetTableModel model = new BudgetTableModel(budget);
			model.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					Utils.packColumns(getJTable(), 2);
				}
			});
			budgetTable.setModel(model);
			JTable jTable = budgetTable.getJTable();
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jTable.getTableHeader().setReorderingAllowed(false);
			jTable.getTableHeader().setDefaultRenderer(new HeaderRenderer(true));
			jTable.setCellSelectionEnabled(true);
			jTable.setDefaultRenderer(Object.class, new CellRenderer());
			jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						selectionChanged();
					}
				}
			});
			// If we do not register with column selection model, the listener on the table selection model receive no event
			// when the user selects a new column in the already selected row (strange, but true !!!)
			jTable.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						selectionChanged();
					}
				}
			});
			budgetTable.getRowJTable().setDefaultRenderer(Object.class, new HeaderRenderer(false));
			budgetTable.setRowHeight((int) (Preferences.INSTANCE.getFontSizeRatio()*budgetTable.getRowHeight()));
		}
		return budgetTable;
	}

	private JTable getJTable() {
		return getTable().getJTable();
	}
	
	/**
	 * This method initializes filter	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFilter() {
		if (filter == null) {
			filter = new JButton();
			filter.setText(LocalizationData.get("BudgetPanel.filter")); //$NON-NLS-1$
			filter.setEnabled(false);
			filter.setToolTipText(LocalizationData.get("BudgetPanel.filter.tooltip")); //$NON-NLS-1$
			filter.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int[] selectedRows = filterSelected(getJTable().getSelectedRows(), budget.getCategoriesSize());
					Filter theFilter = data.getFilter();
					theFilter.setSuspended(true);
					if (selectedRows.length!=budget.getCategoriesSize()) {
						if (selectedRows.length==data.getGlobalData().getCategoriesNumber()) {
							theFilter.setValidCategories(null);
						} else {
							ArrayList<Category> categories = new ArrayList<Category>(selectedRows.length);
							for (int i = 0; i < selectedRows.length; i++) {
								Category category = budget.getCategory(selectedRows[i]);
								categories.addAll(getDataCategories(category));
							}
							theFilter.setValidCategories(categories);
						}
					}
					//TODO Unable to select discontinuous time interval in the filter
					int[] selectedColumns = filterSelected(getJTable().getSelectedColumns(), budget.getDatesSize());
					if (selectedColumns.length!=budget.getDatesSize()) {
						Date from = budget.getDate(selectedColumns[0]);
						Date to = budget.getLastDate(selectedColumns[selectedColumns.length-1]);
						theFilter.setDateFilter(from, to);
					}
					theFilter.setSuspended(false);
				}
			});
		}
		return filter;
	}
	
	private ArrayList<Category> getDataCategories(Category category) {
		ArrayList<Category> result = new ArrayList<Category>();
		if (category.equals(Category.UNDEFINED) || !getGroupSubCategories().isSelected()) {
			result.add(category);
		} else {
			final GlobalData globalData = data.getGlobalData();
			for (int i = 0; i < globalData.getCategoriesNumber(); i++) {
				Category cat = globalData.getCategory(i);
				if (cat.getSuperCategory(globalData.getSubCategorySeparator()).equals(category)) {
					result.add(cat);
				}
			}
		}
		return result;
	}
	
	private int[] filterSelected(int[] selected, int maxValue) {
		if ((selected.length==0) || ((selected[selected.length-1]!=maxValue))) return selected;
		int[] result = new int[selected.length-1];
		System.arraycopy(selected, 0, result, 0, result.length);
		return result;
	}

//	private JCheckBox getChckbxAdd() {
//		if (chckbxAdd == null) {
//			chckbxAdd = new JCheckBox(LocalizationData.get("BudgetPanel.averageColumn.checkBox")); //$NON-NLS-1$
//			chckbxAdd.setSelected(true);
//			chckbxAdd.setToolTipText(LocalizationData.get("BudgetPanel.averageColumn.checkBox.tooltip"));
//			chckbxAdd.setVisible(false);
//		}
//		return chckbxAdd;
//	}

	JCheckBox getChckbxAddSumColumn() {
		if (chckbxAddSumColumn == null) {
			chckbxAddSumColumn = new JCheckBox(LocalizationData.get("BudgetPanel.sumColumn.checkBox")); //$NON-NLS-1$
			chckbxAddSumColumn.setSelected(false);
			chckbxAddSumColumn.setToolTipText(LocalizationData.get("BudgetPanel.sumColumn.checkBox.tooltip")); //$NON-NLS-1$
			chckbxAddSumColumn.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					boolean selected = chckbxAddSumColumn.isSelected();
					((BudgetTableModel)getJTable().getModel()).setHasSumColumn(selected);
				}
			});
		}
		return chckbxAddSumColumn;
	}
	JCheckBox getChckbxAddSumLine() {
		if (chckbxAddSumLine == null) {
			chckbxAddSumLine = new JCheckBox(LocalizationData.get("BudgetPanel.sumLine.checkBox")); //$NON-NLS-1$
			chckbxAddSumLine.setSelected(false);
			chckbxAddSumLine.setToolTipText(LocalizationData.get("BudgetPanel.sumLine.checkBox.tooltip")); //$NON-NLS-1$
			chckbxAddSumLine.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					boolean selected = chckbxAddSumLine.isSelected();
					((BudgetTableModel)getJTable().getModel()).setHasSumLine(selected);
				}
			});
		}
		return chckbxAddSumLine;
	}
	
	private final class CellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		Font bold;
		Font plain;
		
		CellRenderer() {
			plain = new Font(this.getFont().getName(),Font.PLAIN,this.getFont().getSize());
			bold = new Font(this.getFont().getName(),Font.BOLD,this.getFont().getSize());
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			boolean extra = (column==budget.getDatesSize()) || (row==budget.getCategoriesSize());
			if (extra) isSelected = false;
			JLabel result = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			this.setHorizontalAlignment(SwingConstants.RIGHT);
			Font font = extra ? bold : plain;
			result.setFont(font);
			return result;
		}
	}

	private final class HeaderRenderer extends RowHeaderRenderer {
		private boolean centered;
		HeaderRenderer(boolean centered) {
			this.centered = centered;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if ((column==budget.getDatesSize()) || (row==budget.getCategoriesSize())) {
				value = "<html><b>"+value.toString()+"</b></html>"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			Component result = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (centered && (result instanceof JLabel)) ((JLabel)result).setHorizontalAlignment(JLabel.CENTER);
			return result;
		}
	}
	
	JCheckBox getGroupSubCategories() {
		if (groupSubCategories==null) {
			groupSubCategories = new JCheckBox(LocalizationData.get("Subcategories.groupButton.title")); //$NON-NLS-1$
			groupSubCategories.setToolTipText(LocalizationData.get("Subcategories.groupButton.tooltip")); //$NON-NLS-1$
			groupSubCategories.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					budget.setGroupedSubCategories(getGroupSubCategories().isSelected());
				}
			});
		}
		return groupSubCategories;
	}

	private void selectionChanged() {
		int[] rows = getJTable().getSelectedRows();
		int[] columns = getJTable().getSelectedColumns();
		int[] fRows = filterSelected(rows, budget.getCategoriesSize());
		int[] fColumns = filterSelected(columns, budget.getDatesSize());
		getFilter().setEnabled((fRows.length>0) && (fColumns.length>0));
	}
}
