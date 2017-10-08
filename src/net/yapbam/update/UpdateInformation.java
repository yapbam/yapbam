package net.yapbam.update;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import net.yapbam.gui.Preferences;
import net.yapbam.gui.YapbamState;

import net.yapbam.util.ApplicationContext;
import net.yapbam.util.CheckSum;
import net.yapbam.util.CoolHttpConnection;

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
	
	UpdateInformation (URL url) throws IOException {
		CoolHttpConnection ct = new CoolHttpConnection(url, Preferences.INSTANCE.getHttpProxy());
		errorCode = ct.getResponseCode();
		if (errorCode==HttpURLConnection.HTTP_OK) {
			Properties p = new Properties();
			String encoding = ct.getContentEncoding();
			if (encoding==null) {
				throw new IOException("Encoding is null");
			}
			InputStreamReader reader = new InputStreamReader(ct.getInputStream(), encoding);
			try {
				p.load(reader);
			} finally {
				reader.close();
			}
			String serialNumber = p.getProperty("serialNumber");
			YapbamState.INSTANCE.put(ApplicationContext.SERIAL_NUMBER, serialNumber);
			lastestRelease = new ReleaseInfo(p.getProperty("lastestRelease"));
			updateURL = new URL(p.getProperty("updateURL"));
			autoUpdateURL = new URL(p.getProperty("autoUpdateURL"));
			// We operate a string to byte to string conversion to prevent problems encountered
			// with some ANT generated checksums that contained leading zeros.
			autoUpdateCheckSum = CheckSum.toString(CheckSum.toBytes(p.getProperty("autoUpdateCHKSUM")));
			autoUpdateSize = Long.parseLong(p.getProperty("autoUpdateSize"));
			autoUpdaterURL = new URL(p.getProperty("autoUpdateUpdaterURL"));
			// We operate a string to byte to string conversion to prevent problems encountered
			// with some ANT generated checksums that contained leading zeros.
			autoUpdaterCheckSum = CheckSum.toString(CheckSum.toBytes(p.getProperty("autoUpdateUpdaterCHKSUM")));
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
