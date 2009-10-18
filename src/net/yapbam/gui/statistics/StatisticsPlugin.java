package net.yapbam.gui.statistics;

import java.text.NumberFormat;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import net.yapbam.data.AccountFilteredData;
import net.yapbam.data.Category;
import net.yapbam.data.Transaction;
import net.yapbam.gui.AbstractPlugIn;
import net.yapbam.gui.LocalizationData;

public class StatisticsPlugin extends AbstractPlugIn {
	//TODO work with filtered data
	//FIXME Listen for events
	private AccountFilteredData data;
	
	public StatisticsPlugin(AccountFilteredData acFilter, Object restartData) {
		this.data = acFilter;
	}

	@Override
	public JPanel getPanel() {
		HashMap<Category, double[]> categoryToAmount = new HashMap<Category, double[]>(this.data.getGlobalData().getCategoriesNumber());
		for (int i = 0; i < this.data.getGlobalData().getCategoriesNumber(); i++) {
			categoryToAmount.put(this.data.getGlobalData().getCategory(i), new double[]{0,0});
		}
		for (int i = 0; i < this.data.getGlobalData().getTransactionsNumber(); i++) {
			Transaction transaction = this.data.getGlobalData().getTransaction(i);
			if (this.data.isOk(transaction)) {
				for (int j = 0; j < transaction.getSubTransactionSize(); j++) {
					add(categoryToAmount, transaction.getSubTransaction(j).getAmount(), transaction.getSubTransaction(j).getCategory());
				}
				add(categoryToAmount, transaction.getComplement(), transaction.getCategory());				
			}
		}
/*        double totalDebts = 0; 
        for (int i = 0; i < this.data.getGlobalData().getCategoriesNumber(); i++) {
			totalDebts += categoryToAmount.get(this.data.getGlobalData().getCategory(i))[1];
		}*/
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < this.data.getGlobalData().getCategoriesNumber(); i++) {
            double amount = categoryToAmount.get(this.data.getGlobalData().getCategory(i))[1];
			if (amount>0) {
				String title = this.data.getGlobalData().getCategory(i).getName();
				dataset.setValue(title, amount);
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
				return LocalizationData.getCurrencyInstance().format(amount)+" ("+amount/total*100+"%)";
			}
		});
        plot.setSectionOutlinesVisible(true);
        plot.setNoDataMessage("No data available");
		return new ChartPanel(chart);
	}

	private static void add(HashMap<Category, double[]> categoryToAmount, double amount, Category category) {
		double[] values = categoryToAmount.get(category);
		if (amount>0) values[0] = values[0]+amount;
		else values[1] = values[1]-amount;
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
