package net.yapbam.gui.persistence.writing;

import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.worker.WorkInProgressFrame;
import com.fathzer.soft.ajlib.swing.worker.Worker;
import com.fathzer.soft.ajlib.utilities.FileUtils;
import com.fathzer.soft.jclop.SynchronizationState;
import com.fathzer.soft.jclop.swing.MessagePack;

import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.PersistenceAdapter;
import net.yapbam.gui.persistence.DataWrapper;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.SynchronizeCommand;
import net.yapbam.gui.persistence.reading.DataReader;
import net.yapbam.util.Portable;

public class DataWriter {
	private Window owner;
	private DataWrapper<?> data;
	private URI uri;
	private PersistenceAdapter adapter;
	private PersistenceManager manager;

	public DataWriter (PersistenceManager manager, Window owner, DataWrapper<?> data, URI uri) {
		this.owner = owner;
		this.data = data;
		this.uri = uri;
		this.manager = manager;
		this.adapter = manager.getAdapter(uri);
	}

	public boolean save() {
		if ("file".equals(uri.getScheme()) && FileUtils.isIncluded(new File(uri), Portable.getApplicationDirectory())) { //$NON-NLS-1$
			Object[] options = {LocalizationData.get("GenericButton.cancel"),LocalizationData.get("GenericButton.continue")}; //$NON-NLS-1$ //$NON-NLS-2$
			String message = LocalizationData.get("saveDialog.dangerousLocation.message"); //$NON-NLS-1$
			int choice = JOptionPane.showOptionDialog(owner, message,	LocalizationData.get("Generic.warning"), //$NON-NLS-1$
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]); //$NON-NLS-1$
			if (choice==0) {
				return false;
			}
		}
		final Worker<WriterResult, Void> worker = new SaveWorker(manager,data, uri);
		WorkInProgressFrame waitFrame = manager.buildWaitDialog(owner, worker);
		waitFrame.setVisible(true);
		try {
			WriterResult result = worker.get();
			if (result==null) {
				// Saving to local cache was cancelled
				return false;
			}
			data.setURI(uri);
			data.setUnchanged();
			if (result.getState().equals(WriterResult.State.EXCEPTION_WHILE_SYNC)) {
				if (result.getException() instanceof IOException) {
					// An io error occurred ... probably we're not connected to the Internet
					// Do nothing
					//FIXME Should at least inform the user the synchronization failed (See DataReader.doSyncFailed).
					result.getException().printStackTrace();//TODO
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
			if (e.getCause() instanceof FileNotFoundException) {
				JOptionPane.showMessageDialog(owner, LocalizationData.get("saveDialog.fileNotWritable"), LocalizationData.get("Generic.error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				ErrorManager.INSTANCE.log(owner, e.getCause());
			}
			return false;
		} catch (CancellationException e) {
			// The synchronization was cancelled => Mark the file as saved
			data.setURI(uri);
			data.setUnchanged();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	private void doRemoteDeleted() throws ExecutionException {
		String message = Formatter.format(LocalizationData.get("synchronization.question.other"), adapter.getMessage(MessagePack.REMOTE_MISSING_MESSAGE)); //$NON-NLS-1$
		Object[] options = {adapter.getMessage(MessagePack.UPLOAD_ACTION), LocalizationData.get("synchronization.deleteCache.action"), LocalizationData.get("GenericButton.cancel")};  //$NON-NLS-1$//$NON-NLS-2$
		int n = JOptionPane.showOptionDialog(owner, message, LocalizationData.get("Generic.warning"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, //$NON-NLS-1$
				null, options, options[2]);
		if (n==2) {
		} else if (n==0) {
			doSync(SynchronizeCommand.UPLOAD);
		} else {
			adapter.getService().deleteLocal(uri);
		}
	}

	private void doConflict() throws ExecutionException {
		String message = Formatter.format(LocalizationData.get("synchronization.question.other"), adapter.getMessage(MessagePack.CONFLICT_MESSAGE)); //$NON-NLS-1$
		Object[] options = {adapter.getMessage(MessagePack.UPLOAD_ACTION), adapter.getMessage(MessagePack.DOWNLOAD_ACTION), LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$
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
		SynchronizeWorker worker = new SynchronizeWorker(adapter.getService(), uri, command, LocalizationData.getLocale());
		WorkInProgressFrame waitFrame = manager.buildWaitDialog(owner, worker);
		waitFrame.setVisible(true);
		if (command.equals(SynchronizeCommand.DOWNLOAD) && !worker.isCancelled()) {
			//TODO Strange seems like we clear the data before having read them
			// This could results in having the data cleared if the user cancels the download !!!
			data.clear();
			new DataReader(manager, owner, data, uri).doSyncAndRead(SynchronizeCommand.NOTHING);
		}
	}
}
