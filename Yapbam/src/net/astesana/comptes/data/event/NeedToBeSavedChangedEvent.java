package net.astesana.comptes.data.event;

import net.astesana.comptes.data.GlobalData;

public class NeedToBeSavedChangedEvent extends DataEvent {

	public NeedToBeSavedChangedEvent(GlobalData data) {
		super(data);
	}

}
