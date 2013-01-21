package net.yapbam.gui.persistence.writing;

import com.fathzer.soft.jclop.SynchronizationState;

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
