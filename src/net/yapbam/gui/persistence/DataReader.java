package net.yapbam.gui.persistence;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Window;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.Serializer;
import net.yapbam.data.xml.Serializer.SerializationData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.GetPasswordDialog;

class DataReader {
	final static DataReader INSTANCE = new DataReader();
	
	boolean readData(Window owner, GlobalData data, final URI uri) throws ExecutionException {
		final PersistencePlugin plugin = PersistenceManager.MANAGER.getPlugin(uri);
		URI localURI = null;
		Worker<URI, Void> synchroWorker = new Worker<URI, Void>() {
			@Override
			protected URI doInBackground() throws Exception {
				return plugin.synchronizeForOpening(uri);
			}
		};
		while (localURI==null) {
			WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, "Synchronizing with ?", ModalityType.APPLICATION_MODAL, synchroWorker); //TODO
			waitFrame.setSize(250, waitFrame.getSize().height);
			waitFrame.setVisible(true);
			if (synchroWorker.isCancelled()) {
				//FIXME Ask for cancel opening or open with synchronizing
				break; 
			} else {
				try {
					localURI = synchroWorker.get();
				} catch (InterruptedException e) {
					throw new ExecutionException(e);
				}
				if (localURI==null) {
					String message = "The cached data is newest than the Dropbox data";
					Object[] options = {"upload newest Dropbox", "Revert to the Dropbox content", "Cancel"};
					int n = JOptionPane.showOptionDialog(owner, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
							null, options, options[2]);
					if (n==2) return false;
					if (n==0) {
						//FIXME
					} else {
						//FIXME
					}
				}
			}
		}
		if (localURI==null) return false;
		String password = null;
		try {
			//FIXME Test when the localURI is null (cache newer than source)
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
			new ExecutionException(e);
		}
		if (localURI==null) return false;
		final BackgroundReader worker = new BackgroundReader(localURI, password);
		WorkInProgressFrame waitFrame = new WorkInProgressFrame(owner, LocalizationData.get("Generic.wait.title"), ModalityType.APPLICATION_MODAL, worker); //$NON-NLS-1$
		waitFrame.setVisible(true);
		boolean cancelled = worker.isCancelled();
		if (!cancelled) {
				GlobalData redData;
				try {
					redData = worker.get();
					boolean enabled = data.isEventsEnabled();
					data.setEventsEnabled(false);
					data.copy(redData);
					data.setChanged(false);
					data.setURI(uri);
					data.setEventsEnabled(enabled);
				} catch (InterruptedException e) {
					throw new ExecutionException(e);
				}
		}
		return !cancelled;
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
		protected GlobalData doInBackground() throws Exception {
			return Serializer.read(uri, password, this);
		}
	
		@Override
		public void setMax(int length) {
			super.setPhase(getPhase(), length);
		}
	}
}
