package net.yapbam.gui;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.yapbam.gui.accountsummary.AccountsSummaryPlugin;
import net.yapbam.gui.administration.AdministrationPlugIn;
import net.yapbam.gui.budget.BudgetPlugin;
import net.yapbam.gui.graphics.balancehistory.BalanceHistoryPlugIn;
import net.yapbam.gui.preferences.BackupOptions;
import net.yapbam.gui.preferences.EditingOptions;
import net.yapbam.gui.preferences.StartStateOptions;
import net.yapbam.gui.statementview.StatementViewPlugin;
import net.yapbam.gui.statistics.StatisticsPlugin;
import net.yapbam.gui.tools.ToolsPlugIn;
import net.yapbam.gui.transactiontable.TransactionsPlugIn;
import net.yapbam.gui.welcome.WelcomePlugin;
import net.yapbam.util.Crypto;
import net.yapbam.util.FileUtils;
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
	private static final String WELCOME_DIALOG_ALLOWED = "welcome_dialog_enabled"; //$NON-NLS-1$
	private static final String CRASH_REPORT_ACTION = "crash_report_action"; //$NON-NLS-1$
	private static final String KEY = "6a2a46e94506ebc3957df475e1da7f78"; //$NON-NLS-1$
	private static final String PREF_PREFIX = "TransactionEditing."; //$NON-NLS-1$
	private static final String DELETE_ALERT = "alertOnDelete"; //$NON-NLS-1$
	private static final String MODIFY_CHECKED_ALERT = "alertOnModifyChecked"; //$NON-NLS-1$
	private static final String AUTO_FILL_STATEMENT = "autoFillStatement"; //$NON-NLS-1$
	private static final String SET_DUPLICATE_TRANSACTION_DATE_TO_CURRENT = "setDuplicateTransactionDateToCurrent"; //$NON-NLS-1$
	private static final String DATE_BASED_AUTO_STATEMENT = "dateBasedAutoStatement"; //$NON-NLS-1$
	private static final String AUTO_STATEMENT_FORMAT = "statementDateFormat"; //$NON-NLS-1$
	private static final String PREF_START_PREFIX = "StartState.remember."; //$NON-NLS-1$
	private static final String FILE = "file"; //$NON-NLS-1$
	private static final String FILTER = "filter"; //$NON-NLS-1$
	private static final String TABS_ORDER = "tabsOrder"; //$NON-NLS-1$
	private static final String COLUMNS_WIDTH = "columnsWidth"; //$NON-NLS-1$
	private static final String COLUMNS_ORDER = "columnsOrder"; //$NON-NLS-1$
	private static final String HIDDEN_COLUMNS = "hiddenColumns"; //$NON-NLS-1$
	private static final String PREF_BACKUP_PREFIX = "backup.";
	private static final String ENABLED = "enabled";
	private static final String COMPRESSED = "compressed";
	private static final String SPACE_LIMIT = "spaceLimit";
	private static final String URi = "uri";
	private static final String FTP_ACCOUNT = "ftpAccount";
	
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
				FileInputStream inStream = new FileInputStream(getFile());
				try {
					properties.load(inStream);
				} finally {
					inStream.close();
				}
				setAuthentication();
			} catch (Throwable e) {
				// If there's an error, maybe it would be better to display a specific message
				ErrorManager.INSTANCE.log(null, e); //TODO
			}
		} else {
			// On the first run, the file doesn't exist
			setToDefault();
			try {
				save();
			} catch (IOException e) {
				// If there's an error, maybe it would be better to display a specific message
				ErrorManager.INSTANCE.log(null, e); //TODO
			}
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

	void save() throws IOException {
		File file = getFile();
		file.getParentFile().mkdirs();
		FileOutputStream out = FileUtils.getHiddenCompliantStream(file);
		try {
			properties.store(out, "Yapbam preferences"); //$NON-NLS-1$
		} finally {
			out.close();
		}
	}

	/** Get the preferred locale
	 * @return the preferred locale
	 */
	public Locale getLocale() {
		String lang = this.properties.getProperty(LANGUAGE);
		if (lang.equalsIgnoreCase(LANGUAGE_DEFAULT_VALUE)) lang = LocalizationData.SYS_LOCALE.getLanguage();
		String country = this.properties.getProperty(COUNTRY);
		if (country.equalsIgnoreCase(COUNTRY_DEFAULT_VALUE)) country = LocalizationData.SYS_LOCALE.getCountry();
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
	 * <br>If the preferred l&f is not installed, it returns the default look and feel.
	 * @return the name of the preferred look and feel class
	 */
	public String getLookAndFeel() {
		String value = this.properties.getProperty(LOOK_AND_FEEL);
		if (LOOK_AND_FEEL_JAVA_VALUE.equalsIgnoreCase(value)) {
			// Versions before 0.7.4 used LOOK_AND_FEEL_JAVA_VALUE and LOOK_AND_FEEL_CUSTOM_VALUE to code the look and feel
			value = getLookAndFeelName(UIManager.getCrossPlatformLookAndFeelClassName());
		} else if (LOOK_AND_FEEL_CUSTOM_VALUE.equalsIgnoreCase(value)) {
			value = getLookAndFeelName(UIManager.getSystemLookAndFeelClassName());
		}
		if (value==null) {
			value = "Nimbus"; // This is the default Yapbam L&F //$NON-NLS-1$
		}
		return value;
	}
	
	private String getLookAndFeelName(String className) {
		LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
		for (LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels) {
			// Prior the 0.9.8, the class name were used instead of the generic name.
			// It caused problem when changing java version (ie: Nimbus in java 1.6 was implemented by a class in com.sun.etc and in javax.swing in java 1.7)
			if (lookAndFeelInfo.getClassName().equals(className)) return lookAndFeelInfo.getName();
		}
		return null;
	}

	public void setLookAndFeel(String lookAndFeelName) {
		this.properties.put(LOOK_AND_FEEL, lookAndFeelName);
	}
	
	public String getHttpProxyHost() {
		String property = properties.getProperty(PROXY);
		if (property==null) return null;
		return new StringTokenizer(property,":").nextToken(); //$NON-NLS-1$
	}
	
	public int getHttpProxyPort() {
		String property = properties.getProperty(PROXY);
		if (property==null) return -1;
		StringTokenizer tokens = new StringTokenizer(property,":"); //$NON-NLS-1$
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
		return new StringTokenizer(Crypto.decrypt(KEY, property), ":").nextToken(); //$NON-NLS-1$
	}
	
	public String getHttpProxyPassword() {
		String property = this.properties.getProperty(PROXY_AUTHENTICATION);
		if (property == null) return null;
		StringTokenizer tokens = new StringTokenizer(Crypto.decrypt(KEY, property), ":"); //$NON-NLS-1$
		tokens.nextToken();
		return tokens.nextToken();
	}
	
	public void setHttpProxy(String proxyHost, Integer proxyPort, String user, String password) {
		if (proxyHost == null) {
			this.properties.remove(PROXY);
			user = null;
		} else {
			this.properties.put(PROXY, proxyHost + ":" + proxyPort); //$NON-NLS-1$
		}
		if (user == null) {
			Authenticator.setDefault(null);
			this.properties.remove(PROXY_AUTHENTICATION);
		} else {
			this.properties.setProperty(PROXY_AUTHENTICATION, Crypto.encrypt(KEY, user + ":" + password)); //$NON-NLS-1$
			setAuthentication();
		}
	}
	
	private void setAuthentication() {
		String property = this.properties.getProperty(PROXY_AUTHENTICATION);
		if (property==null) {
			Authenticator.setDefault(null);
		} else {
			StringTokenizer tokens = new StringTokenizer(Crypto.decrypt(KEY,property),":"); //$NON-NLS-1$
			final String user = tokens.nextToken();
			final String pwd = tokens.nextToken();
		    Authenticator.setDefault(new Authenticator() {
		        @Override
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
		File file = new File(Portable.getDataDirectory(),"plugins"); //$NON-NLS-1$
		if (!file.exists()) {
			if (!file.mkdirs()) {
				ErrorManager.INSTANCE.display(null, new RuntimeException("unable to create the plugins folder")); //$NON-NLS-1$
			}
		} else if (!file.isDirectory()) {
			ErrorManager.INSTANCE.display(null, new RuntimeException("./plugins is not a directory")); //$NON-NLS-1$
		}
		final List<PlugInContainer> plugins = new ArrayList<PlugInContainer>();
		plugins.add(new PlugInContainer(WelcomePlugin.class));
		plugins.add(new PlugInContainer(TransactionsPlugIn.class));
		plugins.add(new PlugInContainer(BalanceHistoryPlugIn.class));
		plugins.add(new PlugInContainer(StatementViewPlugin.class));
		plugins.add(new PlugInContainer(StatisticsPlugin.class));
		plugins.add(new PlugInContainer(BudgetPlugin.class));
		plugins.add(new PlugInContainer(AccountsSummaryPlugin.class));
		plugins.add(new PlugInContainer(ToolsPlugIn.class));
		plugins.add(new PlugInContainer(AdministrationPlugIn.class));
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (!file.getName().endsWith(".jar")) return false; //$NON-NLS-1$
				plugins.add(new PlugInContainer(file));
				return true;
			}
		});
		String testedPlugin = System.getProperty("testedPlugin.className"); //$NON-NLS-1$
		if (testedPlugin!=null) {
			try {
				Class<? extends AbstractPlugIn> pClass = (Class<? extends AbstractPlugIn>) Class.forName(testedPlugin);
				plugins.add(new PlugInContainer(pClass));
			} catch (Exception e) {
				ErrorManager.INSTANCE.display(null, new RuntimeException("unable to load the plugin "+testedPlugin+" ("+e+")")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		return plugins.toArray(new PlugInContainer[plugins.size()]);
	}

	/** Gets the expert mode.
	 * @return true if the expert mode is on.
	 */
	public boolean isExpertMode() {
		return getBoolean(EXPERT_MODE, false);
	}

	private boolean getBoolean(String key, boolean defaultValue) {
		try {
			String property = this.properties.getProperty(key);
			return property==null?defaultValue:Boolean.parseBoolean(property);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	private void setBoolean(String key, boolean value) {
		Preferences pref = Preferences.INSTANCE;
		pref.setProperty(key, Boolean.toString(value));
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

	/** Removes a property.
	 * @param key The propertyToBeRemoved
	 */
	public void removeProperty(String key) {
		this.properties.remove(key);
	}

	/** Gets the action to do when a crash is detected.
	 * @return 0 if the user should be asked, -1 to ignore, 1 to send a crash report to Yapbam
	 */
	public int getCrashReportAction() {
		try {
			return Integer.parseInt(this.properties.getProperty(CRASH_REPORT_ACTION,"0")); //$NON-NLS-1$
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
	
	private EditingOptions editingOptions;
	public void setEditingOptions(EditingOptions edit) {
		this.editingOptions = edit;
		setBoolean(PREF_PREFIX+DELETE_ALERT, edit.isAlertOnDelete());
		setBoolean(PREF_PREFIX+MODIFY_CHECKED_ALERT, edit.isAlertOnModifyChecked());
		setBoolean(PREF_PREFIX+SET_DUPLICATE_TRANSACTION_DATE_TO_CURRENT, edit.isDuplicateTransactionDateToCurrent());
		setBoolean(PREF_PREFIX+AUTO_FILL_STATEMENT, edit.isAutoFillStatement());
		setBoolean(PREF_PREFIX+DATE_BASED_AUTO_STATEMENT, edit.isDateBasedAutoStatement());
		this.properties.setProperty(PREF_PREFIX+AUTO_STATEMENT_FORMAT, edit.getStatementDateFormat().toPattern());
	}
	
	public EditingOptions getEditingOptions() {
		if (editingOptions==null) {
			String defaultPattern = "yyyyMM";
			String pattern = this.properties.getProperty(PREF_PREFIX+AUTO_STATEMENT_FORMAT, defaultPattern);
			SimpleDateFormat format = new SimpleDateFormat(defaultPattern);
			try {
				format = new SimpleDateFormat(pattern, getLocale());
			} catch (Exception e) {}
			editingOptions = new EditingOptions(
				getBoolean(PREF_PREFIX+DELETE_ALERT, true), getBoolean(PREF_PREFIX+MODIFY_CHECKED_ALERT, true),
				getBoolean(PREF_PREFIX+SET_DUPLICATE_TRANSACTION_DATE_TO_CURRENT, true),
				getBoolean(PREF_PREFIX+AUTO_FILL_STATEMENT, false), getBoolean(PREF_PREFIX+DATE_BASED_AUTO_STATEMENT, false),
				format);
		}
		return this.editingOptions;
	}

	private StartStateOptions startStateOptions;
	public void setStartStateOptions(StartStateOptions options) {
		this.startStateOptions = options;
		setBoolean(PREF_START_PREFIX+FILE, options.isRememberFile());
		setBoolean(PREF_START_PREFIX+FILTER, options.isRememberFilter());
		setBoolean(PREF_START_PREFIX+TABS_ORDER, options.isRememberTabsOrder());
		setBoolean(PREF_START_PREFIX+COLUMNS_WIDTH, options.isRememberColumnsWidth());
		setBoolean(PREF_START_PREFIX+COLUMNS_ORDER, options.isRememberColumnsOrder());
		setBoolean(PREF_START_PREFIX+HIDDEN_COLUMNS, options.isRememberHiddenColumns());
	}

	public StartStateOptions getStartStateOptions() {
		if (startStateOptions==null) {
			startStateOptions = new StartStateOptions(getBoolean(PREF_START_PREFIX+FILE, true), getBoolean(PREF_START_PREFIX+FILTER, false),
				getBoolean(PREF_START_PREFIX+TABS_ORDER, true),	getBoolean(PREF_START_PREFIX+COLUMNS_WIDTH, true),	getBoolean(PREF_START_PREFIX+COLUMNS_ORDER, true),	getBoolean(PREF_START_PREFIX+HIDDEN_COLUMNS, true));
		}
		return this.startStateOptions;
	}
	
	private BackupOptions backupOptions;
	public void setBackupOptions(BackupOptions options) {
		this.backupOptions = options;
		setBoolean(PREF_BACKUP_PREFIX+ENABLED, options.isEnabled());
		setBoolean(PREF_BACKUP_PREFIX+COMPRESSED, options.isCompressed());
		setProperty(PREF_BACKUP_PREFIX+SPACE_LIMIT, options.getSpaceLimit()==null?"none":options.getSpaceLimit().toString());
		URI uri = options.getUri();
		if (uri==null) {
			removeProperty(PREF_BACKUP_PREFIX+URi);
			removeProperty(PREF_BACKUP_PREFIX+FTP_ACCOUNT);
		} else {
			// To preserve a relative secret on the user login and password to an ftp account, we will
			// store these information crypted in a separate property.
			if (uri.getRawUserInfo()==null) {
				removeProperty(PREF_BACKUP_PREFIX+FTP_ACCOUNT);
			} else {
				setProperty(PREF_BACKUP_PREFIX+FTP_ACCOUNT, Crypto.encrypt(KEY, uri.getRawUserInfo()));
			}
			try {
				uri = new URI(uri.getScheme(),"",uri.getHost(),uri.getPort(),uri.getPath(),uri.getQuery(),uri.getFragment());
				setProperty(PREF_BACKUP_PREFIX+URi, uri.toString());
			} catch (URISyntaxException e) {
				// I can't see a reason why this could happen !
				ErrorManager.INSTANCE.log(null, e);
			}
		}
	}

	public BackupOptions getBackupOptions() {
		if (backupOptions==null) {
			String uriStr = getProperty(PREF_BACKUP_PREFIX+URi);
			URI uri = Portable.getBackupDirectory().toURI();
			if (uriStr!=null) {
				try {
					uri = new URI(uriStr);
					String userInfo = getProperty(PREF_BACKUP_PREFIX+FTP_ACCOUNT);
					if (userInfo!=null) {
						uri = new URI(uri.getScheme(),Crypto.decrypt(KEY, userInfo),uri.getHost(),uri.getPort(),uri.getPath(),uri.getQuery(),uri.getFragment());
					}
				} catch (URISyntaxException e) {
					// Hum ... the URI in the preference file is invalid ... maybe somebody has edit it by hand
					// Do nothing, it strange ... the best is to ignore it and use the default value
				}
			}
			String limitStr = getProperty(PREF_BACKUP_PREFIX+SPACE_LIMIT);
			BigInteger limit = new BigInteger("10"); // The default space limit
			try {
				if (limitStr!=null) limit = "none".equalsIgnoreCase(limitStr) ? null : new BigInteger(limitStr);
			} catch (NumberFormatException e) {
				// Same thing as for the URI ... if it is wrong, we ignore and use the default value
			}
			backupOptions = new BackupOptions(getBoolean(PREF_BACKUP_PREFIX+ENABLED, true), uri, getBoolean(PREF_BACKUP_PREFIX+COMPRESSED, true),	limit);
		}
		return this.backupOptions;
	}
}
