package net.yapbam.gui.persistence;

import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
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
		waitFrame.setSize(250, waitFrame.getSize().height);
		Utils.centerWindow(waitFrame, owner);
		return waitFrame;
	}
	
	boolean readData() throws ExecutionException {
		final SynchroWorker synchroWorker = new SynchroWorker(uri);
		buildWaitDialog(owner, synchroWorker).setVisible(true);
		File localFile = plugin.getLocalFile(uri);
		if (synchroWorker.isCancelled()) {
			if (!plugin.getLocalFile(uri).exists()) localFile = null;
			//FIXME Buttons are not localized
			if (JOptionPane.showConfirmDialog(owner, "You cancelled the synchronization. Would you like to open the cached data ?", "Synchronization was cancelled", JOptionPane.YES_NO_OPTION)!=0) {
				return false;
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
					localFile = null;
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
					localFile = null;
				} else if (n==0) {
					//FIXME upload();
				} else {
					//FIXME plugin.download(uri);
				}
			}
		}
		if (localFile == null) return false;
		String password = null;
		URI localURI = localFile.toURI();
		try {
			SerializationData info = Serializer.getSerializationData(localURI);
			// Retrieving the file password
			if (info.isPasswordRequired()) {
				GetPasswordDialog dialog = new GetPasswordDialog(owner,
						LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.question"), //$NON-NLS-1$ //$NON-NLS-2$
						UIManager.getIcon("OptionPane.questionIcon"), null); //$NON-NLS-1$
				dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
				dialog.setVisible(true);
				password = dialog.getPassword();
				while (true) {
					if (password==null) localURI = null; // The user cancels the read
					if ((password==null) || Serializer.isPasswordOk(localURI, password)) break; // If the user cancels or entered the right password ... go next step
					dialog = new GetPasswordDialog(owner,
							LocalizationData.get("FilePasswordDialog.title"), LocalizationData.get("FilePasswordDialog.openFile.badPassword.question"), //$NON-NLS-1$ //$NON-NLS-2$
							UIManager.getIcon("OptionPane.warningIcon"), null); //$NON-NLS-1$
					dialog.setPasswordFieldToolTipText(LocalizationData.get("FilePasswordDialog.openFile.tooltip")); //$NON-NLS-1$
					dialog.setVisible(true);
					password = dialog.getPassword();
				}
			}
		} catch (IOException e) {
			throw new ExecutionException(e);
		}
		if (localURI==null) return false;
		final BackgroundReader worker = new BackgroundReader(localURI, password);
		buildWaitDialog(owner, worker).setVisible(true);
		if (!worker.isCancelled()) {
			GlobalData redData;
			try {
				redData = worker.get();
				boolean enabled = data.isEventsEnabled();
				data.setEventsEnabled(false);
				data.copy(redData);
				data.setChanged(false);
				data.setURI(uri);
				data.setEventsEnabled(enabled);
				return true;
			} catch (InterruptedException e) {
				throw new ExecutionException(e);
			}
		} else {
			return false;
		}
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
}
