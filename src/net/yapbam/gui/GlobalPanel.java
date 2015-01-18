package net.yapbam.gui;

import javax.swing.JPanel;

import net.yapbam.gui.info.InfoPanel;

import java.awt.BorderLayout;

public class GlobalPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private InfoPanel infoPanel;
	private MainPanel mainPanel;

	/**
	 * Create the panel.
	 */
	public GlobalPanel() {
		initialize();
	}
	/**
	 * Create the panel.
	 * @param mainPanel2 
	 */
	public GlobalPanel(MainPanel mainPanel) {
		this();
		this.mainPanel = mainPanel;
		add(getMainPanel(), BorderLayout.CENTER);
	}
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getInfoPanel(), BorderLayout.NORTH);
	}

	InfoPanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new InfoPanel();
		}
		return infoPanel;
	}

	MainPanel getMainPanel() {
		return mainPanel;
	}
}
