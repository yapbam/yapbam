package net.yapbam.data.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** A listenable class allows some other classes to listen to the events occurring on the listenable class.
 *	Please note that if the traceEvents java system property is set to true, all data events are trace on stderr. 
 */
public abstract class DefaultListenable {
	private static final boolean TRACE_LISTENERS = Boolean.getBoolean("traceEventListeners");
	private static final boolean TRACE_EVENTS = Boolean.getBoolean("traceEvents");
	private static int indent = 0;
	
	private transient Collection<DataListener> listeners;
	private transient boolean eventsDisabled;
	
	/** Constructor.
	 * Events throwing is enabled.
	 * @see #setEventsEnabled(boolean)
	 */
	protected DefaultListenable() {
		this.listeners = new ArrayList<DataListener>();
		this.setEventsEnabled(true);
	}
	
	/** Enables/Disables the event throwing (for instance if this instance performs a lot of changes on its data,
	 * it would be most efficient to disable events, perform the changes, enable events, and then send an EverythingChangedEvent).
	 * @param enabled true to enable events throwing.
	 */
	protected void setEventsEnabled(boolean enabled) {
		this.eventsDisabled = !enabled;
	}
	
	/** Tests whether events are enabled or not.
	 * @return true if events are enabled.
	 */
	protected boolean IsEventsEnabled() {
		return !this.eventsDisabled;
	}
    
	/** Sends an event to every listeners.
	 * @param event The event to send.
	 */
	protected void fireEvent(DataEvent event) {
		if (eventsDisabled) return;
		Iterator<DataListener> iterator = listeners.iterator();
		if (TRACE_EVENTS && (listeners.size()==0)) trace("Event "+event+" occurs on "+this+" but nobody is listening");
		while (iterator.hasNext()) {
			DataListener listener = iterator.next();
			if (TRACE_EVENTS) trace("Send event "+event+" on "+this+" to "+listener);
			indent += 2;
			listener.processEvent(event);
			//FIXME catch exceptions thrown by the listeners (for other listeners to receive the event).
			indent -= 2;
		}
	}
	
	private void trace (String message) {
		for (int i = 0; i < indent; i++) {
			System.err.print(' ');
		}
		System.err.println(message);
	}

	/** Adds a new listener on this.
	 * @param listener The listener to add.
	 */
	public void addListener(DataListener listener) {
		if (listeners==null) this.listeners = new ArrayList<DataListener>();
		if (TRACE_EVENTS || TRACE_LISTENERS) System.err.println ("Add listener "+listener+" on "+this);
		listeners.add(listener);
	}
	
	/** Removes all the previously registered listeners.
	 */
	public void clearListeners() {
		if (TRACE_EVENTS || TRACE_LISTENERS) System.err.println ("All listeners are cleared on "+this);
		this.listeners.clear();
	}
}
