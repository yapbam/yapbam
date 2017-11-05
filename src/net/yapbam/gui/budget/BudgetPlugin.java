package net.yapbam.gui.budget;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.budget.BudgetViewPanel;

public class BudgetPlugin extends AbstractPlugIn {
	private final static String STATE_PREFIX = "BudgetView"; //$NON-NLS-1$
	private static final String SUM_COLUMN = "sumColumn"; //$NON-NLS-1$
	private static final String SUM_LINE = "sumLine"; //$NON-NLS-1$
	private static final String PER_YEAR = "perYear"; //$NON-NLS-1$
	private static final String PER_VALUE_DATE = "perValueDate"; //$NON-NLS-1$
	private static final String GROUP_SUB_CATEGORIES = "grouped.subcategories"; //$NON-NLS-1$
	
	private FilteredData data;
	private BudgetViewPanel panel;
	
	public BudgetPlugin(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		this.setPanelTitle(LocalizationData.get("BudgetPanel.title")); //$NON-NLS-1$
		this.setPanelToolTip(LocalizationData.get("BudgetPanel.tooltip")); //$NON-NLS-1$
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
		YapbamState.INSTANCE.put(STATE_PREFIX+PER_VALUE_DATE, Boolean.toString(panel.getValueDate().isSelected()));
		YapbamState.INSTANCE.put(STATE_PREFIX+GROUP_SUB_CATEGORIES, Boolean.toString(panel.getGroupSubCategories().isSelected()));
	}

	@Override
	public void restoreState() {
		panel.getChckbxAddSumColumn().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(STATE_PREFIX+SUM_COLUMN, "false"))); //$NON-NLS-1$
		panel.getChckbxAddSumLine().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(STATE_PREFIX+SUM_LINE, "false"))); //$NON-NLS-1$
		boolean perYear = Boolean.parseBoolean(YapbamState.INSTANCE.get(STATE_PREFIX+PER_YEAR, "false")); //$NON-NLS-1$
		if (perYear) {
			panel.getYear().setSelected(true);
		}
		boolean perValueDate = Boolean.parseBoolean(YapbamState.INSTANCE.get(STATE_PREFIX+PER_VALUE_DATE, "false")); //$NON-NLS-1$
		if (perValueDate) {
			panel.getValueDate().setSelected(true);
		}
		panel.getGroupSubCategories().setSelected(Boolean.parseBoolean(YapbamState.INSTANCE.get(STATE_PREFIX+GROUP_SUB_CATEGORIES, "false"))); //$NON-NLS-1$
	}
}
