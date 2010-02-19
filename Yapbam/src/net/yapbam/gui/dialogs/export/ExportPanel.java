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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.util.NullUtils;

import javax.swing.JFileChooser;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;

public class ExportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$
	
	private JCheckBox title = null;
	private JRadioButton all = null;
	private JRadioButton filtered = null;
	private JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JLabel jLabel = null;
	private JCheckBox includeInitialBalance = null;
	private JFileChooser jFileChooser = null;
	
	private String invalidityCause = null;  //  @jve:decl-index=0:
	private JRadioButton tabSeparated = null;
	private JPanel jPanel = null;
	private JRadioButton custom = null;
	private JTextField customSeparator = null;
	
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
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.gridx = 0;
		gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints22.gridwidth = 0;
		gridBagConstraints22.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints22.gridy = 4;
		GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		gridBagConstraints31.gridx = 0;
		gridBagConstraints31.gridwidth = 0;
		gridBagConstraints31.fill = GridBagConstraints.BOTH;
		gridBagConstraints31.gridy = 5;
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
		this.add(getJFileChooser(), gridBagConstraints31);
		this.add(getJPanel(), gridBagConstraints22);
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
			tableModel.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					updateIsValid();
				}
			});
		}
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
	 * This method initializes jFileChooser	
	 * 	
	 * @return javax.swing.JFileChooser	
	 */
	private JFileChooser getJFileChooser() {
		if (jFileChooser == null) {
			jFileChooser = new JFileChooser();
			jFileChooser.setLocale(new Locale(LocalizationData.getLocale().getLanguage()));
			jFileChooser.updateUI();
//			jFileChooser.setControlButtonsAreShown(false);
			jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
			jFileChooser.setVisible(false);
			jFileChooser.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println (evt); //TODO
				}
			});
		}
		return jFileChooser;
	}

	/**
	 * This method initializes tabSeparated	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getTabSeparated() {
		if (tabSeparated == null) {
			tabSeparated = new JRadioButton();
			tabSeparated.setText(LocalizationData.get("ExportDialog.columnSeparator.defaultSeparator")); //$NON-NLS-1$
			tabSeparated.setToolTipText(LocalizationData.get("ExportDialog.columnSeparator.defaultSeparator.toolTip")); //$NON-NLS-1$
		}
		return tabSeparated;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.gridwidth = 0;
			gridBagConstraints4.gridy = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("ExportDialog.columnSeparator"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
			jPanel.add(getTabSeparated(), gridBagConstraints4);
			jPanel.add(getCustom(), gridBagConstraints5);
			jPanel.add(getCustomSeparator(), gridBagConstraints6);
		}
		return jPanel;
	}

	/**
	 * This method initializes custom	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCustom() {
		if (custom == null) {
			custom = new JRadioButton();
			custom.setText(LocalizationData.get("ExportDialog.columnSeparator.customized")); //$NON-NLS-1$
			custom.setToolTipText(LocalizationData.get("ExportDialog.columnSeparator.customized.toolTip")); //$NON-NLS-1$
		}
		return custom;
	}

	/**
	 * This method initializes customSeparator	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCustomSeparator() {
		if (customSeparator == null) {
			customSeparator = new JTextField();
			customSeparator.setColumns(1);
			customSeparator.setToolTipText(LocalizationData.get("ExportDialog.columnSeparator.customizedChar.toolTip")); //$NON-NLS-1$
		}
		return customSeparator;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
