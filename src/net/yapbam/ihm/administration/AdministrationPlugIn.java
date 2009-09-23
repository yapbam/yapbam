package net.yapbam.ihm.administration;

import java.util.Properties;

import javax.swing.JPanel;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.ihm.AbstractPlugIn;
import net.yapbam.ihm.YapbamState;

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

	@Override
	public void restoreState(Properties properties) {
		super.restoreState(properties);
	}

	@Override
	public void saveState(Properties properties) {
		super.saveState(properties);
		//TODO Save the table state YapbamState.saveState(properties, table, "net.yapbam.ihm.administration.PeriodicalTransactionsPanel.");
	}
}
