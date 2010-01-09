package net.yapbam.budget;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.AbstractDialog;

public class BudgetViewPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JRadioButton month = null;
	private JRadioButton year = null;
	private JButton export = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	
	private Budget budget;
	
	/**
	 * This is the default constructor
	 */
	public BudgetViewPanel() {
		this(new FilteredData(new GlobalData()));
	}

	public BudgetViewPanel(FilteredData data) {
		this.budget = new Budget(data, false);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
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
		this.setSize(300, 200);
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
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridheight = 0;
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getMonth(), gridBagConstraints1);
			jPanel.add(getYear(), gridBagConstraints2);
			jPanel.add(getExport(), gridBagConstraints3);
			ButtonGroup group = new ButtonGroup();
			group.add(getMonth());
			group.add(getYear());
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
					JFileChooser chooser = new JFileChooser((String)null);
					File result = chooser.showDialog(export, LocalizationData.get("BudgetPanel.export"))==JFileChooser.APPROVE_OPTION?chooser.getSelectedFile():null;
					if (result!=null) {
						//TODO
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
			JTable rowView = new JTable(budget.getRowHeaderModel());
			jScrollPane.setRowHeaderView(rowView);
			jScrollPane.setViewportView(getJTable());
			Dimension d = rowView.getPreferredScrollableViewportSize();
			d.width = rowView.getColumnModel().getColumn(0).getPreferredWidth();
			rowView.setPreferredScrollableViewportSize(d);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable(budget.getTableModel());
		}
		return jTable;
	}

}
