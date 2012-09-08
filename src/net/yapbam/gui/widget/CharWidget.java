package net.yapbam.gui.widget;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.astesana.ajlib.swing.widget.TextWidget;

/** A widget that allows the user to enter only one character. 
 * @author Jean-Marc Astesana
 * <BR>License : GPL v3
 */
@SuppressWarnings("serial")
public class CharWidget extends TextWidget {
	/** The character property name.
	 * <br>This property is the character contained in the widget.
	 */
	public static final String CHAR_PROPERTY = "char";
	private Character content;
	private Character defaultChar;

	/** Constructor.
	 * 	<br>The field is empty and the default character is null.
	 * @see CharWidget#setDefaultChar(char)
	 */
	public CharWidget() {
		super(1);
		// We will not use a document listener to listen the field modifications because we want to change the field (truncate it to its first character)
		// and document listener can't modify the source event (it throws an IllegalStateException).
		addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String text = getText();
				if (text.length()==0) {
					setChar(defaultChar);
				} else if (text.length()>1){
					setText(new String(new char[]{text.charAt(text.length()-1)}));
				} else {
					setChar(text.charAt(0));
				}
				selectAll();
			}
		});
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				selectAll();
			}
		});
	}
	
	/** Sets the widget's character.
	 * @param character The character to put in the widget or null to make the field empty (or set it to its default value).
	 * @see CharWidget#setDefaultChar(char)
	 */
	public void setChar(Character character) {
		if (character!= content) {
			Character old = this.content;
			this.content = character;
			setText(content==null?"":new String(new char[]{this.content}));
			this.firePropertyChange(CHAR_PROPERTY, old, content);
		}
	}
	
	/** Gets the widget's character.
	 * @return a character (null if the field is empty)
	 */
	public Character getChar() {
		return this.content;
	}
	
	/** Sets the default character.
	 * <br>When the user delete the character in the field. The field content is automatically set to the default character.
	 * @param defaultChar The new default character (null to leave the field empty).
	 */
	public void setDefaultChar(char defaultChar) {
		this.defaultChar = defaultChar;
	}
}
