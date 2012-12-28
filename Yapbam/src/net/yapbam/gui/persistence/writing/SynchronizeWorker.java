package net.yapbam.gui.persistence.writing;

import java.net.URI;

import com.fathzer.soft.jclop.Cancellable;

import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.persistence.CancelManager;
import net.yapbam.gui.persistence.SynchronizationState;
import net.yapbam.gui.persistence.SynchronizeCommand;
import net.yapbam.gui.persistence.Synchronizer;

public class SynchronizeWorker extends Worker<SynchronizationState, Void> implements Cancellable {
	private URI uri;
	private SynchronizeCommand command;
	private CancelManager cancelManager;

	SynchronizeWorker(URI uri, SynchronizeCommand command) {
		this.uri = uri;
		this.command = command;
		this.cancelManager = new CancelManager(this);
	}

	@Override
	protected SynchronizationState doProcessing() throws Exception {
		if (command.equals(SynchronizeCommand.SYNCHRONIZE)) {
			setPhase(LocalizationData.get("synchronization.synchronizing"), -1); //$NON-NLS-1$
			return Synchronizer.backgroundSynchronize(uri, this);
		} else if (command.equals(SynchronizeCommand.UPLOAD)) {
			setPhase(LocalizationData.get("synchronization.uploading"), -1); //$NON-NLS-1$
			Synchronizer.backgroungUpload(uri, this);
			return SynchronizationState.SYNCHRONIZED;
		} else if (command.equals(SynchronizeCommand.DOWNLOAD)) {
			setPhase(LocalizationData.get("synchronization.downloading"), -1); //$NON-NLS-1$
			Synchronizer.backgroundDownload(uri, this);
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
}
