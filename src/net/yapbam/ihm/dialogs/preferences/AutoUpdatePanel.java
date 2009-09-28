package net.yapbam.ihm.dialogs.preferences;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class AutoUpdatePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public AutoUpdatePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
	}

}
