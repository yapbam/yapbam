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

import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.gui.util.JTableUtils;
import net.yapbam.gui.util.NimbusPatchBooleanTableCellRenderer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

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
			lblWhatAccountsDo = new JLabel("Please select the statements you want to archive?");
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

			JComboBox fieldsCombo = new JComboBox();
			TableColumn importedColumns = table.getColumnModel().getColumn(AccountTableModel.STATEMENT_COLUMN);
			importedColumns.setCellEditor(new DefaultCellEditor(fieldsCombo) {
				@Override
				public Component getTableCellEditorComponent(javax.swing.JTable table,
						Object value, boolean isSelected, int row, int column) {
					JComboBox combo = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
					combo.removeAllItems();
					Statement[] statements = Statement.getStatements(data.getAccount(row));
					for (Statement statement : statements) {
						if (statement.getId()!=null) combo.addItem(statement.getId());
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
					
				System.out.println ("Something changed"); //TODO
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
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{33, 99, 117, 0};
			gbl_panel.rowHeights = new int[]{25, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			GridBagConstraints gbc_allButton = new GridBagConstraints();
			gbc_allButton.weightx = 1.0;
			gbc_allButton.anchor = GridBagConstraints.EAST;
			gbc_allButton.insets = new Insets(0, 0, 0, 5);
			gbc_allButton.gridx = 1;
			gbc_allButton.gridy = 0;
			panel.add(getAllButton(), gbc_allButton);
			GridBagConstraints gbc_noneButton = new GridBagConstraints();
			gbc_noneButton.anchor = GridBagConstraints.EAST;
			gbc_noneButton.gridx = 2;
			gbc_noneButton.gridy = 0;
			panel.add(getNoneButton(), gbc_noneButton);
		}
		return panel;
	}
	private JButton getAllButton() {
		if (allButton == null) {
			allButton = new JButton("Select all");
			allButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((AccountTableModel)getTable().getModel()).setAllExported(true);
				}
			});
		}
		return allButton;
	}
	private JButton getNoneButton() {
		if (noneButton == null) {
			noneButton = new JButton("Deselect all");
			noneButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((AccountTableModel)getTable().getModel()).setAllExported(false);
				}
			});
		}
		return noneButton;
	}
}
