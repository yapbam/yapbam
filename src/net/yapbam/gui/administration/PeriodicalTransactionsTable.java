package net.yapbam.gui.administration;

import java.util.Comparator;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import net.yapbam.data.GlobalData;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.gui.transactiontable.AmountRenderer;
import net.yapbam.gui.transactiontable.BooleanRenderer;
import net.yapbam.gui.transactiontable.DateRenderer;
import net.yapbam.gui.transactiontable.ObjectRenderer;
import net.yapbam.gui.transactiontable.SpreadState;
import net.yapbam.gui.transactiontable.SpreadStateRenderer;
import net.yapbam.gui.transactiontable.SpreadableMouseAdapter;

class PeriodicalTransactionsTable extends JTable {
	private static final long serialVersionUID = 1L;

	public PeriodicalTransactionsTable(PeriodicalTransactionTableModel model) {
		super(model);
		this.setDefaultRenderer(Date.class, new DateRenderer());
		this.setDefaultRenderer(double[].class, new AmountRenderer());
		this.setDefaultRenderer(SpreadState.class, new SpreadStateRenderer());
		this.setDefaultRenderer(Boolean.class, new BooleanRenderer());
		this.setDefaultRenderer(Object.class, new ObjectRenderer());
		this.addMouseListener(new SpreadableMouseAdapter());
		TableRowSorter<PeriodicalTransactionTableModel> sorter = new TableRowSorter<PeriodicalTransactionTableModel>(model);
		sorter.setComparator(3, new Comparator<double[]>() {
			@Override
			public int compare(double[] o1, double[] o2) {
				return (int) Math.signum(o1[0]-o2[0]);
			}
		});
		this.setRowSorter(sorter);
	}

	public PeriodicalTransaction getSelectedTransaction() {
		int index = getSelectedRow();
		return (index < 0)?null:getGlobalData().getPeriodicalTransaction(convertRowIndexToModel(index));
	}

	public GlobalData getGlobalData() {
		return ((PeriodicalTransactionTableModel)this.getModel()).getGlobalData();
	}
}