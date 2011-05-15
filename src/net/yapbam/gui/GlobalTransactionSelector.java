package net.yapbam.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.actions.TransactionSelector;

class GlobalTransactionSelector implements TransactionSelector {
	private ArrayList<PropertyChangeListener> listeners;
	private TransactionSelector internalSelector;
	private PropertyChangeListener internalListener;
	
	GlobalTransactionSelector() {
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
	public Transaction getSelectedTransaction() {
		return internalSelector==null?null:internalSelector.getSelectedTransaction();
	}

	@Override
	public FilteredData getFilteredData() {
		return internalSelector==null?null:internalSelector.getFilteredData();
	}

	public void setInternalSelector(TransactionSelector selector) {
		if (internalSelector!=null) internalSelector.removePropertyChangeListener(SELECTED_PROPERTY, this.internalListener);
		Transaction old = getSelectedTransaction();
		this.internalSelector = selector;
		if (selector!=null) selector.addPropertyChangeListener(SELECTED_PROPERTY, internalListener);
		firePropertyChangedEvent(new PropertyChangeEvent(this, SELECTED_PROPERTY, old, this.getSelectedTransaction()));
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