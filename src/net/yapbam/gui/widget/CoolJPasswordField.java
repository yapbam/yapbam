package net.yapbam.gui.widget;

import javax.swing.JPasswordField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/** A JPasswordField with a property that maps its text.
 * <br>I've found no way to track efficiently the modifications of the text of a JTextField ... so I developed this widget.
 * <br>DocumentListeners are intended to do it, unfortunately, when a text is replace in a field, the listener receive two events:<ol>
 * <li>One when the replaced text is removed.</li>
 * <li>One when the replacing text is inserted</li>
 * </ul>
 * The first event is ... simply absolutely misleading, it corresponds to a value that the text never had.
 * <br>Another problem with DocumentListener is that you can't modify the text into it (it throws IllegalStateException).
 * <br><br>Another way was to use KeyListeners ... but some key events are throw a long time (probably the key auto-repeat interval)
 * after the key was released. And others events (for example a click on an OK button) may occurs before the listener is informed of the change.
 * <br><br>This widget guarantees that no "ghost" property change is thrown !
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */

public class CoolJPasswordField extends JPasswordField {
	private static final long serialVersionUID = 1L;

	public static final String TEXT_PROPERTY = "text";
	
	public CoolJPasswordField() {
		this(0);
	}

	public CoolJPasswordField(int nbColumns) {
		super("", nbColumns);
		this.setDocument(new MyDocument());
	}

	@SuppressWarnings("serial")
	private class MyDocument extends PlainDocument {
		private boolean ignoreEvents = false;
		
		@Override
		public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			String oldValue = new String(CoolJPasswordField.this.getPassword());
			this.ignoreEvents = true;
			super.replace(offset, length, text, attrs);
			this.ignoreEvents = false;
			String newValue = new String(CoolJPasswordField.this.getPassword());
			if (!oldValue.equals(newValue)) CoolJPasswordField.this.firePropertyChange(TEXT_PROPERTY, oldValue, newValue);
		}
		
		@Override
		public void remove(int offs, int len) throws BadLocationException {
			String oldValue = new String(CoolJPasswordField.this.getPassword());
			super.remove(offs, len);
			String newValue = new String(CoolJPasswordField.this.getPassword());
			if (!ignoreEvents && !oldValue.equals(newValue)) CoolJPasswordField.this.firePropertyChange(TEXT_PROPERTY, oldValue, newValue);
		}
	}
}
