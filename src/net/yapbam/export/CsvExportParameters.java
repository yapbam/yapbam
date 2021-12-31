package net.yapbam.export;

import java.nio.charset.Charset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CsvExportParameters implements FormatParams {
	private static final long serialVersionUID = 1L;

	private Charset encoding;
	private char separator;

	public CsvExportParameters() {
		this(Charset.defaultCharset(), ';');
	}

	public CsvExportParameters(char separator) {
		this(Charset.defaultCharset(), separator);
	}

	@Override
	public ExportFormatType getType() {
		return ExportFormatType.CSV;
	}
}