package net.yapbam.update;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

import net.yapbam.gui.Preferences;
import net.yapbam.gui.YapbamState;

public class UpdateInformation {
	private int errorCode;
	private ReleaseInfo lastestRelease;
	private URL updateURL;
	
	UpdateInformation (URL url) throws UnknownHostException, IOException {
		HttpURLConnection ct = (HttpURLConnection) url.openConnection(Preferences.INSTANCE.getHttpProxy());
		errorCode = ct.getResponseCode();
		if (errorCode==HttpURLConnection.HTTP_OK) {
			Properties p = new Properties();
			p.load(new InputStreamReader(ct.getInputStream(),ct.getContentEncoding()));
			String serialNumber = p.getProperty("serialNumber");
			YapbamState.put(VersionManager.SERIAL_NUMBER, serialNumber);
			lastestRelease = new ReleaseInfo(p.getProperty("lastestRelease"));
			updateURL = new URL(p.getProperty("updateURL"));
		}
	}

	public int getHttpErrorCode() {
		return errorCode;
	}
	
	public ReleaseInfo getLastestRelease() {
		return lastestRelease;
	}

	public URL getUpdateURL() {
		return updateURL;
	}
}
