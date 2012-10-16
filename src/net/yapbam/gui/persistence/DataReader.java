package net.yapbam.gui.persistence;

import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.astesana.ajlib.swing.Utils;
import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.Serializer;
import net.yapbam.data.xml.Serializer.SerializationData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.GetPasswordDialog;
import net.yapbam.gui.persistence.BasicReaderResult.State;

class DataReader {
	private Window owner;
	private GlobalData data;
	private URI uri;
	private PersistencePlugin plugin;

	DataReader (Window owner, GlobalData data, URI uri) {
		this.owner = owner;
		this.data = data;
		this.uri = uri;
		this.plugin = PersistenceManager.MANAGER.getPlugin(uri);
	}

	private WorkInProgressFrame buildWaitDialog(Window owner, Worker<?,?> worker) {
		WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, LocalizationData.get("Generic.wait.title"), ModalityType.APPLICATION_MODAL, worker); //$NON-NLS-1$
		waitFrame.setSize(400, waitFrame.getSize().height);
		Utils.centerWindow(waitFrame, owner);
		return waitFrame;
	}
	
	boolean readData() throws ExecutionException {
		final BasicReadWorker basicWorker = new BasicReadWorker(uri);
		buildWaitDialog(owner, basicWorker).setVisible(true);
		BasicReaderResult result;
		try {
			result = basicWorker.get();
			if (result.getState().equals(State.EXCEPTION_WHILE_SYNC)) {
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
	
	private boolean doRemoteNotFound() {
		String message = "<html>That file doesn't exist anymore on Dropbox.<br>.<br>What do you want to do ?</html>";
		Object[] options = {"Upload computer data to Dropbox", "Delete data on the computer", LocalizationData.get("GenericButton.cancel")};
		int n = JOptionPane.showOptionDialog(owner, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]);
		if (n==2) {
		} else if (n==0) {
			//FIXME upload();
			return false;
		} else {
			plugin.getLocalFile(uri).delete();
		}
		return false;
	}

	private boolean doConflict() {
		String message = "<html>Both data stored on your computer and the one on Dropbox were modified.<br>What do you want to do ?</html>";
		Object[] options = {"Upload computer data to Dropbox", "Download Dropbox data to computer", LocalizationData.get("GenericButton.cancel")};
		int n = JOptionPane.showOptionDialog(owner, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[2]);
		if (n==2) {
			return false;
		} else if (n==0) {
			//FIXME upload();
		} else {
			//FIXME plugin.download(uri);
		}
		return false;
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
		final BackgroundReader readWorker = new BackgroundReader(localURI, password);
		buildWaitDialog(owner, readWorker).setVisible(true);
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
	
	/** A worker (see AJLib library) that reads a GlobalData URI in background. 
	 */
	static class BackgroundReader extends Worker<GlobalData, Void> implements ProgressReport {
		private URI uri;
		private String password;
	
		/** Constructor.
		 * @param uri The source URI (null to do nothing)
		 * @param password The password to access to the source (null if no password is needed)
		 */
		public BackgroundReader (URI uri, String password) {
			this.uri = uri;
			this.password = password;
			setPhase(MessageFormat.format(LocalizationData.get("Generic.wait.readingFrom"), uri.getPath()),-1); //$NON-NLS-1$
		}
		
		@Override
		protected GlobalData doProcessing() throws Exception {
			return Serializer.read(uri, password, this);
		}
	
		@Override
		public void setMax(int length) {
			super.setPhase(getPhase(), length);
		}
	}
	
	static class BasicReadWorker extends Worker<BasicReaderResult, Void> implements Cancellable, ProgressReport {
		private URI uri;
		private GlobalData data;
		private boolean isSynchronizing;
		
		BasicReadWorker(URI uri) {
			this.uri = uri;
			this.data = null;
			this.isSynchronizing = true;
		}
		
		@Override
		protected BasicReaderResult doProcessing() throws Exception {
			setPhase("Synchronizing", -1);
			SynchronizationState syncState;
			try {
				syncState = Synchronizer.backgroundSynchronize(uri, this);
			} catch (FileNotFoundException e) {
				throw e;
			} catch (Exception e) {
				return new BasicReaderResult(State.EXCEPTION_WHILE_SYNC, null, e); 
			}
			if (this.isCancelled()) return null;
			this.isSynchronizing = false;
			if (syncState.equals(SynchronizationState.SYNCHRONIZED)) {
				PersistencePlugin plugin = PersistenceManager.MANAGER.getPlugin(uri);
				URI localURI = plugin.getLocalFile(uri).toURI();
				SerializationData info = Serializer.getSerializationData(localURI);
				// Retrieving the file password
				if (info.isPasswordRequired()) {
					return new BasicReaderResult(State.NEED_PASSWORD, syncState);
				} else {
					setPhase(MessageFormat.format(LocalizationData.get("Generic.wait.readingFrom"), plugin.getDisplayableName(uri)),-1); //$NON-NLS-1$
					this.data = Serializer.read(localURI, null, this);
					return new BasicReaderResult(State.FINISHED, syncState);
				}
			} else {
				return new BasicReaderResult(State.REQUEST_USER, syncState);
			}
		}
		@Override
		public void cancel() {
			super.cancel(false);
		}
		@Override
		public void reportProgress(int progress) {
			super.reportProgress(progress);
		}
		@Override
		public void setPhase(String phase, int length) {
			super.setPhase(phase, length);
		}
		@Override
		public void setMax(int length) {
			super.setPhase(getPhase(), length);
		}
		/**
		 * @return the data
		 */
		public GlobalData getData() {
			return data;
		}
		/**
		 * @return the isSynchronizing
		 */
		public boolean isSynchronizing() {
			return isSynchronizing;
		}
	}
}
