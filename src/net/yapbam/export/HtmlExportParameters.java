package net.yapbam.export;

import java.net.URL;

import lombok.AccessLevel;
import lombok.Getter;
import net.yapbam.export.HtmlFormatWriter.HeaderAndFooterBuilder;
import net.yapbam.gui.LocalizationData;

public class HtmlExportParameters extends BasicFormatParams {
	private static final long serialVersionUID = 1L;

	@Getter
	private URL css;
	@Getter(value=AccessLevel.PACKAGE)
	private transient HeaderAndFooterBuilder headAndFoot; //FIXME Probably should not be there but in an exporter subclass
	
	public HtmlExportParameters(URL css, HeaderAndFooterBuilder headAndFoot) {
		super(new NumberFormatParams(LocalizationData.getLocale(), false));
		this.css = css;
		this.headAndFoot = headAndFoot;
	}
	
	public HtmlExportParameters() {
		this(null, new HeaderAndFooterBuilder());
	}

	@Override
	public ExportFormatType getType() {
		return ExportFormatType.HTML;
	}
}