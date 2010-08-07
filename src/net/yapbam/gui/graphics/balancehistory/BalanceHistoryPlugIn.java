package net.yapbam.gui.graphics.balancehistory;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import net.yapbam.data.Account;
import net.yapbam.data.BalanceHistory;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.*;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;

public class BalanceHistoryPlugIn extends AbstractPlugIn {
	private BalanceHistoryPane panel;
	private FilteredData data;

	public BalanceHistoryPlugIn(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		this.panel = new BalanceHistoryPane(data);
		this.setPanelTitle(LocalizationData.get("BalanceHistory.title"));
		this.setPanelToolTip(LocalizationData.get("BalanceHistory.toolTip"));
		testAlert();
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent)) {
					panel.setBalanceHistory();
				}
			}
		});
		data.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent)
					|| (event instanceof AccountAddedEvent) || (event instanceof AccountRemovedEvent) || (event instanceof AccountPropertyChangedEvent)
					|| (event instanceof TransactionAddedEvent) || (event instanceof TransactionRemovedEvent)) {
					panel.setBalanceHistory();
					testAlert();
				}
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
		List<Alert> alerts = new ArrayList<Alert>();
		for (int i=0;i<data.getGlobalData().getAccountsNumber();i++) {
			Account account = data.getGlobalData().getAccount(i);
			BalanceHistory balanceHistory = account.getBalanceData().getBalanceHistory();
			long firstAlertDate = balanceHistory.getFirstAlertDate(today, null, account.getAlertThreshold());
			if (firstAlertDate>=0) {
				Date date = new Date();
				if (firstAlertDate>0) date.setTime(firstAlertDate);
				alerts.add(new Alert(date, account, balanceHistory.getBalance(date)));
			}
		}
//		Account[] filteredAccounts = data.getAccounts();
//		boolean singleAccountInFilteredData = ((filteredAccounts!=null) && (filteredAccounts.length==1)) || ((filteredAccounts==null) && (data.getGlobalData().getAccountsNumber()==1));
//		if (!singleAccountInFilteredData) {
//			Alert alert = data.getBalanceData().getBalanceHistory().getFirstAlert(today, null, AlertThreshold.DEFAULT);
//			long firstAlertDate = data.getBalanceData().getBalanceHistory().getFirstAlertDate(today, null, AlertThreshold.DEFAULT);
//			if (firstAlertDate>=0) {
//				Date date = new Date();
//				if (firstAlertDate>0) date.setTime(firstAlertDate);
//				alerts.add(new Alert(date, null));
//			}
//		}
		panel.setAlerts(alerts.toArray(new Alert[alerts.size()]));
		// Compute when occurs the first alert.
		Date first = null;
		for (int i = 0; i < alerts.size(); i++) {
			if ((first==null) || (alerts.get(i).getDate().compareTo(first)<0)) {
				first = alerts.get(i).getDate();
			}
		}
		String tooltip;
		tooltip = LocalizationData.get("BalanceHistory.toolTip");
		if (first!=null) {
			String dateStr = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()).format(first);
			tooltip = tooltip.replace("'", "''"); // single quotes in message pattern are escape characters. So, we have to replace them with "double simple quote"
			String pattern = "<html>"+tooltip+"<br>"+LocalizationData.get("BalanceHistory.alertTooltipAdd")+"</html>";
			tooltip = MessageFormat.format(pattern, "<b>"+dateStr+"</b>");
		}
		setPanelIcon((first!=null?IconManager.ALERT:null));
		setPanelToolTip(tooltip);
	}

	public JPanel getPanel() {
		return panel;
	}
}
