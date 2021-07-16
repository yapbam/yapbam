package net.yapbam.gui.administration;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Comparator;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.table.TableRowSorter;

import com.fathzer.soft.ajlib.swing.table.JTable;
import com.fathzer.soft.ajlib.swing.table.RowSorter;

import net.yapbam.data.GlobalData;
import net.yapbam.data.PeriodicalTransaction;
import net.yapbam.gui.transactiontable.AmountRenderer;
import net.yapbam.gui.transactiontable.BooleanRenderer;
import net.yapbam.gui.transactiontable.DateRenderer;
import net.yapbam.gui.transactiontable.ObjectRenderer;
import net.yapbam.gui.transactiontable.SpreadState;
import net.yapbam.gui.transactiontable.SpreadStateRenderer;
import net.yapbam.gui.transactiontable.SpreadableMouseAdapter;
import net.yapbam.gui.transactiontable.TransactionTablePainter;
import net.yapbam.gui.util.DoubleArrayComparator;
import net.yapbam.gui.util.LinkEnabler;
import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

class PeriodicalTransactionsTable extends JTable implements PaintedTable {
	private static final long serialVersionUID = 1L;
	private TablePainter painter;

	public PeriodicalTransactionsTable(final PeriodicalTransactionTableModel model) {
		super(model);
		this.setDefaultRenderer(Date.class, new DateRenderer());
		this.setDefaultRenderer(double[].class, new AmountRenderer());
		this.setDefaultRenderer(SpreadState.class, new SpreadStateRenderer());
		this.setDefaultRenderer(Boolean.class, new BooleanRenderer());
		this.setDefaultRenderer(Object.class, new ObjectRenderer());
		LinkEnabler.enable(this, model.getTableSettings().getDescriptionColumn(), model.getTableSettings().getCommentColumn());
		this.addMouseListener(new SpreadableMouseAdapter());
		TableRowSorter<PeriodicalTransactionTableModel> sorter = new RowSorter<PeriodicalTransactionTableModel>(model);
		final PeriodicalTransactionsTableSettings settings = model.getTableSettings();
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
		painter = new TransactionTablePainter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void setRowLook(Component renderer, javax.swing.JTable table, int row, boolean isSelected) {
				super.setRowLook(renderer, table, row, isSelected);
				boolean ended = ((PeriodicalTransaction) model.getTransaction(row)).getNextDate() == null;
				Font font = renderer.getFont().deriveFont(ended ? Font.ITALIC : Font.PLAIN);
				if (ended) {
					renderer.setForeground(Color.GRAY);
				}
				renderer.setFont(font);
			}
			
			@Override
			public int getAlignment(int column) {
		    	if ((column==settings.getDescriptionColumn()) || (column==settings.getCommentColumn()) || (column==settings.getAccountColumn())) {
		    		return SwingConstants.LEFT;
		    	} else if (column==settings.getAmountColumn() || column==settings.getReceiptColumn() || column==settings.getExpenseColumn()) {
		    		return SwingConstants.RIGHT;
		    	} else {
		    		return SwingConstants.CENTER;
		    	}
			}
		};
	}

	public PeriodicalTransaction getSelectedTransaction() {
		int index = getSelectedRow();
		if (index<0) {
			return null;
		} else {
			return ((PeriodicalTransactionTableModel)getModel()).getTransaction(convertRowIndexToModel(index));
		}
	}

	public GlobalData getGlobalData() {
		return ((PeriodicalTransactionTableModel)this.getModel()).getGlobalData();
	}

	@Override
	public TablePainter getPainter() {
		return painter;
	}
}