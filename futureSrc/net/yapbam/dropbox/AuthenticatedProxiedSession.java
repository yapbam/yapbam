package net.yapbam.dropbox;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
 
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.WebAuthSession;
 
/** A Dropbox session compatible with authenticated proxy.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public abstract class AuthenticatedProxiedSession extends WebAuthSession {
	public AuthenticatedProxiedSession(AppKeyPair appKeyPair, AccessType accessType) {
		super(appKeyPair, accessType);
	}

	/** Gets the proxy user name.
	 * @return a String or null if the proxy is not authenticated
	 */
	public abstract String getProxyUserName();

	/** Gets the proxy user password.
	 * @return a String or null if the proxy is not authenticated
	 */
	public abstract String getProxyPassword();

	private HttpHost getProxy() {
		ProxyInfo proxyInfo = getProxyInfo();
		HttpHost proxy = null;
		if (proxyInfo != null && proxyInfo.host != null && !proxyInfo.host.equals("")) {
			if (proxyInfo.port < 0) {
				proxy = new HttpHost(proxyInfo.host);
			} else {
				proxy = new HttpHost(proxyInfo.host, proxyInfo.port);
			}
		}
		return proxy;
	}

	@Override
	public synchronized HttpClient getHttpClient() {
		DefaultHttpClient client = (DefaultHttpClient) super.getHttpClient();
		// Added to allow connection through a authenticated proxy
		// In addition, you should add commons-codec to the classpath
		HttpHost proxy = getProxy();
		if (proxy != null && getProxyUserName()!=null) {
			client.getCredentialsProvider().setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(getProxyUserName(), getProxyPassword()));
		}
		return client;
	}
}