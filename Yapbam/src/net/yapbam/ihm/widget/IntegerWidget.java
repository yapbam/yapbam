package net.yapbam.ihm.widget;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class IntegerWidget extends JTextField {
	private static final long serialVersionUID = 1L;
	private final static boolean DEBUG = false;
	
	private Integer value;
	private int maxValue;
	private int minValue;
	
	public IntegerWidget() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public IntegerWidget(int minValue, int maxValue) {
		super();
		if (minValue>maxValue) throw new IllegalArgumentException();
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.setInputVerifier(new DefaultInputVerifier() {
			protected boolean check(JComponent input, boolean change) {
				if (DEBUG) System.out.println ("IntegerWidget is called");
				IntegerWidget widget = (IntegerWidget)input;
				updateValue();
				if (change && (value!=null)) widget.setText(Integer.toString(value));
				if (DEBUG) System.out.println ("IntegerWidget returns "+(value!=null));
				return value!=null;
			}
		});
	}
	
	private void updateValue() {
		String text = this.getText().trim();
		try {
			value = Integer.parseInt(text);
			if ((value>this.maxValue) || (value<this.minValue)) value = null;
		} catch (NumberFormatException e) {
			value = null;
		}
	}

	public Integer getValue() {
		updateValue();
		if (DEBUG) System.out.println ("IntegerWidget returns "+value);
		return this.value==null?null:this.value;
	}

	public void setValue(Integer value) {
		this.setText(Integer.toString(value));
	}
}
