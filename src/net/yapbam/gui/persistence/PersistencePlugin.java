package net.yapbam.gui.persistence;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import net.astesana.ajlib.swing.dialog.urichooser.AbstractURIChooserPanel;
import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.util.Portable;

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
	public abstract Collection<String> getSchemes();
	
	/** Builds an UI component that implements the uri chooser for this plugin.
	 * <br>Be aware that compiler can't force the returned instance to be a java.awt.Component subclass, but
	 * this is mandatory.
	 * <br>The getSelectedURI of the returned component must have one of the schemes returned by the getSchemes method
	 * of this class.
	 * @return a component
	 * @see AbstractURIChooserPanel#getSelectedURI()
	 * @see #getSchemes()
	 */
	public abstract AbstractURIChooserPanel buildChooser();

	/** Gets displayable form of an URI.
	 * <br>URI may contains secret informations (example: password). This method converts the URI to a string
	 * that can be securely displayed on a screen. 
	 * @param uri The remote URI (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @return a String. The default implementation returns uri.toString().
	 */
	public String getDisplayableName(URI uri) {
		return uri.toString();
	}
	
	/** Gets the local folder where the persistence plugins can save their cache file (if any is needed).
	 * @return a File pointing on an already created folder
	 */
	protected File getCacheFolder() {
		File folder = FileUtils.isWritable(Portable.getDataDirectory()) ? Portable.getDataDirectory() : new File(System.getProperty("java.io.tmpdir"),"yapbam"); //$NON-NLS-1$
		File file = new File(folder,"cache/"+getSchemes().iterator().next());
		if (!file.isDirectory()) file.mkdirs();
		return file;
	}

	/** Gets the local cache file.
	 * @param uri The remote URI (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @return a File
	 */
	protected abstract File getLocalCacheFile(URI uri);
	
	/** Gets the remote uri modification date.
	 * @param uri The remote URI (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @return the date in ms since 1/1/1970 GMT or null if the remote file doesn't exists
	 */
	public abstract Long getRemoteDate(URI uri) throws IOException;

	/** Downloads the uri to a file.
	 * <br>The default implementation does nothing
	 * @param uri The uri to download (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @param file The file where to download
	 * @throws IOException 
	 */
	protected void download(URI uri, File file) throws IOException {}

	/** Uploads a file to a destination uri.
	 * <br>The default implementation does nothing
	 * @param file The file to upload
	 * @param uri The uri where to upload (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @throws IOException 
	 */
	protected void upload(File file, URI uri) throws IOException {}
}
