package net.yapbam.gui.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import net.yapbam.gui.LocalizationData;

import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Service;

/** An abstract persistence plugin that stores the data on a remote host (ftp, cloud service, etc ...).
 * <br>Please note that subclasses must have a default constructor.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public abstract class RemotePersistencePlugin extends PersistencePlugin {
	public static final String ZIP_ENTENSION = ".zip"; //$NON-NLS-1$
	
	private Service service;

	/** Constructor.
	 * @param service The service on which is based the plugin
	 */
	protected RemotePersistencePlugin(Service service) {
		this.service = service;
	}
	
	@Override
	public String getScheme() {
		return this.service.getScheme();
	}
	
	/** Downloads the uri to a file.
	 * @param uri The uri to download (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @param out The stream where to download
	 * @param task The task that ask the download or null if no cancellable task is provided. Please make sure to report the progress and cancel the download if the task is cancelled.
	 * @return true if the upload is done, false if it was cancelled
	 * @throws IOException 
	 */
	public boolean download(URI uri, OutputStream out, Cancellable task) throws IOException {
		return this.service.download(this.service.getEntry(uri), out, task, LocalizationData.getLocale());
	}

	/** Uploads a file to a destination uri.
	 * <br>The default implementation does nothing
	 * @param in The inputStream from which to read to uploaded bytes
	 * @param length The number of bytes to upload
	 * @param uri The uri where to upload (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @return true if the upload is done, false if it was cancelled
	 * @throws IOException 
	 */
	public boolean upload(InputStream in, long length, URI uri, final Cancellable task) throws IOException {
		return this.service.upload(in, length, service.getEntry(uri), task, LocalizationData.getLocale());
	}
	
	/** Gets the remote revision of the URI.
	 * <br>This revision identifies the version of the remote source.
	 * There's no need to have an order relation between revisions. 
	 * @param uri The uri.
	 * @return A String that identifies the revision on the remote source or null if the uri doesn't exists on the remote source.
	 * @throws IOException
	 */
	public String getRemoteRevision(URI uri) throws IOException {
		return this.service.getRemoteRevision(service.getEntry(uri));
	}
	
	/** Gets the revision on which the local cache of a remote URI is based.
	 * <br>This revision was the remote one last time local and remote copies were successfully synchronized. 
	 * @param uri The uri.
	 * @return A String that identifies the revision or null if the local cache doesn't exist or was never been synchronized with the remote source.
	 * @throws IOException
	 */
	protected String getLocalBaseRevision(URI uri) throws IOException {
		return service.getLocalRevision(uri);
	}

	protected final void setLocalBaseRevision(URI uri, String revision) {
		service.setLocalRevision(uri, revision);
	}
	
	protected boolean isLocalSynchronized(URI uri) {
		return service.isSynchronized(uri);
	}

	public File getLocalFileForWriting(URI uri) {
		return service.getLocalFileForWriting(uri);
	}

	@Override
	public File getLocalFile(URI uri) {
		return service.getLocalFile(uri);
	}
	
	/* (non-Javadoc)
	 * @see net.yapbam.gui.persistence.PersistencePlugin#getDisplayableName(java.net.URI)
	 */
	@Override
	public String getDisplayableName(URI uri) {
		return service.getDisplayable(uri);
	}

	public abstract String getConflictMessage();
	public abstract String getDownloadActionMessage();
	public abstract String getUploadActionMessage();
	public abstract String getDeletedMessage();
}
