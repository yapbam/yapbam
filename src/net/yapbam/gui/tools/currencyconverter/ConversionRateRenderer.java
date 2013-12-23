package net.yapbam.gui.tools.currencyconverter;

import java.text.NumberFormat;

import javax.swing.table.DefaultTableCellRenderer;

import net.yapbam.gui.LocalizationData;

public class ConversionRateRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private NumberFormat formatter;
	
	public ConversionRateRenderer () {
		super();
		this.setHorizontalAlignment(RIGHT);
		formatter = NumberFormat.getNumberInstance(LocalizationData.getLocale());
		formatter.setMinimumFractionDigits(3);
		formatter.setMaximumFractionDigits(Integer.MAX_VALUE);
	}

	@Override
	public void setValue(Object value) {
		String text;
		if (value==null) {
			text = ""; //$NON-NLS-1$
		} else {
			text = formatter.format((Double) value);
		}
		setText(text);
	}
}
