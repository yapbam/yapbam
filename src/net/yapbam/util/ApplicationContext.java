package net.yapbam.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.update.ReleaseInfo;

public abstract class ApplicationContext {
	//TODO Could be a generic class if update url and "version.txt" was not hard coded.
	private static final String VERSION_PROPERTY_NAME = "version";
	public static final String SERIAL_NUMBER = "serialNumber";
	private static ReleaseInfo version;
	
	private ApplicationContext() {
		// To prevent subclasses from being created
		super();
	}
	
	/** Gets the version of currently running Yapbam copy.
	 * @return a ReleaseInfo instance ({@link ReleaseInfo#UNKNOWN if the version is unknown}
	 */
	public static synchronized ReleaseInfo getVersion() {
		if (version==null) {
			InputStream inStream = ApplicationContext.class.getResourceAsStream("/net/yapbam/update/version.txt"); //$NON-NLS-1$
			Properties properties = new Properties();
			if (inStream == null) {
				LoggerFactory.getLogger(ApplicationContext.class).warn("Unable to find version file"); //$NON-NLS-1$
			} else {
				try {
					properties.load(inStream);
				} catch (IOException e) {
					LoggerFactory.getLogger(ApplicationContext.class).warn("Unable to read version file", e); //$NON-NLS-1$
				} finally {
					try {
						inStream.close();
					} catch (IOException e) {
						LoggerFactory.getLogger(ApplicationContext.class).warn("Unable to close version file", e); //$NON-NLS-1$
					}
				}
			}
			if (properties.containsKey(VERSION_PROPERTY_NAME)) {
				version = new ReleaseInfo(properties.getProperty(VERSION_PROPERTY_NAME)); //$NON-NLS-1$
			} else {
				version = ReleaseInfo.UNKNOWN;
			}
		}
		return version;
	}
	
	private static void addPropertyParameter(UrlBuilder url, String paramName, String key) throws UnsupportedEncodingException {
		url.appendParameter(paramName, System.getProperty(key, "?"));
	}

	public static URL toURL(String baseURL) {
		try {
			UrlBuilder url = new UrlBuilder(baseURL);
			url.appendParameter(VERSION_PROPERTY_NAME, getVersion().toString());
			url.appendParameter("country", LocalizationData.getLocale().getCountry());
			url.appendParameter("lang", LocalizationData.getLocale().getLanguage());
			addPropertyParameter (url, "osName", "os.name");
			addPropertyParameter (url, "osRelease", "os.version");
			addPropertyParameter (url, "javaVendor", "java.vendor");
			addPropertyParameter (url, "javaVersion", "java.version");
			url.appendParameter("portable", Boolean.toString(Portable.isPortable()));
			String serialNumber = YapbamState.INSTANCE.get(SERIAL_NUMBER);
			if (serialNumber!=null) {
				url.appendParameter("id", serialNumber);
			}
			return url.toURL();
		} catch (Exception e) {
			ErrorManager.INSTANCE.log(null,e);
			return null;
		}
	}
}
