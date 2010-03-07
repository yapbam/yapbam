package net.yapbam.gui.graphics.balancehistory;

import java.util.Date;

import javax.swing.JPanel;

import net.yapbam.data.BalanceData;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;

public class BalanceHistoryPlugIn extends AbstractPlugIn {
	private BalanceHistoryPane panel;
	private BalanceData data;

	public BalanceHistoryPlugIn(FilteredData filteredData, Object restartData) {
		this.data = filteredData.getBalanceData();
		this.panel = new BalanceHistoryPane(data.getBalanceHistory());
		testAlert();
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				panel.setBalanceHistory(data.getBalanceHistory());
				testAlert();
			}
		});
	}
	
	private void testAlert() {
		long firstDateUnder = data.getBalanceHistory().getFirstDateUnder(new Date(), null, 0.0);
//		String result;
//		if (firstDateUnder<0) result = "never";
//		else if (firstDateUnder==0) result = "always";
//		else {
//			Date d = new Date();
//			d.setTime(firstDateUnder);
//			result = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()).format(d);
//		}
		setPanelIcon((firstDateUnder>=0?IconManager.ALERT:null));
	}
	
	public String getPanelTitle() {
		return LocalizationData.get("BalanceHistory.title");
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public String getPanelToolTip() {
		return LocalizationData.get("BalanceHistory.toolTip");
	}
}
