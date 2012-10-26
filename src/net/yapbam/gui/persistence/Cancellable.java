package net.yapbam.gui.persistence;

public interface Cancellable {
	public boolean isCancelled();
	public void setPhase(String phase, int max);
	public void reportProgress(int progress);
	public void setCancelAction(Runnable action);
}
