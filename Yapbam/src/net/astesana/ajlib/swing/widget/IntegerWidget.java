package net.astesana.ajlib.swing.widget;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;

import net.astesana.ajlib.utilities.NullUtils;

/** This widget is an integer input field.
 *  You can set minimum and maximum values accepted by this field.
 *  It is a java bean, so you can listen to its VALUE_PROPERTY change.
 */
public class IntegerWidget extends CoolJTextField {
	private static final long serialVersionUID = 1L;
	private final static boolean DEBUG = false;
	/** The field value property name. */
	public static final String VALUE_PROPERTY = "VALUE_PROPERTY";
	/** An utility constant for Integer.MAX_VALUE.
	 * @see BigInteger#ZERO
	 * @see BigInteger#valueOf(long)
	 */
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
		// Adds a key listener to ignored any invalid characters and to increase/decrease value when up/down arrow key are pressed
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// No car are allowed before a - sign
				if ((getSelectionEnd()<getText().length()) && (getText().substring(getSelectionEnd()).indexOf('-')>=0)) e.consume();
				char car = e.getKeyChar();
				if (car=='-') { // - char is a valid character only if the field accepts value less than zero and in the first place (if there's no other - after the current selection)
					if ((IntegerWidget.this.minValue==null) || (IntegerWidget.this.minValue.compareTo(BigInteger.ZERO)<0)) {
//						System.out.println (IntegerWidget.this.getSelectionStart()+" - "+IntegerWidget.this.getSelectionEnd()); //TODO
						if (IntegerWidget.this.getSelectionStart()!=0) e.consume(); // No - after first position
					} else {
						e.consume();
					}
				} else if (!Character.isDigit(car)) e.consume();
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_UP) {
					if ((value!=null) && (value.compareTo(IntegerWidget.this.maxValue)<0)) setValue(value.add(BigInteger.ONE));
				} else if (e.getKeyCode()==KeyEvent.VK_DOWN) {
					if ((value!=null) && (value.compareTo(IntegerWidget.this.minValue)>0)) setValue(value.subtract(BigInteger.ONE));
				}
			}
		});
		this.addPropertyChangeListener(TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
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
