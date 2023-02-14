package net.yapbam.export;

import java.io.Closeable;
import java.io.IOException;

public interface ExportWriter extends Closeable {
	public void addHeader() throws IOException;

	public void addLineStart() throws IOException;

	public void addLineEnd() throws IOException;

	public void addValue(String value) throws IOException;
	
	public void addFooter() throws IOException;

	public void close() throws IOException;
	
}
