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
	}

	@Override
	public JPanel getPanel() {
		return new BudgetViewPanel(data);
	}

	@Override
	public String getPanelTitle() {
		return LocalizationData.get("BudgetPanel.title"); //$NON-NLS-1$
	}

	@Override
	public String getPanelToolTip() {
		return LocalizationData.get("BudgetPanel.tooltip"); //$NON-NLS-1$
	}
}
