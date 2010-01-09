package net.yapbam.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.UIManager;

import net.yapbam.budget.BudgetPlugin;
import net.yapbam.gui.administration.AdministrationPlugIn;
import net.yapbam.gui.graphics.balancehistory.BalanceHistoryPlugIn;
import net.yapbam.gui.statistics.StatisticsPlugin;
import net.yapbam.gui.tools.ToolsPlugIn;
import net.yapbam.gui.transactiontable.TransactionsPlugIn;
import net.yapbam.util.Crypto;
import net.yapbam.util.Portable;

/** This class represents the Yabpam application preferences */
public class Preferences {
	private static final String LANGUAGE = "lang"; //$NON-NLS-1$
	private static final String COUNTRY = "country"; //$NON-NLS-1$
	private static final String LANGUAGE_DEFAULT_VALUE = "default"; //$NON-NLS-1$
	private static final String COUNTRY_DEFAULT_VALUE = "default"; //$NON-NLS-1$
	private static final String LOOK_AND_FEEL = "look"; //$NON-NLS-1$
	private static final String LOOK_AND_FEEL_JAVA_VALUE = "java"; //$NON-NLS-1$
	private static final String LOOK_AND_FEEL_CUSTOM_VALUE = "custom"; //$NON-NLS-1$
	private static final String PROXY = "proxy"; //$NON-NLS-1$
	private static final String PROXY_AUTHENTICATION = "proxy_pass"; //$NON-NLS-1$
	private static final String AUTO_UPDATE_PERIOD = "auto_update_period"; //$NON-NLS-1$
	private static final String AUTO_UPDATE_SILENT_FAIL	= "auto_update_silent_fail"; //$NON-NLS-1$
	private static final String EXPERT_MODE = "expert_mode"; //$NON-NLS-1$
	private static final String KEY = "6a2a46e94506ebc3957df475e1da7f78"; //$NON-NLS-1$

	/** The Preference instance.
	 * This class is a singleton. All preferences can be accessed through this constant.
	 */
	public static final Preferences INSTANCE = new Preferences();
	
	private Properties properties;
	private boolean firstRun;
	private boolean translatorMode;

	private Preferences() {
		this.properties = new Properties();
		this.firstRun = true;
		if (getFile().exists()) {
			try {
				this.firstRun = false;
				properties.load(new FileInputStream(getFile()));
			    setAuthentication();
			} catch (Throwable e) {
				// If there's another error, maybe it would be better to do something else //TODO
			}
		} else {
			// On the first run, the file doesn't exist
			setToDefault();
			save();
		}
	}

	private static File getFile() {
		return new File (Portable.getLaunchDirectory(), ".yapbampref"); //$NON-NLS-1$
	}
	
	private void setToDefault() {
		this.properties.clear();
		this.properties.put(LANGUAGE, LANGUAGE_DEFAULT_VALUE);
		this.properties.put(COUNTRY, COUNTRY_DEFAULT_VALUE);
		this.properties.put(LOOK_AND_FEEL, LOOK_AND_FEEL_CUSTOM_VALUE);
	}
	
	/** Gets whether it is the first time Yapbam is launched on this machine or not.
	 * @return true if Yapbam is launched for the first time.
	 */
	public boolean isFirstRun() {
		return this.firstRun;
	}

	void save() {
		try {
			properties.store(new FileOutputStream(getFile()), "Yapbam preferences"); //$NON-NLS-1$
		} catch (IOException e) {
			//TODO What could we do ?
		}
	}

	/** Get the preferred locale
	 * @return the preferred locale
	 */
	public Locale getLocale() {
		String lang = this.properties.getProperty(LANGUAGE);
		if (lang.equalsIgnoreCase(LANGUAGE_DEFAULT_VALUE)) lang = Locale.getDefault().getLanguage();
		String country = this.properties.getProperty(COUNTRY);
		if (country.equalsIgnoreCase(COUNTRY_DEFAULT_VALUE)) country = Locale.getDefault().getCountry();
		return new Locale(lang, country);
	}
	
	/** @return true if the preferred country is the OS default.
	 * If the user explicitly choose a country which is the same as the OS default, this method returns false.
	 */
	public boolean isDefaultCountry() {
		return COUNTRY_DEFAULT_VALUE.equalsIgnoreCase((String) this.properties.get(COUNTRY));
	}
	
	/** @return true if the preferred language is the OS default.
	 * If the user explicitly choose a language which is the same as the OS default, this method returns false.
	 */
	public boolean isDefaultLanguage() {
		return LANGUAGE_DEFAULT_VALUE.equalsIgnoreCase((String) this.properties.get(LANGUAGE));
	}
	
	/** Get the preferred look and feel.
	 * @return the name of the look and feel class
	 */
	public String getLookAndFeel() {
		String value = this.properties.getProperty(LOOK_AND_FEEL);
		if (value.equalsIgnoreCase(LOOK_AND_FEEL_JAVA_VALUE)) return UIManager.getCrossPlatformLookAndFeelClassName(); 
		return UIManager.getSystemLookAndFeelClassName();
	}

	public void setLocale(Locale locale, boolean defaultCountry, boolean defaultLanguage) {
		this.properties.put(LANGUAGE, defaultLanguage?LANGUAGE_DEFAULT_VALUE:locale.getLanguage());
		this.properties.put(COUNTRY, defaultCountry?COUNTRY_DEFAULT_VALUE:locale.getCountry());
	}
	
