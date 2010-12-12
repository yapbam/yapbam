package net.yapbam.data.event;

import net.yapbam.data.GlobalData;

/** This event event is sent when the URI where the data is saved changes.
 * @see GlobalData#getURI()
 */
public class URIChangedEvent extends DataEvent {
	/** Constructor.
	 * @param data the GlobalData instance on which the changed occured.
	 */
	public URIChangedEvent(GlobalData data) {
		super(data);
	}

}
