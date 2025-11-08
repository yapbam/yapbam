package net.yapbam.gui.budget;

import net.yapbam.gui.dialogs.export.ExporterParameters;

public class BudgetExporterParameters extends ExporterParameters {
	private String dateSumWording;
	private String categorySumWording;
	
	public BudgetExporterParameters(String dateSumWording, String categorySumWording) {
		this.dateSumWording = dateSumWording;
		this.categorySumWording = categorySumWording;
		super.setSeparator('\t');
	}

	public String getDateSumWording() {
		return dateSumWording;
	}

	public String getCategorySumWording() {
		return categorySumWording;
	}
}
