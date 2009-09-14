package net.yapbam.update;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

import net.yapbam.ihm.Preferences;

public class UpdateInformation {
	private int errorCode;
	private ReleaseInfo lastestRelease;
	private URL updateURL;
	
	UpdateInformation (URL url) throws UnknownHostException, IOException {
		InetAddress host = Preferences.INSTANCE.getHttpProxyHost();
		Proxy proxy = host==null?Proxy.NO_PROXY:new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Preferences.INSTANCE.getHttpProxyPort()));
		HttpURLConnection ct = (HttpURLConnection) url.openConnection(proxy);
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
