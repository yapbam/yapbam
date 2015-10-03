package net.yapbam.gui.widget;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import net.yapbam.gui.Preferences;

@SuppressWarnings("serial")
public class TabbedPaneWithOption extends JLayeredPane {
	public TabbedPaneWithOption(final JComponent main, final JComponent option) {
		super();
		add(main, 1);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				main.setSize(getSize());
			}
		});
		add(option, 0);
		final int margin = (int) (Preferences.INSTANCE.getFontSizeRatio()*3);
		main.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				Rectangle bounds = main.getBounds();
				Dimension preferredSize = option.getPreferredSize();
				bounds.x = bounds.width - preferredSize.width - margin;
				bounds.y = margin;
				bounds.width = preferredSize.width;
				bounds.height = preferredSize.height;
				option.setBounds(bounds);
			}
		});
	}

}
