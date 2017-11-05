package net.yapbam.gui.tools;

import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "net.yapbam.gui.tools.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE;
	private static boolean translatorMode;
	
	static {
		reset();
	}

	private Messages() {
	}

	public static String getString(String key) {
		return translatorMode?"net.yapbam.gui.tools.messages/"+key:RESOURCE_BUNDLE.getString(key); //$NON-NLS-1$
	}
	
	static void reset() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	}
}
