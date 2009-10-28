package net.yapbam.gui.statistics;

import java.util.Iterator;
import java.util.Map;

import net.yapbam.data.Category;
import net.yapbam.gui.LocalizationData;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

@SuppressWarnings("serial")
class PieChartPanel extends ChartPanel {
	private DefaultPieDataset dataset;
	private Map<Category, Summary> categoryToAmount;
	private OptimizedToolTipGenerator toolTipGenerator;
	
	PieChartPanel(Map<Category, Summary> map) {
		super(null);
		this.categoryToAmount = map;
        dataset = new DefaultPieDataset();
        toolTipGenerator = new OptimizedToolTipGenerator();
        updateDataSet();
        
        JFreeChart chart = ChartFactory.createPieChart(LocalizationData.get("StatisticsPlugin.pie.byCategory.title"), dataset, false, true, false); //$NON-NLS-1$
        PiePlot plot = (PiePlot) chart.getPlot();
		plot.setToolTipGenerator(toolTipGenerator);
        plot.setSectionOutlinesVisible(true);
        plot.setNoDataMessage(LocalizationData.get("StatisticsPlugin.pie.byCategory.empty")); //$NON-NLS-1$
        this.setChart(chart);
	}

	void updateDataSet() {
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
		toolTipGenerator.clear();
	}
}
