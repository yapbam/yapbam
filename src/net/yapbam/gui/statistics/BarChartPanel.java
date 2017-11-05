package net.yapbam.gui.statistics;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.Iterator;
import java.util.Map;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
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
	private boolean netValues;

	BarChartPanel(Map<Category, Summary> map) {
		super(null);
		super.setPopupMenu(createPopupMenu(true, false, false, true, false));
		this.netValues = false;
		this.categoryToAmount = map;
		dataset = new DefaultCategoryDataset();
		updateDataSet();

		JFreeChart chart = ChartFactory.createStackedBarChart(
						LocalizationData.get("StatisticsPlugin.bar.title"), null, null, dataset, PlotOrientation.VERTICAL, true, true, false); //$NON-NLS-1$
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setNoDataMessage(LocalizationData.get("StatisticsPlugin.bar.empty")); //$NON-NLS-1$
		// disable bar outlines...
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f, 0.0f, new Color(0, 64, 0));
		GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f, 0.0f, new Color(64, 0, 0));
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

		this.setChart(chart);
	}

	void updateDataSet() {
		dataset.clear();
		Iterator<Category> it = categoryToAmount.keySet().iterator();
		while (it.hasNext()) {
			Category category = (Category) it.next();
			Summary summary = categoryToAmount.get(category);
			String title = category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName(); //$NON-NLS-1$
			if (netValues) {
				double balance = summary.getReceipts()+summary.getDebts();
				if (GlobalData.AMOUNT_COMPARATOR.compare(balance, 0.0)!=0) {
					dataset.addValue(balance>0?balance:0, LocalizationData.get("StatisticsPlugin.bar.receipt"), title); //$NON-NLS-1$
					dataset.addValue(balance>0?0:balance, LocalizationData.get("StatisticsPlugin.bar.debts"), title); //$NON-NLS-1$
				}
			} else {
				if ((GlobalData.AMOUNT_COMPARATOR.compare(summary.getReceipts(), 0.0) != 0)
						|| (GlobalData.AMOUNT_COMPARATOR.compare(summary.getDebts(), 0.0) != 0)) {
					dataset.addValue(summary.getReceipts(), LocalizationData.get("StatisticsPlugin.bar.receipt"), title); //$NON-NLS-1$
					dataset.addValue(summary.getDebts(), LocalizationData.get("StatisticsPlugin.bar.debts"), title); //$NON-NLS-1$
				}
			}
		}
	}

	public void setNetValues(boolean netValues) {
		this.netValues = netValues;
		updateDataSet();
		((CategoryPlot)this.getChart().getPlot()).getRenderer();
	}
}
