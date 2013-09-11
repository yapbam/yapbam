package net.yapbam.gui.persistence;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker.StateValue;

import com.fathzer.soft.ajlib.swing.worker.Worker;


public class CancelManager implements PropertyChangeListener {
	private Runnable action;
	private Worker<?,?> worker;
	
	public CancelManager(Worker<?,?> worker) {
		this.worker = worker;
		this.worker.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Worker.STATE_PROPERTY_NAME)) {
//			System.out.println (evt.getPropertyName()+": "+evt.getOldValue()+" -> "+evt.getNewValue());
			if (action!=null && evt.getNewValue().equals(StateValue.DONE) && worker.isCancelled()) {
//				System.out.println("cancelled");
				action.run();
			}
		}
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
}