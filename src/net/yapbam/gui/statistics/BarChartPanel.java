package net.yapbam.gui.statistics;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.Iterator;
import java.util.Map;

import net.yapbam.data.Category;
import net.yapbam.gui.LocalizationData;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
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
        
    	JFreeChart chart = ChartFactory.createStackedBarChart("Recettes et dépenses par catégories", null, null, dataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
//		plot.setToolTipGenerator(toolTipGenerator);
        plot.setNoDataMessage("todo");
        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.green,
                0.0f, 0.0f, new Color(0,64,0));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.red,
                0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

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
