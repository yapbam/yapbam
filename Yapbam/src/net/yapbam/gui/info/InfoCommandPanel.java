package net.yapbam.gui.info;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import net.yapbam.gui.widget.PageSelector;
import javax.swing.JButton;

public class InfoCommandPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JCheckBox chckbxNewCheckBox;
	private PageSelector pageSelector;
	private JButton markAsReadButton;

	/**
	 * Constructor.
	 */
	public InfoCommandPanel() {
		initialize();
	}
	private void initialize() {
		setOpaque(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcChckbxNewCheckBox = new GridBagConstraints();
		gbcChckbxNewCheckBox.insets = new Insets(0, 0, 0, 5);
		gbcChckbxNewCheckBox.gridx = 0;
		gbcChckbxNewCheckBox.gridy = 0;
		add(getChckbxNewCheckBox(), gbcChckbxNewCheckBox);
		GridBagConstraints gbcPageSelector = new GridBagConstraints();
		gbcPageSelector.weightx = 1.0;
		gbcPageSelector.insets = new Insets(0, 0, 0, 5);
		gbcPageSelector.gridx = 1;
		gbcPageSelector.gridy = 0;
		add(getPageSelector(), gbcPageSelector);
		GridBagConstraints gbcMarkAsReadButton = new GridBagConstraints();
		gbcMarkAsReadButton.insets = new Insets(0, 5, 0, 0);
		gbcMarkAsReadButton.gridx = 2;
		gbcMarkAsReadButton.gridy = 0;
		add(getMarkAsReadButton(), gbcMarkAsReadButton);
	}

	public JCheckBox getChckbxNewCheckBox() {
		if (chckbxNewCheckBox == null) {
			chckbxNewCheckBox = new JCheckBox("show {0} read messages");
		}
		return chckbxNewCheckBox;
	}
	public PageSelector getPageSelector() {
		if (pageSelector == null) {
			pageSelector = new PageSelector();
		}
		return pageSelector;
	}
	public JButton getMarkAsReadButton() {
		if (markAsReadButton == null) {
			markAsReadButton = new JButton("Mark as read");
		}
		return markAsReadButton;
	}
}
