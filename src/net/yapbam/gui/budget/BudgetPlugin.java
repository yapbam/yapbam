package net.yapbam.gui.budget;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.budget.BudgetViewPanel;

public class BudgetPlugin extends AbstractPlugIn {
	private FilteredData data;
	
	public BudgetPlugin(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		this.setPanelTitle(LocalizationData.get("BudgetPanel.title"));
		this.setPanelToolTip(LocalizationData.get("BudgetPanel.tooltip"));
	}

	@Override
	public JPanel getPanel() {
		return new BudgetViewPanel(data);
	}
}