	public void setJavaLookAndFeel(boolean java) {
		this.properties.put(LOOK_AND_FEEL, java?LOOK_AND_FEEL_JAVA_VALUE:LOOK_AND_FEEL_CUSTOM_VALUE);
	}
	
	public boolean isJavaLookAndFeel() {
		return this.properties.get(LOOK_AND_FEEL).equals(LOOK_AND_FEEL_JAVA_VALUE);
	}
	
	public String getHttpProxyHost() {
		String property = properties.getProperty(PROXY);
		if (property==null) return null;
		return new StringTokenizer(property,":").nextToken();
	}
	
	public int getHttpProxyPort() {
		String property = properties.getProperty(PROXY);
		if (property==null) return -1;
		StringTokenizer tokens = new StringTokenizer(property,":");
		tokens.nextToken();
		return Integer.parseInt(tokens.nextToken());
	}

	public Proxy getHttpProxy() throws UnknownHostException {
		String property = getHttpProxyHost();
		if (property==null) return Proxy.NO_PROXY;
		InetAddress host = InetAddress.getByName(property);
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Preferences.INSTANCE.getHttpProxyPort()));
	}

	public String getHttpProxyUser() {
		try {
			String property = this.properties.getProperty(PROXY_AUTHENTICATION);
			if (property==null) return null;
			return new StringTokenizer(Crypto.decrypt(KEY,property),":").nextToken();
		} catch (RuntimeException e) {
			return null; //TODO log the exception
		}
	}
	
	public String getHttpProxyPassword() {
		try {
			String property = this.properties.getProperty(PROXY_AUTHENTICATION);
			if (property==null) return null;
			StringTokenizer tokens = new StringTokenizer(Crypto.decrypt(KEY,property),":");
			tokens.nextToken();
			return tokens.nextToken();
		} catch (RuntimeException e) {
			return null; //TODO log the exception
		}
	}
	
	public void setHttpProxy(String proxyHost, int proxyPort, String user, String password) {
		if (proxyHost==null) {
			this.properties.remove(PROXY);
	        user=null;
		} else {
			this.properties.put(PROXY, proxyHost+":"+proxyPort);
		}
		if (user==null) {
	        Authenticator.setDefault(null);
	        this.properties.remove(PROXY_AUTHENTICATION);
		} else {
		    this.properties.setProperty(PROXY_AUTHENTICATION, Crypto.encrypt(KEY,user+":"+password));
		    setAuthentication();
		}
	}
	
	private void setAuthentication() {
		String property = this.properties.getProperty(PROXY_AUTHENTICATION);
		if (property==null) {
			Authenticator.setDefault(null);
		} else {
			StringTokenizer tokens = new StringTokenizer(Crypto.decrypt(KEY,property),":");
			final String user = tokens.nextToken();
			final String pwd = tokens.nextToken();
		    Authenticator.setDefault(new Authenticator() {
		        protected PasswordAuthentication getPasswordAuthentication() {
		          return new PasswordAuthentication(user,pwd.toCharArray());
		      }});
		}
	}

	public int getAutoUpdatePeriod() {
		try {
			return Integer.parseInt(this.properties.getProperty(AUTO_UPDATE_PERIOD));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public boolean getAutoUpdateSilentFail() {
		try {
			return Boolean.parseBoolean(this.properties.getProperty(AUTO_UPDATE_SILENT_FAIL));
		} catch (Exception e) {
			return false;
		}
	}
	
	/** Set the autoupdate period
	 * @param days number of days between two new versions checks (a negative number means "no auto check")
	 * @param silentFail true if a communication problem during auto check may be silent. false to have an error dialog displayed.
	 */
	public void setAutoUpdate(int days, boolean silentFail) {
		this.properties.setProperty(AUTO_UPDATE_PERIOD, Integer.toString(days));
		this.properties.setProperty(AUTO_UPDATE_SILENT_FAIL, Boolean.toString(silentFail));
	}

	static Class<AbstractPlugIn>[] getPlugins() {
		return new Class[]{TransactionsPlugIn.class, BalanceHistoryPlugIn.class, StatisticsPlugin.class, ToolsPlugIn.class, BudgetPlugin.class, AdministrationPlugIn.class};
	}

	/** Gets the expert mode.
	 * @return true if the expert mode is on.
	 */
	public boolean isExpertMode() {
		try {
			return Boolean.parseBoolean(this.properties.getProperty(EXPERT_MODE));
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isTranslatorMode() {
		return translatorMode;
	}
	
	/** Sets the translator mode.
	 * In translator mode, all the wording should (plugins are responsible for this to be achieved) be replaced
	 * in the GUI by their key in the translation files.
	 * Please note that this mode is not saved and is reset to false when the application quits.
	 * @param translatorMode true to set the translation mode on
	 */
	public void setTranslatorMode(boolean translatorMode) {
		this.translatorMode = translatorMode;
	}
	
	/** Sets a property value.
	 * This method may be used by plugin in order to save their preferences.
	 * As we have to prevent one plugin to overide the key used by other plugins, it is recommended
	 * that the key is prefixed with the package name of the plugin.
	 * @param key the key that will be used by getProperty method to retrieve the property
	 * @param value the property's value.
	 * @see #getProperty(String)
	 */
	public void setProperty (String key, String value) {
		this.properties.setProperty(key, value);
	}
	
	/** Gets a property's value.
	 * @param key the key of the property.
	 * @return the property's value or null if the key doesn't exists.
	 */
	public String getProperty (String key) {
		return this.properties.getProperty(key);
	}
}
