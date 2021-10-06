package net.yapbam.export;

import java.io.IOException;

public interface Exporter<T> {
	void export(T content, ExportWriter formatter) throws IOException;
}