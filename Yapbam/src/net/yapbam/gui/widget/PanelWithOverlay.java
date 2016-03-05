package net.yapbam.gui.widget;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import net.yapbam.gui.Preferences;

@SuppressWarnings("serial")
public class PanelWithOverlay extends JLayeredPane {
	public enum Where {
		NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST
	}
	private Where where;
	
	public PanelWithOverlay(final JComponent background, final JComponent overlay) {
		this(background, overlay, Where.NORTH_EAST);
	}
	
	public PanelWithOverlay(final JComponent background, final JComponent overlay, Where where) {
		super();
		this.where = where;
		add(background, 1);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				background.setSize(getSize());
			}
		});
		add(overlay, 0);
		final int margin = (int) (Preferences.INSTANCE.getFontSizeRatio()*3);
		background.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				Rectangle bounds = background.getBounds();
				Dimension preferredSize = overlay.getPreferredSize();
				bounds.x = isAtEast() ? bounds.width - preferredSize.width - margin : margin;
				bounds.y = isAtNorth() ? margin : bounds.height - preferredSize.height - margin;
				bounds.width = preferredSize.width;
				bounds.height = preferredSize.height;
				overlay.setBounds(bounds);
			}
		});
	}

	private boolean isAtEast() {
		return Where.NORTH_EAST.equals(where) || Where.SOUTH_EAST.equals(where);
	}

	private boolean isAtNorth() {
		return Where.NORTH_EAST.equals(where) || Where.NORTH_WEST.equals(where);
	}
}
