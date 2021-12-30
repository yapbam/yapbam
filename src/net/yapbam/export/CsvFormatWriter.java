package net.yapbam.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

import lombok.AllArgsConstructor;

public class CsvFormatWriter implements ExportWriter {
	@AllArgsConstructor
	public static class CsvExportParameters {
		private Charset encoding;
		private char separator;
		
		public CsvExportParameters() {
			this(Charset.defaultCharset(), ';');
		}

		public CsvExportParameters(char separator) {
			this(Charset.defaultCharset(), separator);
		}
}

	private Writer writer;
	private CSVWriter csv;

	public CsvFormatWriter(OutputStream stream, CsvExportParameters parameters) {
		this.writer = new OutputStreamWriter(stream, parameters.encoding);
		this.csv = new CSVWriter(writer);
		this.csv.setSeparator(parameters.separator);
	}

	@Override
	public void addHeader() throws IOException {
		// Nothing to do
	}

	@Override
	public void addLineStart() throws IOException {
		// Nothing to do
	}

	@Override
	public void addLineEnd() throws IOException {
		csv.newLine();
	}

	@Override
	public void addValue(String value, String... styles) throws IOException {
		csv.writeCell(value);
	}
	
	@Override
	public void addFooter() throws IOException {
		// Nothing to do
	}

	@Override
	public void close() throws IOException {
		csv.flush();
		writer.close();
	}
}
