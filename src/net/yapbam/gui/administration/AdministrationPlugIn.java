package net.yapbam.gui.administration;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

public class AdministrationPlugIn extends AbstractPlugIn {
	private AdministrationPanel panel;

	public AdministrationPlugIn(FilteredData filteredData, Object restartData) {
		this.panel = new AdministrationPanel(filteredData.getGlobalData());
	}
	
	public String getPanelTitle() {
		return LocalizationData.get("AdministrationPlugIn.title"); //$NON-NLS-1$
	}

	@Override
	public String getPanelToolIp() {
		return 	LocalizationData.get("AdministrationPlugIn.toolTip"); //$NON-NLS-1$
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void restoreState() {
		super.restoreState();
		panel.restoreState();
	}

	@Override
	public void saveState() {
		super.saveState();
		panel.saveState();
	}

	@Override
	public boolean allowMenu(int menuId) {
		if ((menuId==ACCOUNT_MENU) || (menuId==FILTER_MENU)) return false;
		return super.allowMenu(menuId);
	}
}
