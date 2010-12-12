package net.yapbam.gui;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

import net.yapbam.data.GlobalData;

/** This class is the main entry point for localization concerns.
 */
public abstract class LocalizationData {
	private static ResourceBundle bundle;
	private static Locale locale;
	private static boolean translatorMode;
	
	static {
		reset();
	}
	
	public static void reset() {
		locale = Preferences.INSTANCE.getLocale();
		GlobalData.setDefaultCurrency(Currency.getInstance(locale));
		translatorMode = Preferences.INSTANCE.isTranslatorMode();
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
//		System.out.println(document);
//		URL[] urls = ((URLClassLoader)LocalizationData.class.getClassLoader()).getURLs();
//		System.out.println ("urls :"+Arrays.asList(urls));
		
		URL url = LocalizationData.class.getResource("/localization/"+getLocale().getLanguage()+"/"+document);
//		if (url!=null) {System.out.println("ok 1"); return url;}
		if (url==null) url = LocalizationData.class.getResource("/localization/"+document);
//		if (url!=null) {System.out.println("ok 2"); return url;}
//		url = LocalizationData.class.getResource("../../../localization/"+getLocale().getLanguage()+"/"+document);
//		if (url!=null) {System.out.println("ok 3"); return url;}
//		if (url==null) url = LocalizationData.class.getResource("../../../localization/"+document);
//		if (url!=null) System.out.println("ok 4");
		return url;
	}
}
