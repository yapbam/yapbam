package net.yapbam.gui.dialogs.export;

public class ExporterJsonFormat implements IExportableFormat {

	private StringBuffer writer;

	public ExporterJsonFormat() {
		this.writer = new StringBuffer();
	}

	@Override
	public void addHeader() {
		this.writer.append("{");
		this.writer.append("\"values\":[");
	}

	@Override
	public void addLineStart() {
		this.writer.append("[");
	}

	@Override
	public void addLineEnd() {
		this.writer.append("],");
	}

	@Override
	public void addValue(String value) {
		this.writer.append(String.format("\"%s\"", value));
		addValueSeparator();
	}

	@Override
	public void addValueSeparator() {
		this.writer.append(",");
	}

	@Override
	public void addFooter() {
		this.writer.append("]}");
	}

	@Override
	public String flushAndGetResultl() {
		return this.writer.toString();
	}

}
