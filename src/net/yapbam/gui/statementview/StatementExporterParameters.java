package net.yapbam.gui.statementview;

import lombok.Getter;
import net.yapbam.gui.dialogs.export.ExporterParameters;

@Getter
public class StatementExporterParameters extends ExporterParameters {
	
	private String statementId;
	private String startBalance;
	private String endBalance;
	
	public StatementExporterParameters(String statementId, String startBalance, String endBalance) {
		this.statementId = statementId;
		this.startBalance = startBalance;
		this.endBalance = endBalance;
	}
}
