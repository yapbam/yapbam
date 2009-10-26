package net.yapbam.gui.tools.currency.converter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.yapbam.gui.LocalizationData;

public class CurrencyNames {
	private static final String BUNDLE_NAME = "net.yapbam.gui.tools.currency.converter.currencyNames"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, LocalizationData.getLocale());

	private CurrencyNames() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key)+" ("+key+")";
		} catch (MissingResourceException e) {
			return key;
		}
	}
}
