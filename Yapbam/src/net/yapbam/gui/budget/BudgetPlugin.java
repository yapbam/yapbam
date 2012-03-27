package net.yapbam.gui.budget;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.budget.BudgetViewPanel;

public class BudgetPlugin extends AbstractPlugIn {
	private final static String STATE_PREFIX = "BudgetView";
	private static final String SUM_COLUMN = "sumColumn";
	private static final String SUM_LINE = "sumLine";
	private static final String PER_YEAR = "perYear";
	
	private FilteredData data;
	private BudgetViewPanel panel;
	
	public BudgetPlugin(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		this.setPanelTitle(LocalizationData.get("BudgetPanel.title"));
		this.setPanelToolTip(LocalizationData.get("BudgetPanel.tooltip"));
	}

	@Override
	public JPanel getPanel() {
		panel = new BudgetViewPanel(data);
		return panel;
	}

	@Override
	public void saveState() {
		YapbamState.INSTANCE.put(STATE_PREFIX+SUM_COLUMN, Boolean.toString(panel.getChckbxAddSumColumn().isSelected()));
		YapbamState.INSTANCE.put(STATE_PREFIX+SUM_LINE, Boolean.toString(panel.getChckbxAddSumLine().isSelected()));
		YapbamState.INSTANCE.put(STATE_PREFIX+PER_YEAR, Boolean.toString(panel.getYear().isSelected()));
	}

	@Override
	public void restoreState() {
		panel.getChckbxAddSumColumn().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(STATE_PREFIX+SUM_COLUMN, "false")));
		panel.getChckbxAddSumLine().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(STATE_PREFIX+SUM_LINE, "false")));
		boolean perYear = Boolean.parseBoolean(YapbamState.INSTANCE.get(STATE_PREFIX+PER_YEAR, "false"));
		if (perYear) panel.getYear().setSelected(true);
	}
}
