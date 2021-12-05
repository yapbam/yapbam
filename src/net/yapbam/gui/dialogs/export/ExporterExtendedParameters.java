package net.yapbam.gui.dialogs.export;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExporterExtendedParameters {

	private String statementId;
	private String startBalance;
	private String endBalance;
	private File css;

	public ExporterExtendedParameters() {
		super();
	}

}