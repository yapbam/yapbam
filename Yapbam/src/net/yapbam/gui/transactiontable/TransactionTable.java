package net.yapbam.gui.transactiontable;

import java.util.Comparator;
import java.util.Date;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.actions.TransactionSelector;
import net.yapbam.gui.actions.TransactionsJTableSelector;
import net.yapbam.gui.util.FriendlyTable;

public class TransactionTable extends FriendlyTable implements TransactionSelector {
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
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addMouseListener(new SpreadableMouseAdapter());
		TableRowSorter<TransactionsTableModel> sorter = new TableRowSorter<TransactionsTableModel>(model);
		sorter.setComparator(4, new Comparator<double[]>() {
			@Override
			public int compare(double[] o1, double[] o2) {
				return (int) Math.signum(o1[0]-o2[0]);
			}
		});
		this.setRowSorter(sorter);
		this.lastSelected = null;
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
		TransactionsJTableSelector selector = new TransactionsJTableSelector(this) {
			@Override
			public int getModelIndex(Transaction transaction) {
				return data.indexOf(transaction);
			}
		};
		selector.setSelectedTransactions(transactions);
	}

	public GlobalData getGlobalData() {
		return data.getGlobalData();
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