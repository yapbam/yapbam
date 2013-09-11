package net.yapbam.gui.persistence.writing;

import java.net.URI;
import java.util.Locale;

import com.fathzer.soft.ajlib.swing.worker.Worker;
import com.fathzer.soft.jclop.Cancellable;
import com.fathzer.soft.jclop.Service;
import com.fathzer.soft.jclop.SynchronizationState;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.CancelManager;
import net.yapbam.gui.persistence.SynchronizeCommand;

public class SynchronizeWorker extends Worker<SynchronizationState, Void> implements Cancellable {
	private URI uri;
	private SynchronizeCommand command;
	private CancelManager cancelManager;
	private Locale locale;
	private Service service;

	SynchronizeWorker(Service service, URI uri, SynchronizeCommand command, Locale locale) {
		this.service = service;
		this.uri = uri;
		this.command = command;
		this.locale = locale;
		this.cancelManager = new CancelManager(this);
	}

	@Override
	protected SynchronizationState doProcessing() throws Exception {
		if (command.equals(SynchronizeCommand.SYNCHRONIZE)) {
			setPhase(LocalizationData.get("synchronization.synchronizing"), -1); //$NON-NLS-1$
			return service.synchronize(uri, this, locale);
		} else if (command.equals(SynchronizeCommand.UPLOAD)) {
			setPhase(LocalizationData.get("synchronization.uploading"), -1); //$NON-NLS-1$
			service.upload(uri, this, locale);
			return SynchronizationState.SYNCHRONIZED;
		} else if (command.equals(SynchronizeCommand.DOWNLOAD)) {
			setPhase(LocalizationData.get("synchronization.downloading"), -1); //$NON-NLS-1$
			service.download(uri, this, locale);
			return SynchronizationState.SYNCHRONIZED;
		} else {
			throw new IllegalArgumentException(command+" is unknown"); //$NON-NLS-1$
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
	public void setCancelAction(Runnable action) {
		this.cancelManager.setAction(action);
	}

	@Override
	public void setMax(int max) {
		super.setPhaseLength(max);
	}
}
