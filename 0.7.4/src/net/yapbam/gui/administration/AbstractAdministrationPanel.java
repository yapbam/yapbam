package net.yapbam.gui.administration;

import javax.swing.JComponent;

public interface AbstractAdministrationPanel {
	public abstract JComponent getPanel();
	public abstract String getPanelTitle();
	public abstract String getPanelToolTip();
	public abstract void saveState();
	public abstract void restoreState();
}
