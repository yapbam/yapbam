package net.yapbam.ihm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.swing.UIManager;

public class Preferences {
	private static final String LANGUAGE = "lang"; //$NON-NLS-1$
	private static final String COUNTRY = "country"; //$NON-NLS-1$
	private static final String LANGUAGE_DEFAULT_VALUE = "default"; //$NON-NLS-1$
	private static final String COUNTRY_DEFAULT_VALUE = "default"; //$NON-NLS-1$
	private static final String LOOK_AND_FEEL = "look"; //$NON-NLS-1$
	private static final String LOOK_AND_FEEL_JAVA_VALUE = "java"; //$NON-NLS-1$
	private static final String LOOK_AND_FEEL_CUSTOM_VALUE = "custom"; //$NON-NLS-1$

	private static final String FILENAME = ".yapbampref"; //$NON-NLS-1$

	public static final Preferences INSTANCE = new Preferences();
	
	private Properties properties;

	private Preferences() {
		this.properties = new Properties();
		if (new File(FILENAME).exists()) {
			try {
				properties.load(new FileInputStream(FILENAME));
			} catch (Throwable e) {
				// If there's another error, maybe it would be better to do something else //TODO
			}
		} else {
			// On the first run, the file doesn't exist
			setToDefault();
			save();
		}
	}
	
	private void setToDefault() {
		this.properties.clear();
		Locale locale = Locale.getDefault();
		this.properties.put(LANGUAGE, locale.getLanguage());
		this.properties.put(COUNTRY, Locale.getDefault().getCountry());
		this.properties.put(LOOK_AND_FEEL, LOOK_AND_FEEL_CUSTOM_VALUE);
	}

	private void save() {
		try {
			properties.store(new FileOutputStream(FILENAME), "Yapbam preferences"); //$NON-NLS-1$
		} catch (IOException e) {
			//TODO What could we do ?
		}
	}

	public Locale getLocale() {
//		String[] countries = Locale.getISOCountries();
		String lang = this.properties.getProperty(LANGUAGE);
		if (lang.equalsIgnoreCase(LANGUAGE_DEFAULT_VALUE)) lang = Locale.getDefault().getLanguage();
		String country = this.properties.getProperty(COUNTRY);
		if (country.equalsIgnoreCase(COUNTRY_DEFAULT_VALUE)) lang = Locale.getDefault().getCountry();
		return new Locale(lang, country);
	}
	
	public boolean isDefaultCountry() {
		return COUNTRY_DEFAULT_VALUE.equalsIgnoreCase((String) this.properties.get(COUNTRY));
	}
	
	public boolean isDefaultLanguage() {
		return LANGUAGE_DEFAULT_VALUE.equalsIgnoreCase((String) this.properties.get(LANGUAGE));
	}
	
	public String getLookAndFeel() {
		String value = this.properties.getProperty(LOOK_AND_FEEL);
		if (value.equalsIgnoreCase(LOOK_AND_FEEL_JAVA_VALUE)) return UIManager.getCrossPlatformLookAndFeelClassName(); 
		return UIManager.getSystemLookAndFeelClassName();
	}
}
