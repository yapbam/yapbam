package net.yapbam.gui.graphics.balancehistory;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.JPanel;

import net.yapbam.data.Account;
import net.yapbam.data.AlertThreshold;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;

public class BalanceHistoryPlugIn extends AbstractPlugIn {
	private BalanceHistoryPane panel;
	private FilteredData data;

	public BalanceHistoryPlugIn(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		this.panel = new BalanceHistoryPane(data.getBalanceData().getBalanceHistory());
		this.setPanelTitle(LocalizationData.get("BalanceHistory.title"));
		this.setPanelToolTip(LocalizationData.get("BalanceHistory.toolTip"));
		testAlert();
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				panel.setBalanceHistory(data.getBalanceData().getBalanceHistory());
				testAlert();
			}
		});
	}
	
	@Override
	public void setDisplayed(boolean displayed) {
		super.setDisplayed(displayed);
		if (displayed) panel.scrollToSelectedDate();
	}

	private void testAlert() {
		Date today = new Date();
//		for (int i=0;i<data.getGlobalData().getAccountsNumber();i++) {
//			Account account = data.getGlobalData().getAccount(i);
//			long firstAlertDate = account.getBalanceData().getBalanceHistory().getFirstAlertDate(today, null, account.getAlertThreshold());
//			if (firstAlertDate>=0) {
//				Date date = new Date();
//				if (firstAlertDate>0) date.setTime(firstAlertDate);
//				System.out.println ("Account "+account+" : "+DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()).format(date));
//			}
//		}
		long firstAlertDate = data.getBalanceData().getBalanceHistory().getFirstAlertDate(today, null, AlertThreshold.DEFAULT);
		String tooltip;
		tooltip = LocalizationData.get("BalanceHistory.toolTip");
		if (firstAlertDate>=0) {
			Date date = new Date();
			if (firstAlertDate>0) date.setTime(firstAlertDate);
			String dateStr = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()).format(date);
			tooltip = tooltip.replace("'", "''"); // single quotes in message pattern are escape characters. So, we have to replace them with "double simple quote"
			String pattern = "<html>"+tooltip+"<br>"+LocalizationData.get("BalanceHistory.alertTooltipAdd")+"</html>";
			tooltip = MessageFormat.format(pattern, "<b>"+dateStr+"</b>");
		}
		setPanelIcon((firstAlertDate>=0?IconManager.ALERT:null));
		setPanelToolTip(tooltip);
	}

	public JPanel getPanel() {
		return panel;
	}
}
