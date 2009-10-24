package net.yapbam.gui.statistics;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import net.yapbam.data.Category;
import net.yapbam.data.FilteredData;
import net.yapbam.data.Transaction;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

public class StatisticsPlugin extends AbstractPlugIn {
	//TODO work with filtered data
	//FIXME Listen for events
	private FilteredData data;
	
	public StatisticsPlugin(FilteredData filteredData, Object restartData) {
		this.data = filteredData;
	}

	@Override
	public JPanel getPanel() {
		HashMap<Category, Summary> categoryToAmount = new HashMap<Category, Summary>(this.data.getGlobalData().getCategoriesNumber());
		for (int i = 0; i < this.data.getGlobalData().getCategoriesNumber(); i++) {
			categoryToAmount.put(this.data.getGlobalData().getCategory(i), new Summary());
		}
		for (int i = 0; i < this.data.getGlobalData().getTransactionsNumber(); i++) {
			Transaction transaction = this.data.getGlobalData().getTransaction(i);
			if (this.data.isOk(transaction)) {
				for (int j = 0; j < transaction.getSubTransactionSize(); j++) {
					categoryToAmount.get(transaction.getSubTransaction(j).getCategory()).add(transaction.getSubTransaction(j).getAmount());
				}
				categoryToAmount.get(transaction.getCategory()).add(transaction.getComplement());				
			}
		}
        DefaultPieDataset dataset = new DefaultPieDataset();
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
        JFreeChart chart = ChartFactory.createPieChart("Dépenses par catégorie", dataset, false, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setToolTipGenerator(new PieToolTipGenerator() {
        	private Double total = null;

        	@SuppressWarnings("unchecked")
			@Override
			public String generateToolTip(PieDataset dataset, Comparable key) {
				if (total == null) {
					total = 0.0;
					for (int i = 0; i < dataset.getItemCount(); i++) {
						total = total + (Double)dataset.getValue(i);
					}
				}
				Double amount = (Double) dataset.getValue(key);
				String amountString = LocalizationData.getCurrencyInstance().format(amount);
				return MessageFormat.format("{0} ({1,number,#.#}%)", amountString, amount/total*100);
			}
		});
        plot.setSectionOutlinesVisible(true);
        plot.setNoDataMessage("Rien à afficher");
		return new ChartPanel(chart);
	}

	@Override
	public String getPanelTitle() {
		return "Statistiques"; //LOCAL
	}

	@Override
	public String getPanelToolIp() {
		return "Ce panneau affiche les statistiques sur votre compte";
	}

}
