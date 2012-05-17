package net.yapbam.data;

public interface ProgressReport {
	public void setMax (int length);
	public void reportProgress(int progress);
}