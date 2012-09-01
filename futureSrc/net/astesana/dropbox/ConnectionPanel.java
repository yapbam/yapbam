package net.astesana.dropbox;

import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.text.MessageFormat;

import java.awt.Insets;

@SuppressWarnings("serial")
class ConnectionPanel extends JPanel {
	
	public static final String STATE_PROPERTY = "State";
	
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
			lblNewLabel_1 = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
		}
		return lblNewLabel_1;
	}
	
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			String message = "<html>Yapbam will only have access to a specific folder (/Applications/Yapbam), not your whole account.<br>" +
					"<br>Click the \"<b>{0}</b>\" button when you are ready to link Yapbam to your account (requires an Internet connection)<br>" +
					"Then, you will be redirected to a browser window where Dropbox will ask you to grant access to Yapbam.<br>" +
					"<br><b>After</b> you''ve granted access to Dropbox click the \"<b>{1}</b>\" button</html>";
			message = MessageFormat.format(message, getConnectButtonName(), okButtonName);
			lblNewLabel = new JLabel();
			lblNewLabel.setText(message);
		}
		return lblNewLabel;
	}

	String getConnectButtonName() {
		return "Start Connection";
	}
	
	private JLabel getTextArea() {
		if (textArea == null) {
			textArea = new JLabel("<html>Storing data to <b>Dropbox</b> requires that you authorize Yapbam to connect to your Yapbam account.</html>"); 
		}
		return textArea;
	}
}
