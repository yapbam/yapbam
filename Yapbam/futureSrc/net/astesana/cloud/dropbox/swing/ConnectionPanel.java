package net.astesana.cloud.dropbox.swing;

import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;

import java.awt.GridBagConstraints;
import java.text.MessageFormat;

import java.awt.Insets;

@SuppressWarnings("serial")
class ConnectionPanel extends JPanel {
	
	public static final String STATE_PROPERTY = "State"; //$NON-NLS-1$
	
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel textArea;

	private String okButtonName;
	
	/**
	 * Create the panel.
	 */
	ConnectionPanel(String okButtonName) {
		this.okButtonName = okButtonName;
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		add(getLblNewLabel_1(), gbc_lblNewLabel_1);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.HORIZONTAL;
		gbc_textArea.weightx = 1.0;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 0;
		add(getTextArea(), gbc_textArea);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 0;
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(getLblNewLabel(), gbc_lblNewLabel);
	}

	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel(UIManager.getIcon("OptionPane.informationIcon")); //$NON-NLS-1$
		}
		return lblNewLabel_1;
	}
	
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			String message = LocalizationData.get("dropbox.Chooser.ConnectionDialog.message.content"); //$NON-NLS-1$
			message = MessageFormat.format(message, getConnectButtonName(), okButtonName);
			lblNewLabel = new JLabel();
			lblNewLabel.setText(message);
		}
		return lblNewLabel;
	}

	String getConnectButtonName() {
		return LocalizationData.get("dropbox.Chooser.ConnectionDialog.startButton"); //$NON-NLS-1$
	}
	
	private JLabel getTextArea() {
		if (textArea == null) {
			textArea = new JLabel(LocalizationData.get("dropbox.Chooser.ConnectionDialog.message.header"));  //$NON-NLS-1$
		}
		return textArea;
	}
}
