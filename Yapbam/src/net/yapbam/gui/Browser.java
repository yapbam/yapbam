package net.yapbam.gui;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import net.astesana.ajlib.swing.dialog.AbstractDialog;

/** A class that is able to open a web browser to display an URI.
 *<br>The main advantage of this class is to display an alert dialog if java is not able to open the browser.
 */
public abstract class Browser {
	/** Displays an URI in a browser.
	 * @param uri The uri to display
	 * @param parent The parent component of the dialog displayed if browser is not available.
	 * @param errorDialogTitle The title of the dialog displayed if browser is not available.
	 */
	public static void show(URI uri, Component parent, String errorDialogTitle) {
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			error(uri, parent, errorDialogTitle);
		} catch (UnsupportedOperationException e) {
			error(uri, parent, errorDialogTitle);
		}
	}

	private static void error(URI uri, Component parent, String errorDialogTitle) {
		String url = uri.toString();
		String message = MessageFormat.format(LocalizationData.get("Browser.unsupported.message"), url); //$NON-NLS-1$
		StringSelection stringSelection = new StringSelection(url);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents( stringSelection, null);
    if (errorDialogTitle==null) errorDialogTitle = "";
    JOptionPane.showMessageDialog(AbstractDialog.getOwnerWindow(parent), message, errorDialogTitle, JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
	}
}
