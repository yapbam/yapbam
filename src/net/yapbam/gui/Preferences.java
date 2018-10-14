package net.yapbam.gui;

import java.awt.Font;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.fathzer.soft.ajlib.swing.FontUtils;
import com.fathzer.soft.ajlib.utilities.FileUtils;
import com.fathzer.soft.ajlib.utilities.StringUtils;

import net.yapbam.gui.accountsummary.AccountsSummaryPlugin;
import net.yapbam.gui.administration.AdministrationPlugIn;
import net.yapbam.gui.archive.ArchivePlugin;
import net.yapbam.gui.budget.BudgetPlugin;
import net.yapbam.gui.graphics.balancehistory.BalanceHistoryPlugIn;
import net.yapbam.gui.preferences.EditingSettings;
import net.yapbam.gui.preferences.EditionWizardSettings;
import net.yapbam.gui.preferences.EditionWizardSettings.Source;
import net.yapbam.gui.preferences.StartStateSettings;
import net.yapbam.gui.preferences.EditionWizardSettings.Mode;
import net.yapbam.gui.recent.RecentFilesPlugin;
import net.yapbam.gui.statementview.StatementViewPlugin;
import net.yapbam.gui.statistics.StatisticsPlugin;
import net.yapbam.gui.tools.ToolsPlugIn;
import net.yapbam.gui.transactiontable.TransactionsPlugIn;
import net.yapbam.gui.transfer.TransferPlugin;
import net.yapbam.gui.util.LookAndFeelUtils;
import net.yapbam.gui.welcome.WelcomePlugin;
import net.yapbam.util.Crypto;
import net.yapbam.util.HttpsPatcherPlugin;
import net.yapbam.util.Portable;
import net.yapbam.util.PreferencesUtils;

/** This class represents the Yapbam application preferences */
public class Preferences {
	private static final String FONT_NAME = "Font.name"; //$NON-NLS-1$
	private static final String FONT_SIZE_RATIO = "Font.sizeRatio"; //$NON-NLS-1$
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
	private static final String EDITION_WIZARD_MODE = "fillAmount"; //$NON-NLS-1$
	private static final String EDITION_WIZARD_SOURCE = "fillAmountWith"; //$NON-NLS-1$
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
	private static final String ROW_SORTER_KEYS = "rowSorterKeys"; //$NON-NLS-1$
	private static final String COLUMNS_WIDTH = "columnsWidth"; //$NON-NLS-1$
	private static final String COLUMNS_ORDER = "columnsOrder"; //$NON-NLS-1$
	private static final String HIDDEN_COLUMNS = "hiddenColumns"; //$NON-NLS-1$
	
	/** The Preference instance.
	 * This class is a singleton. All preferences can be accessed through this constant.
	 */
	public static final Preferences INSTANCE;
	
	static {
		INSTANCE = new Preferences();
	}
	
	private Properties properties;
	private boolean firstRun;
	private boolean translatorMode;

	private Preferences() {
		this.properties = new Properties();
		
		try {
			load();
			if (!firstRun) {
				setAuthentication();
			} else {
				// On the first run, the file doesn't exist
				setToDefault();
				save();
			}
		} catch (Throwable e) {
			// If there's an error, maybe it would be better to display a specific message
			ErrorManager.INSTANCE.log(null, e); //TODO
		}
	}
	
	private static File getFile() {
		return new File (Portable.getDataDirectory(), ".yapbampref"); //$NON-NLS-1$
	}
	
	private void setToDefault() {
		this.properties.clear();
		put(LANGUAGE, LANGUAGE_DEFAULT_VALUE);
		put(COUNTRY, COUNTRY_DEFAULT_VALUE);
	}
	
	private void put(String key, String value) {
		PreferencesUtils.verifyPreferencesCompliance(key, value);
		this.properties.put(key, value);
	}
	
	/** Gets whether it is the first time Yapbam is launched on this machine or not.
	 * @return true if Yapbam is launched for the first time.
	 */
	public boolean isFirstRun() {
		return this.firstRun;
	}

	private void load() throws IOException {
		if (Portable.isPortable()) {
			this.firstRun = !getFile().exists();
			if (!firstRun) {
				FileInputStream inStream = new FileInputStream(getFile());
				try {
					properties.load(inStream);
				} finally {
					inStream.close();
				}
			}
		} else {
			java.util.prefs.Preferences prefs = getJavaPref();
			this.firstRun = PreferencesUtils.isEmpty(prefs);
			PreferencesUtils.fromPreferences(prefs, properties);
		}
	}

