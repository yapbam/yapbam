package net.yapbam.gui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import net.astesana.ajlib.swing.widget.TextWidget;
import net.astesana.ajlib.utilities.NullUtils;
import net.yapbam.util.TextMatcher;

@SuppressWarnings("serial")
/** This class implements a kind of JComboBox that allows to select the field content into a list of predefined values
 * that are shown in a popup.
 * <br>The difference with a JComboBox is the popup selection is updated when the user changes the text field content.
 */
public class PopupTextFieldList extends TextWidget {
	/** A PropertyChangeEvent of this name is fired when a predefined value is selected.
	 * The values of the event are the field content. */
	public static final String PREDEFINED_VALUE = "PREDEFINED_VALUE";
	
	//TODO We probably should implement a value property (see CurrencyWidget)
	
	private JPopupMenu popup;
	private JList list;
	private String predefined=null;
	private String lastText="";
	private int[] groupLimitIndexes;
	private String[] proposals;
	
	private static class UpperLineBorder extends AbstractBorder {
		protected Color lineColor;

		public UpperLineBorder(Color color) {
			lineColor = color;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.setColor(lineColor);
			g.drawLine(x+1, y, x+width-2, y);
		}
	}
	
	private class MyRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			boolean isLimit = false;
			for (int limit : groupLimitIndexes) {
				if (index == limit) {
					isLimit = true;
					break;
				} else if (limit > index) {
					break;
				}
			}
			JComponent label = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (isLimit) {
				Border border = new UpperLineBorder(label.getForeground().brighter());
				label.setBorder(new CompoundBorder(border, label.getBorder()));
			}
			return label;
		}
	}

	/** Constructor.
	 */
	public PopupTextFieldList () {
		popup = new JPopupMenu();
		list = new AutoScrollJList(new PopupListModel());
		groupLimitIndexes = new int[]{3};
		list.setCellRenderer(new MyRenderer());
		popup.add(new JScrollPane(list));
		popup.setFocusable(false);
		this.proposals = new String[0];

		// If the component looses the focus and the popup is shown, we have to hide the popup
		// The following FocusListener will do that
		addFocusListener(new FocusListener() { 
			@Override
			public void focusLost(FocusEvent e) {
				if (popup.isVisible() && !e.isTemporary() && (!e.getOppositeComponent().equals(list))) {
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
				if (e.getButton()==MouseEvent.BUTTON1) {
					setPredefined((String) list.getSelectedValue());
					popup.setVisible(false);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
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
					if (!popup.isVisible()) {
						showPopup();
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
					fillModel(text);
					lastText = text;
					setPredefined((String)null);
					showPopup();
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
	 * @see #setPredefined(String[], int[])
	 */
	public void setPredefined(String[] array) {
		setPredefined (array, null);
	}

	/** Sets the predefined values allowed by the field.
	 * @param array The predefined values.
	 * @param groupSizes The values groups sizes.
	 * <br>The values can be grouped (each will be separated from other by a thin line).
	 * <br>This argument contains the size of each group.
	 * @throws IllegalArgumentException if the sum of the group sizes is greater than the array length.
	 * <br>Note that if that sum is lower than the array length, a group is added containing the extra values.
	 */
	public void setPredefined(String[] array, int[] groupSizes) {
		if (groupSizes!=null){
			int[] indexes = new int[groupSizes.length];
			int currentTotal = 0;
			for (int i=0; i<groupSizes.length; i++) {
				if (groupSizes[i]!=0) {
					currentTotal += groupSizes[i];
					indexes[i] = currentTotal;
				}
			}
			this.groupLimitIndexes = indexes;
		}
		proposals = array;
		fillModel(this.getText());
	}

	private void showPopup() {
		if (list.getModel().getSize()!=0) {
			Dimension size = popup.getPreferredSize();
			if (getWidth()>size.width) popup.setPreferredSize(new Dimension(getWidth(), size.height));
			popup.show(PopupTextFieldList.this, 0, getHeight());
			requestFocus(false);
		} else {
			popup.setVisible(false);
		}
	}

	private void fillModel(String text) {
		int maxProbaSorted = groupLimitIndexes.length==0?0:this.groupLimitIndexes[0];
		TextMatcher matcher = new TextMatcher(TextMatcher.Kind.CONTAINS, text, false, false); //TODO Must match "starts with" ... to be implemented in TextMatcher
		ArrayList<String> okProbaSort = new ArrayList<String>();
		TreeSet<String> okAlphabeticSort = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (String value : this.proposals) {
			if (matcher.matches(value)) {
				if (okProbaSort.size()<maxProbaSorted) {
					okProbaSort.add(value);
				} else {
					okAlphabeticSort.add(value);
				}
			}
		}
		okProbaSort.addAll(okAlphabeticSort);
		((PopupListModel)list.getModel()).setValues(okProbaSort.toArray(new String[okProbaSort.size()]));
		
		if (0 != list.getSelectedIndex()) {
			list.setSelectedIndex(0);
		}
	}

	private final static class PopupListModel extends AbstractListModel {
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
			fireIntervalAdded(this, 0, this.values.length);
		}
	}
}
