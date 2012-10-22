package net.yapbam.gui.persistence.writing;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import net.astesana.ajlib.swing.worker.WorkInProgressFrame;
import net.astesana.ajlib.swing.worker.Worker;
import net.astesana.ajlib.utilities.FileUtils;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.RemotePersistencePlugin;
import net.yapbam.gui.persistence.SynchronizationState;
import net.yapbam.gui.persistence.SynchronizeCommand;
import net.yapbam.gui.persistence.reading.DataReader;
import net.yapbam.util.Portable;

public class DataWriter {
	private Window owner;
	private GlobalData data;
	private URI uri;

	public DataWriter (Window owner, GlobalData data, URI uri) {
		this.owner = owner;
		this.data = data;
		this.uri = uri;
	}

	public boolean save() {
		if (uri.getScheme().equals("file") && FileUtils.isIncluded(new File(uri), Portable.getLaunchDirectory())) { //$NON-NLS-1$
			Object[] options = {LocalizationData.get("GenericButton.cancel"),LocalizationData.get("GenericButton.continue")}; //$NON-NLS-1$ //$NON-NLS-2$
			String message = LocalizationData.get("saveDialog.dangerousLocation.message"); //$NON-NLS-1$
			int choice = JOptionPane.showOptionDialog(owner, message,	LocalizationData.get("Generic.warning"), //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]); //$NON-NLS-1$
			if (choice==0) return false;
		}
		final Worker<WriterResult, Void> worker = new SaveWorker(data, uri);
		WorkInProgressFrame waitFrame = PersistenceManager.buildWaitDialog(owner, worker);
		waitFrame.setVisible(true);
		try {
			WriterResult result = worker.get();
			if (result==null) return false; // Saving to local cache was cancelled
			data.setURI(uri);
			data.setChanged(false);
			if (result.getState().equals(WriterResult.State.EXCEPTION_WHILE_SYNC)) {
				if (result.getException() instanceof IOException) {
					// An io error occurred ... probably we're not connected to the Internet
					// Do nothing
				} else {
					// Not an IO error => Log the error
					ErrorManager.INSTANCE.log(owner, result.getException());
				}
			} else {
				SynchronizationState state = result.getSyncState();
				if (state.equals(SynchronizationState.REMOTE_DELETED)) {
					doRemoteDeleted();
				} else if (state.equals(SynchronizationState.CONFLICT)) {
					doConflict();
				}
			}
		} catch (ExecutionException e) {
			// An exception occurred while saving to the cache
			ErrorManager.INSTANCE.log(owner, e.getCause());
			return false;
		} catch (CancellationException e) {
			// The synchronization was cancelled => Do nothing
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	private void doRemoteDeleted() throws ExecutionException {
		RemotePersistencePlugin plugin = (RemotePersistencePlugin) PersistenceManager.MANAGER.getPlugin(uri);
		String message = MessageFormat.format(LocalizationData.get("synchronization.question.other"), plugin.getDeletedMessage()); //$NON-NLS-1$
		Object[] options = {plugin.getUploadActionMessage(), LocalizationData.get("synchronization.deleteCache.action"), LocalizationData.get("GenericButton.cancel")};  //$NON-NLS-1$//$NON-NLS-2$
		int n = JOptionPane.showOptionDialog(owner, message, LocalizationData.get("Generic.warning"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, //$NON-NLS-1$
				null, options, options[2]);
		if (n==2) {
		} else if (n==0) {
			doSync(SynchronizeCommand.UPLOAD);
		} else {
			PersistenceManager.MANAGER.getPlugin(uri).getLocalFile(uri).delete();
		}
	}

	private void doConflict() throws ExecutionException {
		RemotePersistencePlugin plugin = (RemotePersistencePlugin) PersistenceManager.MANAGER.getPlugin(uri);
		String message = MessageFormat.format(LocalizationData.get("synchronization.question.other"), plugin.getConflictMessage()); //$NON-NLS-1$
		Object[] options = {plugin.getUploadActionMessage(), plugin.getDownloadActionMessage(), LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$
		int n = JOptionPane.showOptionDialog(owner, message, LocalizationData.get("Generic.warning"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, //$NON-NLS-1$
				null, options, options[2]);
		if (n==2) {
			// Cancel, do nothing
		} else if (n==0) {
			doSync(SynchronizeCommand.UPLOAD);
		} else {
			doSync(SynchronizeCommand.DOWNLOAD);
		}
	}

	private void doSync(SynchronizeCommand command) throws ExecutionException {
		SynchronizeWorker worker = new SynchronizeWorker(uri, command);
		WorkInProgressFrame waitFrame = PersistenceManager.buildWaitDialog(owner, worker);
		waitFrame.setVisible(true);
		if (command.equals(SynchronizeCommand.DOWNLOAD) && !worker.isCancelled()) {
			data.clear();
			new DataReader(owner, data, uri).doSyncAndRead(SynchronizeCommand.NOTHING);
		}
	}
}
