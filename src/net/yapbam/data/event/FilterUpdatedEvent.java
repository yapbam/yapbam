package net.yapbam.data.event;

import net.yapbam.data.AccountFilter;

public class FilterUpdatedEvent extends DataEvent {
	public FilterUpdatedEvent(AccountFilter filter) {
		super(filter);
	}
}
