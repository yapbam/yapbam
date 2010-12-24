package net.yapbam.update;

import java.io.IOException;
import java.io.InputStream;
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
	private URL autoUpdateURL;
	private String autoUpdateCheckSum;
	private long autoUpdateSize;
	private URL autoUpdaterURL;
	private String autoUpdaterCheckSum;
	private long autoUpdaterSize;
	
	UpdateInformation (URL url) throws UnknownHostException, IOException {
		HttpURLConnection ct = (HttpURLConnection) url.openConnection(Preferences.INSTANCE.getHttpProxy());
		errorCode = ct.getResponseCode();
		if (errorCode==HttpURLConnection.HTTP_OK) {
			Properties p = new Properties();
			String encoding = ct.getContentEncoding();
			InputStream in = ct.getInputStream();
			p.load(new InputStreamReader(in,encoding));
			String serialNumber = p.getProperty("serialNumber");
			YapbamState.put(VersionManager.SERIAL_NUMBER, serialNumber);
			lastestRelease = new ReleaseInfo(p.getProperty("lastestRelease"));
			updateURL = new URL(p.getProperty("updateURL"));
			autoUpdateURL = new URL(p.getProperty("autoUpdateURL"));
			autoUpdateCheckSum = p.getProperty("autoUpdateCHKSUM");
			autoUpdateSize = Long.parseLong(p.getProperty("autoUpdateSize"));
			autoUpdaterURL = new URL(p.getProperty("autoUpdateUpdaterURL"));
			autoUpdaterCheckSum = p.getProperty("autoUpdateUpdaterCHKSUM");
			autoUpdaterSize = Long.parseLong(p.getProperty("autoUpdateUpdaterSize"));
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

	public URL getAutoUpdateURL() {
		return autoUpdateURL;
	}

	public String getAutoUpdateCheckSum() {
		return autoUpdateCheckSum;
	}

	public long getAutoUpdateSize() {
		return autoUpdateSize;
	}

	public long getAutoUpdaterSize() {
		return autoUpdaterSize;
	}

	public URL getAutoUpdaterURL() {
		return autoUpdaterURL;
	}

	public String getAutoUpdaterCheckSum() {
		return autoUpdaterCheckSum;
	}
}
