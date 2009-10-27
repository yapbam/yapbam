package net.yapbam.gui.tools;

import java.util.Locale;
import java.util.ResourceBundle;

import net.yapbam.gui.Preferences;

public class Messages {
	private static final String BUNDLE_NAME = "net.yapbam.gui.tools.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE;
	
	static {
		reset();
	}

	private Messages() {
	}

	public static String getString(String key) {
		return RESOURCE_BUNDLE.getString(key);
	}
	
	static void reset() {
		Locale oldDefault = Locale.getDefault(); // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4303146
		Locale.setDefault(Preferences.INSTANCE.getLocale());
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		Locale.setDefault(oldDefault);
	}
}
