package net.yapbam.gui.dialogs.preferences;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.GridBagConstraints;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;

import javax.swing.JPasswordField;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

import javax.swing.JCheckBox;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.IntegerWidget;
import com.fathzer.soft.ajlib.swing.widget.PasswordWidget;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;

public class ProxyPanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_PROXY_PORT = 3128;
	private static final int IP_PORT_MAX_VALUE = 65535;
	
	private JRadioButton noProxyButton = null;
	private JRadioButton proxyButton = null;
	private JPanel proxyPanel = null;
	private JLabel proxyLabel = null;
	private TextWidget proxyHostField = null;
	private JLabel proxyPortLabel = null;
	private IntegerWidget proxyPortField = null;
	private JPanel authenticationPanel = null;
	private JLabel userLabel = null;
	private TextWidget userField = null;
	private JLabel passwordLabel = null;
	private PasswordWidget passwordField = null;
	private JCheckBox showPassCheckBox = null;
	
	private PropertyChangeListener updateOkListener;
	
	/**
	 * This is the default constructor
	 */
	public ProxyPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.updateOkListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setOkDisabledCause(buildOkDisabledCause());
			}
		};
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.anchor = GridBagConstraints.NORTH;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.gridy = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridy = 0;
		this.setSize(388, 282);
		this.setLayout(new GridBagLayout());
		this.add(getNoProxyButton(), gridBagConstraints);
		this.add(getProxyButton(), gridBagConstraints1);
		this.add(getProxyPanel(), gridBagConstraints2);
		ButtonGroup group = new ButtonGroup();
		group.add(getNoProxyButton());
		group.add(getProxyButton());
		String host = Preferences.INSTANCE.getHttpProxyHost();
		getNoProxyButton().setSelected(host==null);
		getProxyButton().setSelected(host!=null);
		getProxyHostField().setText(host);
		if (host!=null) {
			getProxyPortField().setText(Integer.toString(Preferences.INSTANCE.getHttpProxyPort()));
		}
		getUserField().setText(Preferences.INSTANCE.getHttpProxyUser());
		getPasswordField().setText(Preferences.INSTANCE.getHttpProxyPassword());
	}
	
	private String buildOkDisabledCause() {
		if (getProxyButton().isSelected()) {
			if (getProxyHost()==null) {
				return Formatter.format(LocalizationData.get("PreferencesDialog.Network.proxyNameMissing"),getTitle()); //$NON-NLS-1$
			} else if (getProxyHost().indexOf(':')>=0) {
				return LocalizationData.get("PreferencesDialog.Network.invalidProxyName"); //$NON-NLS-1$
			} else {
				Integer port = getProxyPort();
				if ((port==0) || (port>IP_PORT_MAX_VALUE)) {
					return Formatter.format(LocalizationData.get("PreferencesDialog.Network.invalidPort"),getTitle(),IP_PORT_MAX_VALUE); //$NON-NLS-1$
				}
			}
		}
		return null;
	}

	/**
	 * This method initializes noProxyButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getNoProxyButton() {
		if (noProxyButton == null) {
			noProxyButton = new JRadioButton();
			noProxyButton.setText(LocalizationData.get("PreferencesDialog.Network.direct")); //$NON-NLS-1$
			noProxyButton.setToolTipText(LocalizationData.get("PreferencesDialog.Network.direct.tooltip")); //$NON-NLS-1$
			noProxyButton.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					setProxyEnabled(!getNoProxyButton().isSelected());
					setOkDisabledCause(buildOkDisabledCause());
				}
			});
		}
		return noProxyButton;
	}
	
	private void setProxyEnabled(boolean enabled) {
		proxyLabel.setEnabled(enabled);
		proxyPortLabel.setEnabled(enabled);
		userLabel.setEnabled(enabled);
		passwordLabel.setEnabled(enabled);
		showPassCheckBox.setEnabled(enabled);
		getProxyHostField().setEnabled(enabled);
		getProxyPortField().setEnabled(enabled);
		getUserField().setEnabled(enabled);
		getPasswordField().setEnabled(enabled);
		Color color = enabled?Color.BLACK:Color.GRAY;
		TitledBorder border = BorderFactory.createTitledBorder(null, LocalizationData.get("PreferencesDialog.Network.authentication"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, color); //$NON-NLS-1$
		getAuthenticationPanel().setBorder(border);
	}

	/**
	 * This method initializes proxyButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getProxyButton() {
		if (proxyButton == null) {
			proxyButton = new JRadioButton();
			proxyButton.setText(LocalizationData.get("PreferencesDialog.Network.proxy.title")); //$NON-NLS-1$
			proxyButton.setToolTipText(LocalizationData.get("PreferencesDialog.Network.proxy.tooltip")); //$NON-NLS-1$
			proxyButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					setOkDisabledCause(buildOkDisabledCause());
				}
			});
		}
		return proxyButton;
	}

	/**
	 * This method initializes proxyPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getProxyPanel() {
		if (proxyPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints7.gridy = 2;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints5.weightx = 1.0;
			proxyPortLabel = new JLabel();
			proxyPortLabel.setText(LocalizationData.get("PreferencesDialog.Network.port.label")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			proxyLabel = new JLabel();
			proxyLabel.setText(LocalizationData.get("PreferencesDialog.Network.proxy.label")); //$NON-NLS-1$
			proxyPanel = new JPanel();
			proxyPanel.setLayout(new GridBagLayout());
			proxyPanel.add(proxyLabel, gridBagConstraints4);
			proxyPanel.add(getProxyHostField(), gridBagConstraints3);
			proxyPanel.add(proxyPortLabel, gridBagConstraints6);
			proxyPanel.add(getProxyPortField(), gridBagConstraints5);
			proxyPanel.add(getAuthenticationPanel(), gridBagConstraints7);
		}
		return proxyPanel;
	}

	/**
	 * This method initializes proxyHostField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getProxyHostField() {
		if (proxyHostField == null) {
			proxyHostField = new TextWidget(10);
			proxyHostField.setToolTipText(LocalizationData.get("PreferencesDialog.Network.proxyField.tooltip")); //$NON-NLS-1$
			proxyHostField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, this.updateOkListener);
		}
		return proxyHostField;
	}

	/**
	 * This method initializes proxyPortField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private IntegerWidget getProxyPortField() {
		if (proxyPortField == null) {
			proxyPortField = new IntegerWidget(BigInteger.ZERO, null); //$NON-NLS-1$
			proxyPortField.setColumns(10);
			proxyPortField.setToolTipText(LocalizationData.get("PreferencesDialog.Network.port.tooltip")); //$NON-NLS-1$
			proxyPortField.addPropertyChangeListener(IntegerWidget.VALUE_PROPERTY, this.updateOkListener);
		}
		return proxyPortField;
	}

	/**
	 * This method initializes authenticationPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAuthenticationPanel() {
		if (authenticationPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints10.gridy = 1;
			passwordLabel = new JLabel();
			passwordLabel.setText(LocalizationData.get("PreferencesDialog.Network.password")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints9.gridwidth = 2;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridy = 0;
			userLabel = new JLabel();
			userLabel.setText(LocalizationData.get("PreferencesDialog.Network.user")); //$NON-NLS-1$
			authenticationPanel = new JPanel();
			authenticationPanel.setLayout(new GridBagLayout());
			authenticationPanel.add(userLabel, gridBagConstraints8);
			authenticationPanel.add(getUserField(), gridBagConstraints9);
			authenticationPanel.add(passwordLabel, gridBagConstraints10);
			authenticationPanel.add(getPasswordField(), gridBagConstraints11);
			authenticationPanel.add(getShowPassCheckBox(), gridBagConstraints12);
		}
		return authenticationPanel;
	}

	/**
	 * This method initializes userField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUserField() {
		if (userField == null) {
			userField = new TextWidget();
			userField.setToolTipText(LocalizationData.get("PreferencesDialog.Network.user.toolTip")); //$NON-NLS-1$
			userField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, updateOkListener);
		}
		return userField;
	}

	/**
	 * This method initializes passwordField	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getPasswordField() {
		if (passwordField == null) {
			passwordField = new PasswordWidget();
			passwordField.setToolTipText(LocalizationData.get("PreferencesDialog.Network.password.tooltip")); //$NON-NLS-1$
			passwordField.addPropertyChangeListener(PasswordWidget.TEXT_PROPERTY, updateOkListener);
		}
		return passwordField;
	}

	public String getProxyHost() {
		String host = getProxyHostField().getText().trim();
		if (!getProxyButton().isSelected() || (host.length()==0)) {
			return null;
		} else {
			return host;
		}
	}

	public Integer getProxyPort() {
		if (getProxyHost()==null) {
			return null;
		} else {
			if (getProxyPortField().getText().trim().length()==0) {
				return DEFAULT_PROXY_PORT; // The default port
			}
			BigInteger port = getProxyPortField().getValue();
			if (port.compareTo(new BigInteger(Integer.toString(IP_PORT_MAX_VALUE)))>0) {
				return Integer.MAX_VALUE;
			} else {
				return port.intValue();
			}
		}
	}

	public String getProxyUser() {
		if (getProxyHost()==null) {
			return null;
		}
		String user = getUserField().getText().trim();
		return user.length()==0?null:user;
	}

	public String getProxyPassword() {
		if (getProxyUser()==null) {
			return null;
		}
		String password = new String(getPasswordField().getPassword()).trim();
		return password.length()==0?null:password;
	}

	/**
	 * This method initializes showPassCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getShowPassCheckBox() {
		if (showPassCheckBox == null) {
			showPassCheckBox = new JCheckBox();
			showPassCheckBox.setText(LocalizationData.get("PreferencesDialog.Network.showPassword")); //$NON-NLS-1$
			showPassCheckBox.setToolTipText(LocalizationData.get("PreferencesDialog.Network.showPassword.toolTip")); //$NON-NLS-1$
			showPassCheckBox.addItemListener(new java.awt.event.ItemListener() {
				char oldEcho;
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					if (e.getStateChange()==ItemEvent.DESELECTED) {
						passwordField.setEchoChar(oldEcho);
					} else {
						oldEcho = passwordField.getEchoChar();
						passwordField.setEchoChar((char) 0);
					}
				}
			});
		}
		return showPassCheckBox;
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.Network.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.Network.toolTip"); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		Preferences.INSTANCE.setHttpProxy(getProxyHost(), getProxyPort(), getProxyUser(), getProxyPassword());
		return false;
	}
}
