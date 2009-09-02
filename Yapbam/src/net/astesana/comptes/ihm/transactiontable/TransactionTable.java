package net.astesana.comptes.ihm.transactiontable;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.astesana.comptes.data.FilteredData;

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
		this.addMouseListener(new MouseAdapter() {
			// This listener waits for clicks on the "spread sub-transaction" zone
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
				int column = columnAtPoint(p);
				int row = rowAtPoint(p);
				TransactionsTableModel model = (TransactionsTableModel) getModel();
				if ((column == 0) && (row >= 0) && (model.getTransaction(row).getSubTransactionSize() > 0)) {
					boolean spread = getRowHeight()!=getRowHeight(row);
					if (spread) {
						setRowHeight(row, getRowHeight());
					} else {
						int numberOfLines = 1 + model.getTransaction(row).getSubTransactionSize();
						if (model.getTransaction(row).getComplement()!=0) numberOfLines++;
						setRowHeight(row, getRowHeight() * numberOfLines);
					}
				}
			}
		});
	}
}
