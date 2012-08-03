package net.yapbam.currency;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.astesana.ajlib.utilities.NullUtils;

public class CurrencyNames {
	private static final String BUNDLE_NAME = "net.yapbam.currency.currencyNames"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE;
	private static Locale resourceBundleLocale;
	private static boolean translatorMode;

	private CurrencyNames() {
	}

	public static String get(String key) {
		reset();
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
	
	private static void reset() {
		if (!NullUtils.areEquals(resourceBundleLocale, Locale.getDefault())) {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
			resourceBundleLocale = Locale.getDefault();
		}
	}
}
