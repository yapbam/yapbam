package net.yapbam.gui.statistics;

import java.util.Iterator;
import java.util.Map;

import net.yapbam.data.Category;
import net.yapbam.gui.LocalizationData;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

@SuppressWarnings("serial")
class BarChartPanel extends ChartPanel {
	private DefaultCategoryDataset dataset;
	private Map<Category, Summary> categoryToAmount;
	private OptimizedToolTipGenerator toolTipGenerator;
	
	BarChartPanel(Map<Category, Summary> map) {
		super(null);
		this.categoryToAmount = map;
        dataset = new DefaultCategoryDataset();
        toolTipGenerator = new OptimizedToolTipGenerator();
        updateDataSet();
        
    	JFreeChart chart = ChartFactory.createStackedBarChart("stacked bar chart", null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
//		plot.setToolTipGenerator(toolTipGenerator);
        plot.setNoDataMessage("todo");
        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);

        this.setChart(chart);
	}

	void updateDataSet() {
		dataset.clear();
		toolTipGenerator.clear();
        Iterator<Category> it = categoryToAmount.keySet().iterator();
        while (it.hasNext()) {
			Category category = (Category) it.next();
            Summary summary = categoryToAmount.get(category);
			String title = category.getName();
			if (!LocalizationData.areEqualsCurrenciesAmounts(summary.getReceipts(),0) ||
					!LocalizationData.areEqualsCurrenciesAmounts(summary.getDebts(),0)) {
				dataset.addValue(summary.getReceipts(), "Recettes", title); //LOCAL
				dataset.addValue(summary.getDebts(), "Dépenses", title); //LOCAL
			}
		}
	}
}
