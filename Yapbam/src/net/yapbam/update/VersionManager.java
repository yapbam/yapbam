package net.yapbam.update;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Properties;

import net.yapbam.ihm.LocalizationData;
import net.yapbam.ihm.YapbamState;

public class VersionManager {
	static final String SERIAL_NUMBER = "serialNumber";
	public static final String YABAM_HOME_URL = "http://www.yapbam.net";

	private static Properties properties;
	
	static {
		InputStream inStream = ClassLoader.getSystemResourceAsStream("version.txt"); //$NON-NLS-1$
		properties = new Properties();
		try {
			properties.load(inStream);
		} catch (IOException e) {
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {}
		}
	}
	
	public static ReleaseInfo getVersion() {
		return new ReleaseInfo(properties.getProperty("version")); //$NON-NLS-1$
	}
	
	private static void addPropertyParameter(StringBuffer url, String paramName, String key) throws UnsupportedEncodingException {
		String property = System.getProperty(key);
		if (property==null) property = "?";
		url.append("&").append(paramName).append("=").append(URLEncoder.encode(property,"UTF-8"));
	}

	public static URL getUpdateURL() {
		try {
			StringBuffer url = new StringBuffer("http://yapbam.sourceforge.net/updateInfo.php");
			url.append("?version=").append(URLEncoder.encode(getVersion().toString(),"UTF-8"));
			url.append("&country=").append(URLEncoder.encode(LocalizationData.getLocale().getCountry(),"UTF-8"));
			url.append("&lang=").append(URLEncoder.encode(LocalizationData.getLocale().getLanguage(),"UTF-8"));
			addPropertyParameter (url, "osName", "os.name");
			addPropertyParameter (url, "osRelease", "os.version");
			addPropertyParameter (url, "javaVendor", "java.vendor");
			addPropertyParameter (url, "javaVersion", "java.version");
			String serialNumber = YapbamState.get(SERIAL_NUMBER);
			if (serialNumber!=null) url.append("&id=").append(URLEncoder.encode(serialNumber,"UTF-8"));
			return new URL(url.toString());
		} catch (Exception e) {
			// TODO What to do if there a problem here ?
			e.printStackTrace();
			return null;
		}
	}
	
	public static UpdateInformation getUpdateInformation() throws UnknownHostException, IOException {
		return new UpdateInformation(getUpdateURL());
	}
}