	public void save() throws IOException {
		if (Portable.isPortable()) {
			File file = getFile();
			file.getParentFile().mkdirs();
			FileOutputStream out = FileUtils.getHiddenCompliantStream(file);
			try {
				properties.store(out, "Yapbam preferences"); //$NON-NLS-1$
			} finally {
				out.close();
			}
		} else {
			PreferencesUtils.toPreferences(getJavaPref(), this.properties, true);
		}
	}

	private static java.util.prefs.Preferences getJavaPref() {
		return java.util.prefs.Preferences.userRoot().node("net.yapbam.prefs"); //$NON-NLS-1$
	}

	/** Gets the preferred locale
	 * @return the preferred locale
	 */
	public Locale getLocale() {
		String lang = this.properties.getProperty(LANGUAGE);
		if (LANGUAGE_DEFAULT_VALUE.equalsIgnoreCase(lang)) {
			lang = LocalizationData.SYS_LOCALE.getLanguage();
		}
		String country = this.properties.getProperty(COUNTRY);
		if (COUNTRY_DEFAULT_VALUE.equalsIgnoreCase(country)) {
			country = LocalizationData.SYS_LOCALE.getCountry();
		}
		return new Locale(lang, country);
	}
	
	static Locale safeGetLocale() {
		return INSTANCE!=null?INSTANCE.getLocale():LocalizationData.SYS_LOCALE;
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
		put(LANGUAGE, defaultLanguage?LANGUAGE_DEFAULT_VALUE:locale.getLanguage());
		put(COUNTRY, defaultCountry?COUNTRY_DEFAULT_VALUE:locale.getCountry());
	}
	
