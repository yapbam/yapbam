package net.yapbam.export;

import java.io.IOException;
import java.util.Date;

import net.yapbam.gui.dialogs.export.ExporterParameters;

public abstract class Exporter<P extends ExporterParameters,C> {
	private final P parameters;

	protected Exporter(P parameters) {
		this.parameters = parameters;
	}
	
	public String format(Object obj) {
		if (obj == null) {
			return ""; //$NON-NLS-1$
		} else if (obj instanceof Date) {
			return parameters.getDateFormat().format(obj);
		} else if (obj instanceof Double) {
			return parameters.getAmountFormat().format(obj);
		} else {
			return obj.toString();
		}
	}
	
	public P getParameters() {
		return parameters;
	}
	
	public abstract void export(C content, ExportWriter formatter) throws IOException;
}