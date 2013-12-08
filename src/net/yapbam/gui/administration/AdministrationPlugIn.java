package net.yapbam.gui.administration;

import javax.swing.JPanel;

import net.yapbam.data.FilteredData;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

/** This plugin implements the Administration panel.
 * @author Jean-Marc Astesana
 */
public class AdministrationPlugIn extends AbstractPlugIn {
	private AdministrationPanel panel;

	public AdministrationPlugIn(FilteredData filteredData, Object restartData) {
		this.panel = new AdministrationPanel(filteredData);
		this.setPanelTitle(LocalizationData.get("AdministrationPlugIn.title")); //$NON-NLS-1$
		this.setPanelToolTip(LocalizationData.get("AdministrationPlugIn.toolTip")); //$NON-NLS-1$
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void restoreState() {
		panel.restoreState();
	}

	@Override
	public void saveState() {
		panel.saveState();
	}

	@Override
	public boolean allowMenu(int menuId) {
		if (menuId==FILTER_MENU) {
			return false;
		}
		return super.allowMenu(menuId);
	}
}
