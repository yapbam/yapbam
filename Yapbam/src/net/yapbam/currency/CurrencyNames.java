package net.yapbam.currency;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CurrencyNames {
	private static final String BUNDLE_NAME = "net.yapbam.tools.currencyNames"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private CurrencyNames() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
