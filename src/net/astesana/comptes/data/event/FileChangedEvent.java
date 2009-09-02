package net.astesana.comptes.data.event;

import net.astesana.comptes.data.GlobalData;

public class FileChangedEvent extends DataEvent {

	public FileChangedEvent(GlobalData data) {
		super(data);
	}

}
