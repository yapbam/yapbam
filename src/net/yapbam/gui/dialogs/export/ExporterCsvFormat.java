package net.yapbam.gui.dialogs.export;

import java.io.StringWriter;

public class ExporterCsvFormat implements IExportableFormat {

	private char separator;

	private StringWriter stringWriter;

	public ExporterCsvFormat(char separator) {
		this.separator = separator;
		this.stringWriter = new StringWriter();
	}

	@Override
	public void addHeader() {
		return;
	}

	@Override
	public void addLineStart() {
		return;
	}

	@Override
	public void addLineEnd() {
		stringWriter.append("\n");
	}

	@Override
	public void addValue(String value) {
		stringWriter.append(value);
		addValueSeparator();
	}
	
	@Override
	public void addValueSeparator() {
		stringWriter.append(separator);
	}

	@Override
	public void addFooter() {
		return;
	}

	@Override
	public String flushAndGetResultl() {
		return stringWriter.toString();
	}

}
