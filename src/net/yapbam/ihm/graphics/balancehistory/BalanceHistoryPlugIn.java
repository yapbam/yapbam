package net.yapbam.ihm.graphics.balancehistory;

import javax.swing.JPanel;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.ihm.AbstractPlugIn;
import net.yapbam.ihm.LocalizationData;

public class BalanceHistoryPlugIn extends AbstractPlugIn {
	private BalanceHistoryPane panel;
	private AccountFilteredData data;

	public BalanceHistoryPlugIn(AccountFilteredData acFilter, Object restartData) {
		this.data = acFilter;
		this.panel = new BalanceHistoryPane(data.getBalanceHistory());
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				panel.setBalanceHistory(data.getBalanceHistory());
			}
		});
	}
	
	public String getPanelTitle() {
		return LocalizationData.get("BalanceHistory.title");
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public String getPanelToolIp() {
		return LocalizationData.get("BalanceHistory.toolTip");
	}

}
