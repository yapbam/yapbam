package net.yapbam.gui.tools.currencyconverter;

import net.yapbam.gui.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceManager {
	private static final String SOURCE_PREF_KEY = "net.yapbam.gui.tools.currencyConverter.source"; //$NON-NLS-1$
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConverterAction.class);

	public static Source getSource() {
		try {
			String property = Preferences.INSTANCE.getProperty(SOURCE_PREF_KEY, Source.ECB.name());
			Source preferedSource = Source.valueOf(property);
			if (Source.YAHOO.equals(preferedSource)) {
				// Unfortunately, Yahoo retired its FOREX rates source
				preferedSource = Source.ECB;
			}
			return preferedSource;
		} catch (IllegalArgumentException e) {
			LOGGER.warn("Invalid value in preferences", e); //$NON-NLS-1$
			return Source.ECB;
		}
	}

	static void setSource(Source source) {
		if (Source.ECB.equals(source)) {
			Preferences.INSTANCE.removeProperty(SOURCE_PREF_KEY);
		} else {
			Preferences.INSTANCE.setProperty(SOURCE_PREF_KEY, source.name());
		}
	}

}
