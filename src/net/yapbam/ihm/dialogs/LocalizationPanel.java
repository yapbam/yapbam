package net.yapbam.ihm.dialogs;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import net.yapbam.ihm.Preferences;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Insets;

public class LocalizationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel countryPanel = null;
	private JPanel languagePanel = null;
	private JRadioButton defaultCButton = null;
	private JRadioButton customButton = null;
	private JScrollPane jScrollPane = null;
	private JList jList = null;
	
	private Locale currentLocale;  //  @jve:decl-index=0:
	private boolean defaultCountry;
	private boolean defaultLanguage;
	
	private Vector<String> countries;
	private JRadioButton defaultLButton = null;
	private JRadioButton frenchButton = null;
	private JRadioButton englishButton = null;
	private JPanel jPanel = null;
	private JButton revertButton = null;
	private JPanel jPanel1 = null;
	private JLabel mustRestart = null;

	/**
	 * This is the default constructor
	 */
	public LocalizationPanel() {
		super();
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
		this.add(getJPanel1(), BorderLayout.SOUTH);
		reset();
	}
	
	private void reset() {
		currentLocale = Preferences.INSTANCE.getLocale();
		defaultCountry = Preferences.INSTANCE.isDefaultCountry();
		defaultLanguage = Preferences.INSTANCE.isDefaultLanguage();
		defaultCButton.setSelected(defaultCountry);
		customButton.setSelected(!defaultCountry);
		
		if (defaultLanguage) {
			defaultLButton.setSelected(true);
		} else if (currentLocale.getLanguage().equals(Locale.FRENCH)) {
			frenchButton.setSelected(true);
		} else {
			englishButton.setSelected(true);
		}
		
		Locale old = Locale.getDefault();
		Locale.setDefault(currentLocale);
		String defaultDisplayCountry = old.getDisplayCountry();
		String tip = MessageFormat.format("En sélectionnant ce choix, le pays retenu sera celui par défaut pour votre système ({0})", defaultDisplayCountry);
		defaultCButton.setToolTipText(tip);
		defaultCButton.setText(MessageFormat.format("Par défaut ({0})",defaultDisplayCountry));
		
		String[] countriesCodes = Locale.getISOCountries();
		for (int i = 0; i < countriesCodes.length; i++) {
			countries.add(new Locale(currentLocale.getLanguage(), countriesCodes[i]).getDisplayCountry());
		}
		int index = Arrays.asList(countriesCodes).indexOf(currentLocale.getCountry());
		jList.setSelectedIndex(index);
		jList.ensureIndexIsVisible(index);
		
		Locale.setDefault(old);
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
			defaultCButton.setName("");
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
			countries = new Vector<String>();
			jList = new JList(countries);
			jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jList;
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
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
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
			jPanel1 = new JPanel();
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getRevertButton(), gridBagConstraints8);
			jPanel1.add(mustRestart, gridBagConstraints7);
		}
		return jPanel1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
