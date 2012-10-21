package net.yapbam.gui.persistence.writing;

import java.net.URI;

import net.astesana.ajlib.swing.worker.Worker;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.persistence.Cancellable;
import net.yapbam.gui.persistence.SynchronizationState;
import net.yapbam.gui.persistence.SynchronizeCommand;
import net.yapbam.gui.persistence.Synchronizer;

public class SynchronizeWorker extends Worker<SynchronizationState, Void> implements Cancellable {
	private URI uri;
	private SynchronizeCommand command;
	private GlobalData data;
	
	SynchronizeWorker(URI uri, GlobalData data, SynchronizeCommand command) {
		this.uri = uri;
		this.command = command;
		this.data = data;
	}

	@Override
	protected SynchronizationState doProcessing() throws Exception {
		if (command.equals(SynchronizeCommand.SYNCHRONIZE)) {
			setPhase("Synchronizing", -1);
			return Synchronizer.backgroundSynchronize(uri, this);
		} else if (command.equals(SynchronizeCommand.UPLOAD)) {
			setPhase("Uploading", -1);
			Synchronizer.backgroungUpload(uri, this);
			return SynchronizationState.SYNCHRONIZED;
		} else if (command.equals(SynchronizeCommand.DOWNLOAD)) {
			setPhase("Downloading", -1);
			Synchronizer.backgroundDownload(uri, this);
			return SynchronizationState.SYNCHRONIZED;
		} else {
			throw new IllegalArgumentException(command+" is unknown");
		}
	}

	@Override
	public void cancel() {
		this.cancel();
	}
	@Override
	public void reportProgress(int progress) {
		super.reportProgress(progress);
	}
	@Override
	public void setPhase(String phase, int length) {
		super.setPhase(phase, length);
	}
}
