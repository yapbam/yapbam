package net.yapbam.gui.persistence.reading;

import java.io.FileNotFoundException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Locale;

import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Service;
import com.fathzer.soft.jclop.SynchronizationState;

import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.data.GlobalData;
import net.yapbam.data.ProgressReport;
import net.yapbam.data.xml.Serializer;
import net.yapbam.data.xml.Serializer.SerializationData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.CancelManager;
import net.yapbam.gui.persistence.SynchronizeCommand;

class SyncAndReadWorker extends Worker<ReaderResult, Void> implements Cancellable, ProgressReport {
	private URI uri;
	private GlobalData data;
	private boolean isSynchronizing;
	
	private SynchronizeCommand command;
	private CancelManager cancelManager;
	private Service service;
	private Locale locale;
	
	SyncAndReadWorker(Service service, URI uri, SynchronizeCommand command) {
		this.service = service;
		this.uri = uri;
		this.data = null;
		this.command = command;
		this.locale = LocalizationData.getLocale();
		this.cancelManager = new CancelManager(this);
	}
	
	@Override
	protected ReaderResult doProcessing() throws Exception {
		SynchronizationState syncState;
		if (!command.equals(SynchronizeCommand.NOTHING)) {
			this.isSynchronizing = true;
			try {
				if (command.equals(SynchronizeCommand.SYNCHRONIZE)) {
					setPhase(LocalizationData.get("synchronization.synchronizing"), -1); //$NON-NLS-1$
					syncState = service.synchronize(uri, this, locale);
				} else if (command.equals(SynchronizeCommand.UPLOAD)) {
					setPhase(LocalizationData.get("synchronization.uploading"), -1); //$NON-NLS-1$
					service.upload(uri, this, locale);
					syncState = SynchronizationState.SYNCHRONIZED;
				} else if (command.equals(SynchronizeCommand.DOWNLOAD)) {
					setPhase(LocalizationData.get("synchronization.downloading"), -1); //$NON-NLS-1$
					service.download(uri, this, locale);
					syncState = SynchronizationState.SYNCHRONIZED;
				} else {
					throw new IllegalArgumentException(command+" is unknown"); //$NON-NLS-1$
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
			URI localURI = service.getLocalFile(uri).toURI();
			SerializationData info = Serializer.getSerializationData(localURI);
			// Retrieving the file password
			if (info.isPasswordRequired()) {
				return new ReaderResult(State.NEED_PASSWORD, syncState);
			} else {
				setPhase(MessageFormat.format(LocalizationData.get("Generic.wait.readingFrom"), service.getDisplayable(uri)),-1); //$NON-NLS-1$
				this.data = Serializer.read(localURI, null, this);
				return new ReaderResult(State.FINISHED, syncState);
			}
		} else {
			return new ReaderResult(State.REQUEST_USER, syncState);
		}
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
	
	@Override
	public void setCancelAction(Runnable action) {
		this.cancelManager.setAction(action);
	}
}