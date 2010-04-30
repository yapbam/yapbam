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
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

import net.yapbam.data.BalanceHistory;
import net.yapbam.gui.LocalizationData;

public class BalanceHistoryPane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BalanceHistory balanceHistory;
	
	private JScrollPane scrollPane;
	private BalanceGraphic graph;
	private BalanceHistoryControlPane control;
	private BalanceRule rule;

	public BalanceHistoryPane(BalanceHistory history) {
		super(new BorderLayout());
		this.balanceHistory = history;
		rule = new BalanceRule(this.balanceHistory);
		
		createGraphic();
		
		control = new BalanceHistoryControlPane();
		control.getIsGridVisible().addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				graph.setGridVisible(e.getStateChange() == ItemEvent.SELECTED);
			}});
		control.getToday().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graph.setSelectedDate(new Date());
				scrollToSelectedDate();
			}
		});
		control.setReportText(getBalanceReportText());
		this.add(control, BorderLayout.SOUTH);
	}

	private String getBalanceReportText() {
		Date date = graph.getSelectedDate();
		String dateStr = DateFormat.getDateInstance(DateFormat.SHORT, LocalizationData.getLocale()).format(date);
		String balance = LocalizationData.getCurrencyInstance().format(this.balanceHistory.getBalance(date));
		String text = MessageFormat.format(LocalizationData.get("BalanceHistory.balance"), dateStr, balance); //$NON-NLS-1$
		return text;
	}

	public void setBalanceHistory(BalanceHistory history) {
		this.balanceHistory = history;
		this.rule.setBalanceHistory(history);
		this.remove(scrollPane);
		Date currentlySelected = graph.getSelectedDate();
		createGraphic();
		graph.setSelectedDate(currentlySelected);
		graph.setGridVisible(control.getIsGridVisible().isSelected());
		control.setReportText(getBalanceReportText());
		scrollToSelectedDate();
		this.validate();
	}
	
	private void createGraphic() {
		graph = new BalanceGraphic(this.balanceHistory, rule.getYAxis());
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
		control.setAlerts(alerts);
	}
}
