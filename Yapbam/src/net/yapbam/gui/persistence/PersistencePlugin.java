package net.yapbam.gui.persistence;

import java.io.File;
import java.net.URI;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;

/** An abstract Yapbam persistence plugin.
 * <br>Yapbam data persistence model allows developers to implement plugins that allow the users to
 * save/read their data in various locations (computer's disks, ftp server, Dropbox, etc ...).
 * <br>A plugin implements the save/read to a particular destination.
 * For example, the FilePersistencePlugin implements saving/reading to/from the computer's disks.
 * <br><br>
 * Please note that subclasses must have a default constructor.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public abstract class PersistencePlugin {
	/** Constructor. */
	protected PersistencePlugin() {}
	
	/** Gets the scheme managed by this plugin.
	 * <br>Each plugin is identified by the scheme (file, ftp, etc ..) it manages. Please note that
	 * the developer is not limited to standard scheme (file, ftp, http, ...). You're free to define
	 * your own scheme. For example, Yapbam uses "Dropbox" scheme to save/read data to/from Dropbox.
	 * @return the scheme managed by the plugin
	 */
	public abstract String getScheme();
	
	/** Builds an UI component that implements the uri chooser for this plugin.
	 * <br>Be aware that compiler can't force the returned instance to be a java.awt.Component subclass, but
	 * this is mandatory.
	 * <br>The getSelectedURI of the returned component must have the same scheme returned by the getScheme method
	 * of this class.
	 * @return a component
	 * @see AbstractURIChooserPanel#getSelectedURI()
	 * @see #getScheme()
	 */
	public abstract AbstractURIChooserPanel buildChooser();

	/** Gets displayable form of an URI.
	 * <br>URI may contains secret informations (example: password). This method converts the URI to a string
	 * that can be securely displayed on a screen. 
	 * @param uri The remote URI (The uri is guaranteed to have one of the schemes returned by getSchemes).
	 * @return a String. The default implementation returns uri.toString().
	 */
	public String getDisplayableName(URI uri) {
		return uri.toString();
	}
	
	/** Gets the local cache file.
	 * @param uri The remote URI (The uri is guaranteed to have one of the schemes returned by getSchemes).
	 * @return a File
	 */
	public abstract File getLocalFile(URI uri);
}
