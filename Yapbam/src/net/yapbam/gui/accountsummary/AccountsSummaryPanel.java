package net.yapbam.gui.accountsummary;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.dialogs.EditAccountDialog;
import net.yapbam.gui.util.CellRenderer;
import net.yapbam.gui.util.JTableUtils;
import net.yapbam.gui.util.SplitPane;
import net.yapbam.util.NumberUtils;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.table.JTable;
import com.fathzer.soft.ajlib.swing.table.NimbusPatchBooleanTableCellRenderer;
import com.fathzer.soft.ajlib.swing.table.RowSorter;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountsSummaryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JScrollPane tablePanel = null;
	private JTable table = null;
	private AccountsSummaryTableModel model;
	private SplitPane splitPane;
	
	private GlobalData data;
	private JScrollPane notePane;
	private JTextArea notesField;
	private JPanel bottomPanel;
	private JLabel lblNotesLabel;
	private JPanel editPanel;
	private JButton editButton;
	
	/**
	 * This is the default constructor
	 */
	public AccountsSummaryPanel() {
		super();
		initialize();
	}

	/**
	 * This is the default constructor
	 */
	public AccountsSummaryPanel(GlobalData data) {
		super();
		this.data = data;
		initialize();
	}
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		add(getSplitPane(), BorderLayout.CENTER);
	}
	
	private SplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new SplitPane(JSplitPane.VERTICAL_SPLIT, true);
			splitPane.setTopComponent(getTablePanel());
			splitPane.setBottomComponent(getBottomPanel());
			splitPane.setDividerVisible(false);
		}
		return splitPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new JScrollPane(getTable());
		}
		return tablePanel;
	}

	/**
	 * This method initializes transactionsTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	JTable getTable() {
		if (table == null) {
			table = new TotalTable();
			this.model = new AccountsSummaryTableModel(table, data);
			table.setModel(this.model);
			table.setRowSorter(new RowSorter<AccountsSummaryTableModel>(model));
			// Patch Nimbus bug (see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524)
			table.setDefaultRenderer(Boolean.class, new NimbusPatchBooleanTableCellRenderer());
			table.setDefaultRenderer(Object.class, new MyRenderer());
			table.setDefaultRenderer(Double.class, new MyRenderer());
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						Account account = getSelectedAccount();
						if (account!=null) {
							if (!getSplitPane().isDividerVisible()) {
								getSplitPane().setDividerLocation(0.5);
							}
							getNotesField().setText(account.getComment()==null?"":account.getComment()); //$NON-NLS-1$
						}
						getSplitPane().setDividerVisible(account!=null);
						getBottomPanel().setVisible(account!=null);
					}
				}
			});
			JTableUtils.fixColumnSize(table, AccountsSummaryTableModel.SELECT_COLUMN, 10);
		}
		return table;
	}
	
	private Account getSelectedAccount() {
		int selected = table.getSelectedRow();
		boolean accountIsSelected = (selected>=0) && (selected<data.getAccountsNumber());
		return accountIsSelected ? data.getAccount(table.convertRowIndexToModel(selected)) : null;
	}
	
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.add(getNotePane());
			bottomPanel.add(getLblNotesLabel(), BorderLayout.NORTH);
			bottomPanel.add(getEditPanel(), BorderLayout.SOUTH);
			bottomPanel.setVisible(false);
		}
		return bottomPanel;
	}

	private JLabel getLblNotesLabel() {
		if (lblNotesLabel == null) {
			lblNotesLabel = new JLabel(LocalizationData.get("AccountDialog.notes")); //$NON-NLS-1$
		}
		return lblNotesLabel;
	}

	private JScrollPane getNotePane() {
		if (notePane == null) {
			notePane = new JScrollPane(getNotesField());
		}
		return notePane;
	}

	private JTextArea getNotesField() {
		if (notesField == null) {
			notesField = new JTextArea();
			notesField.setEditable(false);
			notesField.setLineWrap(true);
			notesField.setWrapStyleWord(true);
		}
		return notesField;
	}

	@SuppressWarnings("serial")
	private class TotalTable extends JTable {
		@Override
		public int getRowCount() {
			// fake an additional row
			int rowCount = super.getRowCount();
			if (rowCount > 1) {
				// Add a total line if there's more than one account
				rowCount++;
			}
			return rowCount;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (row < super.getRowCount()) {
				return super.getValueAt(row, column);
			}
			// We deal with the total line
			column = convertColumnIndexToModel(column);
			if (column == AccountsSummaryTableModel.SELECT_COLUMN) {
				return null;
			} else if (column == AccountsSummaryTableModel.ACCOUNT_COLUMN) {
				return LocalizationData.get("BudgetPanel.sum"); //$NON-NLS-1$
			}
			AccountsSummaryTableModel model = (AccountsSummaryTableModel) getModel();
			Number result;
			if (column == AccountsSummaryTableModel.NB_TRANSACTIONS_COLUMN ||
					column == AccountsSummaryTableModel.NB_UNCHECKED_TRANSACTIONS_COLUMN) {
				result = Long.valueOf(0);
			} else {
				result = Double.valueOf(0.0);
			}
			for (int i = 0; i < data.getAccountsNumber(); i++) {
				if ((Boolean) model.getValueAt(i, AccountsSummaryTableModel.SELECT_COLUMN)) {
					result = NumberUtils.add(result, (Number)model.getValueAt(i, column));
				}
			}
			return result;
		}

		@Override
		public int convertRowIndexToModel(int viewRowIndex) {
			if (viewRowIndex < super.getRowCount()) {
				return super.convertRowIndexToModel(viewRowIndex);
			}
			// can't convert our faked row
			return super.getRowCount();
		}
		
    @Override
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			if ((row == getRowCount() - 1) && (convertColumnIndexToModel(column) == AccountsSummaryTableModel.SELECT_COLUMN)) {
				// Replace the checkbox by an empty string
				renderer = getDefaultRenderer(Object.class);
				column = AccountsSummaryTableModel.ACCOUNT_COLUMN;
				JLabel component = (JLabel) super.prepareRenderer(renderer, row, column);
				component.setText(""); //$NON-NLS-1$
				return component;
			} else {
				return super.prepareRenderer(renderer, row, column);
			}
		}
	}

	private class MyRenderer extends CellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			int rowCount = table.getModel().getRowCount();
			Font font = super.getFont();
			int style = font.getStyle();
			if (row==rowCount) {
				style = style | Font.BOLD;
				isSelected = false;
			} else {
				int mask = Integer.MIN_VALUE & (~Font.BOLD);
				style = style & mask;
			}
			super.setFont(font.deriveFont(style, font.getSize2D()));
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
	private JPanel getEditPanel() {
		if (editPanel == null) {
			editPanel = new JPanel();
			GridBagLayout gblEditPanel = new GridBagLayout();
			editPanel.setLayout(gblEditPanel);
			GridBagConstraints gbcEditButton = new GridBagConstraints();
			gbcEditButton.anchor = GridBagConstraints.WEST;
			gbcEditButton.weightx = 1.0;
			gbcEditButton.gridx = 0;
			gbcEditButton.gridy = 0;
			editPanel.add(getEditButton(), gbcEditButton);
		}
		return editPanel;
	}
	private JButton getEditButton() {
		if (editButton == null) {
			editButton = new JButton(LocalizationData.get("GenericButton.edit"), IconManager.get(Name.EDIT_ACCOUNT)); //$NON-NLS-1$
			editButton.setToolTipText(LocalizationData.get("AccountManager.editAccount.toolTip")); //$NON-NLS-1$
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Account account = getSelectedAccount();
					if (account!=null) {
						EditAccountDialog.edit(data, Utils.getOwnerWindow(getEditButton()), account);
						int index = getTable().convertRowIndexToView(data.indexOf(account));
						getTable().getSelectionModel().setSelectionInterval(index, index);
					}
				}
			});
		}
		return editButton;
	}
}
