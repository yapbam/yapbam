package net.yapbam.gui.dialogs.preferences;

import javax.swing.JPanel;

import net.yapbam.gui.Preferences;

import com.fathzer.soft.ajlib.utilities.NullUtils;

/** This abstract class represents a preference panel.
 * <br>The preference panels are displayed each in a tab of the preferences dialog.
 */
@SuppressWarnings("serial")
public abstract class PreferencePanel extends JPanel {
	/** The name of the property containing the reason why the panel can't be validated.
	 * @see PreferencePanel#getOkDisabledCause()
	 */
	public static final String OK_DISABLED_CAUSE_PROPERTY = "okDisabledCause"; //$NON-NLS-1$
	
	private String okDisabledCause;
	
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
	 * @see Preferences#setProperty(String, String)
	 */
	public abstract boolean updatePreferences();
	
	/** Called by the preferences dialog when the panel is selected/deselected.
	 * It could be used to perform specific actions (for instance refresh the panel).
	 * @param displayed true if the panel is now displayed, false if it becomes invisible.
	 */
	public void setDisplayed(boolean displayed) {
		// Do nothing by default 
	}

	/** Gets the reason why the preference dialog can't be validated.
	 * @return a String explaining why this panel has an invalid state, null, if everything is ok.
	 */
	public final String getOkDisabledCause() {
		return okDisabledCause;
	}
	
	/** Sets the reason why the preference dialog can't be validated.
	 * @param cause a String explaining why this panel has an invalid state. Null, if everything is ok.
	 */
	protected void setOkDisabledCause(String cause) {
		if (!NullUtils.areEquals(cause, okDisabledCause)) {
			String old = this.okDisabledCause;
			this.okDisabledCause = cause;
			this.firePropertyChange(OK_DISABLED_CAUSE_PROPERTY, old, okDisabledCause);
		}
	}

	/** Sets the expert mode of the panel.
	 * <br>Default implementation does nothing. You should override this method if the panel has an expert mode.
	 * @param expertMode true if expertMode is set, false if not
	 */
	protected void setExpertMode(boolean expertMode) {
		// Override this method if an expert mode is available
	}
}
