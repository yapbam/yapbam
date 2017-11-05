package net.yapbam.gui.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.Preferences;

import net.yapbam.util.ApplicationContext;
import net.yapbam.util.CoolHttpConnection;

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
					if (json!=null) {
						for (int i = 0; i < json.size(); i++) {
							JSONObject obj = (JSONObject) json.get(i);
							Message info = Message.build(obj);
							if (info!=null) {
								news.add(info);
							}
						}
						infoPanel.setMessages(news);
					}
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
		
		@Override
		protected JSONArray doProcessing() throws Exception {
			if (SLOW_UPDATE_CHECKING) {
				Thread.sleep(2000);
			}
			URL url = ApplicationContext.toURL(BASE_URL);
			LOGGER.debug("Getting news from {}",url.toString()); //$NON-NLS-1$
			CoolHttpConnection ct = new CoolHttpConnection(url,Preferences.INSTANCE.getHttpProxy());
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
								LOGGER.debug("info: {}",line); //$NON-NLS-1$
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
					LOGGER.error("Redirection to {} was not followed.",ct.getHeaderField("Location")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				throw new IOException ("Unexpected error code "+errorCode); //$NON-NLS-1$
			}
		}
	}
}
