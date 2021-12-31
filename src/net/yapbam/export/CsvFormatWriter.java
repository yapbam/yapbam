package net.yapbam.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.fathzer.soft.ajlib.utilities.CSVWriter;

public class CsvFormatWriter implements ExportWriter {
	private Writer writer;
	private CSVWriter csv;

	public CsvFormatWriter(OutputStream stream, CsvExportParameters parameters) {
		this.writer = new OutputStreamWriter(stream, parameters.getEncoding());
		this.csv = new CSVWriter(writer);
		this.csv.setSeparator(parameters.getSeparator());
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
