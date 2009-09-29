package net.yapbam.ihm.dialogs.preferences;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class PreferencePanel extends JPanel {
	public abstract String getTitle();
	public abstract String getToolTip();
	public abstract boolean updatePreferences();
}
