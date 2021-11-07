package net.yapbam.gui.dialogs.checkbook;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.CheckbookAddedEvent;
import net.yapbam.data.event.CheckbookRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;

public abstract class CheckBookUtils {
	public static final String ALERT_CHANGED_PROPERTY = "CheckBookAlert";
	
	public interface ChangeReceiver {
		void process(boolean hasAlert);
	}
	
	private CheckBookUtils() {
		// Utility class, should not be instantiated
	}
	
	public static boolean hasAlert(Account account) {
		return account.getCheckNumberAlertThreshold() < account.getRemainingChecks();
	}
	
	private static boolean hasAlert(GlobalData data) {
		for (int i=0; i<data.getAccountsNumber(); i++) {
			if (hasAlert(data.getAccount(i))) {
				return true;
			}
		}
		return false;
	}
	
	public static void registerForAlertChange(final GlobalData data, final ChangeReceiver receiver) {
		data.addListener(new DataListener() {
			private boolean hasAlert = hasAlert(data);
			
			@Override
			public void processEvent(DataEvent event) {
				boolean usefulEvent = (event instanceof AccountPropertyChangedEvent && AccountPropertyChangedEvent.CHECK_NUMBER_ALERT_THRESHOLD.equals(((AccountPropertyChangedEvent)event).getProperty())) ||
					event instanceof AccountRemovedEvent ||
					event instanceof CheckbookAddedEvent ||
					event instanceof CheckbookRemovedEvent ||
					event instanceof EverythingChangedEvent;
				if (usefulEvent && (hasAlert(data)!=this.hasAlert)) {
					this.hasAlert = !this.hasAlert;
					receiver.process(this.hasAlert);
				}
			}
		});
	}
}
