package net.yapbam.gui.dialogs.export;

import java.io.IOException;

public interface Exporter<T> {
	void export(T content, IExportableFormat formatter) throws IOException;
}