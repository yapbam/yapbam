package net.yapbam.gui.tools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CurrencyNames {
	private static final String BUNDLE_NAME = "net.yapbam.gui.tools.currencyNames"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE;
	private static boolean translatorMode;

	static {
		reset();
	}

	private CurrencyNames() {
	}

	public static String getString(String key) {
		if (translatorMode) {
			return key;
		} else {
			try {
				return RESOURCE_BUNDLE.getString(key);
			} catch (MissingResourceException e) {
				return key;
			}
		}
	}
	
	static void reset() {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	}
}
