package net.yapbam.gui.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.SwingUtilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.Preferences;
import net.yapbam.util.ApplicationContext;

import com.fathzer.soft.ajlib.swing.worker.Worker;

public abstract class MessagesBuilder {
	private MessagesBuilder() {
		// To prevent this class from being instantiated
		super();
	}

	public static void build(MessagesPanel infoPanel) {
		new UpdateSwingWorker(infoPanel).execute();
	}

	// A SwingWorker that performs the update availability check
	static class UpdateSwingWorker extends Worker<JSONArray, Void> {
		private static final Logger LOGGER = LoggerFactory.getLogger(MessagesBuilder.class);
		private static final boolean SLOW_UPDATE_CHECKING = false;
		private static final String BASE_URL_PROPERTY = "newsURL"; //$NON-NLS-1$
		private static final String BASE_URL = System.getProperty(BASE_URL_PROPERTY, "https://www.yapbam.net/messages"); //$NON-NLS-1$

		private MessagesPanel infoPanel;

		UpdateSwingWorker(MessagesPanel infoPanel) {
			this.infoPanel = infoPanel;
		}
		
		@Override
		public void done() {
			try {
				if (!isCancelled()) {
					JSONArray json = get();
					List<Message> news = new ArrayList<Message>();
					for (int i = 0; i < json.size(); i++) {
						JSONObject obj = (JSONObject) json.get(i);
						Message info = Message.build(obj);
						if (info!=null) {
							news.add(info);
						}
					}
					infoPanel.setMessages(news);
				}
			} catch (InterruptedException e) {
				LOGGER.trace("Worker was interrupted", e); //$NON-NLS-1$
			} catch (ExecutionException e) {
				if (! (e.getCause() instanceof IOException)) {
					ErrorManager.INSTANCE.log(SwingUtilities.getWindowAncestor(infoPanel),e);
				} else {
					LOGGER.warn("Error while communicating with server", e.getCause()); //$NON-NLS-1$
				}
			}
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
		
		@Override
		protected JSONArray doProcessing() throws Exception {
			if (SLOW_UPDATE_CHECKING) {
				Thread.sleep(2000);
			}
			patchSSL();
			URL url = ApplicationContext.toURL(BASE_URL);
			LOGGER.debug("Getting news from {}",url.toString());
			HttpURLConnection ct = (HttpURLConnection) url.openConnection(Preferences.INSTANCE.getHttpProxy());
			int errorCode = ct.getResponseCode();
			if (errorCode==HttpURLConnection.HTTP_OK) {
				String encoding = ct.getContentEncoding();
				if (encoding==null) {
					throw new IOException("Encoding is null"); //$NON-NLS-1$
				}
				InputStreamReader reader = new InputStreamReader(ct.getInputStream(), encoding);
				try {
					BufferedReader r = new BufferedReader(reader);
					try {
						for (String line=r.readLine();line!=null;line=r.readLine()) {
							if (line.startsWith("#")) { //$NON-NLS-1$
								LOGGER.trace(line);
							} else {
								LOGGER.debug("info: {}",line);
								return (JSONArray) JSONValue.parse(line);
							}
						}
						throw new IOException ("Not result found"); //$NON-NLS-1$
					} finally {
						r.close();
					}
				} finally {
					reader.close();
				}
			} else {
				if (errorCode==HttpURLConnection.HTTP_MOVED_PERM || errorCode==HttpURLConnection.HTTP_MOVED_TEMP) {
					LOGGER.error("Redirection to {} is not followed.",ct.getHeaderField("Location"));
				}
				throw new IOException ("Unexpected error code "+errorCode); //$NON-NLS-1$
			}
		}
	}
}
