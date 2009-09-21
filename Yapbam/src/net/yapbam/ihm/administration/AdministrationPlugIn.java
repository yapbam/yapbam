package net.yapbam.ihm.administration;

import javax.swing.JPanel;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.ihm.AbstractPlugIn;

public class AdministrationPlugIn extends AbstractPlugIn {//LOCAL
	private AdministrationPanel panel;

	public AdministrationPlugIn(AccountFilteredData acFilter, Object restartData) {
		this.panel = new AdministrationPanel(acFilter.getGlobalData());
	}
	
	public String getPanelTitle() {
		return "Administration";
	}

	public JPanel getPanel() {
		return panel;
	}
}
