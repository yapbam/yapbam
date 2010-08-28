package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.JPasswordField;
import javax.swing.JCheckBox;

import net.yapbam.gui.LocalizationData;
import java.awt.Insets;
import java.awt.event.ItemEvent;

public class FilePasswordPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JPasswordField passwordField = null;
	private JCheckBox showPassword = null;
	private JPanel jPanel = null;
	private JLabel jLabel1 = null;
	/**
	 * This is the default constructor
	 */
	public FilePasswordPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridy = 3;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("Tapez ci-dessous le mot de passe permettant de protéger l'accès au fichier");		//LOCAL
		this.setSize(447, 198);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints);
		this.add(getJPanel(), gridBagConstraints11);
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
			showPassword.setText(LocalizationData.get("PreferencesDialog.Network.showPassword"));
			showPassword.setToolTipText(LocalizationData.get("PreferencesDialog.Network.showPassword.toolTip"));
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
			jLabel1.setText(LocalizationData.get("PreferencesDialog.Network.password"));
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

	String getPassword() {
		return new String(this.passwordField.getPassword());
	}
	
	void setPassword(String password) {
		this.passwordField.setText(password) ;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
