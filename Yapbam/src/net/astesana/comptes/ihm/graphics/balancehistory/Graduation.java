package net.astesana.comptes.ihm.graphics.balancehistory;

public class Graduation {
	private int position;
	private Object value;
	
	public Graduation(int position, Object value) {
		super();
		this.position = position;
		this.value = value;
	}
	
	public int getPosition() {
		return position;
	}

	public Object getValue() {
		return value;
	}
}
