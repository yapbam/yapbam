package net.yapbam.gui.transactiontable;

import java.awt.Component;
import java.awt.Font;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import com.fathzer.soft.ajlib.swing.table.JTableSelector;
import com.fathzer.soft.ajlib.swing.table.RowSorter;
import com.fathzer.soft.ajlib.utilities.NullUtils;

import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.util.DoubleArrayComparator;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.LinkEnabler;
import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

public class TransactionTable extends FriendlyTable implements TransactionSelector, PaintedTable {
	private static final long serialVersionUID = 1L;
	private Transaction[] lastSelected;
	private FilteredData data;
	
    public TransactionTable(FilteredData data) {
		super();

		this.data = data;
		TransactionsTableModel model = new TransactionsTableModel(this, data);
		this.setModel(model);
		this.setDefaultRenderer(Date.class, new DateRenderer());
		this.setDefaultRenderer(double[].class, new AmountRenderer());
		this.setDefaultRenderer(SpreadState.class, new SpreadStateRenderer());
		this.setDefaultRenderer(Object.class, new ObjectRenderer());
		LinkEnabler.enable(this, model.getTableSettings().getDescriptionColumn(), model.getTableSettings().getCommentColumn());
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addMouseListener(new SpreadableMouseAdapter());
		TableRowSorter<TransactionsTableModel> sorter = new RowSorter<TransactionsTableModel>(model);
		TableSettings settings = model.getTableSettings();
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
		sorter.setComparator(4, new Comparator<double[]>() {
			@Override
			public int compare(double[] o1, double[] o2) {
				return (int) Math.signum(o1[0]-o2[0]);
			}
		});
		this.setRowSorter(sorter);
		this.lastSelected = null;
		//TODO this.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					Transaction[] selectedTransactions = getSelectedTransactions();
					if (!NullUtils.areEquals(selectedTransactions,lastSelected)) { //FIXME
						firePropertyChange(SELECTED_PROPERTY, lastSelected, selectedTransactions);
						lastSelected = selectedTransactions;
					}
				}
			}
		});
	}
    
    private TablePainter painter = new TransactionTablePainter() {
		private static final long serialVersionUID = 1L;

		@Override
		public void setRowLook(Component renderer, JTable table, int row, boolean isSelected) {
			super.setRowLook(renderer, table, row, isSelected);
			boolean isChecked = data.getTransaction(row).isChecked();
			Font font = renderer.getFont().deriveFont(isChecked ? Font.ITALIC : Font.PLAIN + Font.BOLD);
			renderer.setFont(font);
		}
		
		@Override
		public int getAlignment(int column) {
			TableSettings settings = ((TransactionsTableModel)getModel()).getTableSettings();
			if ((column == settings.getAmountColumn()) || (column == settings.getReceiptColumn())
					|| (column == settings.getExpenseColumn()) /*|| (column == settings.getNumberColumn())*/) {
				return SwingConstants.RIGHT;
			} else if ((column == settings.getAccountColumn()) || (column == settings.getDescriptionColumn())) {
				return SwingConstants.LEFT;
			} else {
				return SwingConstants.CENTER;
			}
		}
	};
	
    @Override
    public TablePainter getPainter() {
    	return painter;
    }

	@Override
	public Transaction[] getSelectedTransactions() {
		int[] indexes = getSelectedRows();
		Transaction[] result = new Transaction[indexes.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = data.getTransaction(this.convertRowIndexToModel(indexes[i]));
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see net.yapbam.gui.actions.TransactionSelector#setSelectedTransactions(net.yapbam.data.Transaction[])
	 */
	@Override
	public void setSelectedTransactions(Transaction[] transactions) {
		JTableSelector<Transaction> selector = new JTableSelector<Transaction>(this) {
			@Override
			public int getModelIndex(Transaction transaction) {
				return data==null?-1:data.indexOf(transaction);
			}
		};
		selector.setSelected(transactions);
	}

	public GlobalData getGlobalData() {
		return data==null?null:data.getGlobalData();
	}
	
	@Override
	public FilteredData getFilteredData() {
		return data;
	}
		
	/** Scrolls this table to last line.
	 */
	public void scrollToLastLine() {
		scrollRectToVisible(getCellRect(getRowCount()-1, 0, true));
	}
}