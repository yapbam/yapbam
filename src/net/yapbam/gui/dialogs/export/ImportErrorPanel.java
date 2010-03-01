package net.yapbam.gui.dialogs.export;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import net.yapbam.gui.LocalizationData;

public class ImportErrorPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JLabel jLabel = null;
	private ImportError[] errors;

	/**
	 * This is the default constructor
	 * just for Eclipse Visual Editor
	 */
	public ImportErrorPanel() {
		this(new int[0], new ImportError[0]);
	}
	
	public ImportErrorPanel(int[] importedFileColumns, ImportError[] errors) {
		super();
		this.errors = errors;
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getJScrollPane(), gridBagConstraints);
		this.add(getJLabel(), gridBagConstraints1);
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
			jTable = new JTable(new AbstractTableModel() {
				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
					if (columnIndex==0) {
						return errors[rowIndex].getLineNumber();
					} else {
						String[] fields = errors[rowIndex].getFields();
						if (columnIndex+1>=fields.length) return "";
//						return fields[columnIndex+1];
						return "TODO"; //TODO
					}
				}
				
				@Override
				public int getRowCount() {
					return errors.length;
				}
				
				@Override
				public int getColumnCount() {
					return ExportTableModel.columns.length+1;
				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					if (columnIndex==0) return Integer.class;
					return super.getColumnClass(columnIndex);
				}

				@Override
				public String getColumnName(int column) {
					if (column==0) return LocalizationData.get("ImportDialog.errorMessagelienNumberColumnHeader"); //$NON-NLS-1$
					else return ExportTableModel.columns[column-1];
				}
				
			});
		}
		return jTable;
	}

	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setText(LocalizationData.get("ImportDialog.errorMessagemessage")); //$NON-NLS-1$
		}
		return jLabel;
	}

}
