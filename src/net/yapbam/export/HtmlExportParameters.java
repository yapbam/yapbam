package net.yapbam.export;

import java.net.URL;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.yapbam.export.HtmlFormatWriter.HeaderAndFooterBuilder;

@AllArgsConstructor
public class HtmlExportParameters implements FormatParams {
	private static final long serialVersionUID = 1L;

	@Getter
	private URL css;
	@Getter(value=AccessLevel.PACKAGE)
	private transient HeaderAndFooterBuilder headAndFoot;
	
	public HtmlExportParameters() {
		this(null, new HeaderAndFooterBuilder());
	}

	@Override
	public ExportFormatType getType() {
		return ExportFormatType.HTML;
	}
}