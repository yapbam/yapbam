package net.yapbam.export;

import java.io.IOException;

import lombok.Getter;
import net.yapbam.gui.dialogs.export.ExporterParameters;

public abstract class Exporter<P extends ExporterParameters,C> {
	@Getter
	private final P parameters;

	protected Exporter(P parameters) {
		this.parameters = parameters;
	}
	
	public abstract void export(C content, ExportWriter formatter) throws IOException;
}