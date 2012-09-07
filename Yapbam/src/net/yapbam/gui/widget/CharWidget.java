package net.yapbam.gui.widget;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.astesana.ajlib.swing.widget.TextWidget;

@SuppressWarnings("serial")
public class CharWidget extends TextWidget {
	public static final String CONTENT_PROPERTY = "content";
	private char content;
	private char defaultValue;

	public CharWidget(char initialValue) {
		super(1);
		setText(new String(new char[]{initialValue}));
		// We will not use a document listener to listen the field modifications because we want to change the field (truncate it to its first character)
		// and document listener can't modify the source event (it throws an IllegalStateException).
		addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String text = getText();
				if (text.length()==0) {
					setChar(defaultValue);
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
	
	public void setChar(char character) {
		char old = this.content;
		this.content = character;
		setText(new String(new char[]{this.content}));
		if (old!=content) this.firePropertyChange(CONTENT_PROPERTY, old, content);
	}
	
	public char getChar() {
		return this.content;
	}
	
	public void setDefaultValue(char defaultValue) {
		this.defaultValue = defaultValue;
	}
}
