package net.yapbam.gui.dialogs.export;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.util.NullUtils;

public class ExportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$  //  @jve:decl-index=0:
	
	private JCheckBox title = null;
	private JRadioButton all = null;
	private JRadioButton filtered = null;
	private JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JLabel jLabel = null;
	private JCheckBox includeInitialBalance = null;
	private String invalidityCause = null;  //  @jve:decl-index=0:
	private SeparatorPanel separatorPanel = null;
	
	public String getInvalidityCause() {
		return invalidityCause;
	}

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
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints12.gridwidth = 0;
		gridBagConstraints12.gridy = 4;
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
		this.setSize(538, 444);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints21);
		this.add(getJScrollPane(), gridBagConstraints11);
		this.add(getTitle(), gridBagConstraints);
		this.add(getIncludeInitialBalance(), gridBagConstraints3);
		this.add(getAll(), gridBagConstraints1);
		this.add(getFiltered(), gridBagConstraints2);
		this.add(getSeparatorPanel(), gridBagConstraints12);
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
			ExportTableModel tableModel = new ExportTableModel();
			jTable = new JTable(tableModel);
			// Fit the column width to the size of the column name
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableCellRenderer renderer = jTable.getTableHeader().getDefaultRenderer();
			TableColumnModel columnModel = jTable.getColumnModel();
			for (int i = 0; i < columnModel.getColumnCount(); i++) {
				TableColumn column = columnModel.getColumn(i);
				int w = renderer.getTableCellRendererComponent(null, column.getHeaderValue(),
						false, false, 0, 0).getPreferredSize().width;
				column.setPreferredWidth(w);
			}
			jTable.getTableHeader().setResizingAllowed(false); // Disallow resizing of columns
			jTable.setCellSelectionEnabled(false); // Prevents the user to select cells (would have a strange look)
			tableModel.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					updateIsValid();
				}
			});
		}
		jTable.setFocusable(false);
		return jTable;
	}

	private void updateIsValid() {
		String old = invalidityCause;
		boolean oneSelected = false;
		for (int i = 0; i < jTable.getColumnCount(); i++) {
			if ((Boolean) jTable.getModel().getValueAt(0, i)) {
				oneSelected = true;
				break;
			}
		}
		invalidityCause = oneSelected?null:LocalizationData.get("ExportDialog.nothingToExport"); //$NON-NLS-1$
		if (!NullUtils.areEquals(invalidityCause, old)) {
			this.firePropertyChange(INVALIDITY_CAUSE, old, invalidityCause);
		}
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

	/**
	 * This method initializes separatorPanel	
	 * 	
	 * @return net.yapbam.gui.dialogs.export.SeparatorPanel	
	 */
	private SeparatorPanel getSeparatorPanel() {
		if (separatorPanel == null) {
			separatorPanel = new SeparatorPanel();
		}
		return separatorPanel;
	}

	public Exporter getExporter() {
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		ExportTableModel model = ((ExportTableModel)getJTable().getModel());
		for (int i = 0; i < getJTable().getColumnCount(); i++) {
			int modelColumn = getJTable().convertColumnIndexToModel(i);
			if ((Boolean) model.getValueAt(0, modelColumn)) resultList.add(modelColumn);
		}
		int[] fields = new int[resultList.size()];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = resultList.get(i);
		}
		return new Exporter(fields, title.isSelected(), separatorPanel.getSeparator(), includeInitialBalance.isSelected(), !all.isSelected());
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
