package net.yapbam.gui.accountsummary;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import net.yapbam.data.GlobalData;
import net.yapbam.gui.statementview.CellRenderer;

import javax.swing.JScrollPane;
import javax.swing.JSeparator;
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
	 * 
	 * @return void
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
			jScrollPane.setViewportView(getTransactionsTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes transactionsTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	JTable getTransactionsTable() {
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
		Component separator = new CenteredSeparator();
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			int rowCount = table.getModel().getRowCount();
			if (row==rowCount-2) {
				if (column==0) {
					isSelected = false;
				} else {
					separator.setBackground(table.getBackground()); //TODO not the right color !!!
					separator.setForeground(table.getForeground());
					return separator;
				}
			}
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
	
	private static class CenteredSeparator extends JPanel {
		public CenteredSeparator() {
			super(new GridBagLayout());
			this.setOpaque(true);
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1.0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(0, 12, 0, 2);
			this.add(new JSeparator(), c);
//		c.weighty = 1.0;
		}
	}
}
