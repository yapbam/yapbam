package net.yapbam.gui.dialogs.preferences;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.util.LookAndFeelUtils;
import net.yapbam.util.NullUtils;

import javax.swing.JSlider;

import java.awt.Insets;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fathzer.soft.ajlib.swing.FontUtils;
import com.fathzer.soft.ajlib.swing.Utils;
import com.fathzer.soft.ajlib.swing.widget.AbstractSelector;

public class ThemePanel extends PreferencePanel {
	private static final float MAX_RATIO = 2.5f;
	private static final float MIN_RATIO = 0.75f;

	private static final long serialVersionUID = 1L;

	private String selectedLookAndFeel;
	private String oldSelectedFont;
	private JSlider fontSlider;
	private JPanel fontPanel;
	private JLabel textSampleLabel;
	private JPanel lafPanel;
	private JLabel fontSliderTitle;
	private FontSelector fontSelector;

	/**
	 * This is the default constructor
	 */
	public ThemePanel() {
		super();
		Font defaultFont = Preferences.INSTANCE.getDefaultFont();
		this.oldSelectedFont = defaultFont==null?null:defaultFont.getName();
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
				refreshFontPanel();
			}
		}
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		GridBagConstraints gbcLafPanel = new GridBagConstraints();
		gbcLafPanel.anchor = GridBagConstraints.WEST;
		gbcLafPanel.insets = new Insets(10, 0, 5, 0);
		gbcLafPanel.gridx = 0;
		gbcLafPanel.gridy = 0;
		add(getLAFPanel(), gbcLafPanel);
		GridBagConstraints gbcFontPanel = new GridBagConstraints();
		gbcFontPanel.weightx = 1.0;
		gbcFontPanel.anchor = GridBagConstraints.NORTHWEST;
		gbcFontPanel.weighty = 1.0;
		gbcFontPanel.insets = new Insets(10, 0, 0, 0);
		gbcFontPanel.gridx = 0;
		gbcFontPanel.gridy = 1;
		add(getFontPanel(), gbcFontPanel);
	}

	private void refreshFontPanel() {
		boolean enabled = FontUtils.isDefaultFontSupportedByLookAndFeel(selectedLookAndFeel);
		getFontSlider().setEnabled(enabled);
		getFontSliderTitle().setEnabled(enabled);
		getFontSelector().setEnabled(enabled);
		getTextSampleLabel().setEnabled(enabled);
		getFontSlider().setToolTipText(LocalizationData.get("PreferencesDialog.Theme.fontSize.tooltip."+getEnableSuffix(enabled))); //$NON-NLS-1$
		getFontSelector().setToolTipText(LocalizationData.get("PreferencesDialog.Theme.fontSelector.tooltip."+getEnableSuffix(enabled))); //$NON-NLS-1$
	}

	private String getEnableSuffix(boolean enabled) {
		return enabled?"enabled":"disabled";
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
	
	private String getSelectedFont() {
		return getFontSelector().isEnabled()?getFontSelector().get():null;
	}

	@Override
	public boolean updatePreferences() {
		boolean lfChanged = !selectedLookAndFeel.equals(Preferences.INSTANCE.getLookAndFeel());
		if (lfChanged) {
			Preferences.INSTANCE.setLookAndFeel(selectedLookAndFeel);
		}
		
		boolean fontChanged = !NullUtils.areEquals(getSelectedFont(), oldSelectedFont);
		if (fontChanged) {
			Preferences.INSTANCE.setDefaultFont(getSelectedFont());
		}
		
		int defaultSize = getDefaultFont().getSize();
		int old = (int) (defaultSize*Preferences.INSTANCE.getFontSizeRatio());
		int current = getFontSlider().getValue();
		boolean fontSizeChanged = old!=current;
		if (fontSizeChanged) {
			Preferences.INSTANCE.setFontSizeRatio((float)current/defaultSize);
		}

		return lfChanged || fontSizeChanged || fontChanged;
	}
	private JSlider getFontSlider() {
		if (fontSlider == null) {
			Font dummy = getDefaultFont();
			final Font defaultFont = dummy;
			final int defaultSize = defaultFont.getSize();
			int min = (int) (defaultSize*MIN_RATIO);
			int max = (int) (MAX_RATIO*defaultSize);
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
			refreshFontPanel();
			fontSlider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (!fontSlider.getValueIsAdjusting()) {
						refreshSampleText();
					}
				}
			});
		}
		return fontSlider;
	}

	private void refreshSampleText() {
		getTextSampleLabel().setFont(getDefaultFont().deriveFont((float)fontSlider.getValue()));
	}

	private Font getDefaultFont() {
		Font result = (getSelectedFont()!=null) ? new Font(getSelectedFont(), Font.PLAIN, 12):Preferences.INSTANCE.getDefaultFont();
		if (result==null) {
			LookAndFeel current = UIManager.getLookAndFeel();
			try {
				UIManager.setLookAndFeel(Utils.getLFClassFromName(selectedLookAndFeel));
				result = new JLabel().getFont();
				UIManager.setLookAndFeel(current);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}
	private JPanel getFontPanel() {
		if (fontPanel == null) {
			fontPanel = new JPanel();
			fontPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("PreferencesDialog.Theme.font"))); //$NON-NLS-1$
			GridBagLayout gblFontPanel = new GridBagLayout();
			fontPanel.setLayout(gblFontPanel);
			GridBagConstraints gbcFontSelector = new GridBagConstraints();
			gbcFontSelector.anchor = GridBagConstraints.WEST;
			gbcFontSelector.insets = new Insets(0, 0, 5, 0);
			gbcFontSelector.gridx = 0;
			gbcFontSelector.gridy = 0;
			fontPanel.add(getFontSelector(), gbcFontSelector);
			GridBagConstraints gbcFontSliderTitle = new GridBagConstraints();
			gbcFontSliderTitle.anchor = GridBagConstraints.WEST;
			gbcFontSliderTitle.insets = new Insets(0, 0, 5, 0);
			gbcFontSliderTitle.gridx = 0;
			gbcFontSliderTitle.gridy = 1;
			fontPanel.add(getFontSliderTitle(), gbcFontSliderTitle);
			GridBagConstraints gbcFontSlider = new GridBagConstraints();
			gbcFontSlider.insets = new Insets(0, 0, 5, 0);
			gbcFontSlider.weighty = 1.0;
			gbcFontSlider.weightx = 1.0;
			gbcFontSlider.fill = GridBagConstraints.HORIZONTAL;
			gbcFontSlider.anchor = GridBagConstraints.NORTHWEST;
			gbcFontSlider.gridx = 0;
			gbcFontSlider.gridy = 2;
			fontPanel.add(getFontSlider(), gbcFontSlider);
			GridBagConstraints gbcTextSampleLabel = new GridBagConstraints();
			gbcTextSampleLabel.anchor = GridBagConstraints.WEST;
			gbcTextSampleLabel.gridx = 0;
			gbcTextSampleLabel.gridy = 3;
			fontPanel.add(getTextSampleLabel(), gbcTextSampleLabel);
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
		if (lafPanel == null) {
			lafPanel = new JPanel();
			lafPanel.setBorder(BorderFactory.createTitledBorder(LocalizationData.get("PreferencesDialog.Theme.theme"))); //$NON-NLS-1$
			String current = UIManager.getLookAndFeel().getClass().getName();

			LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
			lafPanel.setLayout(new GridLayout(lfs.length, 1));

			ButtonGroup group = new ButtonGroup();
			int index = 0;
			for (int i = 0; i < lfs.length; i++) {
				String name = lfs[i].getName();
				if (LookAndFeelUtils.isValid(name)) {
					JRadioButton button = new JRadioButton(name);
					if (lfs[i].getClassName().equals(current)) {
						button.setSelected(true);
						selectedLookAndFeel = lfs[i].getName();
					}
					button.addItemListener(new LFAction(lfs[i].getName()));
					if ("nimbus".equalsIgnoreCase(lfs[i].getName())) { //$NON-NLS-1$
						button.setText(lfs[i].getName()+" "+LocalizationData.get("generic.recommended.with.parenthesis")); //$NON-NLS-1$ //$NON-NLS-2$
					}
					group.add(button);
					lafPanel.add(button, index);
					index++;
				}
			}
		}
		return lafPanel;
	}

	private JLabel getFontSliderTitle() {
		if (fontSliderTitle == null) {
			fontSliderTitle = new JLabel(LocalizationData.get("PreferencesDialog.Theme.fontSize")); //$NON-NLS-1$
		}
		return fontSliderTitle;
	}
	private FontSelector getFontSelector() {
		if (fontSelector == null) {
			fontSelector = new FontSelector();
			Font defaultFont = Preferences.INSTANCE.getDefaultFont();
			if (defaultFont!=null) {
				getFontSelector().set(defaultFont.getFontName());
			}
			fontSelector.addPropertyChangeListener(fontSelector.getPropertyName(), new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					refreshSampleText();
				}
			});
		}
		return fontSelector;
	}
	
	private class FontSelector extends AbstractSelector<String, Void> {
		private static final long serialVersionUID = 1L;

		public FontSelector() {
			super(null);
		}

		@Override
		protected void populateCombo() {
			if (FontUtils.isDefaultFontSupportedByLookAndFeel(selectedLookAndFeel)) {
				for (Font f : FontUtils.getAvailableTextFonts(getLocale())) {
					getCombo().addItem(f.getFontName());
				}
			}
		}
		
		@Override
		protected String getLabel() {
			return LocalizationData.get("PreferencesDialog.Theme.fontName"); //$NON-NLS-1$
		}

		@Override
		protected String createNew() {
			return null;
		}

		@Override
		protected String getPropertyName() {
			return "SelectedFont"; //$NON-NLS-1$
		}

		@Override
		protected boolean isNewButtonVisible() {
			return false;
		}
		
		@Override
		protected Component getCustomizedRenderer (Component renderer, String value, int index, boolean isSelected, boolean cellHasFocus) {
			renderer.setFont(new Font(value, Font.PLAIN, 12));
			renderer.setEnabled(isEnabled());
			return renderer;
		}
	}
}
