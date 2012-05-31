package net.yapbam.gui.tools;

import javax.swing.table.DefaultTableCellRenderer;

public class ConversionRateRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public ConversionRateRenderer () {
		super();
	}

	@Override
	public void setValue(Object value) {
		String text;
		if (value==null) {
			text = ""; //$NON-NLS-1$
		} else {
			text = ((Double) value).toString();
		}
		setText(text);
	}
}
