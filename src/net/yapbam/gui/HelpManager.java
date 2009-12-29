package net.yapbam.gui;

import java.awt.Component;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.HashMap;

import javax.swing.JOptionPane;

import net.yapbam.gui.dialogs.AbstractDialog;

public class HelpManager {
	public interface Content {};
	public static final Content REGULAR_EXPRESSIONS = new Content() {};
	
	private static final HashMap<Content, URI> contentToURI = new HashMap<Content, URI>();
	
	static {
		try {
			contentToURI.put(REGULAR_EXPRESSIONS, new URI("http://www.yapbam.net/")); //$NON-NLS-1$
		} catch (URISyntaxException e) {
			//FIXME What if there's a mistake in an URL ? 
			e.printStackTrace();
		}
	}
	
	public static final void show(Component parent, Content content) {
		try {
			URI url = contentToURI.get(content);
			Desktop.getDesktop().browse(url); 
		} catch (Exception exception) {
			String message = MessageFormat.format(LocalizationData.get("HelpManager.errorDialog.message"), exception.toString()); //$NON-NLS-1$
			JOptionPane.showMessageDialog(AbstractDialog.getOwnerWindow(parent), message, LocalizationData.get("HelpManager.errorDialog.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		}
	}
}
