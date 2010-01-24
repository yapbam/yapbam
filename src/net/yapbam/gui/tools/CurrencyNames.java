package net.yapbam.gui.tools;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.yapbam.gui.Preferences;

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
				return RESOURCE_BUNDLE.getString(key)+" ("+key+")";
			} catch (MissingResourceException e) {
				return key;
			}
		}
	}
	
	static void reset() {
		Locale oldDefault = Locale.getDefault(); // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4303146
		Locale.setDefault(Preferences.INSTANCE.getLocale());
		translatorMode = Preferences.INSTANCE.isTranslatorMode();
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
		Locale.setDefault(oldDefault);
	}
}
