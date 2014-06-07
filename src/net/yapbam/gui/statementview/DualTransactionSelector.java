package net.yapbam.gui.statementview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.TransactionSelector;

public class DualTransactionSelector implements TransactionSelector {
	private List<PropertyChangeListener> listeners;
	private StatementViewPanel panel;
	private Transaction[] lastSelected;
	
	public DualTransactionSelector(StatementViewPanel panel) {
		this.listeners = new ArrayList<PropertyChangeListener>();
		this.panel = panel;
		this.lastSelected = new Transaction[0];
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Transaction[] selected = getSelectedTransactions();
				if (!Arrays.equals(lastSelected, selected)) {
					Transaction[] old = lastSelected;
					lastSelected = selected;
					evt = new PropertyChangeEvent(evt.getSource(), SELECTED_PROPERTY, old, lastSelected);
					for (int i = 0; i < listeners.size(); i++) {
						listeners.get(i).propertyChange(evt);
					}
				}
			}
		};
		this.panel.getTransactionsTable().addPropertyChangeListener(SELECTED_PROPERTY, listener);
		this.panel.getUncheckedTransactionsTable().addPropertyChangeListener(SELECTED_PROPERTY, listener);
		this.panel.addPropertyChangeListener(StatementViewPanel.CHECK_MODE_READY_PROPERTY, listener);
	}
	
	@Override
	public Transaction[] getSelectedTransactions() {
		if (panel.isCheckModeReady()) {
			return new Transaction[0];
		}
		return (panel.isCheckMode()?panel.getUncheckedTransactionsTable():panel.getTransactionsTable()).getSelectedTransactions();
	}

	@Override
	public void setSelectedTransactions(Transaction[] transactions) {
		panel.getTransactionsTable().setSelectedTransactions(transactions);
		if (panel.isCheckMode()) {
			panel.getUncheckedTransactionsTable().setSelectedTransactions(transactions);
		}
	}
	
	@Override
	public FilteredData getFilteredData() {
		return panel.getFilteredData();
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.listeners.remove(listener);
	}
}
