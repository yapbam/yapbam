package net.astesana.comptes.data.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class DefaultListenable implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private transient Collection<DataListener> listeners;
	private transient boolean eventsDisabled;
	
	protected DefaultListenable() {
		this.listeners = new ArrayList<DataListener>();
		this.eventsDisabled = false;
	}
	
	protected void setEventsEnabled(boolean enabled) {
		this.eventsDisabled = !enabled;
	}
    	
	protected void fireEvent(DataEvent event) {
		if (eventsDisabled) return;
		Iterator<DataListener> iterator = listeners.iterator();
		while (iterator.hasNext()) {
			iterator.next().processEvent(event);
		}
	}

	public void addListener(DataListener listener) {
		if (listeners==null) this.listeners = new ArrayList<DataListener>();
		listeners.add(listener);
	}
}
