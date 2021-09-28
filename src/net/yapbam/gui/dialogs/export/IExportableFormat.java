package net.yapbam.gui.dialogs.export;

import java.io.IOException;

public interface IExportableFormat {

	public void addHeader() throws IOException;

	public void addLineStart() throws IOException;

	public void addLineEnd() throws IOException;

	public void addValue(String value) throws IOException;
	
	public void addFooter() throws IOException;

	public void close() throws IOException;
	
}
