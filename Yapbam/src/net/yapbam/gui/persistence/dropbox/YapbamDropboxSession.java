package net.yapbam.gui.persistence.dropbox;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.ResourceBundle;

import net.yapbam.gui.Preferences;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.StandardHttpRequestor;
import com.dropbox.core.http.StandardHttpRequestor.Config;
import com.fathzer.soft.jclop.dropbox.DbxConnectionData;

/** Yapbam <-> Dropbox session.
 */
final class YapbamDropboxSession extends DbxConnectionData {
	private static final String NAME = "Yapbam"; //$NON-NLS-1$
	private static final DbxAppInfo APP_INFO;
	
	static {
		// For obvious security reasons, the key.properties file is not released with the source files.
		ResourceBundle bundle = ResourceBundle.getBundle("net.yapbam.gui.persistence.dropbox.keys"); //$NON-NLS-1$
		APP_INFO = new DbxAppInfo(bundle.getString("appKey"), bundle.getString("appSecret")); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	YapbamDropboxSession() {
		super(NAME, buildConfig(), APP_INFO);
	}
	
	private static DbxRequestConfig buildConfig() {
		Config.Builder builder = Config.builder();
		String proxyHost = Preferences.INSTANCE.getHttpProxyHost();
		if (proxyHost!=null) {
	        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(proxyHost, Preferences.INSTANCE.getHttpProxyPort()));
	        final String proxyUser = Preferences.INSTANCE.getHttpProxyUser();
			if (proxyUser != null) {
				Authenticator.setDefault(new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(proxyUser, Preferences.INSTANCE.getHttpProxyPassword().toCharArray());
					}
				});
			}
			builder.withProxy(proxy);
		}
		DbxRequestConfig.Builder rbuilder = DbxRequestConfig.newBuilder(NAME);
		rbuilder.withHttpRequestor(new StandardHttpRequestor(builder.build()));
		return rbuilder.build();
	}
}