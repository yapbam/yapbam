package net.yapbam.gui.persistence.dropbox;

import java.util.Locale;
import java.util.ResourceBundle;

import net.astesana.cloud.dropbox.AuthenticatedProxiedSession;
import net.yapbam.gui.Preferences;

import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

/** Yapbam <-> Dropbox session.
 */
final class YapbamDropboxSession extends AuthenticatedProxiedSession {
	private static final AppKeyPair APP_KEY_PAIR;
	
	static {
		// For obvious security reasons, the key.properties file is not released with the source files.
		ResourceBundle bundle = ResourceBundle.getBundle("net.yapbam.gui.persistence.dropbox.keys"); //$NON-NLS-1$
		APP_KEY_PAIR = new AppKeyPair(bundle.getString("appKey"), bundle.getString("appSecret")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	YapbamDropboxSession() {
		super(APP_KEY_PAIR, Session.AccessType.APP_FOLDER);
	}

	@Override
	public synchronized ProxyInfo getProxyInfo() {
		return new ProxyInfo(Preferences.INSTANCE.getHttpProxyHost(), Preferences.INSTANCE.getHttpProxyPort());
	}

	@Override
	public String getProxyUserName() {
		return Preferences.INSTANCE.getHttpProxyUser();
	}

	@Override
	public String getProxyPassword() {
		return Preferences.INSTANCE.getHttpProxyPassword();
	}

	/* (non-Javadoc)
	 * @see com.dropbox.client2.session.AbstractSession#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return Preferences.INSTANCE.getLocale();
	}
}