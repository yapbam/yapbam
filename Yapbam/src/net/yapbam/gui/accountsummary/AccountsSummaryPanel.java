package net.yapbam.gui.accountsummary;

import javax.swing.JPanel;
import java.awt.BorderLayout;

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
			table.setDefaultRenderer(Object.class, new CellRenderer());
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//			table.setDefaultRenderer(Double.class, new DefaultTableCellRenderer(){
//		    @Override
//			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//		    		boolean hasFocus, int row, int column) {
//		    	super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//		    	if (value.equals(Double.POSITIVE_INFINITY) || value.equals(Double.NEGATIVE_INFINITY)) {
//		    		setText("");
//		    	} else {
//		    		setText(LocalizationData.getCurrencyInstance().format(value));
//		    	}
//			    this.setHorizontalAlignment(SwingConstants.RIGHT);
//		    	return this;
//		    }
//		});
		}
		return table;
	}
}
