package net.yapbam.gui.administration;

import java.util.Comparator;
import java.util.Date;

import javax.swing.table.TableRowSorter;

import com.fathzer.soft.ajlib.swing.table.JTable;
import com.fathzer.soft.ajlib.swing.table.RowSorter;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.gui.transactiontable.AmountRenderer;
import net.yapbam.gui.transactiontable.BooleanRenderer;
import net.yapbam.gui.transactiontable.DateRenderer;
import net.yapbam.gui.transactiontable.ObjectRenderer;
import net.yapbam.gui.transactiontable.SpreadState;
import net.yapbam.gui.transactiontable.SpreadStateRenderer;
import net.yapbam.gui.transactiontable.SpreadableMouseAdapter;
import net.yapbam.gui.util.DoubleArrayComparator;

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
		TableRowSorter<PeriodicalTransactionTableModel> sorter = new RowSorter<PeriodicalTransactionTableModel>(model);
		PeriodicalTransactionsTableSettings settings = model.getTableSettings();
		Comparator<double[]> doubleArrayComparator = new DoubleArrayComparator();
		if (settings.getAmountColumn()!=-1) {
			sorter.setComparator(settings.getAmountColumn(), doubleArrayComparator);
		}
		if (settings.getReceiptColumn()!=-1) {
			sorter.setComparator(settings.getReceiptColumn(), doubleArrayComparator);
		}
		if (settings.getExpenseColumn()!=-1) {
			sorter.setComparator(settings.getExpenseColumn(), doubleArrayComparator);
		}
		this.setRowSorter(sorter);
	}

	public PeriodicalTransaction getSelectedTransaction() {
		int index = getSelectedRow();
		return (index < 0)?null:getGlobalData().getPeriodicalTransaction(convertRowIndexToModel(index));
	}

	public GlobalData getGlobalData() {
		return this.getFilteredData().getGlobalData();
	}

	public FilteredData getFilteredData() {
		return ((PeriodicalTransactionTableModel)this.getModel()).getFilteredData();
	}
}