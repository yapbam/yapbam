package net.yapbam.gui;

import java.awt.Component;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yapbam.gui.dialogs.ErrorDialog;
import net.yapbam.util.ApplicationContext;

/** This class is responsible for handling errors.
 */
public class ErrorManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorManager.class);

	/** The instance of this class.*/
	public static final ErrorManager INSTANCE = new ErrorManager();
	private static final String ENC = "UTF-8"; //$NON-NLS-1$

	private BlockingDeque<Message> errorsQueue;
	private Set<String> encounteredErrors;
	
	private ErrorManager() {
		this.encounteredErrors = new HashSet<String>();
		this.errorsQueue = new LinkedBlockingDeque<Message>();
		final Thread thread = new Thread(new LogSender(), "LogSender"); //$NON-NLS-1$
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				thread.interrupt();
			}
		}));
		thread.setDaemon(true);
		thread.start();
	}

	/** Displays a standard dialog to signal an error.
	 * <br>Note that the throwable instance is not logged (transmitted to yapbam team). 
	 * @param parent The dialog's parent component
	 * @param t the exception that occurred
	 */
	public void display(Component parent, Throwable t) {
		display (parent, t, LocalizationData.get("ErrorManager.message")); //$NON-NLS-1$
	}

	/** Displays a dialog to signal an error.
	 * <br>Note that the throwable instance is not logged (transmitted to yapbam team). 
	 * @param parent The dialog's parent component
	 * @param t the exception that occurred or null if we only want to display a message
	 * @param message The dialog message
	 */
	public void display(Component parent, Throwable t, String message) {
		if (t!=null) {
			StringWriter writer = new StringWriter();
			t.printStackTrace(new PrintWriter(writer));
			String trace = writer.getBuffer().toString();
			message = message + "\n\n" + trace; //$NON-NLS-1$
			LOGGER.error(message, t);
		}
		JOptionPane.showMessageDialog(parent, message, LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
	}
	
	/** Logs a throwable (send it to Yapbam, if the user allowed it).
	 * <br>This should be used to handle every unexpected errors.
	 * @param parent The dialog's parent window (used to ask the user if he allows or now to send crash report to Yapbam team).
	 * @param t the exception that occurred
	 */
	public void log(Window parent, Throwable t) {
		try {
			// To prevent Yapbam from sending always the same errors (and/or showing always the error dialog)
			// We will test if this error as not already been sent during this session
			String trace = getTraceKey(t);
			if (!encounteredErrors.add(trace)) {
				return;
			}
			// Ok, if the program pointer is there, this is a new error.
			// We have to be careful, if an error occurred during the preferences instantiation, Preferences.INSTANCE is null 
			int action = Preferences.safeGetCrashReportAction();
			if (action==0) {
				ErrorDialog errorDialog = new ErrorDialog(parent, t);
				errorDialog.setVisible(true);
				Object result = errorDialog.getResult();
				//Don't remove the following line, it would prevent Yapbam from quit !!!
				errorDialog.dispose(); 
				if (result!=null) {
					action = 1;
				}
			}
			if (action==1) {
				errorsQueue.add(new Message(t));
			} else {
				LOGGER.error("Exception catched", t); //$NON-NLS-1$
			}
		} catch (Throwable e) {
			LOGGER.error("Error while logging exception", e); //$NON-NLS-1$
			// Ok ... the logging process failed.
			// At this point, there's nothing to do.
		}
	}
	
	/** Gets the key of a throwable.
	 * <BR>That key is used to prevent Yapbam from sending twice the same error.
	 * <BR>The stack trace could have been used but it seemed to me that it is better to
	 * return a key based only of the most recent calls in the trace (the same root error can occurred in
	 * a lot of situations).
	 * @param t a Throwable
	 * @return the key.
	 */
	private String getTraceKey(Throwable t) {
		StackTraceElement[] elements = t.getStackTrace();
		StringBuilder buffer = new StringBuilder();
		buffer.append(t.toString());
		int i=0;
		for (StackTraceElement element : elements) {
			if (i==2) {
				break;
			}
			buffer.append(element.toString());
			i++;
		}
		return buffer.toString();
	}
		
	private class LogSender implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					postToYapbam (errorsQueue.take());
				} catch (InterruptedException e) {
					// Ok, not a problem, the shutdown hook has interrupted this thread
					Thread.currentThread().interrupt();
					break;
				} catch (Throwable e) {
					// Well, at this point nothing could help us
					e.printStackTrace();
				}
			}
		}
		
		private void addToBuffer(StringBuilder buffer, String variable, String value) throws UnsupportedEncodingException {
			buffer.append("&").append(URLEncoder.encode(variable, ENC)).append("=").append(URLEncoder.encode(value, ENC)); //$NON-NLS-1$ //$NON-NLS-2$
		}

		private void postToYapbam(Message message) throws IOException {
			// Construct data
			StringWriter writer = new StringWriter();
			message.error.printStackTrace(new PrintWriter(writer));
			String trace = writer.getBuffer().toString();
			StringBuilder data = new StringBuilder();
			data.append(URLEncoder.encode("error", ENC)).append("=").append(URLEncoder.encode(trace, ENC)); //$NON-NLS-1$ //$NON-NLS-2$
			addToBuffer(data, "country", message.country); //$NON-NLS-1$
			addToBuffer(data, "javaVendor", message.javaVendor); //$NON-NLS-1$
			addToBuffer(data, "javaVersion", message.javaVersion); //$NON-NLS-1$
			addToBuffer(data, "lang", message.lang); //$NON-NLS-1$
			addToBuffer(data, "osName", message.osName); //$NON-NLS-1$
			addToBuffer(data, "osVersion", message.osVersion); //$NON-NLS-1$
			addToBuffer(data, "version", message.version); //$NON-NLS-1$
			
			// Send data
			URL url = new URL("https://www.yapbam.net/crashReport.php"); //$NON-NLS-1$
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(Preferences.INSTANCE.getHttpProxy());
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), ENC);
			try {
				wr.write(data.toString());
				wr.flush();
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),ENC));
				try {
					for (String line = rd.readLine(); line != null; line = rd.readLine()) {
						// Process line ... this means do nothing ;-)
						LOGGER.info(line);
					}
				} finally {
					rd.close();
				}
			} finally {
				wr.close();
			}
		}
	}
	
	private static class Message {
		private String version;
		private String country;
		private String lang;
		private String osName;
		private String osVersion;
		private String javaVendor;
		private String javaVersion;
		private Throwable error;
		
		Message (Throwable t) {
			this.error = t;
			this.version = ApplicationContext.getVersion().toString();
			this.country = LocalizationData.getLocale().getCountry();
			this.lang = LocalizationData.getLocale().getLanguage();
			this.osName = System.getProperty("os.name", "?"); //$NON-NLS-1$ //$NON-NLS-2$
			this.osVersion = System.getProperty("os.version", "?"); //$NON-NLS-1$ //$NON-NLS-2$
			this.javaVendor = System.getProperty("java.vendor", "?"); //$NON-NLS-1$ //$NON-NLS-2$
			this.javaVersion = System.getProperty("java.version", "?"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
