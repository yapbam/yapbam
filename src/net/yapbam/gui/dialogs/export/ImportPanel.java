package net.yapbam.gui.dialogs.export;

import java.awt.GridBagLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JButton;

import net.yapbam.gui.LocalizationData;
import javax.swing.JLabel;

public class ImportPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTable = null;
	private JCheckBox ignoreFirstLine = null;
	private JButton first = null;
	private JButton previous = null;
	private JButton next = null;
	private JButton last = null;
	private JCheckBox addToCurrentFile = null;
	private JComboBox accounts = null;
	private JPanel jPanel1 = null;
	private JLabel jLabel1 = null;

	/**
	 * This is the default constructor
	 */
	public ImportPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
		gridBagConstraints91.anchor = GridBagConstraints.WEST;
		gridBagConstraints91.gridy = 4;
		gridBagConstraints91.gridwidth = 0;
		gridBagConstraints91.gridx = 0;
		GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
		gridBagConstraints81.gridx = 0;
		gridBagConstraints81.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints81.gridwidth = 0;
		gridBagConstraints81.gridy = 5;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 4;
		gridBagConstraints5.fill = GridBagConstraints.NONE;
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.gridy = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 3;
		gridBagConstraints4.gridy = 1;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.gridy = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridwidth = 0;
		gridBagConstraints1.gridy = 2;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridwidth = 0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getJScrollPane(), gridBagConstraints);
		this.add(getIgnoreFirstLine(), gridBagConstraints1);
		this.add(getFirst(), gridBagConstraints2);
		this.add(getPrevious(), gridBagConstraints3);
		this.add(getNext(), gridBagConstraints4);
		this.add(getLast(), gridBagConstraints5);
		this.add(getAddToCurrentFile(), gridBagConstraints91);
		this.add(getJPanel1(), gridBagConstraints81);
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
	        jTable = new JTable(new ImportTableModel()) {
	            //Implement table header tool tips.
	            protected JTableHeader createDefaultTableHeader() {
	            	System.out.println ("kjklj");
	                return new JTableHeader(columnModel) {
	                    public String getToolTipText(MouseEvent e) {
	                        String tip = null;
	                        java.awt.Point p = e.getPoint();
	                        int index = columnModel.getColumnIndexAtX(p.x);
	                        int realIndex = columnModel.getColumn(index).getModelIndex();
	                        if (realIndex==1) {
	                        	tip = LocalizationData.get("ImportDialog.linkedTo.toolTip"); //$NON-NLS-1$
	                        } else if (realIndex==2) {
	                        	tip = LocalizationData.get("ImportDialog.importedFields.toolTip"); //$NON-NLS-1$
	                        }
	                        return tip;
	                    }
	                };
	            }
	        };

			//TODO Display file lines instead of these funny titles ;-)
	        JComboBox comboBox = new JComboBox();
	        comboBox.addItem("Bed"); //$NON-NLS-1$
	        comboBox.addItem("Snowboard"); //$NON-NLS-1$
	        comboBox.addItem("Ski"); //$NON-NLS-1$
	        comboBox.addItem("Bar"); //$NON-NLS-1$
	        comboBox.addItem("NightClub"); //$NON-NLS-1$
	        comboBox.addItem("None of the above"); //$NON-NLS-1$
	        
	        TableColumn importedColumns = jTable.getColumnModel().getColumn(2);
			importedColumns.setCellEditor(new DefaultCellEditor(comboBox));

	        jTable.setFillsViewportHeight(true);
		}
		return jTable;
	}

	/**
	 * This method initializes ignoreFirstLine	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getIgnoreFirstLine() {
		if (ignoreFirstLine == null) {
			ignoreFirstLine = new JCheckBox();
			ignoreFirstLine.setText(LocalizationData.get("ImportDialog.ignoreFirstLine")); //$NON-NLS-1$
			ignoreFirstLine.setToolTipText(LocalizationData.get("ImportDialog.ignoreFirstLine.toolTip")); //$NON-NLS-1$
		}
		return ignoreFirstLine;
	}

	/**
	 * This method initializes first	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFirst() {
		if (first == null) {
			first = new JButton();
			first.setToolTipText(LocalizationData.get("ImportDialog.first.toolTip")); //$NON-NLS-1$
			first.setText("<<"); //$NON-NLS-1$
		}
		return first;
	}

	/**
	 * This method initializes previous	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPrevious() {
		if (previous == null) {
			previous = new JButton();
			previous.setToolTipText(LocalizationData.get("ImportDialog.previous.toolTip")); //$NON-NLS-1$
			previous.setText("<"); //$NON-NLS-1$
		}
		return previous;
	}

	/**
	 * This method initializes next	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNext() {
		if (next == null) {
			next = new JButton();
			next.setToolTipText(LocalizationData.get("ImportDialog.next.toolTip")); //$NON-NLS-1$
			next.setText(">"); //$NON-NLS-1$
		}
		return next;
	}

	/**
	 * This method initializes last	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLast() {
		if (last == null) {
			last = new JButton();
			last.setToolTipText(LocalizationData.get("ImportDialog.last.toolTip")); //$NON-NLS-1$
			last.setText(">>"); //$NON-NLS-1$
		}
		return last;
	}

	/**
	 * This method initializes addToCurrentFile	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getAddToCurrentFile() {
		if (addToCurrentFile == null) {
			addToCurrentFile = new JCheckBox();
			addToCurrentFile.setText(LocalizationData.get("ImportDialog.addToCurrentFile")); //$NON-NLS-1$
			addToCurrentFile.setToolTipText(LocalizationData.get("ImportDialog.addToCurrentFile.toolTip")); //$NON-NLS-1$
		}
		return addToCurrentFile;
	}

	/**
	 * This method initializes accounts	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getAccounts() {
		if (accounts == null) {
			accounts = new JComboBox();
			accounts.setToolTipText(LocalizationData.get("ImportDialog.addToAccount.toolTip")); //$NON-NLS-1$
		}
		return accounts;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText(LocalizationData.get("ImportDialog.addToAccount")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.gridy = -1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 0;
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getAccounts(), gridBagConstraints9);
			jPanel1.add(jLabel1, gridBagConstraints6);
		}
		return jPanel1;
	}

}
