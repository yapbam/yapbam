package net.yapbam.gui.widget;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JTextField;

import net.yapbam.util.NullUtils;

/** A widget to enter amount.
 * This widget automatically format the value it contains according to its local's currency.
 * You can restrict the valid values by setting minimum and maximum values.
 * This bean defines two properties :
 * VALUE_PROPERTY : a double, the value currently entered in the field.
 * CONTENT_VALID_PROPERTY : a boolean, true if the text currently entered in the field is a valid value, false if it is not.
 * The CONTENT_VALID_PROPERTY is a read only property.
 */
public class AmountWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	private final static boolean DEBUG = false;
	
	/** Value property identifier. */ 
	public static final String VALUE_PROPERTY = "value";
	/** Content validity property identifier. */
	public static final String CONTENT_VALID_PROPERTY = "contentValid";
	
	private Number value;
	private DecimalFormat format;
	private boolean isEmptyAllowed;
	private Number minValue;
	private Number maxValue;
	private boolean valid;
	
	/** Constructor.
	 *  The local is set to default locale.
	 *  @see #AmountWidget(Locale)
	 */
	public AmountWidget() {
		this(Locale.getDefault());
	}
	
	/** Constructor.
	 * Empty field is not allowed.
	 * minValue is equals to Double.NEGATIVE_INFINITY, maxValue to Double.POSITIVE_INFINITY.
	 * The value is set to null.
	 * @param locale The locale to apply to the widget (use to format the amount typed).
	 */
	public AmountWidget(Locale locale) {
		super();
		this.isEmptyAllowed = false;
		this.minValue = Double.NEGATIVE_INFINITY;
		this.maxValue = Double.POSITIVE_INFINITY;
		format = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateValue();
			}
		});
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!e.isTemporary()) refreshText(value);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});
	}
	
	/** Gets the widget's currency.
	 * @return the windget's currency
	 */
	public Currency getCurrency() {
		return format.getCurrency();
	}
	
	/** Sets the currency.
	 * By default, the currency is the one of the widget's locale.
	 * @param currency
	 */
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
		//FIXME with the French locale, 5 900 is not a valid input
		boolean oldValid = this.valid;
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
		if (this.valid!=oldValid) firePropertyChange(CONTENT_VALID_PROPERTY, oldValid, this.valid);
	}

	/** Determines whether empty text (or blank) are considered as valid or not. 
	 * @return true if blank field is valid.
	 */
	public boolean isEmptyAllowed() {
		return isEmptyAllowed;
	}

	/** Sets the validity of blank text.
	 * @param isEmptyAllowed true if blank text are allowed, false if not.
	 * The value associated with a blank field is always null.
	 */
	public void setEmptyAllowed(boolean isEmptyAllowed) {
		this.isEmptyAllowed = isEmptyAllowed;
		if (this.getText().trim().length()==0) updateValue();
	}

	/** Gets the minimum value allowed in the field.
	 * @return a double (Double.NEGATIVE_INFINITY if there is no limit)
	 */
	public Double getMinValue() {
		return minValue.doubleValue();
	}

	/** Sets the minimum value allowed in the field.
	 * @param minValue a double (Double.NEGATIVE_INFINITY if there is no limit)
	 */
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	/** Gets the maximum value allowed in the field.
	 * @return a double (Double.POSITIVE_INFINITY if there is no limit)
	 */
	public Double getMaxValue() {
		return maxValue.doubleValue();
	}

	/** Sets the maximum value allowed in the field.
	 * @param maxValue a double (Double.POSITIVE_INFINITY if there is no limit)
	 */
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
		refreshText(value);
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
		// Does nothing if amount is equals to current widget amount
		// Be aware of null values
		if (NullUtils.areEquals(value, this.value)) return false;
		Number old = this.value;
		this.value = value;
		firePropertyChange(VALUE_PROPERTY, old, value);
		return true;
	}
	
	/** Gets the content validity.
	 * @return true if the content is valid, false if it is not.
	 */
	public boolean isContentValid() {
		return this.valid;
	}
}
