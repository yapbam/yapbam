package net.yapbam.gui.budget;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import net.yapbam.data.BudgetView;
import net.yapbam.data.Category;
import net.yapbam.data.Filter;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;

import javax.swing.JCheckBox;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.dialog.FileChooser;
import com.fathzer.soft.ajlib.swing.table.RowHeaderRenderer;
import com.fathzer.soft.ajlib.swing.table.Table;
import com.fathzer.soft.ajlib.utilities.CSVWriter;
import com.fathzer.soft.ajlib.utilities.FileUtils;

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
		GridBagConstraints gbcBudgetTable = new GridBagConstraints();
		gbcBudgetTable.insets = new Insets(0, 0, 5, 0);
		gbcBudgetTable.fill = GridBagConstraints.BOTH;
		gbcBudgetTable.gridy = 1;
		gbcBudgetTable.weightx = 1.0;
		gbcBudgetTable.weighty = 1.0;
		gbcBudgetTable.gridx = 0;
		GridBagConstraints gbcTopPanel = new GridBagConstraints();
		gbcTopPanel.insets = new Insets(0, 0, 5, 0);
		gbcTopPanel.gridx = 0;
		gbcTopPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcTopPanel.weightx = 1.0D;
		gbcTopPanel.gridy = 0;
		this.setLayout(new GridBagLayout());
		this.add(getTopPanel(), gbcTopPanel);
		this.add(getTable(), gbcBudgetTable);
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
			GridBagConstraints gbcChckbxAddSumLine = new GridBagConstraints();
			gbcChckbxAddSumLine.anchor = GridBagConstraints.WEST;
			gbcChckbxAddSumLine.weightx = 1.0;
			gbcChckbxAddSumLine.insets = new Insets(0, 0, 0, 5);
			gbcChckbxAddSumLine.gridx = 2;
			gbcChckbxAddSumLine.gridy = 0;
			topPanel.add(getChckbxAddSumLine(), gbcChckbxAddSumLine);
			topPanel.add(getYear(), gridBagConstraints2);
			topPanel.add(getExport(), gridBagConstraints3);
			topPanel.add(getFilter(), gridBagConstraints5);
			ButtonGroup group = new ButtonGroup();
			group.add(getMonth());
			group.add(getYear());
			GridBagConstraints gbcChckbxAddSumColumn = new GridBagConstraints();
			gbcChckbxAddSumColumn.anchor = GridBagConstraints.WEST;
			gbcChckbxAddSumColumn.insets = new Insets(0, 0, 0, 5);
			gbcChckbxAddSumColumn.gridx = 2;
			gbcChckbxAddSumColumn.gridy = 1;
			topPanel.add(getChckbxAddSumColumn(), gbcChckbxAddSumColumn);
			GridBagConstraints gbcGroupSubCategories = new GridBagConstraints();
			gbcGroupSubCategories.weightx = 1.0;
			gbcGroupSubCategories.insets = new Insets(0, 0, 0, 5);
			gbcGroupSubCategories.anchor = GridBagConstraints.WEST;
			gbcGroupSubCategories.gridx = 1;
			gbcGroupSubCategories.gridy = 0;
			topPanel.add(getGroupSubCategories(), gbcGroupSubCategories);
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
							export(result, '\t', LocalizationData.getLocale(), sumLineName, sumColumnName);
						} catch (IOException e1) {
							ErrorManager.INSTANCE.display(BudgetViewPanel.this, e1);
						}
					}
				}
			});
		}
		return export;
	}
	
	/** Exports this budget to a text file.
	 * @param file that will receive the content.
	 * @param columnSeparator the character to use to separate columns
	 * @param locale The locale to use to export the dates and numbers
	 * @throws IOException
	 */
	private void export(File file, char columnSeparator, Locale locale, String dateSumWording, String categorySumWording) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		try {
			CSVWriter out = new CSVWriter(fileWriter);
			out.setSeparator(columnSeparator);
			// Output header line
			DateFormat dateFormater = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
			out.writeCell("");
			for (int i = 0; i < budget.getDatesSize(); i++) {
				out.writeCell(dateFormater.format(budget.getDate(i)));
			}
			if (categorySumWording!=null) {
				out.writeCell(categorySumWording);
			}
			// Output category lines
			NumberFormat currencyFormatter = CSVWriter.getDecimalFormater(locale);
			for (int i=0;i<budget.getCategoriesSize();i++) {
				Category category = budget.getCategory(i);
				out.newLine();
				out.writeCell(category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName());
				for (int j = 0; j < budget.getDatesSize(); j++) {
					Double value = budget.getAmount(budget.getDate(j), category);
					out.writeCell(value!=null?currencyFormatter.format(value):"");
				}
				if (categorySumWording!=null) {
					Double value = budget.getSum(category);
					out.writeCell(value!=null?currencyFormatter.format(value):"");
				}
			}
			if (dateSumWording!=null) {
				out.newLine();
				out.writeCell(dateSumWording);
				for (int j = 0; j < budget.getDatesSize(); j++) {
					Double value = budget.getSum(budget.getDate(j));
					out.writeCell(value!=null?currencyFormatter.format(value):"");
				}
				if (categorySumWording!=null) {
					Double value = budget.getSum();
					out.writeCell(value!=null?currencyFormatter.format(value):"");
				}
			}
			out.flush();
		} finally {
			fileWriter.close();
		}
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
			JTable jTable = budgetTable.getJTable();
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jTable.getTableHeader().setReorderingAllowed(false);
			jTable.getTableHeader().setDefaultRenderer(new HeaderRenderer(true));
			jTable.setCellSelectionEnabled(true);
			jTable.setDefaultRenderer(Object.class, new CellRenderer());
			budgetTable.setModel(model);
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
		if ((selected.length==0) || ((selected[selected.length-1]!=maxValue))) {
			return selected;
		}
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
			if (extra) {
				isSelected = false;
			}
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
			if (centered && (result instanceof JLabel)) {
				((JLabel)result).setHorizontalAlignment(JLabel.CENTER);
			}
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
