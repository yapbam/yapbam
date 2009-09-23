package net.yapbam.ihm.administration;

import java.util.Properties;

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
	public void restoreState(Properties properties) {
		super.restoreState(properties);
		panel.restoreState(properties);
	}

	@Override
	public void saveState(Properties properties) {
		super.saveState(properties);
		panel.saveState(properties);
	}
}
