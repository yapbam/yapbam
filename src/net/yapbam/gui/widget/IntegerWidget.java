package net.yapbam.gui.widget;

import javax.swing.JTextField;

import net.yapbam.util.NullUtils;

/** This widget is an interger input field.
 *  You can set minimum and maximum values accepted by this field.
 *  It is a java beans, so you can listen to its VALUE_PROPERTY change.
 */
public class IntegerWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	private final static boolean DEBUG = false;
	/** The field value property name. */
	public static final String VALUE_PROPERTY = "VALUE_PROPERTY";
	
	private Integer value;
	private int maxValue;
	private int minValue;
	
	/** Constructor.
	 * The min and max values are set to Integer.MIN_VALUE and Integer.MAX_VALUE
	 */
	public IntegerWidget() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/** Constructor.
	 * The field is initialized empty, with a null value.
	 * @param minValue The field minimum value.
	 * @param maxValue The field maximum value.
	 */
	public IntegerWidget(int minValue, int maxValue) {
		super();
		if (minValue>maxValue) throw new IllegalArgumentException();
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	private void updateValue() {
		Integer old = value;
		String text = this.getText().trim();
		try {
			value = Integer.parseInt(text);
			if ((value>this.maxValue) || (value<this.minValue)) value = null;
		} catch (NumberFormatException e) {
			value = null;
		}
		if (!NullUtils.areEquals(value,old)) {
			this.firePropertyChange(VALUE_PROPERTY, old, value);
		}
	}

	/** Gets the current value.
	 * @return an integer or null if the value is not valid.
	 */
	public Integer getValue() {
		updateValue();
		if (DEBUG) System.out.println ("IntegerWidget.getValue() returns "+value);
		return this.value==null?null:this.value;
	}

	/** Sets the current value.
	 * @param value an integer or null to set the field empty.
	 */
	public void setValue(Integer value) {
		if (!value.equals(this.value)) this.setText(value==null?"":Integer.toString(value));
	}
}
