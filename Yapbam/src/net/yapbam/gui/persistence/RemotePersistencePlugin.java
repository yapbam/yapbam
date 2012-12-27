package net.yapbam.gui.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.util.Portable;

public abstract class RemotePersistencePlugin extends PersistencePlugin {
	public static final String ZIP_ENTENSION = ".zip"; //$NON-NLS-1$
	private static final String CACHE_PREFIX = "cache"; //$NON-NLS-1$
	private static final String SYNCHRONIZED_CACHE_PREFIX = "sync"; //$NON-NLS-1$

	/** Gets the local folder where the persistence plugins can save their cache file (if any is needed).
	 * @return a File pointing on an already created folder
	 */
	protected File getCacheFolder(String scheme) {
		File folder = FileUtils.isWritable(Portable.getDataDirectory()) ? Portable.getDataDirectory() : new File(System.getProperty("java.io.tmpdir"),"yapbam"); //$NON-NLS-1$ //$NON-NLS-2$
		File file = new File(folder,"cache/"+scheme); //$NON-NLS-1$
		if (!file.isDirectory()) file.mkdirs();
		return file;
	}

	/** Downloads the uri to a file.
	 * <br>The default implementation does nothing
	 * @param uri The uri to download (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @param out The stream where to download
	 * @param task The task that ask the download or null if no cancellable task is provided. Please make sure to report the progress and cancel the download if the task is cancelled.
	 * @return true if the upload is done, false if it was cancelled
	 * @throws IOException 
	 */
	protected abstract boolean download(URI uri, OutputStream out, Cancellable task) throws IOException;

	/** Uploads a file to a destination uri.
	 * <br>The default implementation does nothing
	 * @param in The inputStream from which to read to uploaded bytes
	 * @param length The number of bytes to upload
	 * @param uri The uri where to upload (The uri is guaranteed to has a scheme returned by getSchemes).
	 * @return true if the upload is done, false if it was cancelled
	 * @throws IOException 
	 */
	protected abstract boolean upload(InputStream in, long length, URI uri, Cancellable task) throws IOException;
	
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
	protected String getLocalBaseRevision(URI uri) throws IOException {
		File file = getLocalFile(uri);
		if (!file.exists()) return null;
		String name = file.getName();
		String revision = name.substring(name.startsWith(CACHE_PREFIX) ? CACHE_PREFIX.length() : SYNCHRONIZED_CACHE_PREFIX.length());
		revision = revision.substring(0, revision.length()-ZIP_ENTENSION.length());
		return revision.length()==0?null:revision;
	}

	protected void setLocalBaseRevision(URI uri, String revision) {
		File file = getLocalFile(uri);
		file.renameTo(new File(file.getParent(), SYNCHRONIZED_CACHE_PREFIX+revision+ZIP_ENTENSION));
	}
	
	protected boolean isLocalSynchronized(URI uri) {
		return getLocalFile(uri).getName().startsWith(SYNCHRONIZED_CACHE_PREFIX);
	}

	public File getLocalFileForWriting(URI uri) {
		File file = getLocalFile(uri);
		if (file.getName().startsWith(SYNCHRONIZED_CACHE_PREFIX)) {
			String name = file.getName().substring(SYNCHRONIZED_CACHE_PREFIX.length());
			file = new File(file.getParent(), CACHE_PREFIX+name);
		}
		return file;
	}

	@Override
	public File getLocalFile(URI uri) {
		// Il y a une subtilité dans l'implémentation :
		// On a besoin de mémoriser la révision de base du fichier de cache. Cette révision va être codée dans le nom du fichier.
		// On va stocker ce fichier dans un répertoire de même nom que le path de l'URI. Le nom du fichier, lui, sera la révision
		// sur laquelle il est basé.
		String path = getLocalPath(uri);
		String fileName = path.substring(0, path.length()-ZIP_ENTENSION.length());
		File cacheDirectory = new File(getCacheFolder(uri.getScheme()), fileName); //$NON-NLS-1$
		if (cacheDirectory.isFile()) {
			// hey ... there's a file where it should be a folder !!!
			// Cache is corrupted, try to repair it
			cacheDirectory.delete();
		}
		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}
		String[] files = cacheDirectory.list();
		// If there's no cache file, return the default cache file
		if (files.length==0) return new File(cacheDirectory, CACHE_PREFIX+ZIP_ENTENSION);
		// There's at least one file in the cache, return the most recent (delete others)
		File result = null;
		for (String f : files) {
			File candidate = new File(cacheDirectory, f);
			if (isValidFile(f) && ((result==null) || (candidate.lastModified()>result.lastModified()))) {
				if (result!=null) result.delete();
				result = candidate;
			} else {
				candidate.delete();
			}
		}
		return result!=null?result:new File(cacheDirectory, CACHE_PREFIX+ZIP_ENTENSION);
	}
	
	private boolean isValidFile(String fileName) {
		return (fileName.startsWith(SYNCHRONIZED_CACHE_PREFIX) || fileName.startsWith(CACHE_PREFIX)) && fileName.endsWith(ZIP_ENTENSION);
	}
	
	public abstract String getLocalPath(URI uri);
	public abstract String getConflictMessage();
	public abstract String getDownloadActionMessage();
	public abstract String getUploadActionMessage();
	public abstract String getDeletedMessage();
}
