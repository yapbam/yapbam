package net.yapbam.gui.util;

import java.awt.Dialog;
import java.awt.Window;

/** Useful methods on Dialogs */
public class DialogUtils {
	
	private DialogUtils() {}

	/** Tests whether there is a modal dialog showing.
	 * @return true a modal dialog is currently opened and visible
	 */
	public static boolean isModalDialogShowing() {
		Window[] windows = Window.getWindows();
		if (windows != null) { // don't rely on current implementation, which at
														// least returns [0].
			for (Window w : windows) {
				if (w.isShowing() && w instanceof Dialog && ((Dialog) w).isModal()) return true;
			}
		}
		return false;
	}
}
