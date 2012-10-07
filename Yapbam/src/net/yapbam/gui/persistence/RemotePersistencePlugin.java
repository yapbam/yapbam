package net.yapbam.gui.persistence;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.util.Portable;

public abstract class RemotePersistencePlugin extends PersistencePlugin {
	/** Gets the local folder where the persistence plugins can save their cache file (if any is needed).
	 * @return a File pointing on an already created folder
	 */
	protected File getCacheFolder(String scheme) {
		File folder = FileUtils.isWritable(Portable.getDataDirectory()) ? Portable.getDataDirectory() : new File(System.getProperty("java.io.tmpdir"),"yapbam"); //$NON-NLS-1$
		File file = new File(folder,"cache/"+getSchemes().iterator().next());
		if (!file.isDirectory()) file.mkdirs();
		return file;
	}

	/** Gets the remote uri modification date.
	 * @param uri The remote URI (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @return the date in ms since 1/1/1970 GMT or null if the remote file doesn't exists
	 */
//	protected abstract Long getRemoteDate(URI uri) throws IOException;

	/** Downloads the uri to a file.
	 * <br>The default implementation does nothing
	 * @param uri The uri to download (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @param file The file where to download
	 * @param task The task that ask the download or null if no cancellable task is provided. Please make sure to report the progress and cancel the download if the task is cancelled.
	 * @return true if the upload is done, false if it was cancelled
	 * @throws IOException 
	 */
	protected abstract boolean download(URI uri, File file, Cancellable task) throws IOException;

	/** Uploads a file to a destination uri.
	 * <br>The default implementation does nothing
	 * @param file The file to upload
	 * @param uri The uri where to upload (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @return true if the upload is done, false if it was cancelled
	 * @throws IOException 
	 */
	protected abstract boolean upload(File file, URI uri, Cancellable task) throws IOException;
	
	/** Gets the remote revision of the URI.
	 * <br>This revision identifies the version of the remote source.
	 * There's no need to have an order relation between revisions. 
	 * @param uri The uri.
	 * @return A String that identifies the revision on the remote source or null if the uri doesn't exists on the remote source.
	 * @throws IOException
	 */
	protected abstract String getRemoteRevision(URI uri) throws IOException;
	
	/** Gets the revision on which the local cache of a remote URI is based.
	 * <br>This revision is the remote one at last time local and remote copies were successfully synchronized. 
	 * @param uri The uri.
	 * @return A String that identifies the revision or null if the local cache doesn't exist or was never been synchronized with the remote source.
	 * @throws IOException
	 */
	protected abstract String getLocalBaseRevision(URI uri) throws IOException;

	protected abstract void setLocalBaseRevision(URI uri, String revision);
	
	protected abstract boolean isLocalSynchronized(URI uri);

	protected abstract File getLocalFileForWriting(URI uri);
}
