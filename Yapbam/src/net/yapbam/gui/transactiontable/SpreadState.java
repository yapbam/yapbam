package net.yapbam.gui.transactiontable;

public enum SpreadState  {
	NOT_SPREADABLE, NOT_SPREAD, SPREAD;

	public boolean isSpreadable() {
		return !NOT_SPREADABLE.equals(this);
	}

	public boolean isSpread() {
		return SPREAD.equals(this);
	}
}