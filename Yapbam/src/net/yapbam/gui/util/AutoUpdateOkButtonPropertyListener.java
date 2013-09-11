package net.yapbam.gui.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;


/** Utility PropertyChangeListener that calls the updateOkButtonEnabled of a dialog each
 * time the property listened by this class is updated.
 * <BR>Here is a typical use: coolJTextField.addPropertyChangeListener(CoolJTextField.TEXT_PROPERTY, new AutoUpdateOkButtonPropertyListener(dialog));
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
public class AutoUpdateOkButtonPropertyListener implements PropertyChangeListener {
	// Note that, previously, I used a keyAdapter listener or a DocumentListener to perform such a functionality.
	// Both simply didn't worked !!!
	// With keyAdapter, if you typed an invalid text and, very, very quickly, press the ok button of the dialog,
	// the ok button was not disabled quickly enough to prevent the dialog to be confirmed.
	// With DocumentListener, "ghost" events are sent (see CoolJTextField comment) 
	
	private AbstractDialog<?,?> dialog;
	
	public AutoUpdateOkButtonPropertyListener (AbstractDialog<?,?> dialog) {
		this.dialog = dialog;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		dialog.updateOkButtonEnabled();
	}
}