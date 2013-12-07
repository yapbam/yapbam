package net.yapbam.gui.archive;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import com.fathzer.soft.ajlib.swing.table.JTable;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.JTableUtils;
import net.yapbam.gui.util.NimbusPatchBooleanTableCellRenderer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.MessageFormat;

@SuppressWarnings("serial")
public class StatementSelectionPanel extends JPanel {
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$

	private JLabel lblWhatAccountsDo;
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JButton allButton;
	private JButton noneButton;
	
	private GlobalData data;
	private String invalidityCause;

	/**
	 * Create the panel.
	 */
	public StatementSelectionPanel() {
		this(null);
	}

	public StatementSelectionPanel(GlobalData data) {
		this.data = data;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getLblWhatAccountsDo(), BorderLayout.NORTH);
		add(getScrollPane(), BorderLayout.CENTER);
		add(getPanel(), BorderLayout.SOUTH);
		updateIsValid();
	}

	private JLabel getLblWhatAccountsDo() {
		if (lblWhatAccountsDo == null) {
			String message = LocalizationData.get("Archive.statementSelection.helpMessage"); //$NON-NLS-1$
			lblWhatAccountsDo = new JLabel(MessageFormat.format(message, getTable().getColumnName(StatementSelectionTableModel.STATEMENT_COLUMN)));
		}
		return lblWhatAccountsDo;
	}
	JTable getTable() {
		if (table == null) {
			table = new JTable();
			// Patch Nimbus bug (see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524)
			table.setDefaultRenderer(Boolean.class, new NimbusPatchBooleanTableCellRenderer());
			StatementSelectionTableModel model = new StatementSelectionTableModel(data);
			table.setModel(model);

			table.getTableHeader().setReorderingAllowed(false); // Disallow columns reordering
			JTableUtils.initColumnSizes(table, Integer.MAX_VALUE);
			table.setPreferredScrollableViewportSize(table.getPreferredSize());

			JComboBox fieldsCombo = new JComboBox();
			TableColumn importedColumns = table.getColumnModel().getColumn(StatementSelectionTableModel.STATEMENT_COLUMN);
			importedColumns.setCellEditor(new DefaultCellEditor(fieldsCombo) {
				@Override
				public Component getTableCellEditorComponent(javax.swing.JTable table,
						Object value, boolean isSelected, int row, int column) {
					JComboBox combo = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
					combo.removeAllItems();
					Statement[] statements = ((StatementSelectionTableModel)table.getModel()).getStatements(row);
					for (Statement statement : statements) {
						if (statement.getId()!=null) {
							combo.addItem(statement.getId());
						}
					}
					Object current = table.getModel().getValueAt(row, column);
					combo.setSelectedItem(current);
					return combo;
				}
				
			});

			table.setFillsViewportHeight(true);
			model.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
					updateIsValid();
				}
			});

		}
		return table;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			GridBagLayout gblPanel = new GridBagLayout();
			panel.setLayout(gblPanel);
			GridBagConstraints gbcAllButton = new GridBagConstraints();
			gbcAllButton.weightx = 1.0;
			gbcAllButton.anchor = GridBagConstraints.EAST;
			gbcAllButton.insets = new Insets(0, 0, 0, 5);
			gbcAllButton.gridx = 1;
			gbcAllButton.gridy = 0;
			panel.add(getAllButton(), gbcAllButton);
			GridBagConstraints gbcNoneButton = new GridBagConstraints();
			gbcNoneButton.anchor = GridBagConstraints.EAST;
			gbcNoneButton.gridx = 2;
			gbcNoneButton.gridy = 0;
			panel.add(getNoneButton(), gbcNoneButton);
			panel.setVisible(false); // Seems better without these buttons
		}
		return panel;
	}
	private JButton getAllButton() {
		if (allButton == null) {
			allButton = new JButton(LocalizationData.get("Generic.selectAll")); //$NON-NLS-1$
			allButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((StatementSelectionTableModel)getTable().getModel()).setAllExported(true);
				}
			});
		}
		return allButton;
	}
	private JButton getNoneButton() {
		if (noneButton == null) {
			noneButton = new JButton(LocalizationData.get("Generic.unselectAll")); //$NON-NLS-1$
			noneButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((StatementSelectionTableModel)getTable().getModel()).setAllExported(false);
				}
			});
		}
		return noneButton;
	}
	
	private void updateIsValid() {
		String old = invalidityCause;
		invalidityCause = LocalizationData.get("Archive.statementSelection.noTransactionSelected"); //$NON-NLS-1$
		for (int i = 0; i < data.getAccountsNumber(); i++) {
			if (((StatementSelectionTableModel)getTable().getModel()).getSelectedStatements(i) != null) {
				invalidityCause = null;
				break;
			}
		}
		if (!NullUtils.areEquals(invalidityCause, old)) {
			this.firePropertyChange(INVALIDITY_CAUSE, old, invalidityCause);
		}
	}

	String getInvalidityCause() {
		return invalidityCause;
	}
}
