package net.yapbam.gui.dialogs.preferences;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

public class LookAndFeelPanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private JRadioButton platformButton = null;
	private JRadioButton javaButton = null;
	private JLabel filler = null;
	/**
	 * This is the default constructor
	 */
	public LookAndFeelPanel() {
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
		gridBagConstraints2.weighty = 1.0D;
		gridBagConstraints2.gridy = 2;
		filler = new JLabel();
		filler.setText(""); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.gridy = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		ButtonGroup group = new ButtonGroup();
		this.add(getPlatformButton(), gridBagConstraints);
		this.add(getJavaButton(), gridBagConstraints1);
		group.add(getPlatformButton());
		group.add(getJavaButton());
		this.add(filler, gridBagConstraints2);
		boolean java = Preferences.INSTANCE.isJavaLookAndFeel();
		getJavaButton().setSelected(java);
		getPlatformButton().setSelected(!java);
	}

	/**
	 * This method initializes platformButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getPlatformButton() {
		if (platformButton == null) {
			platformButton = new JRadioButton();
			platformButton.setText(LocalizationData.get("PreferencesDialog.LookAndFeel.platform")); //$NON-NLS-1$
			platformButton.setToolTipText(LocalizationData.get("PreferencesDialog.LookAndFeel.platform.toolTip")); //$NON-NLS-1$
		}
		return platformButton;
	}

	/**
	 * This method initializes javaButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJavaButton() {
		if (javaButton == null) {
			javaButton = new JRadioButton();
			javaButton.setText(LocalizationData.get("PreferencesDialog.LookAndFeel.java")); //$NON-NLS-1$
			javaButton.setToolTipText(LocalizationData.get("PreferencesDialog.LookAndFeel.java.toolTip")); //$NON-NLS-1$
		}
		return javaButton;
	}

	public boolean isCustomLookAndFeel() {
		return this.getPlatformButton().isSelected();
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.LookAndFeel.title");
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.LookAndFeel.toolTip");
	}

	@Override
	public boolean updatePreferences() {
		boolean result = isCustomLookAndFeel()==Preferences.INSTANCE.isJavaLookAndFeel();
		if (result) Preferences.INSTANCE.setJavaLookAndFeel(!Preferences.INSTANCE.isJavaLookAndFeel());
		return result;
	}
}
