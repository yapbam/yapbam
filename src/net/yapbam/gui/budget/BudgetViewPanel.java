package net.yapbam.gui.budget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LookAndFeel;
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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import net.yapbam.data.BudgetView;
import net.yapbam.data.Category;
import net.yapbam.data.Filter;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AbstractDialog;
import net.yapbam.gui.util.SafeJFileChooser;
import javax.swing.JCheckBox;

public class BudgetViewPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JRadioButton month = null;
	private JRadioButton year = null;
	private JButton export = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JButton filter = null;
	
	private BudgetView budget;
	private FilteredData data;
	private JCheckBox chckbxAdd;
	private JCheckBox chckbxAddSumColumn;
	private JCheckBox chckbxAddSumLine;
	
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
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.BOTH;
		gridBagConstraints4.gridy = 1;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.weighty = 1.0;
		gridBagConstraints4.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.gridy = 0;
		this.setSize(529, 287);
		this.setLayout(new GridBagLayout());
		this.add(getJPanel(), gridBagConstraints);
		this.add(getJScrollPane(), gridBagConstraints4);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 3;
			gridBagConstraints5.gridheight = 2;
			gridBagConstraints5.insets = new Insets(5, 5, 0, 5);
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 4;
			gridBagConstraints3.gridheight = 0;
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.insets = new Insets(5, 5, 0, 5);
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
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getMonth(), gridBagConstraints1);
			GridBagConstraints gbc_chckbxAdd = new GridBagConstraints();
			gbc_chckbxAdd.anchor = GridBagConstraints.WEST;
			gbc_chckbxAdd.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxAdd.gridx = 1;
			gbc_chckbxAdd.gridy = 0;
			jPanel.add(getChckbxAdd(), gbc_chckbxAdd);
			GridBagConstraints gbc_chckbxAddSumLine = new GridBagConstraints();
			gbc_chckbxAddSumLine.gridheight = 2;
			gbc_chckbxAddSumLine.anchor = GridBagConstraints.WEST;
			gbc_chckbxAddSumLine.weightx = 1.0;
			gbc_chckbxAddSumLine.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxAddSumLine.gridx = 2;
			gbc_chckbxAddSumLine.gridy = 0;
			jPanel.add(getChckbxAddSumLine(), gbc_chckbxAddSumLine);
			jPanel.add(getYear(), gridBagConstraints2);
			jPanel.add(getExport(), gridBagConstraints3);
			jPanel.add(getFilter(), gridBagConstraints5);
			ButtonGroup group = new ButtonGroup();
			group.add(getMonth());
			group.add(getYear());
			GridBagConstraints gbc_chckbxAddSumColumn = new GridBagConstraints();
			gbc_chckbxAddSumColumn.anchor = GridBagConstraints.WEST;
			gbc_chckbxAddSumColumn.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxAddSumColumn.gridx = 1;
			gbc_chckbxAddSumColumn.gridy = 1;
			jPanel.add(getChckbxAddSumColumn(), gbc_chckbxAddSumColumn);
		}
		return jPanel;
	}

	/**
	 * This method initializes month	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getMonth() {
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
	private JRadioButton getYear() {
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
					JFileChooser chooser = new SafeJFileChooser(export.getText());
					chooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
					File result = chooser.showSaveDialog(AbstractDialog.getOwnerWindow(export))==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null; //$NON-NLS-1$
					if (result!=null) {
						try {
							budget.export(result, '\t', LocalizationData.getLocale());
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
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			TableModel rowHeaderModel = new BudgetTableRowHeaderModel(budget);
			final JTable rowView = new JTable(rowHeaderModel);
			rowHeaderModel.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					setRowViewSize(rowView);
				}
			});
			rowView.setDefaultRenderer(Object.class, new RowHeaderRenderer(true));
	        LookAndFeel.installColorsAndFont (rowView, "TableHeader.background", "TableHeader.foreground", "TableHeader.font"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			jScrollPane.setRowHeaderView(rowView);
			jScrollPane.setViewportView(getJTable());
			setRowViewSize(rowView);
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	@SuppressWarnings("serial")
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable(new BudgetTableModel(budget));
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jTable.getTableHeader().setReorderingAllowed(false);
			jTable.setCellSelectionEnabled(true);
			jTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
					JLabel result = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
							row, column);
					this.setHorizontalAlignment(SwingConstants.RIGHT);
					return result;
				}
				
			});
			jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					getFilter().setEnabled(getJTable().getSelectedRowCount()>0);
				}
			});
		}
		return jTable;
	}
	
	private void setRowViewSize(final JTable rowView) {
		int width = TableColumnUtils.packColumn(rowView, 0, 2);
		Dimension d = rowView.getPreferredScrollableViewportSize();
		d.width = width;
		rowView.getColumnModel().getColumn(0).setPreferredWidth(width);
		rowView.setPreferredScrollableViewportSize(d);
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
					int[] selectedRows = getJTable().getSelectedRows();
					Filter theFilter = data.getFilter();
					theFilter.setSuspended(true);
					if (selectedRows.length!=getJTable().getRowCount()) {
						if (selectedRows.length==data.getGlobalData().getCategoriesNumber()) {
							theFilter.setValidCategories(null);
						} else {
							ArrayList<Category> categories = new ArrayList<Category>(selectedRows.length);
							for (int i = 0; i < selectedRows.length; i++) {
								categories.add(budget.getCategory(selectedRows[i]));
							}
							theFilter.setValidCategories(categories);
						}
					}
					//FIXME Unable to select discontinuous time interval in the filter
					int[] selectedColumns = jTable.getSelectedColumns();
					if (selectedRows.length!=budget.getDatesSize()) {
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
	private JCheckBox getChckbxAdd() {
		if (chckbxAdd == null) {
			chckbxAdd = new JCheckBox(LocalizationData.get("BudgetPanel.averageColumn.checkBox")); //$NON-NLS-1$
			chckbxAdd.setSelected(true);
			chckbxAdd.setToolTipText(LocalizationData.get("BudgetPanel.averageColumn.checkBox.tooltip"));
			chckbxAdd.setVisible(false);
		}
		return chckbxAdd;
	}
	private JCheckBox getChckbxAddSumColumn() {
		if (chckbxAddSumColumn == null) {
			chckbxAddSumColumn = new JCheckBox(LocalizationData.get("BudgetPanel.sumColumn.checkBox")); //$NON-NLS-1$
			chckbxAddSumColumn.setSelected(true);
			chckbxAddSumColumn.setToolTipText(LocalizationData.get("BudgetPanel.sumColumn.checkBox.tooltip")); //$NON-NLS-1$
			chckbxAddSumColumn.setVisible(false);
		}
		return chckbxAddSumColumn;
	}
	private JCheckBox getChckbxAddSumLine() {
		if (chckbxAddSumLine == null) {
			chckbxAddSumLine = new JCheckBox(LocalizationData.get("BudgetPanel.sumLine.checkBox")); //$NON-NLS-1$
			chckbxAddSumLine.setSelected(true);
			chckbxAddSumLine.setToolTipText(LocalizationData.get("BudgetPanel.sumLine.checkBox.tooltip")); //$NON-NLS-1$
			chckbxAddSumLine.setVisible(false);
		}
		return chckbxAddSumLine;
	}
}
