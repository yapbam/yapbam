package net.yapbam.gui.persistence;

import java.net.URI;

import net.astesana.ajlib.swing.worker.Worker;

class SynchroWorker extends Worker<SynchronizationState, Void> implements Cancellable {
	private URI uri;
	SynchroWorker(URI uri) {
		this.uri = uri;
	}
	@Override
	protected SynchronizationState doProcessing() throws Exception {
		setPhase("Synchronizing", -1);
		return Synchronizer.backgroundSynchronize(uri, this);
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
}