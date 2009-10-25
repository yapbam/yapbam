package net.yapbam.gui.widget;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class AmountWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	private final static boolean DEBUG = false;
	
	public static final String VALUE_PROPERTY = "value";
	
	private Number value;
	private DecimalFormat format;
	
	public AmountWidget() {
		this(Locale.getDefault());
	}
	
	public AmountWidget(Locale locale) {
		super();
		format = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		this.setInputVerifier(new DefaultInputVerifier() {
			protected boolean check(JComponent input, boolean change) {
				if (DEBUG) System.out.println ("AmountWidget.check is called");
				AmountWidget widget = (AmountWidget)input;
				updateValue();
				if (change && (value!=null)) widget.setText(format.format(value));
				if (DEBUG) System.out.println ("AmountWidget.check returns "+(value!=null));
				return value!=null;
			}
		});
	}
	
	public void setCurrency (Currency currency) {
		format.setCurrency(currency);
		int digits = currency.getDefaultFractionDigits();
		format.setMaximumFractionDigits(digits);
		format.setMinimumFractionDigits(digits);
		refreshText(value);
	}

	private void refreshText(Number value) {
		this.setText(value==null?"":format.format(value));
	}
	
	private void updateValue() {
		Number oldValue = value;
		String text = this.getText().trim();
		value = null;
		try {
			value = format.parse(text);
		} catch (ParseException e) {
			char decimalSep = format.getDecimalFormatSymbols().getDecimalSeparator();
			text = text.replace(decimalSep, '.');
			try {
				value = Double.valueOf(text);
			} catch (NumberFormatException e2) {}
		}
		// Be aware of null values
		if ((oldValue==null) && (this.value==null)) return;
		if ((oldValue==null) || !oldValue.equals(this.value)) {
			firePropertyChange(VALUE_PROPERTY, oldValue, value);
		}
	}

	public Double getValue() {
		updateValue();
		if (DEBUG) System.out.println ("AmountWidget.getValue returns "+value);
		return this.value==null?null:new Double(this.value.doubleValue());
	}

	public void setValue(Double value) {
		refreshText(value);
	}
}
