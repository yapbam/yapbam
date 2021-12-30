package net.yapbam.gui.statementview;

import lombok.Getter;
import lombok.Setter;

@Getter
class ExtraExportData {
	private final String statementId;
	private final double startBalance;
	private final double endBalance;
	
	@Setter
	private boolean withStatementId;
	@Setter
	private boolean withStartBalance;
	@Setter
	private boolean withEndBalance;
	
	public ExtraExportData(String statementId, double startBalance, double endBalance) {
		this.statementId = statementId;
		this.startBalance = startBalance;
		this.endBalance = endBalance;
	}
}