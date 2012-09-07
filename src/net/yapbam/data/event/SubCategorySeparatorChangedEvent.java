package net.yapbam.data.event;

import net.yapbam.data.GlobalData;

public class SubCategorySeparatorChangedEvent extends DataEvent {
	private char oldSeparator;
	private char newSeparator;

	public SubCategorySeparatorChangedEvent(GlobalData data, char oldSeparator, char newSeparator) {
		super(data);
		this.oldSeparator = oldSeparator;
		this.newSeparator = newSeparator;
	}

	public char getOldSeparator() {
		return oldSeparator;
	}

	public char getNewSeparator() {
		return newSeparator;
	}
}
