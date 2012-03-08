package net.yapbam.gui.transfer;

import javax.swing.JPanel;

import net.yapbam.data.GlobalData;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class FromOrToPane extends JPanel {
	private static final long serialVersionUID = 1L;
	private AccountWidget panel;
	private GlobalData data;

	/**
	 * Create the panel.
	 */
	public FromOrToPane(GlobalData data) {
		this.data = data;
		initialize();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(getPanel(), gbc_panel);
	}

	private AccountWidget getPanel() {
		if (panel == null) {
			panel = new AccountWidget(data);
		}
		return panel;
	}
}
