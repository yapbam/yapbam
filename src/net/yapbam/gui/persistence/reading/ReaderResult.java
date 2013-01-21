package net.yapbam.gui.persistence.reading;

import com.fathzer.soft.jclop.SynchronizationState;

/** A Worker that tries to synchronize data and then read the local cache. */
class ReaderResult {
	private State state;
	private SynchronizationState syncState;
	private Throwable e;
	
	public ReaderResult(State state, SynchronizationState syncState, Throwable e) {
		super();
		this.state = state;
		this.syncState = syncState;
		this.e = e;
	}

	public ReaderResult(State state, SynchronizationState syncState) {
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
	 * @return the e
	 */
	public Throwable getException() {
		return e;
	}
	
}
