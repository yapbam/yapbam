package net.astesana.ajlib.utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/** This class is the main entry point for localization concerns.
 */
public class LocalizationData {
	public static final Locale SYS_LOCALE = new Locale(System.getProperty("user.language"), System.getProperty("user.country")); //$NON-NLS-1$ //$NON-NLS-2$
	public static final LocalizationData DEFAULT = new LocalizationData("net.astesana.ajlib.Resources"); //$NON-NLS-1$
	
	private ResourceBundle bundle;
	private boolean translatorMode;
		
	public LocalizationData (String bundlePath) {
		translatorMode = false;
		ResourceBundle res = ResourceBundle.getBundle(bundlePath); //$NON-NLS-1$
		setBundle(res);
	}
	
	private void setBundle(ResourceBundle aBundle) {
		bundle = aBundle;
	}
	
	public String getString(String key) {
		return translatorMode?key:bundle.getString(key);
	}
	
	public char getChar(String key) {
		return getString(key).charAt(0);
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public DecimalFormat getCurrencyInstance() {
		return (DecimalFormat) NumberFormat.getCurrencyInstance(getLocale());
	}
	
	public void setTranslatorMode(boolean translatorMode) {
		this.translatorMode = translatorMode; 
	}
}
