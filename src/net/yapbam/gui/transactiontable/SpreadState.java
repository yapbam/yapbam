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
	
	@Override
	public int hashCode() {
		return toInt();
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof SpreadState)) {
			return false;
		} else {
			return toInt()==((SpreadState)obj).toInt();
		}
	}

	private int toInt() {
		int result = 0;
		if (spreadable) {
			result += 1;
		}
		if (spread) {
			result += 2;
		}
		return result;
	}
}
