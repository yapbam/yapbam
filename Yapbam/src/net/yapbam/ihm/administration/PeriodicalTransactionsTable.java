package net.yapbam.ihm.administration;

import java.util.Date;

import javax.swing.JTable;

import net.yapbam.data.GlobalData;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.ihm.transactiontable.AmountRenderer;
import net.yapbam.ihm.transactiontable.DateRenderer;
import net.yapbam.ihm.transactiontable.ObjectRenderer;
import net.yapbam.ihm.transactiontable.SpreadState;
import net.yapbam.ihm.transactiontable.SpreadStateRenderer;
import net.yapbam.ihm.transactiontable.SpreadableMouseAdapter;
import net.yapbam.ihm.transactiontable.BooleanRenderer;

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