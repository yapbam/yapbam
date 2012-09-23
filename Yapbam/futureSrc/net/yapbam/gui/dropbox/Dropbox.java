package net.yapbam.gui.dropbox;

import net.yapbam.gui.Preferences;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;

public abstract class Dropbox {
	private static final String DROPBOX_ACCESS_KEY = "Dropbox.access.key"; //$NON-NLS-1$
	private static final String DROPBOX_ACCESS_SECRET = "Dropbox.access.secret"; //$NON-NLS-1$
	
	private static DropboxAPI<? extends WebAuthSession> dropboxAPI;

	private Dropbox() {}
	
	public static DropboxAPI<? extends WebAuthSession> getAPI() {
		if (dropboxAPI==null) {
			YapbamDropboxSession session = new YapbamDropboxSession();
			String accessKey = Preferences.INSTANCE.getProperty(DROPBOX_ACCESS_KEY);
			String accessSecret = Preferences.INSTANCE.getProperty(DROPBOX_ACCESS_SECRET);
			if (accessKey!=null || accessSecret!=null) {
				session.setAccessTokenPair(new AccessTokenPair(accessKey, accessSecret));
			}
			dropboxAPI = new DropboxAPI<YapbamDropboxSession>(session);
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
		}
	}
}
