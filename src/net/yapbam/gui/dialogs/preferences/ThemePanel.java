package net.yapbam.gui.dialogs.preferences;

import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
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
import javax.swing.JSlider;
import java.awt.Insets;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ThemePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;

	private String selectedLookAndFeel;
	private JSlider fontSlider;
	private JPanel fontPanel;
	private JLabel textSampleLabel;
	private JPanel LAFPanel;

	/**
	 * This is the default constructor
	 */
	public ThemePanel() {
		super();
		initialize();
	}

	private class LFAction implements ItemListener {
		String name;

		LFAction(String className) {
			this.name = className;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				selectedLookAndFeel = this.name;
				/*
				 * try {
				 * // Trying to change the LAF dynamically causes two problems :
				 * // 1) How to change the Mainframe LAF from there ?
				 * // 2) Exceptions are thrown by the dispatch thread after the LAF was changed
				 * 	UIManager.setLookAndFeel(selectedLookAndFeel);
				 * 	Window ownerWindow = AbstractDialog.getOwnerWindow((Component)e.getItem());
				 * 	SwingUtilities.updateComponentTreeUI(null);
				 * 	ownerWindow.pack();
				 * } catch (Throwable ex) {
				 * 	ex.printStackTrace();
				 * }
				 */
				refreshFontSlider();
			}
		}
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		GridBagConstraints gbc_LAFPanel = new GridBagConstraints();
		gbc_LAFPanel.anchor = GridBagConstraints.WEST;
		gbc_LAFPanel.insets = new Insets(10, 0, 0, 5);
		gbc_LAFPanel.gridx = 0;
		gbc_LAFPanel.gridy = 0;
		add(getLAFPanel(), gbc_LAFPanel);
		GridBagConstraints gbc_fontPanel = new GridBagConstraints();
		gbc_fontPanel.weightx = 1.0;
		gbc_fontPanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_fontPanel.weighty = 1.0;
		gbc_fontPanel.insets = new Insets(10, 0, 5, 0);
		gbc_fontPanel.gridx = 0;
		gbc_fontPanel.gridy = 1;
		add(getFontPanel(), gbc_fontPanel);
	}

	public void refreshFontSlider() {
		boolean enabled = Preferences.INSTANCE.isLookAndFeelSupportFontSize(selectedLookAndFeel);
		getFontSlider().setEnabled(enabled);
		getFontSlider().setToolTipText(LocalizationData.get("PreferencesDialog.Theme.fontSize.tooltip."+(enabled?"enabled":"disabled")));
	}

	public String getSelectedLookAndFeel() {
		return selectedLookAndFeel;
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.Theme.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.Theme.toolTip"); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		boolean lfChanged = !selectedLookAndFeel.equals(Preferences.INSTANCE.getLookAndFeel());
		if (lfChanged) Preferences.INSTANCE.setLookAndFeel(selectedLookAndFeel);
		
		int defaultSize = getDefaultFont().getSize();
		int old = (int) (defaultSize*Preferences.INSTANCE.getFontSizeRatio());
		int current = getFontSlider().getValue();
		boolean fontChanged = (old!=current);
		if (fontChanged) Preferences.INSTANCE.setFontSizeRatio((float)current/defaultSize);

		return lfChanged || fontChanged;
	}
	private JSlider getFontSlider() {
		if (fontSlider == null) {
			Font dummy = getDefaultFont();
			final Font defaultFont = dummy;
			final int defaultSize = defaultFont.getSize();
			int min = (int) (defaultSize*.75);
			int max = 2*defaultSize;
			int current = getTextSampleLabel().getFont().getSize();
			if (current<min) {
				current = min;
			} else if (current>max) {
				current = max;
			}
			fontSlider = new JSlider(min, max, current);
			fontSlider.setMajorTickSpacing(5);
			fontSlider.setMinorTickSpacing(1);
			fontSlider.setPaintLabels(true);
			fontSlider.setPaintTicks(true);
			refreshFontSlider();
			fontSlider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (!fontSlider.getValueIsAdjusting()) {
						getTextSampleLabel().setFont(defaultFont.deriveFont((float)fontSlider.getValue()));
					}
				}
			});
		}
		return fontSlider;
	}

	private Font getDefaultFont() {
		//Some L&F not support "defaultFont" -> we use the JLabel default font instead
		Font dummy = Preferences.INSTANCE.getDefaultFont();
		if (dummy==null) {
			dummy = getTextSampleLabel().getFont();
		}
		return dummy;
	}
	private JPanel getFontPanel() {
		if (fontPanel == null) {
			fontPanel = new JPanel();
			fontPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("PreferencesDialog.Theme.fontSize"))); //$NON-NLS-1$
			GridBagLayout gbl_fontPanel = new GridBagLayout();
			fontPanel.setLayout(gbl_fontPanel);
			GridBagConstraints gbc_fontSlider = new GridBagConstraints();
			gbc_fontSlider.insets = new Insets(0, 0, 5, 0);
			gbc_fontSlider.weighty = 1.0;
			gbc_fontSlider.weightx = 1.0;
			gbc_fontSlider.fill = GridBagConstraints.HORIZONTAL;
			gbc_fontSlider.anchor = GridBagConstraints.NORTHWEST;
			gbc_fontSlider.gridx = 0;
			gbc_fontSlider.gridy = 0;
			fontPanel.add(getFontSlider(), gbc_fontSlider);
			GridBagConstraints gbc_textSampleLabel = new GridBagConstraints();
			gbc_textSampleLabel.anchor = GridBagConstraints.WEST;
			gbc_textSampleLabel.gridx = 0;
			gbc_textSampleLabel.gridy = 1;
			fontPanel.add(getTextSampleLabel(), gbc_textSampleLabel);
		}
		return fontPanel;
	}
	private JLabel getTextSampleLabel() {
		if (textSampleLabel == null) {
			textSampleLabel = new JLabel(LocalizationData.get("PreferencesDialog.Theme.textSample")); //$NON-NLS-1$
		}
		return textSampleLabel;
	}
	private JPanel getLAFPanel() {
		if (LAFPanel == null) {
			LAFPanel = new JPanel();
			LAFPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("PreferencesDialog.Theme.theme"))); //$NON-NLS-1$
			String current = UIManager.getLookAndFeel().getClass().getName();

			LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
			LAFPanel.setLayout(new GridLayout(lfs.length, 1));

			ButtonGroup group = new ButtonGroup();
			for (int i = 0; i < lfs.length; i++) {
				String name = lfs[i].getName();
				JRadioButton button = new JRadioButton(name);
				if (lfs[i].getClassName().equals(current)) {
					button.setSelected(true);
					selectedLookAndFeel = lfs[i].getName();
				}
				button.addItemListener(new LFAction(lfs[i].getName()));
				group.add(button);
				LAFPanel.add(button, i);
			}
		}
		return LAFPanel;
	}
}
