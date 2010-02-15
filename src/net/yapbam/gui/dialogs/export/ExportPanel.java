package net.yapbam.gui.dialogs.export;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.yapbam.gui.LocalizationData;

public class ExportPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox title = null;
	private JRadioButton all = null;
	private JRadioButton filtered = null;
	private JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JLabel jLabel = null;
	private JCheckBox includeInitialBalance = null;
	
	/**
	 * This is the default constructor
	 */
	public ExportPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.gridy = 3;
		gridBagConstraints3.gridx = 0;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.gridwidth = 0;
		gridBagConstraints21.gridy = 0;
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.gridwidth = 0;
		gridBagConstraints21.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText(LocalizationData.get("ExportDialog.message")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 1;
		gridBagConstraints11.gridwidth = 0;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.fill = GridBagConstraints.NONE;
		gridBagConstraints11.weighty = 0.0;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 1;
		gridBagConstraints11.gridwidth = 0;
		gridBagConstraints11.weightx = 1.0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 3;
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridy = 3;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 2;
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 2;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 2;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints21);
		this.add(getJScrollPane(), gridBagConstraints11);
		this.add(getTitle(), gridBagConstraints);
		this.add(getIncludeInitialBalance(), gridBagConstraints3);
		this.add(getAll(), gridBagConstraints1);
		this.add(getFiltered(), gridBagConstraints2);
		ButtonGroup group = new ButtonGroup();
		group.add(getAll());
		group.add(getFiltered());
	}

	/**
	 * This method initializes title	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getTitle() {
		if (title == null) {
			title = new JCheckBox();
			title.setText(LocalizationData.get("ExportDialog.headerCheckbox")); //$NON-NLS-1$
			title.setSelected(true);
			title.setToolTipText(LocalizationData.get("ExportDialog.headerCheckbox.toolTip")); //$NON-NLS-1$
		}
		return title;
	}

	/**
	 * This method initializes all	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getAll() {
		if (all == null) {
			all = new JRadioButton();
			all.setSelected(true);
			all.setText(LocalizationData.get("ExportDialog.allRadioButton")); //$NON-NLS-1$
			all.setToolTipText(LocalizationData.get("ExportDialog.allRadioButton.toolTip")); //$NON-NLS-1$
		}
		return all;
	}

	/**
	 * This method initializes filtered	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	JRadioButton getFiltered() {
		if (filtered == null) {
			filtered = new JRadioButton();
			filtered.setText(LocalizationData.get("ExportDialog.filteredRadioButton")); //$NON-NLS-1$
			filtered.setToolTipText(LocalizationData.get("ExportDialog.filteredRadioButton.toolTip")); //$NON-NLS-1$
		}
		return filtered;
	}

	/**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable() {
		if (jTable == null) {
			jTable = new JTable(new ExportTableModel());
			// Fit the column width to the size of the column name
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableCellRenderer renderer = jTable.getDefaultRenderer(String.class);
			TableColumnModel columnModel = jTable.getColumnModel();
			int margin = 10;
			for (int i = 0; i < columnModel.getColumnCount(); i++) {
				TableColumn column = columnModel.getColumn(i);
				String columnName = jTable.getModel().getColumnName(i);
				Component component = renderer.getTableCellRendererComponent(jTable, columnName, false, false, -1, i);
				column.setPreferredWidth(component.getPreferredSize().width+margin);
			}
			jTable.getTableHeader().setResizingAllowed(false); // Disallow resizing of columns
			jTable.setCellSelectionEnabled(false); // Prevents the user to select cells (would have a strange look) 
		}
		return jTable;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTable());
			jTable.setPreferredScrollableViewportSize(getJTable().getPreferredSize());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes includeInitialBalance	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIncludeInitialBalance() {
		if (includeInitialBalance == null) {
			includeInitialBalance = new JCheckBox();
			includeInitialBalance.setText(LocalizationData.get("ExportDialog.includeInitialBalanceCheckBox")); //$NON-NLS-1$
			includeInitialBalance.setSelected(true);
			includeInitialBalance.setToolTipText(LocalizationData.get("ExportDialog.includeInitialBalanceCheckBox.toolTip")); //$NON-NLS-1$
		}
		return includeInitialBalance;
	}

}
