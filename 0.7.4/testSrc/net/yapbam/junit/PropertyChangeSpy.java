package net.yapbam.junit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

final class PropertyChangeSpy implements PropertyChangeListener {
	public PropertyChangeEvent lastEvent = null;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.lastEvent = evt;
		System.out.println (evt.getPropertyName()+" : "+evt.getOldValue()+" -> "+evt.getNewValue()+" on "+evt.getSource());
	}
}