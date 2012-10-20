package net.yapbam.gui.persistence.reading;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.yapbam.data.GlobalData;
import net.yapbam.data.xml.Serializer;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.GetPasswordDialog;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.PersistencePlugin;
import net.yapbam.gui.persistence.SynchronizationState;
import net.yapbam.gui.persistence.reading.SyncAndReadWorker.SynchronizeCommand;

public class DataReader {
	private Window owner;
	private GlobalData data;
	private URI uri;
	private PersistencePlugin plugin;

	public DataReader (Window owner, GlobalData data, URI uri) {
		this.owner = owner;
		this.data = data;
		this.uri = uri;
		this.plugin = PersistenceManager.MANAGER.getPlugin(uri);
	}

	public boolean readData() throws ExecutionException {
		return doSyncAndRead(SynchronizeCommand.SYNCHRONIZE);
	}

	private boolean doSyncAndRead(SynchronizeCommand command) throws ExecutionException {
		SyncAndReadWorker basicWorker = new SyncAndReadWorker(uri, command);
		PersistenceManager.buildWaitDialog(owner, basicWorker).setVisible(true);
		ReaderResult result;
		try {
			result = basicWorker.get();
			if (result.getState().equals(State.EXCEPTION_WHILE_SYNC)) {
				if (!(result.getException() instanceof IOException)) {
					ErrorManager.INSTANCE.log(owner, result.getException());
				}
				// The synchronization failed => Ask the user what to do
				return doSyncFailed();
			} else if (result.getState().equals(State.FINISHED)) {
				// Data was synchronized and red
				copyToData(basicWorker.getData());
				return true;
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (CancellationException e) {
			if (basicWorker.isSynchronizing())	return syncCancelled(); // If the synchronization was cancelled
			// If another phase was cancelled -> Cancel the whole read operation
			return false;
		}
		File localFile = plugin.getLocalFile(uri);
		if (basicWorker.isCancelled()) return false; // Anything but the synchronization was cancelled => Globally cancel
		SynchronizationState state = result.getSyncState();
		if (state.equals(SynchronizationState.CONFLICT)) { // There's a conflict between remote resource a local cache
			return doConflict();
		} else if (state.equals(SynchronizationState.REMOTE_DELETED)) { // The remote resource doesn't exist
			return doRemoteNotFound();
		} else if (state.equals(SynchronizationState.SYNCHRONIZED)) { // The local data is ready to be red
			if (result.getState().equals(State.NEED_PASSWORD)) { // A password is needed
				return doPasswordNeeded(localFile.toURI());
			} else {
				throw new RuntimeException ("Unexpected state: data is synchronized, but not red with no password needed");
			}
		} else {
			throw new RuntimeException ("Unexpected state: "+state);
		}
	}

	private boolean doPasswordNeeded(URI localURI) throws ExecutionException {
		GetPasswordDialog dialog = new GetPasswordDialog(owner,
				LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.question"), //$NON-NLS-1$ //$NON-NLS-2$
				UIManager.getIcon("OptionPane.questionIcon"), null); //$NON-NLS-1$
		dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
		dialog.setVisible(true);
		String password = dialog.getPassword();
		while (true) {
			if (password==null) return false; // The user cancels the read
			try {
				if (Serializer.isPasswordOk(localURI, password)) break; // If the user cancels or entered the right password ... go next step
			} catch (IOException e) {
				throw new ExecutionException(e);
			}
			dialog = new GetPasswordDialog(owner,
					LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.badPassword.question"), //$NON-NLS-1$ //$NON-NLS-2$
					UIManager.getIcon("OptionPane.warningIcon"), null); //$NON-NLS-1$
			dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
			dialog.setVisible(true);
			password = dialog.getPassword();
		}
		return readLocalFile(password);
	}

	private boolean doSyncFailed() throws ExecutionException {
		if (!plugin.getLocalFile(uri).exists()) {
			JOptionPane.showMessageDialog(owner, "Sorry, unable to download the data", LocalizationData.get("ErrorManager.title"), JOptionPane.ERROR_MESSAGE);
			return false;
		} else {
			String[] options = new String[]{LocalizationData.get("GenericButton.yes"), LocalizationData.get("GenericButton.no")}; 
			if (JOptionPane.showOptionDialog(owner, "Synchronization failed. Would you like to open the cached data ?",
					LocalizationData.get("Generic.warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 0)!=0) {
				return false;
			}
			return readLocalFile(null);
		}
	}
	
	private boolean doRemoteNotFound() throws ExecutionException {
		String message = "<html>That file doesn't exist anymore on Dropbox.<br><br>What do you want to do ?<br></html>";
		Object[] options = {"Upload computer data to Dropbox", "Delete data on the computer", LocalizationData.get("GenericButton.cancel")};
		int n = JOptionPane.showOptionDialog(owner, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]);
		if (n==2) {
		} else if (n==0) {
			return doSyncAndRead(SynchronizeCommand.UPLOAD);
		} else {
			plugin.getLocalFile(uri).delete();
		}
		return false;
	}

	private boolean doConflict() throws ExecutionException {
		String message = "<html>Both data stored on your computer and the one on Dropbox were modified.<br><br>What do you want to do ?<br></html>";
		Object[] options = {"Upload computer data to Dropbox", "Download Dropbox data to computer", LocalizationData.get("GenericButton.cancel")};
		int n = JOptionPane.showOptionDialog(owner, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]);
		if (n==2) {
			return false;
		} else if (n==0) {
			return doSyncAndRead(SynchronizeCommand.UPLOAD);
		} else {
			return doSyncAndRead(SynchronizeCommand.DOWNLOAD);
		}
	}

	private boolean syncCancelled() throws ExecutionException {
		if (!plugin.getLocalFile(uri).exists()) return false;
		String[] options = new String[]{LocalizationData.get("GenericButton.yes"), LocalizationData.get("GenericButton.no")}; 
		if (JOptionPane.showOptionDialog(owner, "You cancelled the synchronization. Would you like to open the cached data ?",
				"Synchronization was cancelled", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0)!=0) {
			return false;
		}
		return readLocalFile(null); 
	}
	
	private boolean readLocalFile(String password) throws ExecutionException {
		URI localURI = PersistenceManager.MANAGER.getPlugin(uri).getLocalFile(uri).toURI();
		if (password==null) {
			boolean passwordRequired;
			try {
				passwordRequired = Serializer.getSerializationData(localURI).isPasswordRequired();
			} catch (IOException e) {
				throw new ExecutionException(e);
			}
			if (passwordRequired) {
				return doPasswordNeeded(localURI);
			}
		}
		final OnlyReadWorker readWorker = new OnlyReadWorker(localURI, password);
		PersistenceManager.buildWaitDialog(owner, readWorker).setVisible(true);
		try {
			GlobalData redData = readWorker.get();
			copyToData(redData);
			return true;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (CancellationException e) {
			return false;
		}
	}

	private void copyToData(GlobalData redData) {
		boolean enabled = data.isEventsEnabled();
		data.setEventsEnabled(false);
		data.copy(redData);
		data.setChanged(false);
		data.setURI(uri);
		data.setEventsEnabled(enabled);
	}
}
