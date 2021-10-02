package net.yapbam.gui.dialogs.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringEscapeUtils;

public class ExporterJsonFormat implements IExportableFormat {

	private Writer writer;

	public ExporterJsonFormat(OutputStream stream, Charset encoding) {
		this.writer = new OutputStreamWriter(stream, encoding);
	}

	@Override
	public void addHeader() throws IOException {
		this.writer.append("{");
		this.writer.append("\"values\":[");
	}

	@Override
	public void addLineStart() throws IOException {
		this.writer.append("[");
	}

	@Override
	public void addLineEnd() throws IOException {
		this.writer.append("],");
	}

	@Override
	public void addValue(String value) throws IOException {
		this.writer.append(String.format( //
				"\"%s\"", //
				StringEscapeUtils.escapeJson(value) //
		));
		this.writer.append(",");
	}

	@Override
	public void addFooter() throws IOException {
		this.writer.append("]}");
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

}
