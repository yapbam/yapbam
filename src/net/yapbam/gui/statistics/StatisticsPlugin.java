package net.yapbam.gui.statistics;

import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.data.event.DataEvent;
import net.yapbam.data.event.DataListener;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

public class StatisticsPlugin extends AbstractPlugIn {
	private FilteredData data;
	private boolean displayed;
	private DefaultPieDataset dataset;
	private TreeMap<Category, Summary> categoryToAmount;
	private OptimizedToolTipGenerator toolTipGenerator;
	
	public StatisticsPlugin(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
		categoryToAmount = new TreeMap<Category, Summary>();
        dataset = new DefaultPieDataset();
        toolTipGenerator = new OptimizedToolTipGenerator();
		this.data.addListener(new DataListener() {
			@Override
			public void processEvent(DataEvent event) {
				if (displayed) {
					toolTipGenerator.clear();
					buildSummaries();
					buildDataSet();
				}
			}
		});
	}

	@Override
	public JPanel getPanel() {
		buildSummaries();
        buildDataSet();
        
        JFreeChart chart = ChartFactory.createPieChart(LocalizationData.get("StatisticsPlugin.byCategory.title"), dataset, false, true, false); //$NON-NLS-1$
        PiePlot plot = (PiePlot) chart.getPlot();
		plot.setToolTipGenerator(toolTipGenerator);
        plot.setSectionOutlinesVisible(true);
        plot.setNoDataMessage(LocalizationData.get("StatisticsPlugin.byCategory.empty")); //$NON-NLS-1$
		return new ChartPanel(chart);
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
					categoryToAmount.get(transaction.getSubTransaction(j).getCategory()).add(transaction.getSubTransaction(j).getAmount());
				}
				categoryToAmount.get(transaction.getCategory()).add(transaction.getComplement());				
			}
		}
	}

	private void buildDataSet() {
		dataset.clear();
        Iterator<Category> it = categoryToAmount.keySet().iterator();
        while (it.hasNext()) {
			Category category = (Category) it.next();
            Summary summary = categoryToAmount.get(category);
            double expense = - summary.getReceipts() - summary.getDebts();
			if (expense>0) {
				String title = category.getName();
				dataset.setValue(title, expense);
			}
		}
	}

	@Override
	public String getPanelTitle() {
		return LocalizationData.get("StatisticsPlugin.title"); //$NON-NLS-1$
	}

	@Override
	public String getPanelToolIp() {
		return LocalizationData.get("StatisticsPlugin.tooltip"); //$NON-NLS-1$
	}

	@Override
	public void setDisplayed(boolean displayed) {
		super.setDisplayed(displayed);
		this.displayed = displayed;
		if (displayed) {
			buildSummaries();
	        buildDataSet();
		}
	}
}
