package net.yapbam.gui.graphics.balancehistory;

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
}
