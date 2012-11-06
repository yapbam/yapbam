package net.yapbam.currency;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;

public class CurrencyNames {
	private static final String BUNDLE_NAME = "/net/yapbam/currency/currencyNames"; //$NON-NLS-1$

	private static Properties RESOURCE_BUNDLE;
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
				return (String) RESOURCE_BUNDLE.get(key);
			} catch (MissingResourceException e) {
				return key;
			}
		}
	}
	
	private static void reset() {
		if (!Locale.getDefault().equals(resourceBundleLocale)) {
			Properties properties = new Properties();
			String lang = Locale.getDefault().getLanguage();
			String resourceSuffix = ".properties";
			boolean ok = false;
			try {
				ok = tryLoading(properties, BUNDLE_NAME+"_"+lang+resourceSuffix);
				if (!ok) ok = tryLoading(properties, BUNDLE_NAME+resourceSuffix);
			} catch (IOException e) {
				ok = false;
			}
			if (!ok) throw new MissingResourceException("", "", BUNDLE_NAME);
			RESOURCE_BUNDLE = properties;
			resourceBundleLocale = Locale.getDefault();
		}
	}
	
	private static boolean tryLoading(Properties properties, String name) throws IOException {
		InputStream stream = CurrencyNames.class.getResourceAsStream(name);
		if (stream==null) return false;
		properties.load(new InputStreamReader(stream, "ISO8859-1"));
		return true;
	}
}
