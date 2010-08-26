package net.yapbam.gui.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;

import net.yapbam.gui.LocalizationData;
import java.awt.Dimension;

public class FilePasswordPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel = null;
	private JRadioButton noPassword = null;
	private JRadioButton withPassword = null;
	private JPasswordField password = null;
	private JCheckBox showPassword = null;
	private JPanel jPanel = null;

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
		gridBagConstraints11.gridy = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridy = 0;
		jLabel = new JLabel();
		jLabel.setText("JLabel");
		this.setSize(447, 198);
		this.setLayout(new GridBagLayout());
		this.add(jLabel, gridBagConstraints);
		this.add(getNoPassword(), gridBagConstraints1);
		this.add(getJPanel(), gridBagConstraints11);
		ButtonGroup group = new ButtonGroup();
		group.add(getNoPassword());
		group.add(getWithPassword());
	}

	/**
	 * This method initializes noPassword	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getNoPassword() {
		if (noPassword == null) {
			noPassword = new JRadioButton();
			noPassword.setText("No password protection");
			noPassword.setToolTipText("Choose this option in order to have no password protection on this file");
		}
		return noPassword;
	}

	/**
	 * This method initializes withPassword	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getWithPassword() {
		if (withPassword == null) {
			withPassword = new JRadioButton();
			withPassword.setText("Password :");
			withPassword.setToolTipText("Select this option in order to protect this data access with a password");
		}
		return withPassword;
	}

	/**
	 * This method initializes password	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getPassword() {
		if (password == null) {
			password = new JPasswordField();
			password.setColumns(8);
		}
		return password;
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
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.gridy = -1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getWithPassword(), gridBagConstraints2);
			jPanel.add(getPassword(), gridBagConstraints3);
			jPanel.add(getShowPassword(), gridBagConstraints4);
		}
		return jPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
