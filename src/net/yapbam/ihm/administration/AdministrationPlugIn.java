package net.yapbam.ihm.administration;

import javax.swing.JPanel;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.ihm.AbstractPlugIn;
import net.yapbam.ihm.LocalizationData;

public class AdministrationPlugIn extends AbstractPlugIn {
	private AdministrationPanel panel;

	public AdministrationPlugIn(AccountFilteredData acFilter, Object restartData) {
		this.panel = new AdministrationPanel(acFilter.getGlobalData());
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
}
