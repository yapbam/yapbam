package net.yapbam.gui.persistence;

import com.fathzer.soft.jclop.SynchronizationState;

public class SynchronizationResult {
	private SynchronizationState state;
	private String remoteRevision;
	private String localRevision;
	
	public SynchronizationResult(SynchronizationState state, String remoteRevision, String localRevision) {
		super();
		this.state = state;
		this.remoteRevision = remoteRevision;
		this.localRevision = localRevision;
	}
	
	/**
	 * @return the state
	 */
	public SynchronizationState getState() {
		return state;
	}
	/**
	 * @return the remoteRevision
	 */
	public String getRemoteRevision() {
		return remoteRevision;
	}
	/**
	 * @return the localRevision
	 */
	public String getLocalRevision() {
		return localRevision;
	}
}
