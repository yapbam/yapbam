package net.yapbam.gui.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A Http connection that follow redirects from http to https.
 */
public class CoolHttpConnection {
	private static final Logger LOGGER = LoggerFactory.getLogger(CoolHttpConnection.class);

	private HttpURLConnection ct;
	
	static {
		// Deactivate SSL certificate checking for old versions of java (java has its local approved certificate repository).
		// This results in old java version to not trust recent valid certificates (here we patch versions that does not trust LetsEncrypt).
		patchSSL();
	}
	
	private static void patchSSL() {
		String javaVersion = System.getProperty("java.version");
		boolean deactivate = false;
		if (javaVersion.startsWith("1.8_")) {
			try {
				int release = Integer.parseInt(javaVersion.substring("1.8_".length()));
				if (release < 101) {
					deactivate=true;
				}
			} catch (NumberFormatException e) {
				LOGGER.warn("Unable to find java 8 release number", e);
			}
		} else if (javaVersion.startsWith("1.7")) {
			deactivate = true;
		}
		if (deactivate) {
			deactivateSSLCertificate();
			LOGGER.warn("{} java version is too old, SSL certificate checking is deactivated",javaVersion);
		}
	}

	protected static void deactivateSSLCertificate() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] {
			new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public CoolHttpConnection(URL url, Proxy proxy) throws IOException {
		this.ct = (HttpURLConnection) url.openConnection(proxy);
		//TODO Catch redirect to https.
	}
	
	public int getResponseCode() throws IOException {
		return this.ct.getResponseCode();
	}

	public InputStream getInputStream() throws IOException {
		return this.ct.getInputStream();
	}

	public String getContentEncoding() {
		return ct.getContentEncoding();
	}

	public String getHeaderField(String field) {
		return ct.getHeaderField(field);
	}
}
