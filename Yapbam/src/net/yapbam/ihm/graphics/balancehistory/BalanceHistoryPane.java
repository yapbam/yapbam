package net.yapbam.ihm.graphics.balancehistory;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import net.yapbam.data.BalanceHistory;
import net.yapbam.ihm.LocalizationData;

public class BalanceHistoryPane extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BalanceHistory balanceHistory;
	
	private BalanceGraphic graph;
	private JLabel report;
	private BalanceRule rule;
	private JCheckBox isGridVisible;

	private JScrollPane scrollPane;
	
	public BalanceHistoryPane(BalanceHistory history) {
		super(new BorderLayout());
		this.balanceHistory = history;
		rule = new BalanceRule(this.balanceHistory);
		
		createGraphic();
		
		JPanel southPane = new JPanel(new BorderLayout());
		this.isGridVisible = new JCheckBox("Afficher la grille"); //LOCAL
		this.isGridVisible.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				graph.setGridVisible(e.getStateChange() == ItemEvent.SELECTED);
			}});
		southPane.add(this.isGridVisible,BorderLayout.EAST);
		this.report = new JLabel(getBalanceReportText(),SwingConstants.CENTER);
		southPane.add(this.report,BorderLayout.CENTER);
		
		this.add(southPane, BorderLayout.SOUTH);
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
		createGraphic();
		graph.setGridVisible(isGridVisible.isSelected());
		this.report.setText(getBalanceReportText());
		this.validate();
	}
	
	private void createGraphic() {
		graph = new BalanceGraphic(this.balanceHistory, rule.getYAxis());
		graph.addPropertyChangeListener(BalanceGraphic.SELECTED_DATE_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				report.setText(getBalanceReportText());
			}
		});
		scrollPane = new JScrollPane(graph, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setRowHeaderView(rule);
	}
}
