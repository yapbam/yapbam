package net.yapbam.gui;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.yapbam.gui.administration.AdministrationPlugIn;
import net.yapbam.gui.budget.BudgetPlugin;
import net.yapbam.gui.graphics.balancehistory.BalanceHistoryPlugIn;
import net.yapbam.gui.statementview.StatementViewPlugin;
import net.yapbam.gui.statistics.StatisticsPlugin;
import net.yapbam.gui.tools.ToolsPlugIn;
import net.yapbam.gui.transactiontable.TransactionsPlugIn;
import net.yapbam.gui.welcome.WelcomePlugin;
import net.yapbam.util.Crypto;
import net.yapbam.util.Portable;

/** This class represents the Yapbam application preferences */
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
	private static final String AUTO_UPDATE_INSTALL	= "auto_update_install"; //$NON-NLS-1$
	private static final String EXPERT_MODE = "expert_mode"; //$NON-NLS-1$
	private static final String WELCOME_DIALOG_ALLOWED = "welcome_dialog_enabled";
	private static final String CRASH_REPORT_ACTION = "crash_report_action";
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
				// If there's an error, maybe it would be better to do something
				//TODO
				e.printStackTrace();
			}
		} else {
			// On the first run, the file doesn't exist
			setToDefault();
			save();
		}
	}

	static File getFile() {
		return new File (Portable.getDataDirectory(), ".yapbampref"); //$NON-NLS-1$
	}
	
	private void setToDefault() {
		this.properties.clear();
		this.properties.put(LANGUAGE, LANGUAGE_DEFAULT_VALUE);
		this.properties.put(COUNTRY, COUNTRY_DEFAULT_VALUE);
	}
	
	/** Gets whether it is the first time Yapbam is launched on this machine or not.
	 * @return true if Yapbam is launched for the first time.
	 */
	public boolean isFirstRun() {
		return this.firstRun;
	}

	void save() {
		try {
			File file = getFile();
			file.getParentFile().mkdirs();
			properties.store(new FileOutputStream(file), "Yapbam preferences"); //$NON-NLS-1$
		} catch (IOException e) {
			//TODO What could we do ?
			e.printStackTrace();
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
	
	public void setLocale(Locale locale, boolean defaultCountry, boolean defaultLanguage) {
		this.properties.put(LANGUAGE, defaultLanguage?LANGUAGE_DEFAULT_VALUE:locale.getLanguage());
		this.properties.put(COUNTRY, defaultCountry?COUNTRY_DEFAULT_VALUE:locale.getCountry());
	}
	
	/** Gets the preferred look and feel.
	 * <BR>This method guarantees that the returned l&f is installed in this JVM.
	 * If the preferred l&f is not installed, it returns the system look and feel.
	 * @return the name of the preferred look and feel class
	 */
	public String getLookAndFeel() {
		String value = this.properties.getProperty(LOOK_AND_FEEL);
		if (value==null) {
			value = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"; // This is the default Yapbam L&F
		} else if (value.equalsIgnoreCase(LOOK_AND_FEEL_JAVA_VALUE)) {
			// Versions before 0.7.4 used LOOK_AND_FEEL_JAVA_VALUE and LOOK_AND_FEEL_CUSTOM_VALUE to code the look and feel
			return UIManager.getCrossPlatformLookAndFeelClassName();
		} else if (value.equalsIgnoreCase(LOOK_AND_FEEL_CUSTOM_VALUE)) {
			return UIManager.getSystemLookAndFeelClassName();
		}
		LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels) {
			if (lookAndFeelInfo.getClassName().equals(value)) return value;
		}
		return UIManager.getSystemLookAndFeelClassName();
	}

	public void setLookAndFeel(String lookAndFeelClassName) {
		this.properties.put(LOOK_AND_FEEL, lookAndFeelClassName);
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
		String property = this.properties.getProperty(PROXY_AUTHENTICATION);
		if (property == null) return null;
		return new StringTokenizer(Crypto.decrypt(KEY, property), ":").nextToken();
	}
	
	public String getHttpProxyPassword() {
		String property = this.properties.getProperty(PROXY_AUTHENTICATION);
		if (property == null) return null;
		StringTokenizer tokens = new StringTokenizer(Crypto.decrypt(KEY, property), ":");
		tokens.nextToken();
		return tokens.nextToken();
	}
	
	public void setHttpProxy(String proxyHost, int proxyPort, String user, String password) {
		if (proxyHost == null) {
			this.properties.remove(PROXY);
			user = null;
		} else {
			this.properties.put(PROXY, proxyHost + ":" + proxyPort);
		}
		if (user == null) {
			Authenticator.setDefault(null);
			this.properties.remove(PROXY_AUTHENTICATION);
		} else {
			this.properties.setProperty(PROXY_AUTHENTICATION, Crypto.encrypt(KEY, user + ":" + password));
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

	/** Tests whether the welcome screen is allowed or not.
	 * @return true is the welcome screen may pop up every time yapbam is launched.
	 */
	public boolean isWelcomeAllowed() {
		String property = this.properties.getProperty(WELCOME_DIALOG_ALLOWED);
		if (property==null) return true;
		try {
			return Boolean.parseBoolean(property);
		} catch (Exception e) {
			return true;
		}
	}
	
	public void setWelcomeAllowed(boolean allowed) {
		this.properties.setProperty(WELCOME_DIALOG_ALLOWED, Boolean.toString(allowed));
	}
	
	/** Gets the automatic check for updates period.
	 * @return the number of days between two checks, a negative number if there's no automatic check.
	 */
	public int getAutoUpdatePeriod() {
		try {
			return Integer.parseInt(this.properties.getProperty(AUTO_UPDATE_PERIOD));
		} catch (Exception e) {
			return 0;
		}
	}
	
	public boolean getAutoUpdateInstall() {
		try {
			return Boolean.parseBoolean(this.properties.getProperty(AUTO_UPDATE_INSTALL));
		} catch (Exception e) {
			return false;
		}
	}
	
	/** Sets the autoupdate instructions
	 * @param days number of days between two new versions checks (a negative number means "no auto check")
	 * @param autoInstall true if the update must be automatically installed.
	 */
	public void setAutoUpdate(int days, boolean autoInstall) {
		this.properties.setProperty(AUTO_UPDATE_PERIOD, Integer.toString(days));
		this.properties.setProperty(AUTO_UPDATE_INSTALL, Boolean.toString(autoInstall));
	}

	static PlugInContainer[] getPlugins() {
		File file = new File(Portable.getDataDirectory(),"plugins");
		if (!file.exists()) {
			if (!file.mkdirs()) {
				ErrorManager.INSTANCE.display(null, new RuntimeException("unable to create the plugins folder"));
			}
		} else if (!file.isDirectory()) {
			ErrorManager.INSTANCE.display(null, new RuntimeException("./plugins is not a directory"));
		}
		final List<PlugInContainer> plugins = new ArrayList<PlugInContainer>();
		plugins.add(new PlugInContainer(WelcomePlugin.class));
		plugins.add(new PlugInContainer(TransactionsPlugIn.class));
		plugins.add(new PlugInContainer(BalanceHistoryPlugIn.class));
		plugins.add(new PlugInContainer(StatisticsPlugin.class));
		plugins.add(new PlugInContainer(ToolsPlugIn.class));
		plugins.add(new PlugInContainer(BudgetPlugin.class));
		plugins.add(new PlugInContainer(StatementViewPlugin.class));
		plugins.add(new PlugInContainer(AdministrationPlugIn.class));
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (!file.getName().endsWith(".jar")) return false;
				plugins.add(new PlugInContainer(file));
				return true;
			}
		});
		String testedPlugin = System.getProperty("testedPlugin.className");
		if (testedPlugin!=null) {
			try {
				plugins.add(new PlugInContainer(Class.forName(testedPlugin)));
			} catch (ClassNotFoundException e) {
				ErrorManager.INSTANCE.display(null, new RuntimeException("unable to load the plugin "+testedPlugin+" ("+e+")"));
			}
		}
		return plugins.toArray(new PlugInContainer[plugins.size()]);
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

	/** Gets the action to do when a crash is detected.
	 * @return 0 if the user should be asked, -1 to ignore, 1 to send a crash report to Yapbam
	 */
	public int getCrashReportAction() {
		try {
			return Integer.parseInt(this.properties.getProperty(CRASH_REPORT_ACTION,"0"));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/** Sets the action to do when a crash is detected.
	 * @param action 0 if the user should be asked, -1 to ignore, 1 to send a crash report to Yapbam
	 * @throws IllegalArgumentException if the action is an invalid parameter (not one of the value listed above) 
	 */
	public void setCrashReportAction(int action) {
		if (Math.abs(action)>1) throw new IllegalArgumentException();
		this.properties.setProperty(CRASH_REPORT_ACTION,Integer.toString(action));
	}
}
