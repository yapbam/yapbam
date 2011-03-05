package net.yapbam.update;

import java.net.Proxy;
import java.net.URL;

/** This class can be used to get the hash code of a file downloadable at an URL. */
public class GetHashAndSize {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String release = "0.8.1";
		System.out.println ("lastestRelease=0.8.1 (05/03/2011)");
		System.out.println ("updateURL=http://sourceforge.net/project/platformdownload.php?group_id=276272");
		System.out.println ();
		try {
			String zipURL = "http://www.yapbam.net/update"+release+"/yapbam-"+release+".zip";
			System.out.println ("autoUpdateURL="+zipURL);
			SecureDownloader sd = new SecureDownloader(new URL(zipURL), null, Proxy.NO_PROXY);
			System.out.println ("autoUpdateCHKSUM="+sd.getCheckSum());
			System.out.println ("autoUpdateSize="+sd.getLength());
			System.out.println ();

			String updaterURL = "http://www.yapbam.net/update"+release+"/updater.jar";
			System.out.println ("autoUpdateUpdaterURL="+updaterURL);
			sd = new SecureDownloader(new URL(updaterURL), null, Proxy.NO_PROXY);
			System.out.println ("autoUpdateUpdaterCHKSUM="+sd.getCheckSum());
			System.out.println ("autoUpdateUpdaterSize="+sd.getLength());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
