package net.yapbam.gui.widget;

import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JTextField;

import net.yapbam.gui.LocalizationData;

public class AmountWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	private final static boolean DEBUG = false;
	
	private Number value;
	
	public AmountWidget() {
		super();
		this.setInputVerifier(new DefaultInputVerifier() {
			protected boolean check(JComponent input, boolean change) {
				if (DEBUG) System.out.println ("AmountWidget.check is called");
				AmountWidget widget = (AmountWidget)input;
				updateValue();
				if (change && (value!=null)) widget.setText(LocalizationData.getCurrencyInstance().format(value));
				if (DEBUG) System.out.println ("AmountWidget.check returns "+(value!=null));
				return value!=null;
			}
		});
	}
	
	private void updateValue() {
		String text = this.getText().trim();
		value = null;
		try {
			value = LocalizationData.getCurrencyInstance().parse(text);
		} catch (ParseException e) {
			char decimalSep = LocalizationData.getCurrencyInstance().getDecimalFormatSymbols().getDecimalSeparator();
			text = text.replace(decimalSep, '.');
			try {
				value = Double.valueOf(text);
			} catch (NumberFormatException e2) {}
		}
	}

	public Double getValue() {
		updateValue();
		if (DEBUG) System.out.println ("AmountWidget.getValue returns "+value);
		return this.value==null?null:new Double(this.value.doubleValue());
	}

	public void setValue(Double value) {
		this.setText(LocalizationData.getCurrencyInstance().format(value));
	}
}
