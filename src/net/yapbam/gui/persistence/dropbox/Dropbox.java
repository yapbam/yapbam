package net.yapbam.gui.persistence.dropbox;

import net.yapbam.gui.Preferences;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;

public class Dropbox extends DropboxAPI<WebAuthSession> {
	private static final String DROPBOX_ACCESS_KEY = "Dropbox.access.key"; //$NON-NLS-1$
	private static final String DROPBOX_ACCESS_SECRET = "Dropbox.access.secret"; //$NON-NLS-1$
	
	private static DropboxAPI<? extends WebAuthSession> dropboxAPI;

	private Dropbox() {
		super (getYapbamSession());
	}

	private static WebAuthSession getYapbamSession() {
		YapbamDropboxSession session = new YapbamDropboxSession();
		String accessKey = Preferences.INSTANCE.getProperty(DROPBOX_ACCESS_KEY);
		String accessSecret = Preferences.INSTANCE.getProperty(DROPBOX_ACCESS_SECRET);
		if (accessKey!=null || accessSecret!=null) {
			session.setAccessTokenPair(new AccessTokenPair(accessKey, accessSecret));
		}
		return session;
	}
	
	public static DropboxAPI<? extends WebAuthSession> getAPI() {
		if (dropboxAPI==null) {
			dropboxAPI = new Dropbox();
		}
		return dropboxAPI;
	}
	
	public static void storeKeys(AccessTokenPair pair) {
		if (pair==null) {
			Preferences.INSTANCE.removeProperty(DROPBOX_ACCESS_KEY);
			Preferences.INSTANCE.removeProperty(DROPBOX_ACCESS_SECRET);
			dropboxAPI = null;
		} else {
			Preferences.INSTANCE.setProperty(DROPBOX_ACCESS_KEY, pair.key);
			Preferences.INSTANCE.setProperty(DROPBOX_ACCESS_SECRET, pair.secret);
			if (dropboxAPI!=null && !dropboxAPI.getSession().getAccessTokenPair().equals(pair)) {
				dropboxAPI.getSession().setAccessTokenPair(pair);
			}
		}
	}

	@Override
	public com.dropbox.client2.DropboxAPI.Entry metadata(String path, int fileLimit, String hash, boolean list, String rev) throws DropboxException {
//		long start = System.currentTimeMillis();
		DropboxAPI.Entry result = super.metadata(path, fileLimit, hash, list, rev);
//		System.out.println("metadata on "+path+" in "+(System.currentTimeMillis()-start)+"ms");
		return result;
	}
}
