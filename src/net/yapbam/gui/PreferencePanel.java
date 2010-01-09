package net.yapbam.gui;

import javax.swing.JPanel;

@SuppressWarnings("serial")
/** This abstract class represents a preference panel.
 * The preference panels are displayed each in a tab of the preferences dialog.
 */
public abstract class PreferencePanel extends JPanel {
	/** Gets the panel's title.
	 * This title will be used as the tab title in the preferences dialog.
	 * @return the title
	 */
	public abstract String getTitle();
	
	/** Gets the panels tooltip.
	 * @return the tooltip to display for the tab in the preferences dialog.
	 */
	public abstract String getToolTip();
	
	/** Updates the preferences attached to this panel.
	 * This method is called when the preferences dialog's ok button is pressed.
	 * @return true if the preferences changes implies a Yapbam restart.
	 */
	public abstract boolean updatePreferences();
	
	/** Called by the preferences dialog when the panel is selected/deselected.
	 * It could be used to perform specific actions (for instance refresh the panel).
	 * @param displayed true if the panel is now displayed, false if it becomes invisible.
	 */
	public void setDisplayed(boolean displayed) {}
}
