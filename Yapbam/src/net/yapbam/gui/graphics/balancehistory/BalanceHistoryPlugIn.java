package net.yapbam.gui.graphics.balancehistory;

import javax.swing.JPanel;

import net.yapbam.data.BalanceData;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

public class BalanceHistoryPlugIn extends AbstractPlugIn {
	private BalanceHistoryPane panel;
	private BalanceData data;

	public BalanceHistoryPlugIn(FilteredData filteredData, Object restartData) {
		this.data = filteredData.getBalanceData();
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
