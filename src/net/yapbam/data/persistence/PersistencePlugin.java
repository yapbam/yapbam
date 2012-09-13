package net.yapbam.data.persistence;

public abstract class PersistencePlugin {
	public abstract String getName();
	public abstract void save ();
	public abstract void load ();
}
