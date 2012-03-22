package net.yapbam.gui.graphics.balancehistory;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.data.BalanceHistory;
import net.yapbam.data.FilteredData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.util.DateUtils;

public class BalanceHistoryGraphPane extends JPanel {
	private static final long serialVersionUID = 1L;
		
	private JScrollPane scrollPane;
	private BalanceGraphic graph;
	private BalanceHistoryControlPane control;
	private AlertsPane alerts;
	private BalanceRule rule;
	private FilteredData data;
	
	private BalanceHistory getBalanceHistory() {
		return this.data.getBalanceData().getBalanceHistory();
	}

	public BalanceHistoryGraphPane(FilteredData data) {
		super(new BorderLayout());
		this.data = data;
		rule = new BalanceRule(this.getBalanceHistory());
		
		createGraphic();
		
		control = new BalanceHistoryControlPane();
		alerts = new AlertsPane();
		control.getIsGridVisible().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				graph.setGridVisible(e.getStateChange() == ItemEvent.SELECTED);
			}});
		control.getToday().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Date today = DateUtils.getMidnight(new Date());
				setSelectedDate(today);
			}
		});
		control.setReportText(getBalanceReportText());
		alerts.getAlerts().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Alert alert = alerts.getSelectedAlert();
				BalanceHistoryGraphPane.this.data.getFilter().setValidAccounts(Collections.singletonList(alert.getAccount()));
				setSelectedDate(alert.getDate());
			}
		});
		this.add(alerts, BorderLayout.NORTH);
		this.add(control, BorderLayout.SOUTH);
	}

	private String getBalanceReportText() {
		Date date = graph.getSelectedDate();
		String dateStr = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()).format(date);
		String balance = LocalizationData.getCurrencyInstance().format(this.getBalanceHistory().getBalance(date));
		String text = MessageFormat.format(LocalizationData.get("BalanceHistory.balance"), dateStr, balance); //$NON-NLS-1$
		return text;
	}

	void setBalanceHistory() {
		this.rule.setBalanceHistory(this.getBalanceHistory());
		this.remove(scrollPane);
		Date currentlySelected = graph.getSelectedDate();
		createGraphic();
		graph.setSelectedDate(currentlySelected);
		graph.setPreferredEndDate(data.getFilter().getValueDateTo());
		graph.setGridVisible(control.getIsGridVisible().isSelected());
		control.setReportText(getBalanceReportText());
		scrollToSelectedDate();
		this.validate();
	}
	
	private void createGraphic() {
		graph = new BalanceGraphic(this.getBalanceHistory(), rule.getYAxis());
		graph.setToolTipText(LocalizationData.get("BalanceHistory.chart.toolTip")); //$NON-NLS-1$
		graph.addPropertyChangeListener(BalanceGraphic.SELECTED_DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				control.setReportText(getBalanceReportText());
			}
		});
		scrollPane = new JScrollPane(graph, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setRowHeaderView(rule);
	}

	private void setSelectedDate(Date date) {
		if (NullUtils.compareTo(date, BalanceHistoryGraphPane.this.data.getFilter().getValueDateFrom(), true)<0) {
			BalanceHistoryGraphPane.this.data.getFilter().setValueDateFilter(date, BalanceHistoryGraphPane.this.data.getFilter().getValueDateTo());
		} else if (NullUtils.compareTo(date, BalanceHistoryGraphPane.this.data.getFilter().getValueDateTo(), false)>0) {
			BalanceHistoryGraphPane.this.data.getFilter().setValueDateFilter(BalanceHistoryGraphPane.this.data.getFilter().getValueDateFrom(), date);
		}
		graph.setSelectedDate(date);
		scrollToSelectedDate();
	}

	/** Scrolls the graphic in order to have the currently selected date visible.
	 */
	void scrollToSelectedDate() {
		JViewport viewport = scrollPane.getViewport();
		int viewWidth = viewport.getWidth();
		int selected = graph.getX(graph.getSelectedDate());
		int graphWidth = graph.getPreferredSize().width;
		if ((viewport.getViewPosition().x > selected) || (viewport.getViewPosition().x+viewWidth<selected)) {
			//Do nothing if selected date is already visible.
			int position = selected-viewWidth/2;
			if (position < 0) position = 0;
			else if (position + viewWidth > graphWidth) position = graphWidth-viewWidth;
			viewport.setViewPosition(new Point(position, 0));
		}
	}

	public void setAlerts(Alert[] alerts) {
		this.alerts.setAlerts(alerts);
	}
}
