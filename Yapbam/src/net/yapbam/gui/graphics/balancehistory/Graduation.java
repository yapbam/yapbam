package net.yapbam.gui.graphics.balancehistory;

import java.text.MessageFormat;

public class Graduation {
	private int position;
	private double value;
	
	public Graduation(int position, double value) {
		super();
		this.position = position;
		this.value = value;
	}
	
	public int getPosition() {
		return position;
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0,number,currency}[{1}]", getValue(), getPosition());
	}
}
