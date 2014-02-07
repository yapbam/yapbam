package net.yapbam.update;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.util.Portable;

public abstract class VersionManager {
	//TODO Could be a generic class if update url and "version.txt" was not hard coded.
	private static final String UTF_8 = "UTF-8";
	private static final String VERSION_PROPERTY_NAME = "version";
	private static final String BASE_UPDATE_URL = "http://yapbam.sourceforge.net/updateInfo.php";
	private static final String BETA_BASE_UPDATE_URL = "http://yapbam.sourceforge.net/updateInfoBeta.php";
	
	static final String SERIAL_NUMBER = "serialNumber";

	private static ReleaseInfo version;
	
	private VersionManager() {
		// To prevent subclasses from being created
	}
	
	/** Gets the version of currently running Yapbam copy.
	 * @return a ReleaseInfo instance ({@link ReleaseInfo#UNKNOWN if the version is unknown}
	 */
	public static ReleaseInfo getVersion() {
		if (version==null) {
			InputStream inStream = VersionManager.class.getResourceAsStream("version.txt"); //$NON-NLS-1$
			Properties properties = new Properties();
			if (inStream == null) {
				LoggerFactory.getLogger(VersionManager.class).warn("Unable to find version file"); //$NON-NLS-1$
			} else {
				try {
					properties.load(inStream);
				} catch (IOException e) {
					LoggerFactory.getLogger(VersionManager.class).warn("Unable to read version file", e); //$NON-NLS-1$
				} finally {
					try {
						inStream.close();
					} catch (IOException e) {
						LoggerFactory.getLogger(VersionManager.class).warn("Unable to close version file", e); //$NON-NLS-1$
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
	
	private static void addPropertyParameter(StringBuilder url, String paramName, String key) throws UnsupportedEncodingException {
		String property = System.getProperty(key, "?");
		url.append("&").append(paramName).append("=").append(URLEncoder.encode(property,UTF_8));
	}

	private static URL getUpdateURL() {
		try {
			StringBuilder url = new StringBuilder(Boolean.getBoolean("BetaUpdating")?BETA_BASE_UPDATE_URL:BASE_UPDATE_URL);
			url.append("?version=").append(URLEncoder.encode(version.toString(),UTF_8));
			url.append("&country=").append(URLEncoder.encode(LocalizationData.getLocale().getCountry(),UTF_8));
			url.append("&lang=").append(URLEncoder.encode(LocalizationData.getLocale().getLanguage(),UTF_8));
			addPropertyParameter (url, "osName", "os.name");
			addPropertyParameter (url, "osRelease", "os.version");
			addPropertyParameter (url, "javaVendor", "java.vendor");
			addPropertyParameter (url, "javaVersion", "java.version");
			url.append("&portable=").append(URLEncoder.encode(Boolean.toString(Portable.isPortable()),UTF_8));
			String serialNumber = YapbamState.INSTANCE.get(SERIAL_NUMBER);
			if (serialNumber!=null) {
				url.append("&id=").append(URLEncoder.encode(serialNumber,UTF_8));
			}
			return new URL(url.toString());
		} catch (Exception e) {
			ErrorManager.INSTANCE.log(null,e);
			return null;
		}
	}
	
	public static UpdateInformation getUpdateInformation() throws IOException {
		return new UpdateInformation(getUpdateURL());
	}
}
