package net.yapbam.gui.widget;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

/** This class is a focusListener that performs a select all on the text component each time this
 * component gains the focus.
 * You just have to register it in the focusListener of the component.
 * @see JTextComponent#addFocusListener(FocusListener)
 */
public class AutoSelectFocusListener implements FocusListener {
	public static AutoSelectFocusListener INSTANCE = new AutoSelectFocusListener();

	private AutoSelectFocusListener() {}

	public void focusLost(FocusEvent e) {}

	public void focusGained(FocusEvent e) {
		// Do nothing if component is not a JTextComponent
		Component component = e.getComponent();
		if (component instanceof JTextComponent) {
			((JTextComponent) component).selectAll();
		}
	}
}