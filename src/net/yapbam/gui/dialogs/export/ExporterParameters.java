package net.yapbam.gui.dialogs.export;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import net.yapbam.export.FormatParams;

@Setter
public class ExporterParameters<D> implements Serializable {
	private static final long serialVersionUID = 3L;
	
	@Getter
	private FormatParams formatParams;
	@Getter
	private final transient D dataExtension;

	public ExporterParameters(D dataExtension) {
		super();
		this.formatParams = null;
		this.dataExtension = dataExtension;
	}
	
	public String format(Object obj) {
		return formatParams.format(obj);
	}

}