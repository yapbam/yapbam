package net.yapbam.gui.dialogs.export;

public interface IExportableFormat {

	public void addHeader();

	public void addLineStart();

	public void addLineEnd();

	public void addValue(String value);
	
	public void addValueSeparator();

	public void addFooter();

	public String flushAndGetResultl();
	
}
