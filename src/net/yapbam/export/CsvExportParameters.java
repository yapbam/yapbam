package net.yapbam.export;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.yapbam.gui.LocalizationData;

public class CsvExportParameters extends BasicFormatParams {
	private static final long serialVersionUID = 1L;

	private transient Charset encoding;
	private String encodingAsString;
	@Getter
	@Setter
	private char separator;

	public CsvExportParameters() {
		this(Charset.defaultCharset(), ';');
	}

	public CsvExportParameters(char separator) {
		this(Charset.defaultCharset(), separator);
	}
	
	public CsvExportParameters(@NonNull Charset encoding, char separator) {
		super(new NumberFormatParams(LocalizationData.getLocale(), true));
		this.encoding = encoding;
		this.encodingAsString = encoding.name();
		this.separator = separator;
	}

	@Override
	public ExportFormatType getType() {
		return ExportFormatType.CSV;
	}

	public Charset getEncoding() {
		if (encoding==null) {
			encoding = Charset.forName(encodingAsString);
		}
		return encoding;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((encodingAsString == null) ? 0 : encodingAsString.hashCode());
		result = prime * result + separator;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CsvExportParameters other = (CsvExportParameters) obj;
		return encodingAsString.equals(other.encodingAsString) && separator==other.separator;
	}
}