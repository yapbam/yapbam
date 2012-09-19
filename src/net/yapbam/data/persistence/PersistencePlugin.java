package net.yapbam.data.persistence;

import javax.swing.Icon;


public abstract class PersistencePlugin {
	public abstract String getName();
	
	public abstract void save ();
	public abstract void load ();
	
	public abstract String getScheme();
	/** Builds an UI component that implements the chooser for this plugin.
	 * <br>Be aware that compiler can't force the returned instance to be a java.awt.Component subclass, but
	 * this is mandatory.
	 * @return a component
	 */
	public abstract AbstractURIChooserPanel buildChooser();
	public abstract String getTooltip();
	public abstract Icon getIcon();
}
