package net.yapbam.gui.dialogs.export;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.export.ExportFormatType;
import net.yapbam.gui.LocalizationData;

public class ExportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$

	private JCheckBox title;
	private JRadioButton all;
	private JRadioButton filtered;
	private JTable jTable;
	private JScrollPane jScrollPane;
	private JCheckBox includeInitialBalance;
	private String invalidityCause;
	private SeparatorPanel separatorPanel;
	private ButtonGroup group;
	private JComboBox<ExportFormatType> exportFormats;

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
	 */
	private void initialize() {

		JLabel tableMessageLabel = new JLabel();
		tableMessageLabel.setText(LocalizationData.get("ExportDialog.message")); //$NON-NLS-1$

		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(tableMessageLabel, BorderLayout.NORTH);
		tablePanel.add(getJScrollPane(), BorderLayout.CENTER);

		JPanel preferencePanelLeft = new JPanel();
		preferencePanelLeft.setBorder(new EmptyBorder(5, 5, 5, 5));
		preferencePanelLeft.setLayout(new BoxLayout(preferencePanelLeft, BoxLayout.Y_AXIS));
		preferencePanelLeft.add(getTitle());
		preferencePanelLeft.add(getIncludeInitialBalance());

		JPanel preferencePanelRight = new JPanel();
		preferencePanelRight.setBorder(new EmptyBorder(5, 5, 5, 5));
		preferencePanelRight.setLayout(new BoxLayout(preferencePanelRight, BoxLayout.Y_AXIS));
		preferencePanelRight.add(getAll());
		preferencePanelRight.add(getFiltered());

		JLabel exportFormatLabel = new JLabel();
		exportFormatLabel.setText(LocalizationData.get("ExportDialog.formatMessage"));

		JPanel formatPanel = new JPanel();
		formatPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		formatPanel.setLayout(new BorderLayout());
		formatPanel.add(exportFormatLabel, BorderLayout.NORTH);
		formatPanel.add(getExportFormats(), BorderLayout.CENTER);

		JPanel preferencePanel = new JPanel();
		preferencePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		preferencePanel.setLayout(new BorderLayout());
		preferencePanel.add(preferencePanelLeft, BorderLayout.WEST);
		preferencePanel.add(preferencePanelRight, BorderLayout.CENTER);
		preferencePanel.add(formatPanel, BorderLayout.SOUTH);
		

		JPanel preferenceSeparatorPanel = new JPanel();
		preferenceSeparatorPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		preferenceSeparatorPanel.setLayout(new BorderLayout());
		preferenceSeparatorPanel.add(getSeparatorPanel(), BorderLayout.CENTER);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(tablePanel);
		this.add(preferencePanel);
		this.add(preferenceSeparatorPanel);

		group = new ButtonGroup();
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
				int w = renderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0)
						.getPreferredSize().width;
				column.setPreferredWidth(w);
			}
			// Disallow resizing of columns
			jTable.getTableHeader().setResizingAllowed(false);
			// Prevent the user to select cells (would have a strange look)
			jTable.setCellSelectionEnabled(false);
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
		invalidityCause = oneSelected ? null : LocalizationData.get("ExportDialog.nothingToExport"); //$NON-NLS-1$
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
			includeInitialBalance
					.setToolTipText(LocalizationData.get("ExportDialog.includeInitialBalanceCheckBox.toolTip")); //$NON-NLS-1$
		}
		return includeInitialBalance;
	}

	private JComboBox<ExportFormatType> getExportFormats() {
		if (exportFormats == null) {
			// Warning: We could use the JComboBox(ExportFormatType[]) but it leads to Eclipse Window Builder not being able to parse the code
			// So let's create an empty Combo and add its elements
			exportFormats = new JComboBox<ExportFormatType>();
			for (ExportFormatType value : ExportFormatType.values()) {
				exportFormats.addItem(value);
			}
			exportFormats.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if (ExportPanel.this.separatorPanel != null) {
								Component[] components = ExportPanel.this.separatorPanel.getComponents();
								for (Component component : components) {
									component.setEnabled(ExportFormatType.CSV.equals(ExportPanel //
											.this.exportFormats.getSelectedItem()));
								}
								ExportPanel.this.separatorPanel.updateUI();
							}
						}
					});
				}
			});
			exportFormats.setToolTipText(LocalizationData.get("ExportDialog.formatMessage.tooltip"));
			exportFormats.setSelectedItem(ExportFormatType.CSV);
		}
		return exportFormats;
	}

	/**
	 * This method initializes separatorPanel
	 * 
	 * @return net.yapbam.gui.dialogs.export.SeparatorPanel
	 */
	private SeparatorPanel getSeparatorPanel() {
		if (separatorPanel == null) {
			separatorPanel = SeparatorPanel.createColumnSeparatorPanel();
		}
		return separatorPanel;
	}

	public DataExporterParameters getExporterParameters() {
		ExportTableModel model = (ExportTableModel) getJTable().getModel();
		int[] viewToModel = new int[getJTable().getColumnCount()];
		boolean[] selected = new boolean[viewToModel.length];
		for (int i = 0; i < viewToModel.length; i++) {
			int modelColumn = getJTable().convertColumnIndexToModel(i);
			viewToModel[i] = modelColumn;
			selected[modelColumn] = (Boolean) model.getValueAt(0, modelColumn);
		}
		return new DataExporterParameters(viewToModel, selected, title.isSelected(), separatorPanel.getSeparator(),
				getIncludeInitialBalance().isSelected(), !all.isSelected(),
				ExportFormatType.valueOf(exportFormats.getSelectedItem() + ""));
	}

	public boolean setParameters(DataExporterParameters parameters) {
		title.setSelected(parameters.isInsertHeader());
		separatorPanel.setSeparator(parameters.getSeparator());
		exportFormats.setSelectedItem(parameters.getExportFormat() == null //
				? ExportFormatType.CSV
				: parameters.getExportFormat() //
		);
		getIncludeInitialBalance().setSelected(parameters.isExportInitialBalance());
		JRadioButton sel = parameters.isExportFilteredData() ? filtered : all;
		group.setSelected(sel.getModel(), true);
		boolean ok = jTable.getColumnCount() == parameters.getViewIndexesToModel().length;
		if (ok) {
			for (int i = jTable.getColumnCount() - 1; i >= 0; i--) {
				int modelIndex = parameters.getViewIndexesToModel()[i];
				jTable.moveColumn(jTable.convertColumnIndexToView(modelIndex), i);
			}
			ExportTableModel model = (ExportTableModel) jTable.getModel();
			for (int i = 0; i < parameters.getSelectedModelColumns().length; i++) {
				model.setValueAt(parameters.getSelectedModelColumns()[i], 0, i);
			}
		}
		return ok;
	}
}
