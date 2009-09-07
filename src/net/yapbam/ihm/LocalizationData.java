package net.yapbam.ihm;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class LocalizationData {
	private static ResourceBundle bundle;
	private static Locale locale;
	
	static {
		reset();
	}
	
	public static void reset() {
		locale = Preferences.INSTANCE.getLocale();
		Locale oldDefault = Locale.getDefault(); // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4303146
		Locale.setDefault(locale);
		ResourceBundle res = ResourceBundle.getBundle("Resources", locale); //$NON-NLS-1$
		Locale.setDefault(oldDefault);
		setBundle(res);
	}
	
	private static void setBundle(ResourceBundle aBundle) {
		bundle = aBundle;
	}
	
	public static String get(String key) {
		return bundle.getString(key);
	}
	
	public static char getChar(String key) {
		return get(key).charAt(0);
	}

	public static Locale getLocale() {
		return locale;
	}

	public static DecimalFormat getCurrencyInstance() {
		return (DecimalFormat) NumberFormat.getCurrencyInstance(getLocale());
	}
	
	public static URL getURL(String document) {
		URL url = Object.class.getResource("/localization/"+getLocale().getLanguage()+"/"+document);
		if (url==null) url = Object.class.getResource("/localization/"+document);
		return url;
	}
}
