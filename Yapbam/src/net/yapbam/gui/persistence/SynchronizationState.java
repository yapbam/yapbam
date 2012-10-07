package net.yapbam.gui.persistence;

public enum SynchronizationState {
		/** Remote data and local cache are synchronized. */
		SYNCHRONIZED,
		/** A local cache exists, but remote resource doesn't exist. */
		REMOTE_DELETED,
		/** The local cache is newer than remote data. */
		CONFLICT,
//		/** The remote data is newer than local cache. */
//		REMOTE_IS_NEWER
	}