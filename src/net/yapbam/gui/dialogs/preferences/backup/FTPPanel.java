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
import java.util.LinkedList;
import java.util.List;

import javax.swing.border.TitledBorder;

import net.yapbam.gui.LocalizationData;

public class FTPPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTextField hostField;
	private JTextField folderField;
	private JRadioButton ftpRdnButton;
	private JTextField userField;
	private JPasswordField passwordField;
	private JCheckBox showPassWordCheckBox;

	private List<JLabel> labels;

	/**
	 * Create the panel.
	 */
	public FTPPanel() {
		initialize();
	}

	private void initialize() {
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		labels = new LinkedList<JLabel>(); 
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_ftpRdnButton = new GridBagConstraints();
		gbc_ftpRdnButton.weightx = 1.0;
		gbc_ftpRdnButton.gridwidth = 0;
		gbc_ftpRdnButton.anchor = GridBagConstraints.WEST;
		gbc_ftpRdnButton.insets = new Insets(0, 0, 5, 5);
		gbc_ftpRdnButton.gridx = 0;
		gbc_ftpRdnButton.gridy = 0;
		add(getFtpRdnButton(), gbc_ftpRdnButton);
		GridBagConstraints gbc_showPassWordCheckBox = new GridBagConstraints();
		gbc_showPassWordCheckBox.anchor = GridBagConstraints.NORTH;
		gbc_showPassWordCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_showPassWordCheckBox.gridx = 2;
		gbc_showPassWordCheckBox.gridy = 4;
		add(getShowPassWordCheckBox(), gbc_showPassWordCheckBox);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.anchor = GridBagConstraints.NORTH;
		gbc_passwordField.weighty = 1.0;
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
		labels.add(new JLabel(LocalizationData.get("PreferencesDialog.Network.password"))); //$NON-NLS-1$
		add(labels.get(labels.size()-1), gbc_label_3);
		GridBagConstraints gbc_userField = new GridBagConstraints();
		gbc_userField.insets = new Insets(0, 0, 5, 5);
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
		labels.add(new JLabel(LocalizationData.get("PreferencesDialog.Network.user"))); //$NON-NLS-1$
		add(labels.get(labels.size()-1), gbc_label_2);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		labels.add(new JLabel(LocalizationData.get("Backup.preference.ftp.server"))); //$NON-NLS-1$
		add(labels.get(labels.size()-1), gbc_label);
		GridBagConstraints gbc_hostField = new GridBagConstraints();
		gbc_hostField.gridwidth = 0;
		gbc_hostField.weightx = 1.0;
		gbc_hostField.insets = new Insets(0, 0, 5, 5);
		gbc_hostField.fill = GridBagConstraints.HORIZONTAL;
		gbc_hostField.gridx = 1;
		gbc_hostField.gridy = 1;
		add(getHostField(), gbc_hostField);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.WEST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 2;
		labels.add(new JLabel(LocalizationData.get("Backup.preference.ftp.directory"))); //$NON-NLS-1$
		add(labels.get(labels.size()-1), gbc_label_1);
		GridBagConstraints gbc_folderField = new GridBagConstraints();
		gbc_folderField.gridwidth = 0;
		gbc_folderField.insets = new Insets(0, 0, 5, 5);
		gbc_folderField.weightx = 1.0;
		gbc_folderField.fill = GridBagConstraints.HORIZONTAL;
		gbc_folderField.gridx = 1;
		gbc_folderField.gridy = 2;
		add(getFolderField(), gbc_folderField);
	}

	private JTextField getHostField() {
		if (hostField == null) {
			hostField = new JTextField();
			hostField.setToolTipText(LocalizationData.get("Backup.preference.ftp.server.tooltip")); //$NON-NLS-1$
			hostField.setColumns(10);
		}
		return hostField;
	}

	private JTextField getFolderField() {
		if (folderField == null) {
			folderField = new JTextField();
			folderField.setToolTipText(LocalizationData.get("Backup.preference.ftp.directory.tooltip")); //$NON-NLS-1$
			folderField.setColumns(10);
		}
		return folderField;
	}
	
	public JRadioButton getFtpRdnButton() {
		if (ftpRdnButton == null) {
			ftpRdnButton = new JRadioButton(LocalizationData.get("Backup.preference.ftp.ftp")); //$NON-NLS-1$
			ftpRdnButton.setToolTipText(LocalizationData.get("Backup.preference.ftp.ftp.tooltip")); //$NON-NLS-1$
			ftpRdnButton.setSelected(true);
			ftpRdnButton.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean selected = e.getStateChange()==ItemEvent.SELECTED;
					getUserField().setVisible(selected);
					getPasswordField().setVisible(selected);
					getHostField().setVisible(selected);
					getFolderField().setVisible(selected);
					getShowPassWordCheckBox().setVisible(selected);
					for (JLabel label : labels) {
						label.setVisible(selected);
					}
				}
			});
		}
		return ftpRdnButton;
	}
	
	private JTextField getUserField() {
		if (userField == null) {
			userField = new JTextField();
			userField.setToolTipText(LocalizationData.get("Backup.preference.ftp.user.tooltip")); //$NON-NLS-1$
		}
		return userField;
	}
	
	private JPasswordField getPasswordField() {
		if (passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setToolTipText(LocalizationData.get("Backup.preference.ftp.password.tooltip")); //$NON-NLS-1$
			passwordField.setColumns(10);
		}
		return passwordField;
	}
	
	private JCheckBox getShowPassWordCheckBox() {
		if (showPassWordCheckBox == null) {
			showPassWordCheckBox = new JCheckBox(LocalizationData.get("PreferencesDialog.Network.showPassword")); //$NON-NLS-1$
			showPassWordCheckBox.addItemListener(new ItemListener() {
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
			showPassWordCheckBox.setToolTipText(LocalizationData.get("PreferencesDialog.Network.showPassword.toolTip")); //$NON-NLS-1$
		}
		return showPassWordCheckBox;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getFtpRdnButton().setEnabled(enabled);
		getHostField().setEditable(enabled);
		getHostField().setEnabled(enabled);
		getFolderField().setEditable(enabled);
		getFolderField().setEnabled(enabled);
		getUserField().setEditable(enabled);
		getUserField().setEnabled(enabled);
		getPasswordField().setEditable(enabled);
		getPasswordField().setEnabled(enabled);
		getShowPassWordCheckBox().setEnabled(enabled);
		for (JLabel label : labels) {
			label.setEnabled(enabled);
		}
	}
}
