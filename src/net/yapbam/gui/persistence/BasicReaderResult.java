package net.yapbam.gui.persistence;

class BasicReaderResult {
	public enum State {
		FINISHED,
		REQUEST_USER,
		NEED_PASSWORD,
		EXCEPTION_WHILE_SYNC
	}
	
	private State state;
	private SynchronizationState syncState;
	private Throwable e;
	
	public BasicReaderResult(State state, SynchronizationState syncState, Throwable e) {
		super();
		this.state = state;
		this.syncState = syncState;
		this.e = e;
	}

	public BasicReaderResult(State state, SynchronizationState syncState) {
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
