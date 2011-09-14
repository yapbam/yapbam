package net.yapbam.gui.widget;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** A basic documentListener to perform an action when the document is modified. */
public abstract class BasicDocumentListener implements DocumentListener {
	@Override
	public void insertUpdate(DocumentEvent e) {
		modified();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		modified();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	/** The method that is called when the document content changes. */
	protected abstract void modified();
}
