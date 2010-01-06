package net.yapbam.gui.statistics;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;

public class BudgetViewPanel extends JPanel { //LOCAL

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JRadioButton month = null;
	private JRadioButton year = null;
	private JButton export = null;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	
	private FilteredData data;
	private Budget budget;
	
	/**
	 * This is the default constructor
	 */
	public BudgetViewPanel() {
		this(new FilteredData(new GlobalData()));
	}

	public BudgetViewPanel(FilteredData data) {
		this.data = data;
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
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getMonth(), gridBagConstraints1);
			jPanel.add(getYear(), gridBagConstraints2);
			jPanel.add(getExport(), gridBagConstraints3);
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
			month.setText("Vue par mois");
			month.setToolTipText("Cliquez ici pour obtenir la ventilation par mois");
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
			year.setToolTipText("Cliquez ici pour obtenir la ventilation par année");
			year.setText("Vue par an");
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
			export.setText("Exporter");
			export.setToolTipText("Exporte le tableau au format texte");
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
			d.width = rowView.getPreferredSize().width;
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
