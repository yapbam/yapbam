package net.yapbam.gui.budget;

import lombok.Getter;
import net.yapbam.gui.dialogs.export.ExporterParameters;

@Getter
public class BudgetExporterParameters extends ExporterParameters {
	private String dateSumWording;
	private String categorySumWording;
	
	public BudgetExporterParameters(String dateSumWording, String categorySumWording) {
		this.dateSumWording = dateSumWording;
		this.categorySumWording = categorySumWording;
		super.setSeparator('\t');
	}
}
