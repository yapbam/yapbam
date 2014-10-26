package net.yapbam.gui.persistence;

import java.io.File;
import java.net.URI;
import java.util.Locale;

import com.fathzer.soft.jclop.Service;
import com.fathzer.soft.jclop.swing.URIChooser;

/** An abstract persistence adapter.
 * <br>JClop data persistence model allows developers to implement adapters that allow the users to
 * save/read their data in various locations (computer's disks, ftp server, Cloud services, etc ...).
 * <br>An adapter implements the save/read to a particular destination.
 * For example, the FilePersistenceAdapter implements saving/reading to/from the computer's disks.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public abstract class PersistenceAdapter {
	private Locale locale;
	private Service service;

	/** Constructor.
	 * @param service The remote service, or null if the adapter is not a remote one.
	 * <br>Actually, the only  case where service is null, is saving/reading to the local disk.
	 */
	protected PersistenceAdapter(Service service) {
		this.service = service;
		this.locale = Locale.getDefault();
	}
	
	/** Gets this adapter persistence service. 
	 * @return a service or null if the adapter is not based on a remote service (for example saving/reading to local disk)
	 */
	public Service getService() {
		return this.service;
	}
	
	/** Builds an UI component that implements the uri chooser for this adapter.
	 * <br>Be aware that compiler can't force the returned instance to be a java.awt.Component subclass, but
	 * this is mandatory.
	 * <br>The getSelectedURI of the returned component must have the same scheme returned by the getScheme method
	 * of this class.
	 * @return a component or null if the adapter doesn't provide any chooser facility
	 * @see URIChooser#getSelectedURI()
	 */
	public abstract URIChooser buildChooser();

	/** Gets the local file attached to an URI.
	 * @param uri The URI (The uri is guaranteed to have the scheme returned by getScheme).
	 * @return a File
	 */
	public final File getLocalFile(URI uri) {
		return service.getLocalFile(uri);
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public Locale getLocale() {
		return this.locale;
	}
	
	public final String getMessage(String key) {
		return this.service.getMessage(key, getLocale());
	}
}
