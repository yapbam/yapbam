package net.yapbam.gui.statistics;

import java.text.MessageFormat;

import net.yapbam.gui.LocalizationData;

import org.jfree.chart.labels.PieToolTipGenerator;
import org.jfree.data.general.PieDataset;

class OptimizedToolTipGenerator implements PieToolTipGenerator {
	Double total = null;

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
		return MessageFormat.format("{0} : {1} ({2,number,#.#}%)", key, amountString, amount/total*100);
	}

	public void clear() {
		total = null;
	}
}
