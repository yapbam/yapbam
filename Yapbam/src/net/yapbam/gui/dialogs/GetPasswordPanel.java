package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;

import javax.swing.JPasswordField;
import javax.swing.JCheckBox;

import net.yapbam.gui.LocalizationData;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.SwingConstants;

public class GetPasswordPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JPasswordField passwordField = null;
	private JCheckBox showPassword = null;
	private JPanel jPanel = null;
	private JLabel jLabel1 = null;
	private JPanel warningPanel = null;
	private JLabel warningField = null;
	/**
	 * This is the default constructor
	 */
	public GetPasswordPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints2.weightx = 0.0D;
		gridBagConstraints2.weighty = 1.0D;
		gridBagConstraints2.gridy = 4;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		this.setSize(447, 198);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints);
		this.add(getJPanel(), gridBagConstraints11);
		this.add(getWarningPanel(), gridBagConstraints2);
	}

	/**
	 * This method initializes passwordField	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getPasswordField() {
		if (passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setColumns(10);
			passwordField.setColumns(8);
		}
		return passwordField;
	}

	/**
	 * This method initializes showPassword	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getShowPassword() {
		if (showPassword == null) {
			showPassword = new JCheckBox();
			showPassword.setText(LocalizationData.get("PreferencesDialog.Network.showPassword")); //$NON-NLS-1$
			showPassword.setToolTipText(LocalizationData.get("PreferencesDialog.Network.showPassword.toolTip")); //$NON-NLS-1$
			showPassword.addItemListener(new java.awt.event.ItemListener() {
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
		return showPassword;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints1.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText(LocalizationData.get("PreferencesDialog.Network.password")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints3.weightx = 1.0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getPasswordField(), gridBagConstraints3);
			jPanel.add(getShowPassword(), gridBagConstraints4);
			jPanel.add(jLabel1, gridBagConstraints1);
		}
		return jPanel;
	}

	public String getPassword() {
		return new String(this.passwordField.getPassword());
	}
	
	public void setPassword(String password) {
		this.passwordField.setText(password) ;
	}

	/**
	 * This method initializes warningPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWarningPanel() {
		if (warningPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.weightx = 0.0D;
			gridBagConstraints5.weighty = 1.0D;
			gridBagConstraints5.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints5.gridy = 0;
			warningField = new JLabel();
			warningField.setIcon(UIManager.getIcon("OptionPane.warningIcon")); //$NON-NLS-1$
			warningField.setVerticalAlignment(SwingConstants.TOP);
			warningPanel = new JPanel();
			warningPanel.setLayout(new GridBagLayout());
			warningPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			warningPanel.setVisible(false);
			warningPanel.add(warningField, gridBagConstraints5);
		}
		return warningPanel;
	}
	
	/** Changes the warning message.
	 * This panel is able to display a warning message, something like:
	 * We aware that loosing your passwords leads to loosing your data.
	 * @param message the new message
	 */
	public void setWarningMessage(String message) {
		if (message!=null) this.warningField.setText(message);
		this.getWarningPanel().setVisible(message!=null);
	}

	/** Sets the question.
	 * @param question The question
	 */
	public void setQuestion(String question) {
		this.jLabel.setText(question);
	}
	
	/** Sets the icon before the question.
	 * @param icon an icon or null to have no icon
	 */
	public void setIcon(Icon icon) {
		this.jLabel.setIcon(icon);
	}
	
	/** Sets the password field tooltip.
	 * This tip is null by default.
	 * @param tooltip The new Tooltip
	 */
	public void setPasswordFieldToolTipText (String tooltip) {
		this.getPasswordField().setToolTipText(tooltip);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
