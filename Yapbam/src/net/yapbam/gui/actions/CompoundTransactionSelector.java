package net.yapbam.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.TransactionSelector;

public class CompoundTransactionSelector implements TransactionSelector {
	private List<PropertyChangeListener> listeners;
	private TransactionSelector internalSelector;
	private PropertyChangeListener internalListener;
	private FilteredData data;
	
	public CompoundTransactionSelector(FilteredData data) {
		this.data = data;
		this.listeners = new ArrayList<PropertyChangeListener>();
		this.internalSelector = null;
		this.internalListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChangedEvent(evt);
			}
		};
	}
	
	@Override
	public Transaction[] getSelectedTransactions() {
		return internalSelector==null?new Transaction[0]:internalSelector.getSelectedTransactions();
	}

	@Override
	public void setSelectedTransactions(Transaction[] transactions) {
		if (internalSelector!=null) {
			internalSelector.setSelectedTransactions(transactions);
		}
	}
	
	@Override
	public FilteredData getFilteredData() {
		return internalSelector==null?data:internalSelector.getFilteredData();
	}

	public void setInternalSelector(TransactionSelector selector) {
		if (internalSelector!=null) {
			internalSelector.removePropertyChangeListener(SELECTED_PROPERTY, this.internalListener);
		}
		Transaction[] old = getSelectedTransactions();
		this.internalSelector = selector;
		if (selector!=null) {
			selector.addPropertyChangeListener(SELECTED_PROPERTY, internalListener);
		}
		firePropertyChangedEvent(new PropertyChangeEvent(this, SELECTED_PROPERTY, old, this.getSelectedTransactions()));
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.listeners.remove(listener);
	}

	private void firePropertyChangedEvent(PropertyChangeEvent evt) {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).propertyChange(evt);
		}
	}
}