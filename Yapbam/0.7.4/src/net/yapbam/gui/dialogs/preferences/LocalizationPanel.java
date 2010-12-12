package net.yapbam.gui.dialogs.preferences;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Insets;

public class LocalizationPanel extends PreferencePanel {

	private static final long serialVersionUID = 1L;
	private JPanel countryPanel = null;
	private JPanel languagePanel = null;
	private JRadioButton defaultCButton = null;
	private JRadioButton customButton = null;
	private JScrollPane jScrollPane = null;
	private JList jList = null;
	
	private boolean jListIsAdjusting = false;
	
	private JRadioButton defaultLButton = null;
	private JRadioButton frenchButton = null;
	private JRadioButton englishButton = null;
	private JButton revertButton = null;
	private ItemListener basicItemListener;
	private HashMap<String,String> displayCountrytoCode;  //  @jve:decl-index=0:
	private JCheckBox translatorButton = null;
	private JRadioButton portugueseButton = null;
	
	/**
	 * This is the default constructor
	 */
	public LocalizationPanel() {
		super();
		basicItemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					checkSomethingChanged();
				}
			}
		};
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(548, 200);
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbc_countryPanel = new GridBagConstraints();
		gbc_countryPanel.gridheight = 2;
		gbc_countryPanel.weighty = 1.0;
		gbc_countryPanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_countryPanel.fill = GridBagConstraints.VERTICAL;
		gbc_countryPanel.insets = new Insets(0, 0, 5, 5);
		gbc_countryPanel.gridx = 0;
		gbc_countryPanel.gridy = 0;
		add(getCountryPanel(), gbc_countryPanel);
		GridBagConstraints gbc_languagePanel = new GridBagConstraints();
		gbc_languagePanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_languagePanel.weightx = 1.0;
		gbc_languagePanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_languagePanel.insets = new Insets(0, 0, 5, 0);
		gbc_languagePanel.gridx = 1;
		gbc_languagePanel.gridy = 0;
		add(getLanguagePanel(), gbc_languagePanel);
		GridBagConstraints gbc_revertButton = new GridBagConstraints();
		gbc_revertButton.gridx = 1;
		gbc_revertButton.gridy = 1;
		add(getRevertButton(), gbc_revertButton);
		reset();
	}
	
	private void reset() {
		Locale locale = Preferences.INSTANCE.getLocale();
		boolean defaultCountry = Preferences.INSTANCE.isDefaultCountry();
		if (defaultCountry) {
			defaultCButton.setSelected(defaultCountry);
		} else {
			jList.setSelectedValue(locale.getDisplayCountry(locale), true);
		}
		
		boolean defaultLanguage = Preferences.INSTANCE.isDefaultLanguage();
		if (defaultLanguage) {
			defaultLButton.setSelected(true);
		} else if (locale.getLanguage().equals(Locale.FRENCH.getLanguage())) {
			frenchButton.setSelected(true);
		} else if (locale.getLanguage().equals(new Locale("pt").getLanguage())) {
			portugueseButton.setSelected(true);
		} else {
			englishButton.setSelected(true);
		}
		
		if (Preferences.INSTANCE.isExpertMode()) {
			getTranslatorButton().setSelected(Preferences.INSTANCE.isTranslatorMode());
		}
	}

	/**
	 * This method initializes countryPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCountryPanel() {
		if (countryPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 0;
			countryPanel = new JPanel();
			countryPanel.setLayout(new GridBagLayout());
			countryPanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("PreferencesDialog.Localization.country"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
			countryPanel.add(getDefaultCButton(), gridBagConstraints);
			countryPanel.add(getCustomButton(), gridBagConstraints1);
			ButtonGroup group = new ButtonGroup();
			group.add(getDefaultCButton());
			group.add(getCustomButton());
			countryPanel.add(getJScrollPane(), gridBagConstraints2);
			group = new ButtonGroup();
			group.add(getDefaultLButton());
			group.add(getEnglishButton());
			group.add(getFrenchButton());
			group.add(getPortugueseButton());
		}
		return countryPanel;
	}

	/**
	 * This method initializes languagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLanguagePanel() {
		if (languagePanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints7.weighty = 1.0D;
			gridBagConstraints7.gridy = 4;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.weighty = 0.0D;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 3;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.gridy = 1;
			languagePanel = new JPanel();
			languagePanel.setLayout(new GridBagLayout());
			languagePanel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("PreferencesDialog.Localization.language"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51))); //$NON-NLS-1$ //$NON-NLS-2$
			languagePanel.add(getDefaultLButton(), gridBagConstraints3);
			languagePanel.add(getFrenchButton(), gridBagConstraints4);
			languagePanel.add(getEnglishButton(), gridBagConstraints5);
			languagePanel.add(getTranslatorButton(), gridBagConstraints6);
			languagePanel.add(getPortugueseButton(), gridBagConstraints7);
		}
		return languagePanel;
	}

	/**
	 * This method initializes defaultCButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDefaultCButton() {
		if (defaultCButton == null) {
			defaultCButton = new JRadioButton();
			String defaultDisplayCountry = Locale.getDefault().getDisplayCountry(Preferences.INSTANCE.getLocale());
			String tip = MessageFormat.format(LocalizationData.get("PreferencesDialog.Localization.defaultCountry.toolTip"), defaultDisplayCountry); //$NON-NLS-1$
			defaultCButton.setToolTipText(tip);
			defaultCButton.setText(MessageFormat.format(LocalizationData.get("PreferencesDialog.Localization.defaultCountry"),defaultDisplayCountry)); //$NON-NLS-1$
			defaultCButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						jList.clearSelection();
						checkSomethingChanged();
					}
				}
			});
		}
		return defaultCButton;
	}

	/**
	 * This method initializes customButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCustomButton() {
		if (customButton == null) {
			customButton = new JRadioButton();
			customButton.setText(LocalizationData.get("PreferencesDialog.Localization.customCountry")); //$NON-NLS-1$
			customButton.setToolTipText(LocalizationData.get("PreferencesDialog.Localization.customCountry.tooTip")); //$NON-NLS-1$
			customButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if ((!jListIsAdjusting) && (jList.getSelectedIndex()<0)) {
							jList.setSelectedValue(Locale.getDefault().getDisplayCountry(Preferences.INSTANCE.getLocale()), true);
						}
						checkSomethingChanged();
					}
				}
			});
		}
		return customButton;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJList() {
		if (jList == null) {
			String[] countryCodes = Locale.getISOCountries();
			String[] countries = new String[countryCodes.length];
			displayCountrytoCode = new HashMap<String,String>();
			for (int i = 0; i < countryCodes.length; i++) {
				countries[i] = new Locale(Preferences.INSTANCE.getLocale().getLanguage(), countryCodes[i]).getDisplayCountry(Preferences.INSTANCE.getLocale());
				displayCountrytoCode.put(countries[i], countryCodes[i]);
			}
			Arrays.sort(countries);
			jList = new JList(countries);
			jList.setToolTipText(LocalizationData.get("PreferencesDialog.Localization.countryList.tooltip")); //$NON-NLS-1$
			jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting() == false) {
				        if (jList.getSelectedIndex() > 0) {
				        	jListIsAdjusting = true;
				            customButton.setSelected(true);
				        	jListIsAdjusting = false;
				        }
				        checkSomethingChanged();
				    }
				}
			});
		}
		return jList;
	}

	void checkSomethingChanged() {
		revertButton.setVisible(isChanged());
	}

	/** Returns true if the panel reflects something different from Preferences.INSTANCE */
	public boolean isChanged() {
		Locale loc = Preferences.INSTANCE.getLocale();
		boolean change = !(loc.equals(getBuiltLocale()) &&
				(Preferences.INSTANCE.isDefaultCountry()==isDefaultCountry()) &&
				(Preferences.INSTANCE.isDefaultLanguage()==isDefaultLanguage()) &&
				(Preferences.INSTANCE.isTranslatorMode()==isTranslatorMode()));
		return change;
	}

	public Locale getBuiltLocale() {
		String country = getDefaultCButton().isSelected()?Locale.getDefault().getCountry():displayCountrytoCode.get((String) jList.getSelectedValue());
		
		String lang = Locale.getDefault().getLanguage();
		if (getFrenchButton().isSelected()) {
			lang = Locale.FRENCH.getLanguage();
		} else if (getEnglishButton().isSelected()) {
			lang = Locale.ENGLISH.getLanguage();
		} else if (getPortugueseButton().isSelected()) {
			lang = "pt";
		}
		return new Locale(lang, country);
	}

	public boolean isDefaultCountry() {
		return getDefaultCButton().isSelected();
	}

	public boolean isDefaultLanguage() {
		return getDefaultLButton().isSelected();
	}
	
	public boolean isTranslatorMode() {
		return getTranslatorButton().isSelected();
	}

	/**
	 * This method initializes defaultLButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDefaultLButton() {
		if (defaultLButton == null) {
			defaultLButton = new JRadioButton();
			defaultLButton.setText(LocalizationData.get("PreferencesDialog.Localization.defaultLanguage")); //$NON-NLS-1$
			defaultLButton.setToolTipText(LocalizationData.get("PreferencesDialog.Localization.defaultLanguage.toolTip")); //$NON-NLS-1$
			defaultLButton.addItemListener(basicItemListener);
		}
		return defaultLButton;
	}

	/**
	 * This method initializes frenchButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getFrenchButton() {
		if (frenchButton == null) {
			frenchButton = new JRadioButton();
			frenchButton.setText(Locale.FRENCH.getDisplayLanguage(Locale.FRENCH));
			frenchButton.addItemListener(basicItemListener);
		}
		return frenchButton;
	}

	/**
	 * This method initializes englishButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getEnglishButton() {
		if (englishButton == null) {
			englishButton = new JRadioButton();
			englishButton.setText(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH));
			englishButton.addItemListener(basicItemListener);
		}
		return englishButton;
	}

	/**
	 * This method initializes revertButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRevertButton() {
		if (revertButton == null) {
			revertButton = new JButton();
			revertButton.setText(LocalizationData.get("PreferencesDialog.Localization.revert")); //$NON-NLS-1$
			revertButton.setToolTipText(LocalizationData.get("PreferencesDialog.Localization.revert.toolTip")); //$NON-NLS-1$
			revertButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					reset();
				}
			});
		}
		return revertButton;
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.Localization.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.Localization.toolTip"); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		boolean needIHMRefresh = false;
		if (isChanged()) {
			needIHMRefresh = !(getBuiltLocale().equals(Preferences.INSTANCE.getLocale()) && (isTranslatorMode()==Preferences.INSTANCE.isTranslatorMode()));
			Preferences.INSTANCE.setLocale(getBuiltLocale(), isDefaultCountry(), isDefaultLanguage());
			Preferences.INSTANCE.setTranslatorMode(isTranslatorMode());
			if (needIHMRefresh) {
				LocalizationData.reset();
			}
		}
		return needIHMRefresh;
	}

	/**
	 * This method initializes translatorButton	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getTranslatorButton() {
		if (translatorButton == null) {
			translatorButton = new JCheckBox();
			if (Preferences.INSTANCE.isExpertMode()) {
				translatorButton.setText(LocalizationData.get("PreferencesDialog.translatorMode")); //$NON-NLS-1$
				translatorButton.setToolTipText(LocalizationData.get("PreferencesDialog.translatorMode.tooltip")); //$NON-NLS-1$
				translatorButton.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {	checkSomethingChanged();}
				});
			} else {
				translatorButton.setVisible(false);
			}
		}
		return translatorButton;
	}

	/**
	 * This method initializes portugueseButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getPortugueseButton() {
		if (portugueseButton == null) {
			portugueseButton = new JRadioButton();
			Locale portuguese = new Locale("pt");
			portugueseButton.setText(portuguese.getDisplayLanguage(portuguese));
			portugueseButton.addItemListener(basicItemListener);
		}
		return portugueseButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
