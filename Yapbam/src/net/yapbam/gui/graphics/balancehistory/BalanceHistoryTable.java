package net.yapbam.gui.graphics.balancehistory;

import java.util.Arrays;
import java.util.Date;

import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import com.fathzer.soft.ajlib.swing.table.JTableSelector;

import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.TransactionSelector;
import net.yapbam.gui.util.FriendlyTable;
import net.yapbam.gui.util.LinkEnabler;
import net.yapbam.gui.util.PaintedTable;
import net.yapbam.gui.util.TablePainter;

public class BalanceHistoryTable extends FriendlyTable implements TransactionSelector, PaintedTable {
	private static final long serialVersionUID = 1L;
	private Transaction[] lastSelected;
	private FilteredData data;
	private TablePainter painter;

	public BalanceHistoryTable(FilteredData data) {
		super();
		this.data = data;
		this.painter = new BalanceHistoryTablePainter(data);
		BalanceHistoryModel model = new BalanceHistoryModel(data==null?null:data.getBalanceData());
		this.setModel(model);
		if (data!=null) {
			BalanceHistoryCellRenderer cellRenderer = new BalanceHistoryCellRenderer();
			setDefaultRenderer(Date.class, cellRenderer);
			setDefaultRenderer(Object.class, cellRenderer);
			LinkEnabler.enable(this, model.getSettings().getDescriptionColumn(), model.getSettings().getCommentColumn());
		}
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.lastSelected = new Transaction[0];
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					Transaction[] selectedTransaction = getSelectedTransactions();
					if (!Arrays.equals(selectedTransaction,lastSelected)) {
						firePropertyChange(SELECTED_PROPERTY, lastSelected, selectedTransaction);
						lastSelected = selectedTransaction;
					}
				}
			}
		});
		TableRowSorter<BalanceHistoryModel> sorter = new TableRowSorter<BalanceHistoryModel>(model) {
			// As the sort can only occurred on one column, instead of sorting the rows, we will just override the convertRowIndexToModel
			// It enough to have the illusion the table is sorted and faster than a "real" sort.
			// Moreover, if we had to sort, we should have been very careful, because sorting on the value date value leads to
			// wrong order (the sort have to take care of the remaining balance).
			@Override
			public int convertRowIndexToModel(int index) {
				if (isReverse()) {
					// Use the reverse
					return getModel().getRowCount()-1-index;
				} else {
					// Use the native sort
					return index;
				}
			}
			
			@Override
			public int convertRowIndexToView(int index) {
				if (isReverse()) {
					// Use the reverse
					return getModel().getRowCount()-1-index;
				} else {
					// Use the native sort
					return index;
				}
			}

			private boolean isReverse() {
				return getSortKeys().isEmpty() || getSortKeys().get(0).getSortOrder().equals(SortOrder.DESCENDING);
			}

			@Override
			public void sort() {
				// The model doesn't need to be sorted, we just have to know if the order is the model one or not
			}
		};
		for (int i = 0; i < model.getColumnCount(); i++) {
			sorter.setSortable(i, i==model.getSettings().getValueDateColumn());
			//TODO Could be cool to allow sorting on transaction date ...
		}
		setRowSorter(sorter);
	}
	
	public Transaction[] getSelectedTransactions() {
		int[] indexes = getSelectedRows();
		Transaction[] result = new Transaction[indexes.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = ((BalanceHistoryModel)this.getModel()).getTransaction(this.convertRowIndexToModel(indexes[i]));
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
				return ((BalanceHistoryModel) getModel()).find(transaction);
			}
		};
		selector.setSelected(transactions);
	}

	public FilteredData getFilteredData() {
		return data;
	}

	@Override
	public TablePainter getPainter() {
		return painter;
	}
}