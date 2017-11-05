package net.yapbam.gui.statistics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.yapbam.data.Category;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.SortOrder;

@SuppressWarnings("serial")
class PieChartPanel extends ChartPanel {
	private DefaultPieDataset dataset;
	private Map<Category, Summary> categoryToAmount;
	private OptimizedToolTipGenerator toolTipGenerator;
	
	PieChartPanel(Map<Category, Summary> map) {
		super(null);
		super.setLocale(LocalizationData.getLocale());
		super.setPopupMenu(createPopupMenu(true, false, false, true, false));
		this.categoryToAmount = map;
		dataset = new DefaultPieDataset();
		toolTipGenerator = new OptimizedToolTipGenerator();
		updateDataSet();

		JFreeChart chart = ChartFactory.createPieChart(
				LocalizationData.get("StatisticsPlugin.pie.byCategory.title"), dataset, false, true, false); //$NON-NLS-1$
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setToolTipGenerator(toolTipGenerator);
		plot.setSectionOutlinesVisible(true);
		plot.setNoDataMessage(LocalizationData.get("StatisticsPlugin.pie.byCategory.empty")); //$NON-NLS-1$
		this.setChart(chart);
	}

	void updateDataSet() {
		dataset.clear();
		double total = 0.0;
		Iterator<Category> it = categoryToAmount.keySet().iterator();
		HashMap<String,Double> map = new HashMap<String, Double>();
		while (it.hasNext()) {
			Category category = (Category) it.next();
			Summary summary = categoryToAmount.get(category);
			double expense = -summary.getReceipts() - summary.getDebts();
			if (GlobalData.AMOUNT_COMPARATOR.compare(expense, 0.0)>0) {
				total = total + expense;
				map.put(category.equals(Category.UNDEFINED)?LocalizationData.get("Category.undefined"):category.getName(), expense); //$NON-NLS-1$
			}
		}
		Iterator<Entry<String, Double>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Double> entry = (Map.Entry<String, Double>) iterator.next();
			// I hesitated to included the value as the raw value and as the percentage of total expenses in the dataset key
			// If you want to add these informations to the title, you could do it there.
			dataset.setValue(entry.getKey(), entry.getValue());
		}
		dataset.sortByKeys(SortOrder.ASCENDING);
		toolTipGenerator.clear();
	}
}
