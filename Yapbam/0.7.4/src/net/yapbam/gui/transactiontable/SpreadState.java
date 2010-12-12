package net.yapbam.gui.transactiontable;

public class SpreadState implements Comparable<SpreadState> {
	boolean spreadable;
	boolean spread;
	
	public SpreadState(boolean spreadable, boolean spread) {
		super();
		this.spreadable = spreadable;
		this.spread = spread;
	}

	@Override
	public int compareTo(SpreadState o) {
		return this.toInt() - o.toInt();
	}
	
	private int toInt() {
		int result = 0;
		if (spreadable) result += 1;
		if (spread) result += 2;
		return result;
	}
}
