package net.yapbam.gui.statementview;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.FilteredData;
import net.yapbam.data.GlobalData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.actions.TransactionSelector;
import net.yapbam.gui.util.FriendlyTable;

public class StatementTable extends FriendlyTable implements TransactionSelector {
	private static final long serialVersionUID = 1L;
	private Transaction lastSelected;
	private FilteredData data;

	public StatementTable(FilteredData data) {
		super();
		this.data = data;
		this.setModel(new StatementTableModel(this, new Transaction[0]));
		setAutoCreateRowSorter(true);
		setDefaultRenderer(Object.class, new CellRenderer());
		this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

	public Transaction getSelectedTransaction() {
		int index = getSelectedRow();
		return index < 0 ? null : ((StatementTableModel)this.getModel()).getTransactions()[this.convertRowIndexToModel(index)];
	}
	
	public GlobalData getGlobalData() {
		return data.getGlobalData();
	}
	
	public FilteredData getFilteredData() {
		return data;
	}
		
	/** Scrolls this table to last line.
	 */
	public void scrollToLastLine() {
		scrollRectToVisible(getCellRect(getRowCount()-1, 0, true));
	}

	public void setTransactions(Transaction[] transactions) {
		((StatementTableModel)this.getModel()).setTransactions(transactions);
	}
}