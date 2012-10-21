package net.yapbam.gui.persistence.reading;

import java.io.FileNotFoundException;
import java.net.URI;
import java.text.MessageFormat;

import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.Serializer;
import net.yapbam.data.xml.Serializer.SerializationData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.Cancellable;
import net.yapbam.gui.persistence.PersistenceManager;
import net.yapbam.gui.persistence.PersistencePlugin;
import net.yapbam.gui.persistence.SynchronizationState;
import net.yapbam.gui.persistence.Synchronizer;

class SyncAndReadWorker extends Worker<ReaderResult, Void> implements Cancellable, ProgressReport {
	private URI uri;
	private GlobalData data;
	private boolean isSynchronizing;
	
	private SynchronizeCommand command;
	
	/** A command that specifies what to do during synchronization phase. */
	enum SynchronizeCommand {
		SYNCHRONIZE,
		UPLOAD,
		DOWNLOAD,
		NOTHING
	}
	
	SyncAndReadWorker(URI uri, SynchronizeCommand command) {
		this.uri = uri;
		this.data = null;
		
		this.command = command;
	}
	
	@Override
	protected ReaderResult doProcessing() throws Exception {
		SynchronizationState syncState;
		if (!command.equals(SynchronizeCommand.NOTHING)) {
			this.isSynchronizing = true;
			try {
				if (command.equals(SynchronizeCommand.SYNCHRONIZE)) {
					setPhase("Synchronizing", -1);
					syncState = Synchronizer.backgroundSynchronize(uri, this);
				} else if (command.equals(SynchronizeCommand.UPLOAD)) {
					setPhase("Uploading", -1);
					Synchronizer.backgroungUpload(uri, this);
					syncState = SynchronizationState.SYNCHRONIZED;
				} else if (command.equals(SynchronizeCommand.DOWNLOAD)) {
					setPhase("Downloading", -1);
					syncState = SynchronizationState.SYNCHRONIZED;
				} else {
					throw new IllegalArgumentException(command+" is unknown");
				}
			} catch (FileNotFoundException e) {
				throw e;
			} catch (Exception e) {
				return new ReaderResult(State.EXCEPTION_WHILE_SYNC, null, e); 
			}
			if (this.isCancelled()) return null;
		} else {
			syncState = SynchronizationState.SYNCHRONIZED;
		}
		this.isSynchronizing = false;
		if (syncState.equals(SynchronizationState.SYNCHRONIZED)) {
			PersistencePlugin plugin = PersistenceManager.MANAGER.getPlugin(uri);
			URI localURI = plugin.getLocalFile(uri).toURI();
			SerializationData info = Serializer.getSerializationData(localURI);
			// Retrieving the file password
			if (info.isPasswordRequired()) {
				return new ReaderResult(State.NEED_PASSWORD, syncState);
			} else {
				setPhase(MessageFormat.format(LocalizationData.get("Generic.wait.readingFrom"), plugin.getDisplayableName(uri)),-1); //$NON-NLS-1$
				this.data = Serializer.read(localURI, null, this);
				return new ReaderResult(State.FINISHED, syncState);
			}
		} else {
			return new ReaderResult(State.REQUEST_USER, syncState);
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