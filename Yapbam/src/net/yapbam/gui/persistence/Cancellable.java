package net.yapbam.gui.persistence;

public interface Cancellable {
	public boolean isCancelled();
	public void cancel();
	public void setPhaseLength(int max);
	public void reportProgress(int progress);
}
