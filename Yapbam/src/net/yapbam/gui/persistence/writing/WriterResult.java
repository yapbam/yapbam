package net.yapbam.gui.persistence.writing;

import net.yapbam.gui.persistence.SynchronizationState;

class WriterResult {
	public enum State {
		FINISHED,
		EXCEPTION_WHILE_SYNC
	}
	
	private State state;
	private SynchronizationState syncState;
	private Throwable e;
	
	public WriterResult(State state, SynchronizationState syncState, Throwable e) {
		super();
		this.state = state;
		this.syncState = syncState;
		this.e = e;
	}

	public WriterResult(State state, SynchronizationState syncState) {
		this(state, syncState, null);
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @return the syncState
	 */
	public SynchronizationState getSyncState() {
		return syncState;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return e;
	}
}
