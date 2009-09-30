package net.yapbam.gui.administration;

import java.util.Date;

import javax.swing.JTable;

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
	}

	public PeriodicalTransaction getSelectedTransaction() {
		int index = getSelectedRow();
		if (index < 0) return null;
		return getGlobalData().getPeriodicalTransaction(index);
	}

	public GlobalData getGlobalData() {
		return ((PeriodicalTransactionTableModel)this.getModel()).getGlobalData();
	}
}