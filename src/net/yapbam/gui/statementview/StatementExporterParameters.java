package net.yapbam.gui.statementview;

import java.net.URL;

import lombok.Getter;
import lombok.Setter;
import net.yapbam.gui.dialogs.export.ExporterParameters;

@Getter
public class StatementExporterParameters extends ExporterParameters {
	private final String statementId;
	private final double startBalance;
	private final double endBalance;
	
	@Setter
	private boolean withStatementId;
	@Setter
	private boolean withStartBalance;
	@Setter
	private boolean withEndBalance;
	@Setter
	private URL css;
	
	public StatementExporterParameters(String statementId, double startBalance, double endBalance) {
		this.statementId = statementId;
		this.startBalance = startBalance;
		this.endBalance = endBalance;
	}
}
