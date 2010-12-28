package net.yapbam.updater;

import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "net.yapbam.updater.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		return RESOURCE_BUNDLE.getString(key);
	}
}
