package net.yapbam.gui.statistics;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;

import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

public class StatisticsPlugin extends AbstractPlugIn {
	private FilteredData data;
	private boolean displayed;
	private TreeMap<Category, Summary> categoryToAmount;
	private PieChartPanel pie;
	private BarChartPanel bar;
	private JTabbedPane tabbedPane;
	
	public StatisticsPlugin(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		categoryToAmount = new TreeMap<Category, Summary>();
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (displayed) buildSummaries();
			}
		});
		this.setPanelTitle(LocalizationData.get("StatisticsPlugin.title"));
		this.setPanelToolTip(LocalizationData.get("StatisticsPlugin.tooltip"));
		this.setPrintingSupported(true);
	}

	@Override
	public JPanel getPanel() {
		tabbedPane = new JTabbedPane();
		this.bar = new BarChartPanel(categoryToAmount);
		tabbedPane.addTab(LocalizationData.get("StatisticsPlugin.bar.tabname"), null, this.bar, LocalizationData.get("StatisticsPlugin.bar.tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		this.pie = new PieChartPanel(categoryToAmount);
		tabbedPane.addTab(LocalizationData.get("StatisticsPlugin.pie.tabname"), null, this.pie, LocalizationData.get("StatisticsPlugin.pie.tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		buildSummaries();
		JPanel result = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(5, 0, 0, 0), 0, 0);
		result.add(tabbedPane, c);
		return result;
	}

	@Override
	protected Printable getPrintable() {
		return new Printable() {
			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				ChartPanel pane = (ChartPanel) tabbedPane.getSelectedComponent();
				return pane.print(graphics, pageFormat, pageIndex);
			}
		};
	}

	private void buildSummaries() {
		categoryToAmount.clear();
		for (int i = 0; i < this.data.getGlobalData().getCategoriesNumber(); i++) {
			categoryToAmount.put(this.data.getGlobalData().getCategory(i), new Summary());
		}
		for (int i = 0; i < this.data.getTransactionsNumber(); i++) {
			Transaction transaction = this.data.getTransaction(i);
			if (this.data.isOk(transaction)) {
				for (int j = 0; j < transaction.getSubTransactionSize(); j++) {
					SubTransaction subTransaction = transaction.getSubTransaction(j);
					if (this.data.isOk(subTransaction)) categoryToAmount.get(subTransaction.getCategory()).add(subTransaction.getAmount());
				}
				Category category = transaction.getCategory();
				if (this.data.isComplementOk(transaction)) categoryToAmount.get(category).add(transaction.getComplement());
			}
		}
		pie.updateDataSet();
		bar.updateDataSet();
	}

	@Override
	public void setDisplayed(boolean displayed) {
		super.setDisplayed(displayed);
		this.displayed = displayed;
		if (displayed) buildSummaries();
	}
}