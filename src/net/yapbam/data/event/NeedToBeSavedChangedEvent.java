package net.yapbam.data.event;

import net.yapbam.data.GlobalData;

public class NeedToBeSavedChangedEvent extends DataEvent {

	public NeedToBeSavedChangedEvent(GlobalData data) {
		super(data);
	}

}
