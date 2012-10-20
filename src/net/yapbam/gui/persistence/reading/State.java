package net.yapbam.gui.persistence.reading;

/** The result of a background synchronize and read operation. */
public enum State {
	FINISHED,
	REQUEST_USER,
	NEED_PASSWORD,
	EXCEPTION_WHILE_SYNC
}