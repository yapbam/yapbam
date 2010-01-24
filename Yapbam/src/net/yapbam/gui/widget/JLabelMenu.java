package net.yapbam.gui.widget;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import net.yapbam.gui.IconManager;

/** This abstract class implements a popupmenu that pops up when the user clicks
 * on a label.
 * In order to use this widget, you have to overide the fillpopup method.
 * @see #fillPopUp(JPopupMenu)
 */
@SuppressWarnings("serial")
public abstract class JLabelMenu extends JLabel{

	public JLabelMenu(String text) {
		super(text, IconManager.SPREAD, SwingConstants.RIGHT);
		this.setHorizontalTextPosition(SwingConstants.LEADING);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JPopupMenu popup = new JPopupMenu();
				fillPopUp(popup);
			    Dimension size = popup.getPreferredSize();
			    popup.setPreferredSize(new Dimension(Math.max(size.width, getSize().width), size.height));
				popup.show(e.getComponent(), 0, getSize().height);
			}
		});
	}
	
	protected abstract void fillPopUp(JPopupMenu popup);
}
