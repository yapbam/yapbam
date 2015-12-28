package net.yapbam.gui.archive;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.table.JTable;
import com.fathzer.soft.ajlib.swing.table.NimbusPatchBooleanTableCellRenderer;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.GlobalData;
import net.yapbam.data.Statement;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.JTableUtils;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

@SuppressWarnings("serial")
public class StatementSelectionPanel extends JPanel {
	static final String INVALIDITY_CAUSE = "invalidityCause"; //$NON-NLS-1$
	static final String ARCHIVE_MODE = "archiveMode"; //$NON-NLS-1$

	private JLabel lblWhatAccountsDo;
	private JTable table;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JButton allButton;
	private JButton noneButton;
	
	private GlobalData data;
	private GlobalData archiveData;
	private CharSequence[] alerts;
	private String invalidityCause;
	private JPanel top;
	private JPanel rdbtns;
	private JRadioButton toArchive;
	private JRadioButton fromArchive;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	public StatementSelectionPanel(GlobalData data, GlobalData archiveData, CharSequence[] alerts) {
		this.data = data;
		this.archiveData = archiveData;
		this.alerts = alerts.clone();
		initialize();
		onModeChanged();
	}
	
	private void onModeChanged() {
		boolean isArchiveMode = isArchiveMode();
		((StatementSelectionTableModel)getTable().getModel()).setData(getSource(), !isArchiveMode);
		String message = LocalizationData.get(isArchiveMode?"Archive.statementSelection.helpMessage": //$NON-NLS-1$
				"Archive.statementSelection.restoreHelpMessage"); //$NON-NLS-1$
		StatementSelectionTableModel model = (StatementSelectionTableModel) getTable().getModel();
		lblWhatAccountsDo.setText(Formatter.format(message, getTable().getColumnName(model.getStatementColumn())));
		firePropertyChange(ARCHIVE_MODE, !isArchiveMode, isArchiveMode);
	}

	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getScrollPane(), BorderLayout.CENTER);
		add(getPanel(), BorderLayout.SOUTH);
		add(getTop(), BorderLayout.NORTH);
		updateIsValid();
	}

	private JLabel getLblWhatAccountsDo() {
		if (lblWhatAccountsDo == null) {
			lblWhatAccountsDo = new JLabel();
		}
		return lblWhatAccountsDo;
	}
	
	JTable getTable() {
		if (table == null) {
			final StatementSelectionTableModel model = new StatementSelectionTableModel(data, alerts);
			table = new JTable();
			table.setDefaultRenderer(Icon.class, new AlertCellRenderer());
			// Patch Nimbus bug (see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524)
			table.setDefaultRenderer(Boolean.class, new NimbusPatchBooleanTableCellRenderer());
			table.setModel(model);
			// Disallow columns reordering
			table.getTableHeader().setReorderingAllowed(false);
			JTableUtils.initColumnSizes(table, Integer.MAX_VALUE);
			table.setPreferredScrollableViewportSize(table.getPreferredSize());

			JComboBox fieldsCombo = new JComboBox();
			table.setDefaultEditor(Statement.class, new DefaultCellEditor(fieldsCombo) {
				@Override
				public Component getTableCellEditorComponent(javax.swing.JTable table,
						Object value, boolean isSelected, int row, int column) {
					JComboBox combo = (JComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
					combo.removeAllItems();
					Statement[] statements = ((StatementSelectionTableModel)table.getModel()).getStatements(row);
					for (Statement statement : statements) {
						if (statement.getId()!=null) {
							combo.addItem(statement);
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
			// Seems better without these all and none buttons
			panel.setVisible(false); 
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
	
	GlobalData getSource() {
		return isArchiveMode()?data:archiveData;
	}
	
	private void updateIsValid() {
		String old = invalidityCause;
		invalidityCause = LocalizationData.get("Archive.statementSelection.noTransactionSelected"); //$NON-NLS-1$
		for (int i = 0; i < getSource().getAccountsNumber(); i++) {
			if (!((StatementSelectionTableModel)getTable().getModel()).getSelectedStatements(i).isEmpty()) {
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
	private JPanel getTop() {
		if (top == null) {
			top = new JPanel();
			top.setLayout(new BorderLayout(0, 0));
			top.add(getLblWhatAccountsDo());
			top.add(getRdbtns(), BorderLayout.NORTH);
		}
		return top;
	}
	private JPanel getRdbtns() {
		if (rdbtns == null) {
			rdbtns = new JPanel();
			rdbtns.setLayout(new BorderLayout(0, 0));
			rdbtns.add(getToArchive(), BorderLayout.NORTH);
			rdbtns.add(getFromArchive(), BorderLayout.SOUTH);
		}
		return rdbtns;
	}
	private JRadioButton getToArchive() {
		if (toArchive == null) {
			toArchive = new JRadioButton(LocalizationData.get("Archive.menu.name")); //$NON-NLS-1$
			buttonGroup.add(toArchive);
			toArchive.setToolTipText(LocalizationData.get("Archive.archive.tooltip")); //$NON-NLS-1$
			toArchive.setSelected(true);
			toArchive.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					onModeChanged();
				}
			});
		}
		return toArchive;
	}
	private JRadioButton getFromArchive() {
		if (fromArchive == null) {
			fromArchive = new JRadioButton(LocalizationData.get("Archive.restore.title")); //$NON-NLS-1$
			buttonGroup.add(fromArchive);
			fromArchive.setToolTipText(LocalizationData.get("Archive.restore.tooltip")); //$NON-NLS-1$
		}
		return fromArchive;
	}
	
	public boolean isArchiveMode() {
		return toArchive.isSelected();
	}
}
