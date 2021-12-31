package net.yapbam.export;

import net.yapbam.gui.LocalizationData;

public class JsonExportParameters extends BasicFormatParams {
	private static final long serialVersionUID = 1L;

	public JsonExportParameters() {
		super(new NumberFormatParams(LocalizationData.getLocale(), true));
	}

	@Override
	public ExportFormatType getType() {
		return ExportFormatType.JSON;
	}
}
