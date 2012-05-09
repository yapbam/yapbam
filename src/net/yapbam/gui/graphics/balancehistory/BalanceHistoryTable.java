package net.yapbam.gui.graphics.balancehistory;

import java.util.Arrays;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.astesana.ajlib.swing.table.JTableSelector;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.actions.TransactionSelector;
import net.yapbam.gui.statementview.CellRenderer;
import net.yapbam.gui.util.FriendlyTable;

public class BalanceHistoryTable extends FriendlyTable implements TransactionSelector {
	private static final long serialVersionUID = 1L;
	private Transaction[] lastSelected;
	private FilteredData data;

	public BalanceHistoryTable(FilteredData data) {
		super();
		this.data = data;
		this.setModel(new BalanceHistoryModel(data.getBalanceData()));
		setDefaultRenderer(Object.class, new CellRenderer());
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
		selector.setSelectedTransactions(transactions);
	}

	public FilteredData getFilteredData() {
		return data;
	}
}