package net.yapbam.ihm.transactiontable;

import java.util.Date;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.yapbam.data.FilteredData;

public class TransactionTable extends JTable {
	private static final long serialVersionUID = 1L;

	public TransactionTable(FilteredData data) {
		super();
		this.setModel(new TransactionsTableModel(this, data));
		this.setDefaultRenderer(Date.class, new DateRenderer());
		this.setDefaultRenderer(double[].class, new AmountRenderer());
		this.setDefaultRenderer(SpreadState.class, new SpreadStateRenderer());
		this.setDefaultRenderer(Object.class, new ObjectRenderer());
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addMouseListener(new SpreadableMouseAdapter());
	}
}