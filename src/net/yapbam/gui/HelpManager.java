package net.yapbam.gui;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.util.Portable;

public class HelpManager {
	public static final class Content {
		String key;
		Content (String key) {
			this.key=key;
		}
	};
	public static final Content REGULAR_EXPRESSIONS = new Content("regular_expressions"); //$NON-NLS-1$
	
	private static final String BUNDLE_NAME = "net.yapbam.gui.helpUrls"; //$NON-NLS-1$
	private static ResourceBundle RESOURCE_BUNDLE;
	private static Locale currentLocale = null;
		
	static void loadLanguagesLocations() {
		if ((currentLocale==null) || !currentLocale.equals(Preferences.INSTANCE.getLocale())) {
			currentLocale = Preferences.INSTANCE.getLocale();
			Locale oldDefault = Locale.getDefault(); // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4303146
			Locale.setDefault(currentLocale);
			RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
			Locale.setDefault(oldDefault);
		}
	}
	
	public static final void show(Component parent, Content content) {
		try {
			loadLanguagesLocations();
			URI url = new File(Portable.getLaunchDirectory(),RESOURCE_BUNDLE.getString(content.key)).toURI();
			Desktop.getDesktop().browse(url); 
		} catch (IOException exception) {
			String message = MessageFormat.format(LocalizationData.get("HelpManager.errorDialog.message"), exception.toString()); //$NON-NLS-1$
			JOptionPane.showMessageDialog(AbstractDialog.getOwnerWindow(parent), message, LocalizationData.get("HelpManager.errorDialog.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		}
	}
}
