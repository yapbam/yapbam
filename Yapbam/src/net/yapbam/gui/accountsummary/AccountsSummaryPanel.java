package net.yapbam.gui.accountsummary;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.statementview.CellRenderer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class AccountsSummaryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable table = null;
	private AccountsSummaryTableModel model;
	
	private GlobalData data;
	
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
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getJScrollPane(), BorderLayout.CENTER);
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes transactionsTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	JTable getTable() {
		if (table == null) {
			table = new JTable();
			this.model = new AccountsSummaryTableModel(table, data);
			table.setModel(this.model);
			table.setDefaultRenderer(Object.class, new MyRenderer());
			table.setDefaultRenderer(Double.class, new MyRenderer());
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return table;
	}
	
	private class MyRenderer extends CellRenderer {
		private static final long serialVersionUID = 1L;
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			int rowCount = table.getModel().getRowCount();
			Font font = super.getFont();
			int style = font.getStyle();
			if (row==rowCount-1) {
				style = style | Font.BOLD;
			} else {
				int mask = Integer.MIN_VALUE & (~Font.BOLD);
				style = style & mask;
			}
			super.setFont(font.deriveFont(style, font.getSize2D()));
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
}
