package net.yapbam.ihm;

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

import net.yapbam.util.Crypto;

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
	
	private static final String KEY = "6a2a46e94506ebc3957df475e1da7f78"; //$NON-NLS-1$


	private static final String FILENAME = ".yapbampref"; //$NON-NLS-1$

	/** Current instance */
	public static final Preferences INSTANCE = new Preferences();
	
	private Properties properties;

	private Preferences() {
		this.properties = new Properties();
		if (new File(FILENAME).exists()) {
			try {
				properties.load(new FileInputStream(FILENAME));
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
	
	private void setToDefault() {
		this.properties.clear();
		this.properties.put(LANGUAGE, LANGUAGE_DEFAULT_VALUE);
		this.properties.put(COUNTRY, COUNTRY_DEFAULT_VALUE);
		this.properties.put(LOOK_AND_FEEL, LOOK_AND_FEEL_CUSTOM_VALUE);
	}

	public void save() {
		try {
			properties.store(new FileOutputStream(FILENAME), "Yapbam preferences"); //$NON-NLS-1$
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
}
