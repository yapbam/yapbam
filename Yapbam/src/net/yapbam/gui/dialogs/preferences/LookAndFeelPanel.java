package net.yapbam.gui.dialogs.preferences;

import java.awt.GridBagLayout;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Semaphore;

import javax.swing.JLabel;

import org.jfree.chart.labels.ItemLabelPosition;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;

public class LookAndFeelPanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	
	private JRadioButton[] buttons = null;
	private JLabel filler = null;
	/**
	 * This is the default constructor
	 */
	public LookAndFeelPanel() {
		super();
		initialize();
	}
	
	private static class LFAction implements ItemListener {
		String className;

		LFAction(String className) {
			this.className = className;
		}
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange()==ItemEvent.SELECTED) {
				System.out.println(this.className); //TODO
			}
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new GridBagLayout());
		
		String current = UIManager.getLookAndFeel().getName();

		LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
		ButtonGroup group = new ButtonGroup();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		for (int i=0 ; i<lfs.length; i++) {
			String name = lfs[i].getName();
			JRadioButton button = new JRadioButton(name);
			if (name.equals(current)) button.setSelected(true);
			button.addItemListener(new LFAction(lfs[i].getClassName()));
			group.add(button);
			c.gridy = i;
			this.add(button, c);
		}
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.weighty = 1.0D;
		c2.weightx = 1.0D;
		c2.gridy = lfs.length;
		c2.anchor = GridBagConstraints.NORTHWEST;
		filler = new JLabel();
		filler.setBorder(BorderFactory.createTitledBorder("x"));
		filler.setText("xxxxxxxx"); //$NON-NLS-1$
		this.add(filler, c2);
	}

	/**
	 * This method initializes platformButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
//	private JRadioButton getPlatformButton() {
//		if (platformButton == null) {
//			platformButton = new JRadioButton();
//			platformButton.setText(LocalizationData.get("PreferencesDialog.LookAndFeel.platform")); //$NON-NLS-1$
//			platformButton.setToolTipText(LocalizationData.get("PreferencesDialog.LookAndFeel.platform.toolTip")); //$NON-NLS-1$
//		}
//		return platformButton;
//	}

	/**
	 * This method initializes javaButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
//	private JRadioButton getJavaButton() {
//		if (javaButton == null) {
//			javaButton = new JRadioButton();
//			javaButton.setText(LocalizationData.get("PreferencesDialog.LookAndFeel.java")); //$NON-NLS-1$
//			javaButton.setToolTipText(LocalizationData.get("PreferencesDialog.LookAndFeel.java.toolTip")); //$NON-NLS-1$
//		}
//		return javaButton;
//	}

	public boolean isCustomLookAndFeel() {
//TODO		return this.getPlatformButton().isSelected();
		return true;
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
