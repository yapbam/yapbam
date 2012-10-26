package net.yapbam.gui.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import net.astesana.ajlib.utilities.LocalizationData;
import net.astesana.ajlib.utilities.NullUtils;

public abstract class Synchronizer {
	private Synchronizer() {}
	
	/** Synchronizes local cache with remote resource.
	 * <br>This method is guaranteed to not call any of the Swing API calls that should be called on the event dispatch thread,
	 * so it could typically used as a Worker doInBackground task.
	 * @param uri The remote URI
	 * @return The synchronization state
	 * @throws FileNotFoundException if neither the remote resource nor its cache file does exist 
	 * @throws IOException if an exception occurs while synchronizing
	 */
	public static SynchronizationState backgroundSynchronize(URI uri, Cancellable task) throws IOException {
		PersistencePlugin p = PersistenceManager.MANAGER.getPlugin(uri);
		if (p instanceof RemotePersistencePlugin) {
			RemotePersistencePlugin plugin = (RemotePersistencePlugin) p;
			String remoteRevision = plugin.getRemoteRevision(uri);
			String localRevision = plugin.getLocalBaseRevision(uri);
	//System.out.println("remote rev: "+remoteRevision+", local rev:"+localRevision);
			File file = plugin.getLocalFile(uri);
			if (remoteRevision==null) { // If remote uri doesn't exist
				if (!file.exists()) throw new FileNotFoundException(); // The local cache doesn't exist
				if (localRevision==null) { // The local cache was never synchronized
					backgroungUpload(uri, task); // upload the cache to server
					return SynchronizationState.SYNCHRONIZED;
				} else { // Remote was deleted
					return SynchronizationState.REMOTE_DELETED;
				}
			} else { // The remote uri exists
				if (remoteRevision.equals(localRevision)) { // Cache and remote have the same origin 
					if (plugin.isLocalSynchronized(uri)) { // The cache and the remote are the same
						return SynchronizationState.SYNCHRONIZED;
					} else {
						// cache was changed but not yet uploaded
						backgroungUpload(uri, task);
						return SynchronizationState.SYNCHRONIZED;
					}
				} else { // Cache and remote have not the same origin
					if (!file.exists()) { // The local cache doesn't exist
						backgroundDownload(uri, task);
						return SynchronizationState.SYNCHRONIZED;
					} else { // The local cache exists
						if (plugin.isLocalSynchronized(uri)) { // The local cache was already synchronized
							backgroundDownload(uri, task);
							return SynchronizationState.SYNCHRONIZED;
						} else { // The local cache was not synchronized with the remote uri
							return SynchronizationState.CONFLICT;
						}
					}
				}
			}
		} else { 
			File file = p.getLocalFile(uri);
			if (!file.exists()) throw new FileNotFoundException();
			return SynchronizationState.SYNCHRONIZED;
		}
	}

	public static boolean backgroundDownload(URI uri, Cancellable task) throws IOException {
		RemotePersistencePlugin plugin = (RemotePersistencePlugin) PersistenceManager.MANAGER.getPlugin(uri);
		File file = plugin.getLocalFile(uri);
		file.getParentFile().mkdirs();
		String revision = null;
		String downloadedRevision = ""; //$NON-NLS-1$
		// We do not download directly to the target file, to prevent file from being corrupted if the copy fails
		File tmpFile = new File(file.getParent(), file.getName()+".tmp"); //$NON-NLS-1$
		boolean done = true;
		while (done && !NullUtils.areEquals(revision, downloadedRevision)) {
			// While the downloaded revision is not the last one on the server
			downloadedRevision = plugin.getRemoteRevision(uri);
			done = plugin.download(uri, tmpFile, task);
			revision = plugin.getRemoteRevision(uri);
		}
		if (done) {
			file.delete();
			tmpFile.renameTo(file);
			plugin.setLocalBaseRevision(uri, revision);
		} else {
			tmpFile.delete();
		}
		return done;
	}

	public static boolean backgroungUpload(URI uri, Cancellable task) throws IOException {
		RemotePersistencePlugin plugin = (RemotePersistencePlugin) PersistenceManager.MANAGER.getPlugin(uri);
		File file = plugin.getLocalFile(uri);
		boolean done = plugin.upload(file, uri, task);
		if (done) {
			plugin.setLocalBaseRevision(uri, plugin.getRemoteRevision(uri));
		}
		return done;
	}
}
