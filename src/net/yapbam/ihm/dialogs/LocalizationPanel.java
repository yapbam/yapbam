package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridLayout;
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
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import net.yapbam.ihm.Preferences;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LocalizationPanel extends JPanel {

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
	private JPanel jPanel = null;
	private JButton revertButton = null;
	private JPanel southPanel = null;
	private JLabel mustRestart = null;
	private ItemListener basicItemListener;
	
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
		this.setLayout(new BorderLayout());
		this.setSize(548, 200);
		this.add(getJPanel(), BorderLayout.CENTER);
		this.add(getSouthPanel(), BorderLayout.SOUTH);
		reset();
	}
	
	private void reset() {
		Locale locale = Preferences.INSTANCE.getLocale();
		boolean defaultCountry = Preferences.INSTANCE.isDefaultCountry();
		boolean defaultLanguage = Preferences.INSTANCE.isDefaultLanguage();
		defaultCButton.setSelected(defaultCountry);
		customButton.setSelected(!defaultCountry);
		
		if (defaultLanguage) {
			defaultLButton.setSelected(true);
		} else if (locale.getLanguage().equals(Locale.FRENCH)) {
			frenchButton.setSelected(true);
		} else {
			englishButton.setSelected(true);
		}
		
		int index = Arrays.asList(Locale.getISOCountries()).indexOf(locale.getCountry());
		jList.setSelectedIndex(index);
		jList.ensureIndexIsVisible(index);
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
			countryPanel.setBorder(BorderFactory.createTitledBorder(null, "Pays", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
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
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.gridy = 0;
			languagePanel = new JPanel();
			languagePanel.setLayout(new GridBagLayout());
			languagePanel.setBorder(BorderFactory.createTitledBorder(null, "Langue", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			languagePanel.add(getDefaultLButton(), gridBagConstraints3);
			languagePanel.add(getFrenchButton(), gridBagConstraints4);
			languagePanel.add(getEnglishButton(), gridBagConstraints5);
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
			String tip = MessageFormat.format("En sélectionnant ce choix, le pays retenu sera celui par défaut pour votre système ({0})", defaultDisplayCountry);
			defaultCButton.setToolTipText(tip);
			defaultCButton.setText(MessageFormat.format("Par défaut ({0})",defaultDisplayCountry));
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
			customButton.setText("Personnalisé");
			customButton.setToolTipText("Cliquez sur ce choix, puis sélectionnez le pays dans la liste ci-dessous pour choisir un pays différent de celui de votre système");
			customButton.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						if ((!jListIsAdjusting) && (jList.getSelectedIndex()<0)) {
							int index = Arrays.asList(Locale.getISOCountries()).indexOf(Locale.getDefault().getCountry());
							jList.setSelectedIndex(index);
							jList.ensureIndexIsVisible(index);
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
			for (int i = 0; i < countryCodes.length; i++) {
				countries[i] = new Locale(Preferences.INSTANCE.getLocale().getLanguage(), countryCodes[i]).getDisplayCountry(Preferences.INSTANCE.getLocale());
			}
			jList = new JList(countries);
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
		Locale loc = Preferences.INSTANCE.getLocale();
		boolean change = !(loc.equals(getBuiltLocale()) &&
				(Preferences.INSTANCE.isDefaultCountry()==isDefaultCountry()) &&
				(Preferences.INSTANCE.isDefaultLanguage()==isDefaultLanguage()));
		southPanel.setVisible(change);
	}

	public Object getBuiltLocale() {
		String country = (String) (isDefaultCountry()?Locale.getDefault().getCountry():jList.getSelectedValue());
		String lang = Locale.getDefault().getLanguage();
		if (getFrenchButton().isSelected()) {
			lang = Locale.FRENCH.getLanguage();
		} else if (getEnglishButton().isSelected()) {
			lang = Locale.ENGLISH.getLanguage();
		}
		return new Locale(lang, country);
	}

	public boolean isDefaultCountry() {
		return getDefaultCButton().isSelected();
	}

	public boolean isDefaultLanguage() {
		return getDefaultLButton().isSelected();
	}

	/**
	 * This method initializes defaultLButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDefaultLButton() {
		if (defaultLButton == null) {
			defaultLButton = new JRadioButton();
			defaultLButton.setText("Langue du système (Anglais par défaut)");
			defaultLButton.setToolTipText("Cliquer ici pour utiliser la langue par défaut du système ou, à si elle n'est pas disponible, l'anglais");
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
			frenchButton.setText(Locale.FRENCH.getDisplayLanguage(Preferences.INSTANCE.getLocale()));
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
			englishButton.setText(Locale.ENGLISH.getDisplayLanguage(Preferences.INSTANCE.getLocale()));
			englishButton.addItemListener(basicItemListener);
		}
		return englishButton;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.setRows(1);
			jPanel = new JPanel();
			jPanel.setLayout(gridLayout1);
			jPanel.add(getCountryPanel(), null);
			jPanel.add(getLanguagePanel(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes revertButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRevertButton() {
		if (revertButton == null) {
			revertButton = new JButton();
			revertButton.setText("Annuler");
		}
		return revertButton;
	}

	/**
	 * This method initializes southPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.fill = GridBagConstraints.NONE;
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints7.gridy = 0;
			mustRestart = new JLabel();
			mustRestart.setText("Les modifications seront prises en compte au prochain redémarrage");
			southPanel = new JPanel();
			southPanel.setLayout(new GridBagLayout());
			southPanel.setVisible(false);
			southPanel.add(getRevertButton(), gridBagConstraints8);
			southPanel.add(mustRestart, gridBagConstraints7);
		}
		return southPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
