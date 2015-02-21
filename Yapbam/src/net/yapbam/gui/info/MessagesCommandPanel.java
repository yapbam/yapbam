package net.yapbam.gui.info;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;

import net.yapbam.gui.LocalizationData;

import com.fathzer.soft.ajlib.swing.widget.PageSelector;

public class MessagesCommandPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JCheckBox showReadCheckBox;
	private PageSelector pageSelector;
	private JButton markAsReadButton;

	/**
	 * Constructor.
	 */
	public MessagesCommandPanel() {
		initialize();
	}
	private void initialize() {
		setOpaque(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcShowReadCheckBox = new GridBagConstraints();
		gbcShowReadCheckBox.anchor = GridBagConstraints.WEST;
		gbcShowReadCheckBox.weightx = 1.0;
		gbcShowReadCheckBox.insets = new Insets(0, 0, 0, 5);
		gbcShowReadCheckBox.gridx = 0;
		gbcShowReadCheckBox.gridy = 0;
		add(getShowReadCheckBox(), gbcShowReadCheckBox);
		GridBagConstraints gbcPageSelector = new GridBagConstraints();
		gbcPageSelector.weightx = 1.0;
		gbcPageSelector.insets = new Insets(0, 0, 0, 5);
		gbcPageSelector.gridx = 1;
		gbcPageSelector.gridy = 0;
		add(getPageSelector(), gbcPageSelector);
		GridBagConstraints gbcMarkAsReadButton = new GridBagConstraints();
		gbcMarkAsReadButton.weightx = 1.0;
		gbcMarkAsReadButton.anchor = GridBagConstraints.EAST;
		gbcMarkAsReadButton.insets = new Insets(0, 5, 0, 0);
		gbcMarkAsReadButton.gridx = 2;
		gbcMarkAsReadButton.gridy = 0;
		add(getMarkAsReadButton(), gbcMarkAsReadButton);
	}

	public JCheckBox getShowReadCheckBox() {
		if (showReadCheckBox == null) {
			showReadCheckBox = new JCheckBox();
		}
		return showReadCheckBox;
	}
	public PageSelector getPageSelector() {
		if (pageSelector == null) {
			pageSelector = new PageSelector();
		}
		return pageSelector;
	}
	public JButton getMarkAsReadButton() {
		if (markAsReadButton == null) {
			markAsReadButton = new JButton(LocalizationData.get("messages.markAsRead")); //$NON-NLS-1$
		}
		return markAsReadButton;
	}
}
