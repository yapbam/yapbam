package net.yapbam.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.text.StringEscapeUtils;

public class JsonFormatWriter implements ExportWriter {
	private Writer writer;
	private boolean isFirstLine = true;
	private boolean isFirstValue = true;

	public JsonFormatWriter(OutputStream stream, Charset encoding) {
		if (!StandardCharsets.UTF_8.equals(encoding) &&  !StandardCharsets.UTF_16.equals(encoding)
				&&  !StandardCharsets.UTF_16BE.equals(encoding) &&  !StandardCharsets.UTF_16LE.equals(encoding)) {
			throw new IllegalArgumentException("JSON requires UTF encoding");
		}
		this.writer = new OutputStreamWriter(stream, encoding);
	}

	@Override
	public void addHeader() throws IOException {
		this.writer.append("{");
		this.writer.append("\"values\":[");
	}

	@Override
	public void addLineStart() throws IOException {
		if (isFirstLine) {
			isFirstLine = false;
		} else {
			this.writer.append(",");
		}
		this.writer.append("[");
	}

	@Override
	public void addLineEnd() throws IOException {
		this.writer.append("]");
		this.isFirstValue = true;
	}

	@Override
	public void addValue(String value) throws IOException {
		if (isFirstValue) {
			isFirstValue = false;
		} else {
			this.writer.append(",");
		}
		this.writer.append(String.format("\"%s\"", StringEscapeUtils.escapeJson(value)));
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
