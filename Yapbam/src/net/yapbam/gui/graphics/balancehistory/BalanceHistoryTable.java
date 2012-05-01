package net.yapbam.gui.graphics.balancehistory;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.astesana.ajlib.utilities.NullUtils;
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
		this.lastSelected = null;
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					Transaction[] selectedTransaction = getSelectedTransactions();
					if (!NullUtils.areEquals(selectedTransaction,lastSelected)) { 					//FIXME The equality 
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
	
	public FilteredData getFilteredData() {
		return data;
	}
}