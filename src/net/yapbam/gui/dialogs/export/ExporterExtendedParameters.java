package net.yapbam.gui.dialogs.export;

import java.net.URL;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExporterExtendedParameters {

	private String statementId;
	private String startBalance;
	private String endBalance;
	private URL css;

	public ExporterExtendedParameters() {
		super();
	}

}