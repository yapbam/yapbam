package net.yapbam.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.yapbam.gui.IconManager.Name;
import net.yapbam.gui.info.MessagesPanel;
import net.yapbam.gui.widget.PanelWithOverlay;

import java.awt.BorderLayout;

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
		add (new PanelWithOverlay(getMainPanel(), getInfoDisplayer()));
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
