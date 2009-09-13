package net.yapbam.update;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

public class VersionManager {
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
//		, LocalizationData.get("VersionManager.unknown")
	}

	public static URL getUpdateURL() {
		try {
			return new URL(properties.getProperty("updateURL", "http://yapbam.sourceforge.net/updateInfo.properties")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (MalformedURLException e) {
			// TODO What to do if there a problem here ?
			e.printStackTrace();
			return null;
		}
	}
	
	public static UpdateInformation getUpdateInformation() throws UnknownHostException, IOException {
		return new UpdateInformation(getUpdateURL());
	}

	public static final String YABAM_HOME_URL = "http://yapbam.sourceforge.net";
}
