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
	protected File getCacheFolder() {
		File folder = FileUtils.isWritable(Portable.getDataDirectory()) ? Portable.getDataDirectory() : new File(System.getProperty("java.io.tmpdir"),"yapbam"); //$NON-NLS-1$
		File file = new File(folder,"cache/"+getSchemes().iterator().next());
		if (!file.isDirectory()) file.mkdirs();
		return file;
	}

	/** Gets the remote uri modification date.
	 * @param uri The remote URI (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @return the date in ms since 1/1/1970 GMT or null if the remote file doesn't exists
	 */
	protected abstract Long getRemoteDate(URI uri) throws IOException;

	/** Downloads the uri to a file.
	 * <br>The default implementation does nothing
	 * @param uri The uri to download (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @param file The file where to download
	 * @param task The task that ask the download. Please make sure to report the progress and cancel the download if the task is cancelled.
	 * @throws IOException 
	 */
	protected abstract void download(URI uri, File file, Cancellable task) throws IOException;

	/** Uploads a file to a destination uri.
	 * <br>The default implementation does nothing
	 * @param file The file to upload
	 * @param uri The uri where to upload (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @throws IOException 
	 */
	protected abstract void upload(File file, URI uri, Cancellable task) throws IOException;
	
	protected abstract String getRevision(URI uri) throws IOException;
	
	protected abstract String getLocalRevision(URI uri) throws IOException;
}
