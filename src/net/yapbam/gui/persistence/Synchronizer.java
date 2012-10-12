package net.yapbam.gui.persistence;

import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.ajlib.utilities.NullUtils;

public class Synchronizer {
	public static Synchronizer INSTANCE = new Synchronizer();
	
	// The synchronization scheme implemented in this class do not see if the local cache is based on a previous version of the remote data.
	// For instance here is a scenario that explains the previous sentence:
	// 1 - User A modifies the data locally, but do not synchronize with the remote host
	// 2 - User B modifies the data and synchronizes with the remote host
	// 3 - User A saves the data to the remote host ... a override B work with no warning at all :-(
	// A cool functionality could be to inform the user that there's a conflict ?
	// Maybe, this is just a developer perspective, far from what my gran'mother can understand ...
	
	protected Synchronizer() {}
	
	public File synchronize(Window owner, final URI uri) throws ExecutionException {
		final RemotePersistencePlugin plugin = (RemotePersistencePlugin) PersistenceManager.MANAGER.getPlugin(uri);
		File localCacheFile = plugin.getLocalFile(uri);
		Worker<SynchronizationState, Void> synchroWorker = new SynchroWorker(uri);
		WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, "Synchronizing", ModalityType.APPLICATION_MODAL, synchroWorker);
		waitFrame.setSize(250, waitFrame.getSize().height);
		waitFrame.setVisible(true);
		if (synchroWorker.isCancelled()) {
			if (!plugin.getLocalFile(uri).exists()) localCacheFile = null;
			//FIXME Buttons are not localized
			if (JOptionPane.showConfirmDialog(owner, "You cancelled the synchronization. Would you like to open the cached data ?", "Synchronization was cancelled", JOptionPane.YES_NO_OPTION)!=0) {
				return null;
			}
		} else {
			SynchronizationState state; 
			try {
				state = synchroWorker.get();
			} catch (InterruptedException e) {
				throw new ExecutionException(e);
			}
			if (state.equals(SynchronizationState.CONFLICT)) {
				String message = "<html>Both data stored on your computer and the one on Dropbox were modified.<br>What do you want to do ?</html>";
				Object[] options = {"Upload computer data to Dropbox", "Download Dropbox data to computer", "Cancel"};
				int n = JOptionPane.showOptionDialog(owner, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options, options[2]);
				if (n==2) {
					localCacheFile = null;
				} else if (n==0) {
					//FIXME upload();
				} else {
					//FIXME plugin.download(uri);
				}
			} else if (state.equals(SynchronizationState.REMOTE_DELETED)) {
				String message = "<html>That file doesn't exist anymore on Dropbox.<br>.<br>What do you want to do ?</html>";
				Object[] options = {"Upload computer data to Dropbox", "Delete data on the computer", "Cancel"};
				int n = JOptionPane.showOptionDialog(owner, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options, options[2]);
				if (n==2) {
					localCacheFile = null;
				} else if (n==0) {
					//FIXME upload();
				} else {
					//FIXME plugin.download(uri);
				}
			}
		}
		return localCacheFile;
	}

	/** Synchronizes local cache with remote resource.
	 * <br>This method is guaranteed to not call any of the Swing API, so it could typically used as a Worker doInBackground task.
	 * @param uri The remote URI
	 * @return The synchronization state
	 * @throws FileNotFoundException if neither the remote resource nor its cache file does exist 
	 * @throws IOException if an exception occurs while synchronizing
	 */
	static SynchronizationState backgroundSynchronize(URI uri, Cancellable task) throws IOException {
		RemotePersistencePlugin plugin = (RemotePersistencePlugin) PersistenceManager.MANAGER.getPlugin(uri);
		String remoteRevision = plugin.getRemoteRevision(uri);
		String localRevision = plugin.getLocalBaseRevision(uri);
//System.out.println("remote rev: "+remoteRevision+", local rev:"+localRevision);
		File file = plugin.getLocalFile(uri);
		if (remoteRevision==null) { // If remote uri doesn't exist
			if (!file.exists()) throw new FileNotFoundException(); // The local cache doesn't exist
			if (localRevision==null) { // The local cache was never synchronized
				upload(uri, task); // upload the cache to server
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
					upload(uri, task);
					return SynchronizationState.SYNCHRONIZED;
				}
			} else { // Cache and remote have not the same origin
				if (!file.exists()) { // The local cache doesn't exist
					download(uri, task);
					return SynchronizationState.SYNCHRONIZED;
				} else { // The local cache exists
					if (plugin.isLocalSynchronized(uri)) { // The local cache was already synchronized
						download(uri, task);
						return SynchronizationState.SYNCHRONIZED;
					} else { // The local cache was not synchronized with the remote uri
						return SynchronizationState.CONFLICT;
					}
				}
			}
		}
	}

	private static void download(URI uri, Cancellable task) throws IOException {
		RemotePersistencePlugin plugin = (RemotePersistencePlugin) PersistenceManager.MANAGER.getPlugin(uri);
		System.out.println ("downloading "+uri);
		File file = plugin.getLocalFile(uri);
		file.getParentFile().mkdirs();
		String revision = null;
		String downloadedRevision = "";
		// We do not download directly to the target file, to prevent file from being corrupted if the copy fails
		File tmpFile = new File(file.getParent(), file.getName()+".tmp");
		while (!NullUtils.areEquals(revision, downloadedRevision)) {
			// While the downloaded revision is not the last one on the server
			downloadedRevision = plugin.getRemoteRevision(uri);
			plugin.download(uri, tmpFile, task);
			revision = plugin.getRemoteRevision(uri);
		}
		file.delete();
		tmpFile.renameTo(file);
		plugin.setLocalBaseRevision(uri, revision);
	}

	private static void upload(URI uri, Cancellable task) throws IOException {
		RemotePersistencePlugin plugin = (RemotePersistencePlugin) PersistenceManager.MANAGER.getPlugin(uri);
		System.out.println ("uploading "+uri);
		File file = plugin.getLocalFile(uri);
		plugin.upload(file, uri, task);
		plugin.setLocalBaseRevision(uri, plugin.getRemoteRevision(uri));
	}
	
//	public static void main(String[] args) {
//		try {
//			URI uri = new URI("Dropbox://Jean-Marc+Astesana:0vqjj9jznct586f-1mg71myi8q7z65v@dropbox.yapbam.net/x.zip");
//			Synchronizer.INSTANCE.backgroundSynchronize(uri, null);
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
