package net.yapbam.gui.widget;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.yapbam.util.NullUtils;

@SuppressWarnings("serial")
/** This class implements a kind of JComboBox that allows to select the field content into a list of predefined values
 * that are shown in a popup.
 * The difference with a JComboBox is the popup selection is updated when the user changes the text field content.
 * There's currently no button to deploy the popup (the user need to use the down arrow key), it would probably improve the user experience.
 */
public class PopupTextFieldList extends JTextField {
	/** The property name. A PropertyChangeEvent is fired when a predefined value is selected. */
	public static final String PREDEFINED_VALUE = "PREDEFINED_VALUE";
	
	private JPopupMenu popup;
	private JList list;
	private String predefined=null;
	private String lastText="";

	/** Constructor.
	 */
	public PopupTextFieldList () {
		popup = new JPopupMenu();
		list = new AutoScrollJList(new PopupListModel());
		popup.add(new JScrollPane(list));

		// If the component looses the focus and the popup is shown, we have to hide the popup
		// The following FocusListener will do that
		addFocusListener(new FocusListener() { 
			@Override
			public void focusLost(FocusEvent e) {
				if (popup.isVisible() && !e.isTemporary()) {
					popup.setVisible(false);
					e.getOppositeComponent().requestFocus();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {}
		});
		// When the user click the list, we have to set the value clicked in the list, hide the popup, then transfer back the focus to the textField
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse clicked");
				if (e.getButton()==MouseEvent.BUTTON1) {
					setPredefined((String) list.getSelectedValue());
					popup.setVisible(false);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("mouse released");
				requestFocus();
			}
		});
		// When the user press the down arrow, show the popup, if it's hidden.
		// Up, down and enter key respectivly selects next element, selects previous element, set the textField to the selected element
		// When a key is type, and changed the content of the field, this adapter also refresh the list selected element
        addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) { // down arrow key was pressed
					if (!popup.isVisible() && (list.getModel().getSize()!=0)) {
						Dimension size = popup.getPreferredSize();
						if (getWidth()>size.width) popup.setPreferredSize(new Dimension(getWidth(), size.height));
						popup.show(PopupTextFieldList.this, 0, getHeight());
						requestFocus(false);
					} else {
						int index = list.getSelectedIndex();
						if (index < list.getModel().getSize()) {
							index++;
							list.setSelectedIndex(index);
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_UP) { // Up arrow key was pressed
					if (popup.isVisible()) {
						int index = list.getSelectedIndex();
						if (index > 0) {
							index--;
							list.setSelectedIndex(index);
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Enter key pressed
					if (popup.isVisible()) {
						if (list.getSelectedIndex()>=0) {
							System.out.println ("called after VK_ENTER");
							setPredefined((String) list.getSelectedValue());
						}
						popup.setVisible(false);
						e.consume();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				String text = getText();
				if (!text.equals(lastText)) {
					int index;
					if (text.length()==0) {
						index = -1;
					} else {
						index = ((PopupListModel)list.getModel()).indexOf(text);
						if (index<0) {
							index = -index-1;
							if (index >= list.getModel().getSize()) {
								index = -1;
							} else {
								String listElement = (String)list.getModel().getElementAt(index);
								int min = Math.min(text.length(), listElement.length());
								if (!text.substring(0, min).equalsIgnoreCase(listElement.substring(0, min))) {
									index = -1;
								}
							}
						}
					}
					if (index != list.getSelectedIndex()) {
						list.setSelectedIndex(index);
					}
					lastText = text;
					System.out.println ("called after keyReleased");
					setPredefined((String)null);
				}
			}
		});
	}
	
	public PopupTextFieldList(int columns) {
		this();
		this.setColumns(columns);
	}

	private void setPredefined(String value) {
		if (value!=null) {
			lastText = value;
			setText(value);
		}
		if (!NullUtils.areEquals(predefined, value)) {
			String oldPredefined = predefined;
			predefined = value;
			this.firePropertyChange(PREDEFINED_VALUE, oldPredefined, predefined);
		}
	}

	/** Sets the predefined values allowed by the field.
	 * @param array The predefined values.
	 */
	public void setPredefined(String[] array) {
		((PopupListModel)this.list.getModel()).setValues(array);
	}

	private final class PopupListModel extends AbstractListModel {
		String[] values;
		
		PopupListModel() {
			this.values = new String[0];
		}
		
		@Override
		public int getSize() {
			return values.length;
		}

		@Override
		public Object getElementAt(int index) {
			return values[index];
		}

		public void setValues(String[] values) {
			int n = this.values.length;
			if (n>0) {
				this.values = new String[0];
				fireIntervalRemoved(this, 0, n);
			}
			this.values = values.clone();
			Arrays.sort(this.values, String.CASE_INSENSITIVE_ORDER);
			fireIntervalAdded(this, 0, this.values.length);
		}
		
		/** Same result as Arrays.binarySearch */
		public int indexOf(String value) {
			int result = Arrays.binarySearch(values, value, String.CASE_INSENSITIVE_ORDER);
			return result;
		}
	}
}
