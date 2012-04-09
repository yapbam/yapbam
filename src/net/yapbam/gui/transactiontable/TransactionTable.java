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
import net.yapbam.gui.util.FriendlyTable;

public class TransactionTable extends FriendlyTable implements TransactionSelector {
	private static final long serialVersionUID = 1L;
	private Transaction lastSelected;
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
					Transaction selectedTransaction = getSelectedTransaction();
					if (!NullUtils.areEquals(selectedTransaction,lastSelected)) {
						firePropertyChange(SELECTED_PROPERTY, lastSelected, selectedTransaction);
						lastSelected = selectedTransaction;
					}
				}
			}
		});
	}

	@Override
	public Transaction getSelectedTransaction() {
		int index = getSelectedRow();
		return index < 0 ? null : data.getTransaction(this.convertRowIndexToModel(index));
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