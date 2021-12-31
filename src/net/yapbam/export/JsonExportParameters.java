package net.yapbam.export;

public class JsonExportParameters implements FormatParams {
	private static final long serialVersionUID = 1L;

	@Override
	public ExportFormatType getType() {
		return ExportFormatType.JSON;
	}

}
