package net.yapbam.gui.archive;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.gui.util.JTableUtils;
import net.yapbam.gui.util.NimbusPatchBooleanTableCellRenderer;

@SuppressWarnings("serial")
public class FilterPanel extends JPanel {
	private JLabel lblWhatAccountsDo;
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel accountPanel;
	private JPanel panel;
	private JButton allButton;
	private JButton noneButton;
	
	private GlobalData data;

	/**
	 * Create the panel.
	 */
	public FilterPanel() {
		this(null);
	}

	public FilterPanel(GlobalData data) {
		this.data = data;
		initialize();
	}

	private void initialize() {
		add(getAccountPanel());
	}

	private JLabel getLblWhatAccountsDo() {
		if (lblWhatAccountsDo == null) {
			lblWhatAccountsDo = new JLabel("What accounts do you want to migrate ?");
		}
		return lblWhatAccountsDo;
	}
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			// Patch Nimbus bug (see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524)
			table.setDefaultRenderer(Boolean.class, new NimbusPatchBooleanTableCellRenderer());
			AccountTableModel model = new AccountTableModel(data);
			table.setModel(model);

			table.getTableHeader().setReorderingAllowed(false); // Disallow columns reordering
			JTableUtils.initColumnSizes(table, Integer.MAX_VALUE);
			table.setPreferredScrollableViewportSize(table.getPreferredSize());

			JComboBox<String> fieldsCombo = new JComboBox<String>();
			TableColumn importedColumns = table.getColumnModel().getColumn(AccountTableModel.STATEMENT_COLUMN);
			importedColumns.setCellEditor(new DefaultCellEditor(fieldsCombo) {

				@Override
				public Component getTableCellEditorComponent(JTable table,
						Object value, boolean isSelected, int row, int column) {
					// TODO Auto-generated method stub
					System.out.println (row);
					JComboBox<String> combo = (JComboBox<String>) super.getTableCellEditorComponent(table, value, isSelected, row, column);
					combo.removeAllItems();
					Statement[] statements = Statement.getStatements(data.getAccount(row));
					for (Statement statement : statements) {
						if (statement.getId()!=null) combo.addItem(statement.getId());
					}
					return combo;
				}
				
			});

			table.setFillsViewportHeight(true);
			model.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent e) {
//					updateAddToAccountPanel();
//					updateIsValid();
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
	private JPanel getAccountPanel() {
		if (accountPanel == null) {
			accountPanel = new JPanel();
			accountPanel.setLayout(new BorderLayout(0, 0));
			accountPanel.add(getLblWhatAccountsDo(), BorderLayout.NORTH);
			accountPanel.add(getScrollPane());
			accountPanel.add(getPanel(), BorderLayout.SOUTH);
		}
		return accountPanel;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getAllButton());
			panel.add(getNoneButton());
		}
		return panel;
	}
	private JButton getAllButton() {
		if (allButton == null) {
			allButton = new JButton("Select all");
		}
		return allButton;
	}
	private JButton getNoneButton() {
		if (noneButton == null) {
			noneButton = new JButton("Deselect all");
		}
		return noneButton;
	}
}
