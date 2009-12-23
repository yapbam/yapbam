package net.yapbam.gui.widget;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JTextField;

import net.yapbam.util.NullUtils;

public class AmountWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	private final static boolean DEBUG = true;
	
	public static final String VALUE_PROPERTY = "value";
	
	private Number value;
	private DecimalFormat format;
	private boolean isEmptyAllowed;
	private Number minValue;
	private Number maxValue;
	private boolean valid;
	
	public AmountWidget() {
		this(Locale.getDefault());
	}
	
	public AmountWidget(Locale locale) {
		super();
		this.isEmptyAllowed = false;
		this.minValue = Double.MIN_VALUE;
		this.maxValue = Double.MAX_VALUE;
		format = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		this.setInputVerifier(new DefaultInputVerifier() {
			protected boolean check(JComponent input, boolean change) {
				if (change) refreshText(value);
				return valid;
			}
		});
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateValue();
			}
		});
	}
	
	public Currency getCurrency() {
		return format.getCurrency();
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
		String text = this.getText().trim();
		Number changed = null;
		if (text.length()==0) {
			this.valid = isEmptyAllowed;
		} else {
			try {
				changed = format.parse(text);
			} catch (ParseException e) {
				char decimalSep = format.getDecimalFormatSymbols().getDecimalSeparator();
				text = text.replace(decimalSep, '.');
				try {
					changed = Double.valueOf(text);
				} catch (NumberFormatException e2) {}
			}
			this.valid = (changed!=null) && (changed.doubleValue()>=minValue.doubleValue()) && (changed.doubleValue()<=maxValue.doubleValue());
		}
		internalSetValue(changed==null?null:changed.doubleValue());
	}

	public boolean isEmptyAllowed() {
		return isEmptyAllowed;
	}

	public void setEmptyAllowed(boolean isEmptyAllowed) {
		this.isEmptyAllowed = isEmptyAllowed;
	}

	public Double getMinValue() {
		return minValue.doubleValue();
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue.doubleValue();
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	/** Gets the current value.
	 * @return a number or null.
	 */
	public Double getValue() {
		updateValue();
		if (DEBUG) System.out.println ("AmountWidget.getValue returns "+value);
		return this.value==null?null:new Double(this.value.doubleValue());
	}

	/** Sets the current value.
	 * @param value a value, or null.
	 */
	public void setValue(Double value) {
		if (internalSetValue(value)) {
			refreshText(value);
		}
	}
	
	@Override
	public void setText(String t) {
		super.setText(t);
		updateValue();
	}

	/** Set the value without changing the content of the TextField.
	 * This method does nothing if the value is equals to the current widget value.
	 * @param value
	 * @return true if the value was changed
	 */
	private boolean internalSetValue(Double value) {
		// Does nothing if date is equals to current widget date
		// Be aware of null values
		if (NullUtils.areEquals(value, this.value)) return false;
		Number old = this.value;
		this.value = value;
		firePropertyChange(VALUE_PROPERTY, old, value);
		return true;
	}
}
