package net.yapbam.gui.dialogs;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.yapbam.gui.util.AbstractDialog;

/** Utility DocumentListener that calls the updateOkButtonEnabled of a dialog each
 * time the document listened by this class is updated.
 * <BR>Here is a typical use: textField.getDocument().addDocumentListener(new AutoUpdateOkButtonDocumentListener(dialog));
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
class AutoUpdateOkButtonDocumentListener implements DocumentListener {
	// Note that, previously, I used a keyAdapter listener to perform such a functionnality.
	// It simply didn't worked !!! If you typed an invalid text, very, very quickly, press the ok button of the dialog,
	// the ok button was not disabled quickly enough to prevent the dialog to be confirmed.
	
	private AbstractDialog<?,?> dialog;
	
	AutoUpdateOkButtonDocumentListener (AbstractDialog<?,?> dialog) {
		this.dialog = dialog;
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		dialog.updateOkButtonEnabled();
	}


	@Override
	public void removeUpdate(DocumentEvent e) {
		dialog.updateOkButtonEnabled();
	}


	@Override
	public void changedUpdate(DocumentEvent e) {
		// Do nothing
	}
}