package net.yapbam.update;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;

public class UpdateInformation {
	private int errorCode;
	private ReleaseInfo lastestRelease;
	private URL updateURL;
	
	UpdateInformation (URL url) throws UnknownHostException, IOException {
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

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		try {
//			UpdateInformation updateInfo = VersionManager.getUpdateInformation();
//			System.out.println (updateInfo.getHttpErrorCode());
//			System.out.println (updateInfo.getLastestRelease());
//			if (VersionManager.getVersion().compareTo(updateInfo.getLastestRelease())<0) {
//				System.out.println ("Please Visit "+updateInfo.getUpdateURL());				
//			}
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
}
