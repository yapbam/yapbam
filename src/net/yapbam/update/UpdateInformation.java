package net.yapbam.update;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

public class UpdateInformation {
	private int errorCode;
	private ReleaseInfo lastestRelease;
	private URL updateURL;
	
	UpdateInformation (URL url) throws UnknownHostException, IOException {
		//TODO Connect via a proxy : http://forums.sun.com/thread.jspa?threadID=500360
		HttpURLConnection ct = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
		errorCode = ct.getResponseCode();
		if (errorCode==HttpURLConnection.HTTP_OK) {
			Properties p = new Properties();
			p.load(ct.getInputStream());
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
