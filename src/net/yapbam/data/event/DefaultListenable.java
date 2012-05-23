package net.yapbam.data.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** A listenable class allows some other classes to listen to the events occurring on the listenable class.
 *	<br>Please note that the following java system properties may be used to track the events:<ul>
 *		<li>if the traceAll system property is set to true, all data events are traced on stderr.<li> 
 *		<li>if the traceEvents system property is set to true, every event sent is traced on stderr.<li> 
 *		<li>if the traceEventListeners system property is set to true, the modifications in the listeners list are traced on stderr.<li>
 *	</ul>
 */
public abstract class DefaultListenable {
	private static final boolean TRACE_LISTENERS = Boolean.getBoolean("traceEventListeners"); //$NON-NLS-1$
	private static final boolean TRACE_EVENTS = Boolean.getBoolean("traceEvents"); //$NON-NLS-1$
	private static final boolean TRACE_ALL = Boolean.getBoolean("traceAll"); //$NON-NLS-1$
	private static int indent = 0;
	
	private transient Collection<DataListener> listeners;
	private transient boolean eventsDisabled;
	
	/** Constructor.
	 * <br>Events throwing is enabled.
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
	@SuppressWarnings("nls")
	protected void fireEvent(DataEvent event) {
		if (eventsDisabled) return;
		if (TRACE_EVENTS && !TRACE_ALL) trace("Event "+event+" occurs on "+this); //$NON-NLS-1$ //$NON-NLS-2$
		Iterator<DataListener> iterator = listeners.iterator();
		if (TRACE_ALL && (listeners.size()==0)) trace("Event "+event+" occurs on "+this+" but nobody is listening"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		while (iterator.hasNext()) {
			DataListener listener = iterator.next();
			if (TRACE_ALL) trace("Send event "+event+" on "+this+" to "+listener); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			indent += 2;
			try {
				listener.processEvent(event);
			} finally {
				indent -= 2;
			}
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
		if (TRACE_ALL || TRACE_LISTENERS) System.err.println ("Add listener "+listener+" on "+this); //$NON-NLS-1$ //$NON-NLS-2$
		listeners.add(listener);
	}
	
	/** Removes all the previously registered listeners.
	 */
	public void clearListeners() {
		if (TRACE_ALL || TRACE_LISTENERS) System.err.println ("All listeners are cleared on "+this); //$NON-NLS-1$
		this.listeners.clear();
	}
	
	/** Gets the number of listeners.
	 * @return a positive or null integer
	 */
	public int getNumberOfListeners() {
		return this.listeners.size();
	}
}
