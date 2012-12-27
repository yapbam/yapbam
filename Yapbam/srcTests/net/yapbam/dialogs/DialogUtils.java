package net.yapbam.dialogs;

import java.awt.Dialog;
import java.awt.Window;

/** Useful methods on Dialogs.
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class DialogUtils {
	
	private DialogUtils() {}

	/** Tests whether there is a modal dialog showing.
	 * @return true a modal dialog is currently opened and visible
	 */
	public static boolean isModalDialogShowing() {
		Window[] windows = Window.getWindows();
		if (windows != null) { // don't rely on current implementation, which at least returns an empty array.
			for (Window w : windows) {
				if (w.isShowing() && w instanceof Dialog && ((Dialog) w).isModal()) return true;
			}
		}
		return false;
	}
}
