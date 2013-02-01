package net.yapbam.gui.widget;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import net.yapbam.gui.IconManager;
import net.yapbam.gui.IconManager.Name;

/** This abstract class implements a popupmenu that pops up when the user clicks
 * on a label.
 * In order to use this widget, you have to override the fillpopup method.
 * @see #fillPopUp(JPopupMenu)
 */
@SuppressWarnings("serial")
public abstract class JLabelMenu extends JLabel{

	public JLabelMenu(String text) {
		super(text, IconManager.get(Name.SPREAD), SwingConstants.LEFT);
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
		
	@Override
	public void setSize(Dimension d) {
		this.setSize(d.width, d.height);
	}

	@Override
	public void setSize(int width, int height) {
		int textWidth = this.getFontMetrics(getFont()).stringWidth(getText());
		int remaining = width - textWidth - getIcon().getIconWidth();
		Border border = getBorder();
		if (border!=null) {
			Insets insets = border.getBorderInsets(this);
			remaining = remaining - insets.left - insets.right;
		}
		setIconTextGap(Math.max(0, remaining));
		super.setSize(width, height);
	}

	/** Fills the popup.
	 * Subclasses have to implements this method to populate the popup.
	 * This method is called each time the popup have to be displayed.
	 * @param popup
	 */
	protected abstract void fillPopUp(JPopupMenu popup);
}
