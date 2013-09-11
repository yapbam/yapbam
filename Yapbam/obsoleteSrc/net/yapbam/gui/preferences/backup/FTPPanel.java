package net.yapbam.gui.dialogs.preferences.backup;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;

import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import javax.swing.border.TitledBorder;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;
import com.fathzer.soft.ajlib.swing.widget.PasswordWidget;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;
import com.fathzer.soft.ajlib.swing.widget.IntegerWidget;
import com.fathzer.soft.ajlib.utilities.NullUtils;
import com.fathzer.soft.ajlib.utilities.StringUtils;
import net.yapbam.gui.ErrorManager;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;

public class FTPPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private TextWidget hostField;
	private JTextField folderField;
	private JRadioButton ftpRdnButton;
	private TextWidget userField;
	private PasswordWidget passwordField;
	private JCheckBox showPassWordCheckBox;

	private List<JLabel> labels;

	private String okDisabledCause;

	private PropertyChangeListener updateOkListener;
	private PropertyChangeListener autoSelectListener;
	private JPanel panel;
	private JPanel hostPanel;
	private IntegerWidget portField;

	/**
	 * Create the panel.
	 */
	public FTPPanel() {
		initialize();
	}

	private void initialize() {
		this.updateOkListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateOkDisabledCause();
			}
		};
		this.autoSelectListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String text = (String) evt.getNewValue();
				if ((text!=null) && (text.length()>0)) getFtpRdnButton().setSelected(true);
			}
		};
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		labels = new LinkedList<JLabel>(); 
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_ftpRdnButton = new GridBagConstraints();
		gbc_ftpRdnButton.weightx = 1.0;
		gbc_ftpRdnButton.gridwidth = 0;
		gbc_ftpRdnButton.anchor = GridBagConstraints.WEST;
		gbc_ftpRdnButton.insets = new Insets(0, 0, 5, 0);
		gbc_ftpRdnButton.gridx = 0;
		gbc_ftpRdnButton.gridy = 0;
		add(getFtpRdnButton(), gbc_ftpRdnButton);
		GridBagConstraints gbc_hostPanel = new GridBagConstraints();
		gbc_hostPanel.weightx = 1.0;
		gbc_hostPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_hostPanel.gridwidth = 0;
		gbc_hostPanel.gridx = 1;
		gbc_hostPanel.gridy = 1;
		add(getHostPanel(), gbc_hostPanel);
		GridBagConstraints gbc_showPassWordCheckBox = new GridBagConstraints();
		gbc_showPassWordCheckBox.insets = new Insets(0, 0, 5, 0);
		gbc_showPassWordCheckBox.gridx = 2;
		gbc_showPassWordCheckBox.gridy = 4;
		add(getShowPassWordCheckBox(), gbc_showPassWordCheckBox);
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
		JLabel label = new JLabel(LocalizationData.get("PreferencesDialog.Network.password")); //$NON-NLS-1$
		labels.add(label);
		add(label, gbc_label_3);
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
		labels.add(new JLabel(LocalizationData.get("PreferencesDialog.Network.user"))); //$NON-NLS-1$
		add(labels.get(labels.size()-1), gbc_label_2);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		labels.add(new JLabel(LocalizationData.get("Backup.preference.ftp.server"))); //$NON-NLS-1$
		add(labels.get(labels.size()-1), gbc_label);
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.WEST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 2;
		labels.add(new JLabel(LocalizationData.get("Backup.preference.ftp.directory"))); //$NON-NLS-1$
		add(labels.get(labels.size()-1), gbc_label_1);
		GridBagConstraints gbc_folderField = new GridBagConstraints();
		gbc_folderField.gridwidth = 0;
		gbc_folderField.insets = new Insets(0, 0, 5, 0);
		gbc_folderField.weightx = 1.0;
		gbc_folderField.fill = GridBagConstraints.HORIZONTAL;
		gbc_folderField.gridx = 1;
		gbc_folderField.gridy = 2;
		add(getFolderField(), gbc_folderField);
		this.okDisabledCause = null;
		this.getFtpRdnButton().setSelected(false);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weighty = 1.0;
		gbc_panel.gridwidth = 0;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 5;
		add(getPanel(), gbc_panel);
	}
	
	private void updateOkDisabledCause() {
		String old = this.okDisabledCause;
		this.okDisabledCause = null;
		if (getFtpRdnButton().isSelected()) { // If the button is not selected, the fields content doesn't matter
			if (getHostField().getText().trim().length()==0) {
				// No host name entered !
				this.okDisabledCause = MessageFormat.format(LocalizationData.get("Backup.preference.ftp.server.missing"),LocalizationData.get("Backup.preference.title"));  //$NON-NLS-1$//$NON-NLS-2$
			} else {
				BigInteger port = getPortField().getValue();
				if ((port!=null) && ((port.equals(BigInteger.ZERO)) || (port.compareTo(BigInteger.valueOf(65535))>0))) {
					// Port is out of range !
					this.okDisabledCause = MessageFormat.format(LocalizationData.get("Backup.preference.ftp.invalidPort"),LocalizationData.get("Backup.preference.title"), 65535);  //$NON-NLS-1$//$NON-NLS-2$
				} else if (getUserField().getText().trim().length()==0) {
					// No user account entered
					this.okDisabledCause = MessageFormat.format(LocalizationData.get("Backup.preference.ftp.user.missing"),LocalizationData.get("Backup.preference.title"));  //$NON-NLS-1$//$NON-NLS-2$
				}
			}
		}
		if (!NullUtils.areEquals(this.okDisabledCause, old)) {
			this.firePropertyChange(PreferencePanel.OK_DISABLED_CAUSE_PROPERTY, old, okDisabledCause);
		}
	}
	
	public String getOkDisabledCause() {
		return this.okDisabledCause;
	}

	private JTextField getHostField() {
		if (hostField == null) {
			hostField = new TextWidget();
			hostField.setToolTipText(LocalizationData.get("Backup.preference.ftp.server.tooltip")); //$NON-NLS-1$
			hostField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, this.updateOkListener);
			hostField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, this.autoSelectListener);
		}
		return hostField;
	}

	private JTextField getFolderField() {
		if (folderField == null) {
			folderField = new TextWidget();
			folderField.setToolTipText(LocalizationData.get("Backup.preference.ftp.directory.tooltip")); //$NON-NLS-1$
			folderField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, this.autoSelectListener);
		}
		return folderField;
	}
	
	public JRadioButton getFtpRdnButton() {
		if (ftpRdnButton == null) {
			ftpRdnButton = new JRadioButton(LocalizationData.get("Backup.preference.ftp.ftp")); //$NON-NLS-1$
			ftpRdnButton.setSelected(true);
			ftpRdnButton.setToolTipText(LocalizationData.get("Backup.preference.ftp.ftp.tooltip")); //$NON-NLS-1$
			ftpRdnButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					updateOkDisabledCause();
				}
			});
		}
		return ftpRdnButton;
	}
	
	private JTextField getUserField() {
		if (userField == null) {
			userField = new TextWidget();
			userField.setToolTipText(LocalizationData.get("Backup.preference.ftp.user.tooltip")); //$NON-NLS-1$
			userField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, this.updateOkListener);
			userField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, this.autoSelectListener);
		}
		return userField;
	}
	
	private PasswordWidget getPasswordField() {
		if (passwordField == null) {
			passwordField = new PasswordWidget();
			passwordField.setToolTipText(LocalizationData.get("Backup.preference.ftp.password.tooltip")); //$NON-NLS-1$
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
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
		}
		return panel;
	}

	private JPanel getHostPanel() {
		if (hostPanel == null) {
			hostPanel = new JPanel();
			GridBagLayout gbl_hostPanel = new GridBagLayout();
			hostPanel.setLayout(gbl_hostPanel);
			GridBagConstraints gbc_hostField = new GridBagConstraints();
			gbc_hostField.weightx = 1.0;
			gbc_hostField.fill = GridBagConstraints.HORIZONTAL;
			gbc_hostField.insets = new Insets(0, 0, 5, 0);
			gbc_hostField.gridx = 0;
			gbc_hostField.gridy = 0;
			hostPanel.add(getHostField(), gbc_hostField);
			GridBagConstraints gbc_label_3 = new GridBagConstraints();
			gbc_label_3 .anchor = GridBagConstraints.WEST;
			gbc_label_3.insets = new Insets(0, 5, 5, 5);
			gbc_label_3.gridx = 1;
			gbc_label_3.gridy = 0;
			JLabel label = new JLabel(LocalizationData.get("PreferencesDialog.Network.port.label")); //$NON-NLS-1$
			labels.add(label);
			hostPanel.add(label, gbc_label_3);
			GridBagConstraints gbc_portField = new GridBagConstraints();
			gbc_portField.insets = new Insets(0, 0, 5, 0);
			gbc_portField.fill = GridBagConstraints.HORIZONTAL;
			gbc_portField.gridx = 2;
			gbc_portField.gridy = 0;
			hostPanel.add(getPortField(), gbc_portField);
		}
		return hostPanel;
	}
	
	private IntegerWidget getPortField() {
		if (portField == null) {
			portField = new IntegerWidget(BigInteger.ZERO, null); //$NON-NLS-1$
			portField.setToolTipText(LocalizationData.get("Backup.preference.ftp.port.tooltip")); //$NON-NLS-1$
			portField.setColumns(5);
			portField.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, this.updateOkListener);
		}
		return portField;
	}

	public URI getURI() {
		try {
			StringBuilder uriBuf = new StringBuilder("ftp://").append(getUserField().getText()); //$NON-NLS-1$
			if (getPasswordField().getPassword().length>0) uriBuf.append(':').append(getPasswordField().getPassword());
			uriBuf.append('@').append(getHostField().getText());
			if (portField.getValue()!=null) {
				uriBuf.append(':').append(portField.getValue());
			}
			String folder = getFolderField().getText();
			if (folder.startsWith("/")) folder = folder.substring(1); //$NON-NLS-1$
			if (folder.length()>0) uriBuf.append('/').append(folder);
			URI uri = new URI(uriBuf.toString());
			return uri;
		} catch (URISyntaxException e) {
			ErrorManager.INSTANCE.log(AbstractDialog.getOwnerWindow(this), e);
			return null;
		}
	}
	
	public void setURI(URI uri) {
		if (!"ftp".equalsIgnoreCase(uri.getScheme())) throw new IllegalArgumentException(); //$NON-NLS-1$
		getHostField().setText(uri.getHost());
		int port = uri.getPort();
		if (port>0)	getPortField().setValue(BigInteger.valueOf(port));
		String userInfo = uri.getUserInfo();
		String[] split = StringUtils.split(userInfo, ':');
		getUserField().setText(split[0]);
		if (split.length>1) getPasswordField().setText(split[1]);
		getFolderField().setText(uri.getPath());
	}
}
