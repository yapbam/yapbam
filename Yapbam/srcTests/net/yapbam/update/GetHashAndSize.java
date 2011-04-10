package net.yapbam.update;

import java.net.Proxy;
import java.net.URL;

import net.yapbam.util.SecureDownloader;

/** This class can be used to get the hash code of a file downloadable at an URL. */
public class GetHashAndSize {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String release = "0.8.2";
		String date = "10/04/2011";
		System.out.println ("lastestRelease="+release+" ("+date+")");
		System.out.println ("updateURL=http://sourceforge.net/project/platformdownload.php?group_id=276272");
		System.out.println ();
		SecureDownloader sd = new SecureDownloader(Proxy.NO_PROXY);
		try {
			String zipURL = "http://www.yapbam.net/update"+release+"/yapbam-"+release+".zip";
			System.out.println ("autoUpdateURL="+zipURL);
			sd.download(new URL(zipURL), null);
			System.out.println ("autoUpdateCHKSUM="+sd.getCheckSum());
			System.out.println ("autoUpdateSize="+sd.getDownloadedSize());
			System.out.println ();

			String updaterURL = "http://www.yapbam.net/update"+release+"/updater.jar";
			System.out.println ("autoUpdateUpdaterURL="+updaterURL);
			sd.download(new URL(updaterURL), null);
			System.out.println ("autoUpdateUpdaterCHKSUM="+sd.getCheckSum());
			System.out.println ("autoUpdateUpdaterSize="+sd.getDownloadedSize());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
