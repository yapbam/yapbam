package net.yapbam.gui.persistence.reading;

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

import com.fathzer.soft.jclop.SynchronizationState;
import com.fathzer.soft.jclop.UnreachableHostException;
import com.fathzer.soft.jclop.swing.MessagePack;

import net.yapbam.data.xml.YapbamSerializer;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.dialogs.GetPasswordDialog;
import net.yapbam.gui.persistence.DataWrapper;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.PersistenceAdapter;
import net.yapbam.gui.persistence.SynchronizeCommand;
import net.yapbam.gui.persistence.UnsupportedSchemeException;
import net.yapbam.util.HtmlUtils;

public class DataReader {
	private Window owner;
	private DataWrapper<?> data;
	private URI uri;
	private PersistenceAdapter adapter;
	private PersistenceManager manager;

	public DataReader (PersistenceManager manager, Window owner, DataWrapper<?> data, URI uri) {
		this.owner = owner;
		this.data = data;
		this.uri = uri;
		this.manager = manager;
		this.adapter = manager.getAdapter(uri);
	}

	public boolean read() throws ExecutionException {
		return doSyncAndRead(SynchronizeCommand.SYNCHRONIZE);
	}

	public boolean doSyncAndRead(SynchronizeCommand command) throws ExecutionException {
		if (this.adapter==null) throw new ExecutionException(new UnsupportedSchemeException(uri));
		SyncAndReadWorker basicWorker = new SyncAndReadWorker(adapter.getService(), data, uri, command);
		manager.buildWaitDialog(owner, basicWorker).setVisible(true);
		ReaderResult result;
		try {
			result = basicWorker.get();
			if (result.getState().equals(State.EXCEPTION_WHILE_SYNC)) {
				if (!(result.getException() instanceof IOException)) {
					ErrorManager.INSTANCE.log(owner, result.getException());
				}
				// The synchronization failed => Ask the user what to do
				return doSyncFailed(result.getException());
			} else if (result.getState().equals(State.FINISHED)) {
				// Data was synchronized and red
				data.commit(uri, basicWorker.getData());
				return true;
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (CancellationException e) {
			if (basicWorker.isSynchronizing())	return syncCancelled(); // If the synchronization was cancelled
			// If another phase was cancelled -> Cancel the whole read operation
			return false;
		} catch (ExecutionException e) {
			return doErrorOccurred(e);
		}
		File localFile = adapter.getLocalFile(uri);
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
				throw new RuntimeException ("Unexpected state: data is synchronized, but not red with no password needed"); //$NON-NLS-1$
			}
		} else {
			throw new RuntimeException ("Unexpected state: "+state); //$NON-NLS-1$
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
				if (YapbamSerializer.isPasswordOk(localURI, password)) break; // If the user cancels or entered the right password ... go next step
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

	private boolean doSyncFailed(Throwable throwable) throws ExecutionException {
		boolean internetIsDown = throwable instanceof UnreachableHostException;
		if (!adapter.getLocalFile(uri).exists()) {
			String message = LocalizationData.get("synchronization.downloadFailed");
			if (internetIsDown) message = "<html>"+HtmlUtils.removeHtmlTags(adapter.getMessage("com.fathzer.soft.jclop.connectionFailed"))+"<br><br>"+HtmlUtils.removeHtmlTags(message)+"</html>";
			JOptionPane.showMessageDialog(owner, message, LocalizationData.get("ErrorManager.title"), internetIsDown?JOptionPane.WARNING_MESSAGE:JOptionPane.ERROR_MESSAGE);  //$NON-NLS-1
			return false;
		} else {
			//TODO be more precise in the next messages?
			String[] options = new String[]{LocalizationData.get("GenericButton.yes"), LocalizationData.get("GenericButton.no")};  //$NON-NLS-1$ //$NON-NLS-2$
			if (JOptionPane.showOptionDialog(owner, LocalizationData.get("synchronization.question.failed"), //$NON-NLS-1$
					LocalizationData.get("Generic.warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 0)!=0) { //$NON-NLS-1$
				return false;
			}
			return readLocalFile(null);
		}
	}

	private boolean doErrorOccurred(ExecutionException e) throws ExecutionException {
		// An error occurred while reading the cache file
		if (adapter.getService().isLocal() || (e.getCause() instanceof FileNotFoundException)) throw e; // If not a remote service, or the URI is not found, directly throw the exception
		if (adapter.getService().isLocal()) {
			e.printStackTrace();
			return false;
		} else {
			String[] options = new String[]{LocalizationData.get("GenericButton.yes"), LocalizationData.get("GenericButton.no")};  //$NON-NLS-1$ //$NON-NLS-2$
			if (JOptionPane.showOptionDialog(owner, LocalizationData.get("synchronization.question.cacheCorrupted"), //$NON-NLS-1$
					LocalizationData.get("ErrorManager.title"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, 0)!=0) { //$NON-NLS-1$
				return false;
			}
			adapter.getLocalFile(uri).delete();
			return doSyncAndRead(SynchronizeCommand.DOWNLOAD);
		}
	}
	
	private boolean doRemoteNotFound() throws ExecutionException {
		String message = MessageFormat.format(LocalizationData.get("synchronization.question.other"), adapter.getMessage(MessagePack.REMOTE_MISSING_MESSAGE)); //$NON-NLS-1$
		Object[] options = {adapter.getMessage(MessagePack.UPLOAD_ACTION), LocalizationData.get("synchronization.deleteCache.action"), LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$ //$NON-NLS-2$
		int n = JOptionPane.showOptionDialog(owner, message, LocalizationData.get("Generic.warning"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, //$NON-NLS-1$
				null, options, options[2]);
		if (n==2) {
		} else if (n==0) {
			return doSyncAndRead(SynchronizeCommand.UPLOAD);
		} else {
			adapter.getService().deleteLocal(uri);
		}
		return false;
	}

	private boolean doConflict() throws ExecutionException {
		String message = MessageFormat.format(LocalizationData.get("synchronization.question.other"),adapter.getMessage(MessagePack.CONFLICT_MESSAGE)); //$NON-NLS-1$
		Object[] options = {adapter.getMessage(MessagePack.UPLOAD_ACTION), adapter.getMessage(MessagePack.DOWNLOAD_ACTION), LocalizationData.get("GenericButton.cancel")}; //$NON-NLS-1$ //$NON-NLS-1$
		int n = JOptionPane.showOptionDialog(owner, message, LocalizationData.get("Generic.warning"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, //$NON-NLS-1$
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
		if (!adapter.getLocalFile(uri).exists()) return false;
		String[] options = new String[]{LocalizationData.get("GenericButton.yes"), LocalizationData.get("GenericButton.no")};  //$NON-NLS-1$ //$NON-NLS-2$
		if (JOptionPane.showOptionDialog(owner, LocalizationData.get("synchronization.question.cancelled"), //$NON-NLS-1$
				LocalizationData.get("Generic.warning"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0)!=0) { //$NON-NLS-1$
			return false;
		}
		return readLocalFile(null); 
	}
	
	private boolean readLocalFile(String password) throws ExecutionException {
		URI localURI = adapter.getLocalFile(uri).toURI();
		if (password==null) {
			boolean passwordRequired;
			try {
				passwordRequired = YapbamSerializer.getSerializationData(localURI).isPasswordRequired();
			} catch (IOException e) {
				throw new ExecutionException(e);
			}
			if (passwordRequired) {
				return doPasswordNeeded(localURI);
			}
		}
		final OnlyReadWorker readWorker = new OnlyReadWorker(localURI, password);
		manager.buildWaitDialog(owner, readWorker).setVisible(true);
		try {
			data.commit(uri, readWorker.get());
			return true;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (CancellationException e) {
			return false;
		}
	}
}
