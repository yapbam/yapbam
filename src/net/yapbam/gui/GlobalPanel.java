package net.yapbam.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.info.MessagesPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GlobalPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MessagesPanel infoPanel;
	private MainPanel mainPanel;
	private JButton infoDisplayer;

	/**
	 * Create the panel.
	 */
	public GlobalPanel() {
		this(new MainPanel(new AbstractPlugIn[0]));
	}
	/**
	 * Create the panel.
	 * @param mainPanel2 
	 */
	public GlobalPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		initialize();
	}
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getInfoPanel(), BorderLayout.NORTH);
		getInfoPanel().setDisplayButton(getInfoDisplayer());
		final JLayeredPane layered = new JLayeredPane();
		add(layered, BorderLayout.CENTER);
		layered.add(getMainPanel());
		layered.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				getMainPanel().setSize(layered.getSize());
			}
		});
		layered.add(getInfoDisplayer(), JLayeredPane.MODAL_LAYER);
		final int margin = (int) (Preferences.INSTANCE.getFontSizeRatio()*3);
		getMainPanel().addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				Rectangle bounds = getMainPanel().getBounds();
				Dimension preferredSize = getInfoDisplayer().getPreferredSize();
				bounds.x = bounds.width - preferredSize.width - margin;
				bounds.y = margin;
				bounds.width = preferredSize.width;
				bounds.height = preferredSize.height;
				getInfoDisplayer().setBounds(bounds);
			}
		});
	}
	
	private JButton getInfoDisplayer() {
		if (infoDisplayer==null) {
			infoDisplayer = new JButton(IconManager.get(Name.MESSAGE));
			infoDisplayer.setBorder(BorderFactory.createEmptyBorder());
			infoDisplayer.setBorderPainted(false);
			infoDisplayer.setFocusPainted(false);
			infoDisplayer.setContentAreaFilled(false);
			infoDisplayer.setVisible(false);
		}
		return infoDisplayer;
	}

	MessagesPanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new MessagesPanel();
			infoPanel.setVisible(false);
		}
		return infoPanel;
	}

	MainPanel getMainPanel() {
		return mainPanel;
	}
}
