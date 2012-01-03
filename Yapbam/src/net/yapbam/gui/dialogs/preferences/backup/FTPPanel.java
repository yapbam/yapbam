package net.yapbam.gui.dialogs.preferences.backup;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.border.TitledBorder;

import net.yapbam.gui.LocalizationData;

public class FTPPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextField hostField;
	private JTextField folderField;
	private JRadioButton ftpRdnButton;
	private JTextField userField;
	private JPasswordField passwordField;
	private JCheckBox checkBox;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public FTPPanel() {
		initialize();
	}

	private void initialize() {
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_ftpRdnButton = new GridBagConstraints();
		gbc_ftpRdnButton.anchor = GridBagConstraints.WEST;
		gbc_ftpRdnButton.insets = new Insets(0, 0, 5, 5);
		gbc_ftpRdnButton.gridx = 0;
		gbc_ftpRdnButton.gridy = 0;
		add(getFtpRdnButton(), gbc_ftpRdnButton);
		GridBagConstraints gbc_checkBox = new GridBagConstraints();
		gbc_checkBox.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox.gridx = 2;
		gbc_checkBox.gridy = 4;
		add(getCheckBox(), gbc_checkBox);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.weightx = 1.0;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 4;
		add(getPasswordField(), gbc_passwordField);
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.WEST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 4;
		add(new JLabel(LocalizationData.get("PreferencesDialog.Network.password")), gbc_label_3);
		GridBagConstraints gbc_userField = new GridBagConstraints();
		gbc_userField.insets = new Insets(0, 0, 5, 0);
		gbc_userField.gridwidth = 0;
		gbc_userField.weightx = 1.0;
		gbc_userField.fill = GridBagConstraints.HORIZONTAL;
		gbc_userField.gridx = 1;
		gbc_userField.gridy = 3;
		add(getUserField(), gbc_userField);
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.WEST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 3;
		add(new JLabel(LocalizationData.get("PreferencesDialog.Network.user")), gbc_label_2);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		add(new JLabel("Serveur :"), gbc_label);
		GridBagConstraints gbc_hostField = new GridBagConstraints();
		gbc_hostField.gridwidth = 0;
		gbc_hostField.weightx = 1.0;
		gbc_hostField.insets = new Insets(0, 0, 5, 0);
		gbc_hostField.fill = GridBagConstraints.HORIZONTAL;
		gbc_hostField.gridx = 1;
		gbc_hostField.gridy = 1;
		add(getHostField(), gbc_hostField);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.WEST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 2;
		add(new JLabel("R\u00E9pertoire :"), gbc_label_1);
		GridBagConstraints gbc_folderField = new GridBagConstraints();
		gbc_folderField.gridwidth = 0;
		gbc_folderField.insets = new Insets(0, 0, 5, 0);
		gbc_folderField.weightx = 1.0;
		gbc_folderField.fill = GridBagConstraints.HORIZONTAL;
		gbc_folderField.gridx = 1;
		gbc_folderField.gridy = 2;
		add(getFolderField(), gbc_folderField);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weighty = 1.0;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 5;
		add(getPanel(), gbc_panel);
	}

	private JTextField getHostField() {
		if (hostField == null) {
			hostField = new JTextField();
			hostField.setToolTipText("Entrez ici le nom du serveur o\u00F9 sauvegarder vos donn\u00E9es");
			hostField.setColumns(10);
		}
		return hostField;
	}

	private JTextField getFolderField() {
		if (folderField == null) {
			folderField = new JTextField();
			folderField.setToolTipText("Entrez ici le chemin du r\u00E9pertoire o\u00F9 stocker vos donn\u00E9es");
			folderField.setColumns(10);
		}
		return folderField;
	}
	
	JRadioButton getFtpRdnButton() {
		if (ftpRdnButton == null) {
			ftpRdnButton = new JRadioButton("FTP");
		}
		return ftpRdnButton;
	}
	
	private JTextField getUserField() {
		if (userField == null) {
			userField = new JTextField();
			userField.setToolTipText("Entrez ici le mot de passe du compte Ftp");
		}
		return userField;
	}
	
	private JPasswordField getPasswordField() {
		if (passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setToolTipText("Entrez ici le mot de passe du compte");
			passwordField.setColumns(10);
		}
		return passwordField;
	}
	
	private JCheckBox getCheckBox() {
		if (checkBox == null) {
			checkBox = new JCheckBox(LocalizationData.get("PreferencesDialog.Network.showPassword")); //$NON-NLS-1$
			checkBox.addItemListener(new ItemListener() {
				char oldEcho;
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange()==ItemEvent.DESELECTED) {
						passwordField.setEchoChar(oldEcho);
					} else {
						oldEcho = passwordField.getEchoChar();
						passwordField.setEchoChar((char) 0);
					}
				}
			});
			checkBox.setToolTipText(LocalizationData.get("PreferencesDialog.Network.showPassword.toolTip")); //$NON-NLS-1$
		}
		return checkBox;
	}
	
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
		}
		return panel;
	}
}
