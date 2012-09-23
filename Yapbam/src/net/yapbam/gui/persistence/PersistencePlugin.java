package net.yapbam.gui.persistence;

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

	public abstract void save ();
	public abstract void load ();
	
	/** Builds an UI component that implements the uri chooser for this plugin.
	 * <br>Be aware that compiler can't force the returned instance to be a java.awt.Component subclass, but
	 * this is mandatory.
	 * <br>The getSelectedURI of the returned component must have the scheme returned by the getScheme method
	 * of this class.
	 * @return a component
	 * @see AbstractURIChooserPanel#getSelectedURI()
	 * @see #getScheme()
	 */
	public abstract AbstractURIChooserPanel buildChooser();
}
