package net.yapbam.gui;

interface BackgroundTaskContext {
	void exceptionOccured(Throwable cause);
	void doAfter();
}
