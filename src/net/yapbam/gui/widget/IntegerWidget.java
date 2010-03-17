package net.yapbam.gui.widget;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigInteger;

import javax.swing.JTextField;

import net.yapbam.util.NullUtils;

/** This widget is an integer input field.
 *  You can set minimum and maximum values accepted by this field.
 *  It is a java bean, so you can listen to its VALUE_PROPERTY change.
 */
public class IntegerWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	private final static boolean DEBUG = false;
	/** The field value property name. */
	public static final String VALUE_PROPERTY = "VALUE_PROPERTY";
	/** An utility constant for Integer.MAX_VALUE */
	public static final BigInteger INTEGER_MAX_VALUE = BigInteger.valueOf(Integer.MAX_VALUE);
	
	private BigInteger value;
	private BigInteger maxValue;
	private BigInteger minValue;
	
	/** Constructor.
	 * The min and max values are not set
	 */
	public IntegerWidget() {
		this(null, null);
	}

	/** Constructor.
	 * The field is initialized empty, with a null value.
	 * @param minValue The field minimum value or null if the field has no minimal value.
	 * @param maxValue The field maximum value or null if the field has no maximal value.
	 */
	public IntegerWidget(BigInteger minValue, BigInteger maxValue) {
		super();
		if ((minValue!=null)&&(maxValue!=null)&&(minValue.compareTo(maxValue)>0)) throw new IllegalArgumentException();
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar())) e.consume();
				//FIXME - is a valid character if minValue < 0
			}
			@Override
			public void keyReleased(KeyEvent e) {
				updateValue();
			}
		});
	}
	
	private void updateValue() {
		BigInteger old = value;
		String text = this.getText().trim();
		try {
			value = new BigInteger(text);
			if (((this.maxValue!=null)&&(value.compareTo(this.maxValue)>0)) || ((this.minValue!=null)&&(value.compareTo(this.minValue)<0))) {
				// Value is out of bounds
				value = null;
			}
		} catch (NumberFormatException e) {
			value = null;
		}
		if (!NullUtils.areEquals(value,old)) {
			if (DEBUG) System.out.println ("->"+value);
			this.firePropertyChange(VALUE_PROPERTY, old, value);
		}
	}

	@Override
	public void setText(String t) {
		super.setText(t);
		updateValue();
	}

	/** Gets the current value.
	 * @return an integer or null if the value is not valid.
	 */
	public BigInteger getValue() {
		if (DEBUG) System.out.println ("IntegerWidget.getValue() returns "+value);
		return this.value==null?null:this.value;
	}

	/** Sets the current value.
	 * @param value a big integer or null to set the field empty.
	 */
	public void setValue(BigInteger value) {
		if (!NullUtils.areEquals(value,this.value)) this.setText(value==null?"":value.toString());
	}

	/** Sets the current value.
	 * @param value an integer or null to set the field empty.
	 */
	public void setValue(Integer value) {
		setValue(BigInteger.valueOf(value));
	}
}
