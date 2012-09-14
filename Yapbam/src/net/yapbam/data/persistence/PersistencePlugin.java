package net.yapbam.data.persistence;

import javax.swing.Icon;


public abstract class PersistencePlugin {
	public abstract String getName();
	
	public abstract void save ();
	public abstract void load ();
	
	public abstract String getScheme();
	public abstract AbstractURIChooserPanel getChooser();
	public abstract String getTooltip();
	public abstract Icon getIcon();
}
