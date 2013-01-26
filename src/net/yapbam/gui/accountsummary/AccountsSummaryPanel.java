package net.yapbam.gui.accountsummary;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import net.astesana.ajlib.swing.table.RowSorter;
import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.statementview.CellRenderer;
import net.yapbam.gui.util.SplitPane;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
			table.setDefaultRenderer(Object.class, new MyRenderer());
			table.setDefaultRenderer(Double.class, new MyRenderer());
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						int selected = table.getSelectedRow();
						boolean accountIsSelected = (selected>=0) && (selected<data.getAccountsNumber());
						if (accountIsSelected) {
							Account account = data.getAccount(table.convertRowIndexToModel(selected));
							if (!getSplitPane().isDividerVisible()) getSplitPane().setDividerLocation(0.5);
							getNotesField().setText(account.getComment()==null?"":account.getComment());
						}
						getSplitPane().setDividerVisible(accountIsSelected);
						getBottomPanel().setVisible(accountIsSelected);
					}
				}
			});
			table.setRowHeight((int) (Preferences.INSTANCE.getFontSizeRatio()*table.getRowHeight()));
		}
		return table;
	}
	
	private JPanel getBottomPanel() {
		if (bottomPanel == null) {
			bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.add(getNotePane());
			bottomPanel.add(getLblNotesLabel(), BorderLayout.NORTH);
			bottomPanel.setVisible(false);
		}
		return bottomPanel;
	}

	private JLabel getLblNotesLabel() {
		if (lblNotesLabel == null) {
			lblNotesLabel = new JLabel(LocalizationData.get("AccountDialog.notes"));
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
			notesField.setToolTipText(LocalizationData.get("AccountDialog.notes.tooltip")); //$NON-NLS-1$
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
			if (rowCount > 1) rowCount++; // Add a total line if there's more than one account
			return rowCount;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (row < super.getRowCount()) {
				return super.getValueAt(row, column);
			}
			// We deal with the total line
			column = convertColumnIndexToModel(column);
			if (column == 0) return LocalizationData.get("BudgetPanel.sum"); //$NON-NLS-1$
			double result = 0.0;
			for (int i = 0; i < data.getAccountsNumber(); i++) {
				if (column == 3) result += data.getAccount(i).getBalanceData().getCheckedBalance();
				else if (column == 1) result += data.getAccount(i).getBalanceData().getCurrentBalance();
				else if (column == 2) result += data.getAccount(i).getBalanceData().getFinalBalance();
			}
			return result;
		}

		@Override
		public int convertRowIndexToModel(int viewRowIndex) {
			if (viewRowIndex < super.getRowCount()) {
				return super.convertRowIndexToModel(viewRowIndex);
			}
			return super.getRowCount(); // can't convert our faked row
		}
	}

	private class MyRenderer extends CellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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
}
