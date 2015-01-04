package net.yapbam.gui;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.LoggerFactory;

import net.yapbam.util.ApplicationContext;

import com.fathzer.soft.ajlib.swing.worker.Worker;

public class NewsBuilder {

	public static void build(InfoPanel infoPanel) {
		new UpdateSwingWorker(SwingUtilities.getWindowAncestor(infoPanel)).execute();
	}

	// A SwingWorker that performs the update availability check
	static class UpdateSwingWorker extends Worker<JSONArray, Void> {
		private static final boolean SLOW_UPDATE_CHECKING = false;
		private static final String BASE_URL = "http://www.yapbam.net/news";

		private Window owner;

		UpdateSwingWorker(Window owner) {
			this.owner = owner;
		}
		
		@Override
		public void done() {
			try {
				if (!isCancelled()) {
					JSONArray news = get();
					System.out.println (news);
				}
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
				if (! (e.getCause() instanceof IOException)) {
					ErrorManager.INSTANCE.log(owner,e);
				} else {
					LoggerFactory.getLogger(NewsBuilder.class).debug("Error while communicating with server", e.getCause());
				}
			}
		}

		@Override
		protected JSONArray doProcessing() throws Exception {
			System.out.println ("let's go!");
			if (SLOW_UPDATE_CHECKING) {
				Thread.sleep(2000);
			}
			URL url = ApplicationContext.toURL(BASE_URL);
			System.out.println (url); //TODO
			HttpURLConnection ct = (HttpURLConnection) url.openConnection(Preferences.INSTANCE.getHttpProxy());
			int errorCode = ct.getResponseCode();
			if (errorCode==HttpURLConnection.HTTP_OK) {
				String encoding = ct.getContentEncoding();
				if (encoding==null) {
					throw new IOException("Encoding is null");
				}
				InputStreamReader reader = new InputStreamReader(ct.getInputStream(), encoding);
				try {
//					return (JSONArray) JSONValue.parse(reader);
					BufferedReader r = new BufferedReader(reader);
					try {
						for (String line=r.readLine();line!=null;line=r.readLine()) {
							if (line.startsWith("#")) {
								System.out.println (line);
							} else {
								return (JSONArray) JSONValue.parse(line);
							}
						}
						throw new IOException ("Not result found");
					} finally {
						r.close();
					}
				} finally {
					reader.close();
				}
			} else {
				throw new IOException ("Unexpected error code "+errorCode);
			}
		}
	}
	
	private static class News {
	}
}
