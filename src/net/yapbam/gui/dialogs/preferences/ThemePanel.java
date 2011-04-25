package net.yapbam.gui.dialogs.preferences;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;

public class ThemePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;

	private String selectedLookAndFeel;

	/**
	 * This is the default constructor
	 */
	public ThemePanel() {
		super();
		initialize();
	}

	private class LFAction implements ItemListener {
		String className;

		LFAction(String className) {
			this.className = className;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				selectedLookAndFeel = this.className;
				/*
				 * try { // Try to change the LAF dynamically. // Two problems :
				 * // 1�) How to change the Mainframe LAF from there ? // 2�)
				 * Exceptions are thrown by the dispatch thread after the LAF
				 * was changed UIManager.setLookAndFeel(selectedLookAndFeel);
				 * Window ownerWindow =
				 * AbstractDialog.getOwnerWindow((Component)e.getItem());
				 * SwingUtilities.updateComponentTreeUI(null);
				 * ownerWindow.pack(); } catch (Throwable ex) {
				 * ex.printStackTrace(); }
				 */
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

		String current = UIManager.getLookAndFeel().getClass().getName();

		LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
		ButtonGroup group = new ButtonGroup();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		for (int i = 0; i < lfs.length; i++) {
			String name = lfs[i].getName();
			JRadioButton button = new JRadioButton(name);
			if (lfs[i].getClassName().equals(current)) {
				button.setSelected(true);
				selectedLookAndFeel = lfs[i].getClassName();
			}
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
		this.add(new JLabel(), c2);
	}

	public String getSelectedLookAndFeel() {
		return selectedLookAndFeel;
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.Theme.title");
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.Theme.toolTip");
	}

	@Override
	public boolean updatePreferences() {
		boolean result = !selectedLookAndFeel.equals(Preferences.INSTANCE.getLookAndFeel());
		if (result) Preferences.INSTANCE.setLookAndFeel(selectedLookAndFeel);
		return result;
	}
}
