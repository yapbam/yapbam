package net.yapbam.gui.persistence;

/** A command that specifies what to do during synchronization phase. */
public enum SynchronizeCommand {
	SYNCHRONIZE,
	UPLOAD,
	DOWNLOAD,
	NOTHING
}