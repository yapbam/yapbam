package net.yapbam.gui;

import java.awt.Component;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

import net.yapbam.gui.dialogs.ErrorDialog;

/** This class is responsible for handling errors.
 */
public class ErrorManager {
	/** The instance of this class.*/
	public static final ErrorManager INSTANCE = new ErrorManager();
	
	private ErrorManager() {}

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
	 * @param t the exception that occurred
	 * @param message The dialog message
	 */
	public void display(Component parent, Throwable t, String message) {
		JOptionPane.showMessageDialog(parent, message, LocalizationData.get("ErrorManager.title"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
	}
	
	/** Logs a throwable (send it to Yapbam, if the user allowed it).
	 * <br>This should be used to handle every unexpected errors.
	 * @param parent The dialog's parent window (used to ask the user if he allows or now to send crash report to Yapbam team).
	 * @param t the exception that occurred
	 */
	public void log(Window parent, Throwable t) {
		try {
			System.err.println("Exception "+t+" was catched by "+this.getClass().getName()); //TODO
			int action =Preferences.INSTANCE.getCrashReportAction();
			if (action==0) {
				ErrorDialog errorDialog = new ErrorDialog(parent, t);
				errorDialog.setVisible(true);
				Object result = errorDialog.getResult();
				errorDialog.dispose(); //Don't remove this line, it would prevent Yapbam from quit !!!
				if (result!=null) {
					action = 1;
				}
			}
			if (action==1) {
				postToYapbam(t);
			} else {
				t.printStackTrace();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			// Ok ... the logging process failed.
			// At this point, there's nothing to do.
		}
	}
	
	private void postToYapbam(Throwable t) throws IOException {
		// Construct data
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		String trace = writer.getBuffer().toString();
		System.out.println (trace);
		String data = URLEncoder.encode("throwable", "UTF-8") + "=" + URLEncoder.encode(trace, "UTF-8");
//		data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");
		
		// Send data
		URL url = new URL("http://www.yapbam.net/crashReport.php");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		try {
			wr.write(data);
			wr.flush();
			
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			try {
				for (String line = rd.readLine(); line != null; line = rd.readLine()) {
					// Process line...
					System.out.println (line); //TODO
				}
			} finally {
				rd.close();
			}
		} finally {
			wr.close();
		}
	}
	
	public static void main (String[] args) {
		INSTANCE.log(null, new RuntimeException("just a test")); //TODO
		INSTANCE.log(null, new RuntimeException("just a second test")); //TODO
	}
}
