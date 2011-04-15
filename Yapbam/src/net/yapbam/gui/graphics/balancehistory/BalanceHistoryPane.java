package net.yapbam.gui.graphics.balancehistory;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.yapbam.data.Account;
import net.yapbam.data.BalanceHistory;
import net.yapbam.data.FilteredData;
import net.yapbam.data.event.AccountAddedEvent;
import net.yapbam.data.event.AccountPropertyChangedEvent;
import net.yapbam.data.event.AccountRemovedEvent;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.data.event.EverythingChangedEvent;
import net.yapbam.data.event.TransactionsAddedEvent;
import net.yapbam.data.event.TransactionsRemovedEvent;
import net.yapbam.gui.IconManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.NullUtils;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BalanceHistoryPane extends JPanel {
	private static final long serialVersionUID = 1L;
	static final String FIRST_ALERT = "FIRST_ALERT"; //$NON-NLS-1$
	private BalanceHistoryGraphPane graph;
	private Date firstAlert;
	private JTabbedPane tabbedPane;
	private FilteredData data;
	private BalanceHistoryTablePane tablePane;

	/**
	 * Create the panel.
	 */
	public BalanceHistoryPane(FilteredData data) {
		this.data = data;
		this.graph = new BalanceHistoryGraphPane(data);
		setLayout(new BorderLayout(0, 0));
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		tabbedPane.addTab(LocalizationData.get("BalanceHistory.graph.title"), null, this.graph, LocalizationData.get("BalanceHistory.graph.toolTip")); //$NON-NLS-1$ //$NON-NLS-2$
		data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent)) {
					graph.setBalanceHistory();
				}
			}
		});
		data.getGlobalData().addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if ((event instanceof EverythingChangedEvent)
					|| (event instanceof AccountAddedEvent) || (event instanceof AccountRemovedEvent) || (event instanceof AccountPropertyChangedEvent)
					|| (event instanceof TransactionsAddedEvent) || (event instanceof TransactionsRemovedEvent)) {
					graph.setBalanceHistory();
					testAlert();
				}
			}
		});
		testAlert();
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setDisplayed(true);
			}
		});
		tablePane = new BalanceHistoryTablePane();
		tabbedPane.addTab(LocalizationData.get("BalanceHistory.transaction.title"), null, tablePane, LocalizationData.get("BalanceHistory.transaction.tooltip")); //LOCAL //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	void setDisplayed (boolean displayed) {
		if (tabbedPane.getSelectedIndex()==0) {
			graph.scrollToSelectedDate();
		}
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
		graph.setAlerts(alerts.toArray(new Alert[alerts.size()]));
		// Compute when occurs the first alert.
		Date first = null;
		for (int i = 0; i < alerts.size(); i++) {
			if ((first==null) || (alerts.get(i).getDate().compareTo(first)<0)) {
				first = alerts.get(i).getDate();
			}
		}
		setFirstAlert(first);
	}
	
	private void setFirstAlert (Date first) {
		if (!NullUtils.areEquals(first, firstAlert)) {
			Date old = firstAlert;
			firstAlert = first;
			tabbedPane.setIconAt(0, first!=null?IconManager.ALERT:null);
			String tooltip;
			tooltip = LocalizationData.get("BalanceHistory.graph.toolTip"); //$NON-NLS-1$
			if (first!=null) {
				String dateStr = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()).format(first);
				tooltip = tooltip.replace("'", "''"); // single quotes in message pattern are escape characters. So, we have to replace them with "double simple quote" //$NON-NLS-1$ //$NON-NLS-2$
				String pattern = "<html>"+tooltip+"<br>"+LocalizationData.get("BalanceHistory.alertTooltipAdd")+"</html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				tooltip = MessageFormat.format(pattern, "<b>"+dateStr+"</b>"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			tabbedPane.setToolTipTextAt(0, tooltip);
			this.firePropertyChange(FIRST_ALERT, old, firstAlert);
		}
	}

	public Date getFirstAlert() {
		return this.firstAlert;
	}

	public void saveState() {
		this.tablePane.saveState();
	}

	public void restoreState() {
		this.tablePane.restoreState();
	}
}
