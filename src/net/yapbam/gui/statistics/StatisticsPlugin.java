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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.util.ResourceBundleWrapper;

import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.SubTransaction;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.YapbamState;
import net.yapbam.gui.filter.FilterView;
import net.yapbam.gui.widget.TabbedPane;

public class StatisticsPlugin extends AbstractPlugIn {
	private FilteredData data;
	private boolean displayed;
	private TreeMap<Category, Summary> categoryToAmount;
	private PieChartPanel pie;
	private BarChartPanel bar;
	private TabbedPane tabbedPane;
	
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
		// Hack to set the right Locale for JFreeChart panels (it seems there's a bug in JFreeChart: setLocale doesn't refresh the popup menus).
		new SimpleChartPanel();
		// FIXME The properties dialog is not localized if the locale has just been set using the preference panel.
	}

	@SuppressWarnings("serial")
	private static class SimpleChartPanel extends ChartPanel {
		public SimpleChartPanel() {
			super(null);
			localizationResources = ResourceBundleWrapper.getBundle("org.jfree.chart.LocalizationBundle", LocalizationData.getLocale());
		}
	}
	
	@Override
	public JPanel getPanel() {
		tabbedPane = new TabbedPane();
		this.bar = new BarChartPanel(categoryToAmount);
		tabbedPane.addTab(LocalizationData.get("StatisticsPlugin.bar.tabname"), null, this.bar, LocalizationData.get("StatisticsPlugin.bar.tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		this.pie = new PieChartPanel(categoryToAmount);
		tabbedPane.addTab(LocalizationData.get("StatisticsPlugin.pie.tabname"), null, this.pie, LocalizationData.get("StatisticsPlugin.pie.tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		buildSummaries();
		JPanel result = new JPanel(new GridBagLayout());
// Start implementation of lateral filter panel
		GridBagConstraints cfv = new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		result.add(new FilterView(), cfv);
		GridBagConstraints c = new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
//	GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
//	new Insets(0, 0, 0, 0), 0, 0);
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

	@Override
	public void saveState() {
		YapbamState.INSTANCE.saveState(tabbedPane, this.getClass().getCanonicalName());
	}

	@Override
	public void restoreState() {
		YapbamState.INSTANCE.restoreState(tabbedPane, this.getClass().getCanonicalName());
	}
	
}
