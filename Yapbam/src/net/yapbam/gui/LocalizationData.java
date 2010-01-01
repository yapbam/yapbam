package net.yapbam.gui;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

/** This class is the main entry point for localization concerns.
 */
public abstract class LocalizationData {
	private static ResourceBundle bundle;
	private static Locale locale;
	private static double currencyPrecision;
	private static boolean translatorMode;
	
	static {
		reset();
	}
	
	public static void reset() {
		locale = Preferences.INSTANCE.getLocale();
		translatorMode = Preferences.INSTANCE.isTranslatorMode();
		currencyPrecision = Math.pow(10, -Currency.getInstance(locale).getDefaultFractionDigits())/2;		
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
		return translatorMode?key:bundle.getString(key);
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
	
	/** As amount are represented by doubles, and doubles are unable to represent exactly decimal numbers,
	 * we have to take care when we compare two amounts.
	 * This method compares to amounts according to the currency.
	 * @param amount1 first amount to compare.
	 * @param amount2 second amount to compare.
	 * @return true if the two amount are the same for the current locale's currency.
	 */
	public static boolean areEqualsCurrenciesAmounts (double amount1, double amount2) {
		return (Math.abs(amount1-amount2)<currencyPrecision);
	}
}
