package net.yapbam.popup;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PopupTextFieldList extends JTextField {
	private String[] values;
	private JPopupMenu popup;
	private JList list;

	public PopupTextFieldList (String[] predefinedValues) {
		this.values = predefinedValues;
		Arrays.sort(this.values);
		popup = new JPopupMenu();
		list = new AutoScrolJList(values);
		popup.add(new JScrollPane(list));
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton()==MouseEvent.BUTTON1) {
					setText((String) list.getSelectedValue());
					popup.setVisible(false);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				requestFocus();
			}
		});
		
        addKeyListener(new KeyAdapter() {
			private String lastText="";

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) { // down arrow key was pressed
					if (!popup.isVisible()) {
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
						if (list.getSelectedIndex()>=0) setText((String) list.getSelectedValue());
						popup.setVisible(false);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				String text = getText();
				if (!text.equals(lastText)) {
					int index = Arrays.binarySearch(values, text);
					if (index<0) {
						index = -index-1;
						if (index >= list.getModel().getSize()) {
							index = -1;
						} else {
							String listElement = (String)list.getModel().getElementAt(index);
							int min = Math.min(text.length(), listElement.length());
							if (!text.substring(0, min).equals(listElement.substring(0, min))) {
								index = -1;
							}
						}
					}
					if (index != list.getSelectedIndex()) {
						list.setSelectedIndex(index);
						System.out.println (e.getKeyCode()+" -> "+getText()+" -> "+index);
					}
					lastText = text;
				}
			}
		});
	}
}

@SuppressWarnings("serial")
class AutoScrolJList extends JList {
	public AutoScrolJList(String[] values) {
		super(values);
		setAutoscrolls(true);
	}

	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);
		if (index>=0) {
			ensureIndexIsVisible(index);
		} else {
			clearSelection();
		}
	}
}