	/** Gets the preferred look and feel.
	 * <br>If the preferred l&f is not installed, it returns the default look and feel.
	 * @return the name of the preferred look and feel class
	 */
	public String getLookAndFeel() {
		String value = this.properties.getProperty(LOOK_AND_FEEL);
		value = LookAndFeelUtils.isValid(value)?value:null;
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
			if (lookAndFeelInfo.getClassName().equals(className)) {
				return lookAndFeelInfo.getName();
			}
		}
		return null;
	}

	public void setLookAndFeel(String lookAndFeelName) {
		put(LOOK_AND_FEEL, lookAndFeelName);
	}
	
	public String getHttpProxyHost() {
		String property = properties.getProperty(PROXY);
		if ((property==null) || (property.length()==0)) {
			return null;
		}
		int index = property.lastIndexOf(':');
		if (index<0) {
			return property;
		} else {
			return property.substring(0, index); 
		}
	}
	
	public int getHttpProxyPort() {
		String property = properties.getProperty(PROXY);
		if (property==null) {
			return -1;
		}
		String[] tokens = StringUtils.split(property, ':');
		try {
			return Integer.parseInt(tokens[tokens.length-1]);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public Proxy getHttpProxy() throws UnknownHostException {
		String property = getHttpProxyHost();
		if (property==null) {
			return Proxy.NO_PROXY;
		}
		InetAddress host = InetAddress.getByName(property);
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Preferences.INSTANCE.getHttpProxyPort()));
	}

	public String getHttpProxyUser() {
		String property = this.properties.getProperty(PROXY_AUTHENTICATION);
		if (property == null) {
			return null;
		}
		return new StringTokenizer(Crypto.decrypt(KEY, property), ":").nextToken(); //$NON-NLS-1$
	}
	
	public String getHttpProxyPassword() {
		String property = this.properties.getProperty(PROXY_AUTHENTICATION);
		if (property == null) {
			return null;
		}
		StringTokenizer tokens = new StringTokenizer(Crypto.decrypt(KEY, property), ":"); //$NON-NLS-1$
		tokens.nextToken();
		return tokens.nextToken();
	}
	
	public void setHttpProxy(String proxyHost, Integer proxyPort, String user, String password) {
		if (proxyHost == null) {
			this.properties.remove(PROXY);
			user = null;
		} else {
			put(PROXY, proxyHost + ":" + proxyPort); //$NON-NLS-1$
		}
		if (user == null) {
			Authenticator.setDefault(null);
			this.properties.remove(PROXY_AUTHENTICATION);
		} else {
			put(PROXY_AUTHENTICATION, Crypto.encrypt(KEY, user + ":" + password)); //$NON-NLS-1$
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
					return new PasswordAuthentication(user, pwd.toCharArray());
				}
			});
		}
	}

	/** Tests whether the welcome screen is allowed or not.
	 * @return true is the welcome screen may pop up every time yapbam is launched.
	 */
	public boolean isWelcomeAllowed() {
		String property = this.properties.getProperty(WELCOME_DIALOG_ALLOWED);
		if (property==null) {
			return true;
		}
		return Boolean.parseBoolean(property);
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
		} catch (NumberFormatException e) {
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
		// Load the default plugins
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
		plugins.add(new PlugInContainer(TransferPlugin.class));
		plugins.add(new PlugInContainer(RecentFilesPlugin.class));
		plugins.add(new PlugInContainer(ArchivePlugin.class));
		plugins.add(new PlugInContainer(HttpsPatcherPlugin.class));

		// Load additional plugins
		File file = new File(Portable.getDataDirectory(),"plugins"); //$NON-NLS-1$
		if (!file.exists()) {
			if (!file.mkdirs()) {
				// We are unable to create the plugin directory. It's probably because the installation directory is write protected (for example by windows UAC).
				// It doesn't matter
			}
		}
//		else if (!file.isDirectory()) {
//			ErrorManager.INSTANCE.display(null, null, MessageFormat.format("It seems your yapbam copy is corrupted.\n {0} is not a directory.",file.getAbsolutePath()));
//		}
		if (file.exists() && file.isDirectory()) {
			// Add the custom plugins
			file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (!file.getName().endsWith(".jar")) { //$NON-NLS-1$
						return false;
					}
					plugins.add(new PlugInContainer(file));
					return true;
				}
			});
		}
		
		// Load plugin under development
		String testedPlugin = System.getProperty("testedPlugin.className"); //$NON-NLS-1$
		if (testedPlugin!=null) {
			String[] testedPlugins = StringUtils.split(testedPlugin, ',');
			for (String className : testedPlugins) {
				if (className.length()!=0) {
					try {
						@SuppressWarnings("unchecked")
						Class<? extends AbstractPlugIn> pClass = (Class<? extends AbstractPlugIn>) Class.forName(className);
						plugins.add(new PlugInContainer(pClass));
					} catch (Exception e) {
						ErrorManager.INSTANCE.display(null, e, "Unable to load the plugin "+className); //$NON-NLS-1$
					}
				}
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
	
	public static boolean safeIsTranslatorMode() {
		return INSTANCE!=null?INSTANCE.isTranslatorMode():false;
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
	 * As we have to prevent one plugin to override the key used by other plugins, it is recommended
	 * that the key is prefixed with the package name of the plugin.
	 * @param key the key that will be used by getProperty method to retrieve the property
	 * @param value the property's value.
	 * @see #getProperty(String)
	 */
	public void setProperty (String key, String value) {
		PreferencesUtils.verifyPreferencesCompliance(key, value);
		this.properties.setProperty(key, value);
	}
	
	/** Gets a property's value.
	 * @param key the key of the property.
	 * @return the property's value or null if the key doesn't exists.
	 */
	public String getProperty (String key) {
		return this.properties.getProperty(key);
	}

	/** Gets a property's value.
	 * @param key the key of the property.
	 * @param defaultValue The default value of the property (if the key doesn't exist).
	 * @return the property's value or its default value if the key doesn't exists.
	 */
	public String getProperty(String key, String defaultValue) {
		String result = getProperty(key);
		if (result==null) {
			result = defaultValue;
		}
		return result;
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

	static int safeGetCrashReportAction() {
		return INSTANCE==null?0:INSTANCE.getCrashReportAction();
	}

	/** Sets the action to do when a crash is detected.
	 * @param action 0 if the user should be asked, -1 to ignore, 1 to send a crash report to Yapbam
	 * @throws IllegalArgumentException if the action is an invalid parameter (not one of the value listed above) 
	 */
	public void setCrashReportAction(int action) {
		if (Math.abs(action)>1) {
			throw new IllegalArgumentException();
		}
		this.properties.setProperty(CRASH_REPORT_ACTION,Integer.toString(action));
	}
	
	private EditingSettings editingOptions;
	public void setEditingOptions(EditingSettings edit) {
		this.editingOptions = edit;
		setBoolean(PREF_PREFIX+DELETE_ALERT, edit.isAlertOnDelete());
		setBoolean(PREF_PREFIX+MODIFY_CHECKED_ALERT, edit.isAlertOnModifyChecked());
		setBoolean(PREF_PREFIX+SET_DUPLICATE_TRANSACTION_DATE_TO_CURRENT, edit.isDuplicateTransactionDateToCurrent());
		setBoolean(PREF_PREFIX+AUTO_FILL_STATEMENT, edit.isAutoFillStatement());
		setBoolean(PREF_PREFIX+DATE_BASED_AUTO_STATEMENT, edit.isDateBasedAutoStatement());
		this.properties.setProperty(PREF_PREFIX+AUTO_STATEMENT_FORMAT, edit.getStatementDateFormat().toPattern());
		this.properties.setProperty(PREF_PREFIX+EDITION_WIZARD_MODE, edit.getEditionWizardSettings().getMode().name());
		this.properties.setProperty(PREF_PREFIX+EDITION_WIZARD_SOURCE, edit.getEditionWizardSettings().getSource().name());
	}
	
	public EditingSettings getEditionSettings() {
		if (editingOptions==null) {
			String defaultPattern = "yyyyMM"; //$NON-NLS-1$
			String pattern = this.properties.getProperty(PREF_PREFIX+AUTO_STATEMENT_FORMAT, defaultPattern);
			SimpleDateFormat format = new SimpleDateFormat(defaultPattern);
			try {
				format = new SimpleDateFormat(pattern, getLocale());
			} catch (RuntimeException e) {
				// If statement format is wrong, use the default format
			}
			EditionWizardSettings edwSettings = new EditionWizardSettings(
					Mode.valueOf(getProperty(PREF_PREFIX+EDITION_WIZARD_MODE,Mode.NEVER.name())),
					Source.valueOf(getProperty(PREF_PREFIX+EDITION_WIZARD_SOURCE,Source.MOST_PROBABLE.name())));
			editingOptions = new EditingSettings(
				getBoolean(PREF_PREFIX+DELETE_ALERT, true), getBoolean(PREF_PREFIX+MODIFY_CHECKED_ALERT, true),
				getBoolean(PREF_PREFIX+SET_DUPLICATE_TRANSACTION_DATE_TO_CURRENT, true),
				getBoolean(PREF_PREFIX+AUTO_FILL_STATEMENT, false), getBoolean(PREF_PREFIX+DATE_BASED_AUTO_STATEMENT, false),
				format, edwSettings);
		}
		return this.editingOptions;
	}

	private StartStateSettings startStateOptions;
	public void setStartStateOptions(StartStateSettings options) {
		this.startStateOptions = options;
		setBoolean(PREF_START_PREFIX+FILE, options.isRememberFile());
		setBoolean(PREF_START_PREFIX+FILTER, options.isRememberFilter());
		setBoolean(PREF_START_PREFIX+TABS_ORDER, options.isRememberTabsOrder());
		setBoolean(PREF_START_PREFIX+COLUMNS_WIDTH, options.isRememberColumnsWidth());
		setBoolean(PREF_START_PREFIX+COLUMNS_ORDER, options.isRememberColumnsOrder());
		setBoolean(PREF_START_PREFIX+HIDDEN_COLUMNS, options.isRememberHiddenColumns());
		setBoolean(PREF_START_PREFIX+ROW_SORTER_KEYS, options.isRememberRowsSortKeys());
	}

	public StartStateSettings getStartStateOptions() {
		if (startStateOptions==null) {
			startStateOptions = new StartStateSettings(getBoolean(PREF_START_PREFIX+FILE, true), getBoolean(PREF_START_PREFIX+FILTER, false),
				getBoolean(PREF_START_PREFIX+TABS_ORDER, true),	getBoolean(PREF_START_PREFIX+COLUMNS_WIDTH, true),	getBoolean(PREF_START_PREFIX+COLUMNS_ORDER, true),	getBoolean(PREF_START_PREFIX+HIDDEN_COLUMNS, true), getBoolean(PREF_START_PREFIX+ROW_SORTER_KEYS, false));
		}
		return this.startStateOptions;
	}

	/** Tests whether Preferences is able to save preferences at this time.
	 * <br>It is not during the instantiation of the preferences singleton.
	 * So, code executing during this instantiation should not refer to Preferences.INSTANCE
	 * @return true if the singleton is ready to be modified.
	 */
	public static boolean canSave() {
		return INSTANCE!=null;
	}
	
	/** Gets the default font.
	 * @return a Font or null if the current L&F not have a default font (I think only Nimbus supports this).
	 */
	public Font getDefaultFont() {
		Font trueDefault = FontUtils.getDefaultFont();
		if (trueDefault==null) {
			return null;
		}
		
		String fontName = getProperty(FONT_NAME);
		if (fontName!=null) {
			for (Font font : FontUtils.getAvailableTextFonts(getLocale())) {
			    if (fontName.equals(font.getName())) { 
			    	return new Font(fontName, Font.PLAIN, 12);
			    }
			}
		}
		return trueDefault;
	}
	
	public void setDefaultFont(String fontName) {
		if (fontName==null) {
			removeProperty(FONT_NAME);
		} else {
			setProperty(FONT_NAME, fontName);
		}
	}

	private float getFloat(String key, float defaultValue) {
		try {
			String property = this.properties.getProperty(key);
			return property==null?defaultValue:Float.parseFloat(property);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	private void setFloat(String key, float value) {
		Preferences pref = Preferences.INSTANCE;
		pref.setProperty(key, Float.toString(value));
	}

	public float getFontSizeRatio() {
		return getFloat(FONT_SIZE_RATIO, 1.0f);
	}

	public void setFontSizeRatio(float f) {
		setFloat(FONT_SIZE_RATIO, f);
	}
}
