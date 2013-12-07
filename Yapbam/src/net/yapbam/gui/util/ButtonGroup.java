package net.yapbam.gui.util;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.io.Serializable;

import javax.swing.JToggleButton;
 
/**
 * This class is, like javax.swing.ButtonGroup, used to create a multiple-exclusion scope for
 * a set of buttons. Creating a set of buttons with the same <code>ButtonGroup</code> object means that
 * turning "on" one of those buttons turns off all other buttons in the group.
 * <p>
 * The main difference of this group with the swing one is that you can deselected buttons by clicking on them
 * and have a group without no selected button.<br>
 * Another difference is that this class extends the java.util.Observable class and calls its observers update method
 * when the selected button changes. 
 * </p>
 */
public class ButtonGroup extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *  The buttons list
	 */
	private List<JToggleButton> buttons;
	/**
	 * The current selection.
	 */
	private JToggleButton selected;
	private ItemListener listener;

	/**
	 * Constructor.
	 */
	public ButtonGroup() {
		this.buttons = new ArrayList<JToggleButton>();
		this.selected = null;
		this.listener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				JToggleButton b = (JToggleButton) e.getItem();
				if (e.getStateChange()==ItemEvent.SELECTED) {
					setSelected(b);
				} else if (selected==b) {
					setSelected(null);
				}
			}
		};
	}

	/**
	 * Adds a button to this group.
	 * @param b the button to be added
	 * @exception NullPointerException if the button is null
	 */
	public void add(JToggleButton b) {
		if (b==null) {
			throw new NullPointerException();
		}
		buttons.add(b);
		b.addItemListener(listener);
		if (b.isSelected()) {
			setSelected(b);
		}
	}

	/**
	 * Removes a button from this group.
	 * @param b the button to be removed
	 * @exception IllegalArgumentException if the button is unknown in this group
	 * @exception NullPointerException if the button is null
	 */
	public void remove(JToggleButton b) {
		if (b == null) {
			throw new NullPointerException();
		}
		if (this.buttons.remove(b)) {
			b.removeItemListener(this.listener);
			if (this.selected==b) {
				setSelected(null);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Clears the selection such that none of the buttons in the
	 * <code>ButtonGroup</code> are selected.
	 */
	public void clearSelection() {
		if (selected != null) {
			setSelected(null);
		}
	}

	/**
	 * Returns the selected button.
	 * @return the selected button
	 */
	public JToggleButton getSelected() {
		return this.selected;
	}
	
	/** Changes the selected button.
	 * @param b the button to be selected (null deselects all buttons)
	 * @exception IllegalArgumentException if the button is not in this group
	 */
	public void setSelected(JToggleButton b) {
		if (b==this.selected) {
			return;
		}
		if ((b!=null) && (!this.buttons.contains(b))) {
			throw new IllegalArgumentException();
		}
		JToggleButton old = this.selected;
		this.selected = b;
		if (b!=null) {
			b.setSelected(true);
		}
		if (old!=null) {
			old.setSelected(false);
		}
		this.setChanged();
		this.notifyObservers(this.selected);
	}
}
