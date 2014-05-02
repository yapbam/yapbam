package net.yapbam.gui.dialogs.preferences;

import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JRadioButton;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.preferences.EditionWizardSettings;
import net.yapbam.gui.preferences.EditionWizardSettings.Mode;
import net.yapbam.gui.preferences.EditionWizardSettings.Source;

import com.fathzer.soft.ajlib.swing.ButtonGroup;
import com.fathzer.soft.ajlib.utilities.NullUtils;

public class EditionWizardPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JRadioButton alwaysBtn;
	private JLabel lbl;
	private JRadioButton neverBtn;
	private JRadioButton whenEmptyBtn;
	private JLabel lbl2;
	private JRadioButton lastBtn;
	private JRadioButton mostProbableBtn;
	private final ButtonGroup buttonGroup;
	private final ButtonGroup buttonGroup1;
	private JPanel withWhatPanel;

	/**
	 * Create the panel.
	 */
	public EditionWizardPanel() {
		buttonGroup = new ButtonGroup();
		buttonGroup1 = new ButtonGroup();
		initialize();
		buttonGroup.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				withWhatPanel.setVisible(!NullUtils.areEquals(arg, getNeverBtn()));
			}
		});
		neverBtn.setSelected(true);
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcNeverBtn = new GridBagConstraints();
		gbcNeverBtn.anchor = GridBagConstraints.WEST;
		gbcNeverBtn.insets = new Insets(0, 15, 5, 5);
		gbcNeverBtn.gridx = 0;
		gbcNeverBtn.gridy = 1;
		add(getNeverBtn(), gbcNeverBtn);
		GridBagConstraints gbcAlwaysBtn = new GridBagConstraints();
		gbcAlwaysBtn.anchor = GridBagConstraints.WEST;
		gbcAlwaysBtn.insets = new Insets(0, 15, 5, 5);
		gbcAlwaysBtn.gridx = 0;
		gbcAlwaysBtn.gridy = 2;
		add(getAlwaysBtn(), gbcAlwaysBtn);
		GridBagConstraints gbcLbl = new GridBagConstraints();
		gbcLbl.insets = new Insets(5, 5, 5, 5);
		gbcLbl.anchor = GridBagConstraints.WEST;
		gbcLbl.gridx = 0;
		gbcLbl.gridy = 0;
		add(getLbl(), gbcLbl);
		GridBagConstraints gbcWithWhatPanel = new GridBagConstraints();
		gbcWithWhatPanel.fill = GridBagConstraints.BOTH;
		gbcWithWhatPanel.gridheight = 2;
		gbcWithWhatPanel.gridx = 1;
		gbcWithWhatPanel.gridy = 2;
		add(getWithWhatPanel(), gbcWithWhatPanel);
		GridBagConstraints gbcWhenEmptyBtn = new GridBagConstraints();
		gbcWhenEmptyBtn.insets = new Insets(0, 15, 0, 5);
		gbcWhenEmptyBtn.anchor = GridBagConstraints.WEST;
		gbcWhenEmptyBtn.gridx = 0;
		gbcWhenEmptyBtn.gridy = 3;
		add(getWhenEmptyBtn(), gbcWhenEmptyBtn);
	}

	private JRadioButton getAlwaysBtn() {
		if (alwaysBtn == null) {
			alwaysBtn = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.always")); //$NON-NLS-1$
			buttonGroup.add(alwaysBtn);
		}
		return alwaysBtn;
	}
	private JLabel getLbl() {
		if (lbl == null) {
			lbl = new JLabel(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.title")); //$NON-NLS-1$
		}
		return lbl;
	}
	private JRadioButton getNeverBtn() {
		if (neverBtn == null) {
			neverBtn = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.never")); //$NON-NLS-1$
			buttonGroup.add(neverBtn);
		}
		return neverBtn;
	}
	private JRadioButton getWhenEmptyBtn() {
		if (whenEmptyBtn == null) {
			whenEmptyBtn = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.ifNull")); //$NON-NLS-1$
			buttonGroup.add(whenEmptyBtn);
		}
		return whenEmptyBtn;
	}
	private JLabel getLbl2() {
		if (lbl2 == null) {
			lbl2 = new JLabel(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.with")); //$NON-NLS-1$
		}
		return lbl2;
	}
	private JRadioButton getLastBtn() {
		if (lastBtn == null) {
			lastBtn = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.last")); //$NON-NLS-1$
			lastBtn.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.last.tooltip")); //$NON-NLS-1$
			buttonGroup1.add(lastBtn);
		}
		return lastBtn;
	}
	private JRadioButton getMostProbableBtn() {
		if (mostProbableBtn == null) {
			mostProbableBtn = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.mostProbable")); //$NON-NLS-1$
			mostProbableBtn.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.wizard.mostProbable.tooltip")); //$NON-NLS-1$
			mostProbableBtn.setSelected(true);
			buttonGroup1.add(mostProbableBtn);
		}
		return mostProbableBtn;
	}
	private JPanel getWithWhatPanel() {
		if (withWhatPanel == null) {
			withWhatPanel = new JPanel();
			GridBagLayout gblWithWhatPanel = new GridBagLayout();
			withWhatPanel.setLayout(gblWithWhatPanel);
			GridBagConstraints gbcLbl2 = new GridBagConstraints();
			gbcLbl2.anchor = GridBagConstraints.WEST;
			gbcLbl2.gridheight = 2;
			gbcLbl2.insets = new Insets(0, 0, 0, 5);
			gbcLbl2.gridx = 0;
			gbcLbl2.gridy = 0;
			withWhatPanel.add(getLbl2(), gbcLbl2);
			GridBagConstraints gbcLastBtn = new GridBagConstraints();
			gbcLastBtn.anchor = GridBagConstraints.WEST;
			gbcLastBtn.insets = new Insets(0, 0, 5, 0);
			gbcLastBtn.gridx = 1;
			gbcLastBtn.gridy = 0;
			withWhatPanel.add(getLastBtn(), gbcLastBtn);
			GridBagConstraints gbcMostProbableBtn = new GridBagConstraints();
			gbcMostProbableBtn.anchor = GridBagConstraints.WEST;
			gbcMostProbableBtn.gridx = 1;
			gbcMostProbableBtn.gridy = 1;
			withWhatPanel.add(getMostProbableBtn(), gbcMostProbableBtn);
		}
		return withWhatPanel;
	}
	
	public EditionWizardSettings getSettings() {
		Mode mode;
		if (getNeverBtn().isSelected()) {
			mode = Mode.NEVER;
		} else if (getAlwaysBtn().isSelected()) {
			mode = Mode.ALWAYS;
		} else if (getWhenEmptyBtn().isSelected()) {
			mode = Mode.WHEN_NULL;
		} else {
			throw new IllegalStateException();
		}
		Source source = getLastBtn().isSelected()?Source.LAST:Source.MOST_PROBABLE;
		return new EditionWizardSettings(mode, source);
	}
	
	public void setSettings(EditionWizardSettings settings) {
		Mode mode = settings.getMode();
		getNeverBtn().setSelected(Mode.NEVER.equals(mode));
		getAlwaysBtn().setSelected(Mode.ALWAYS.equals(mode));
		getWhenEmptyBtn().setSelected(Mode.WHEN_NULL.equals(mode));
		Source source = settings.getSource();
		getLastBtn().setSelected(Source.LAST.equals(source));
		getMostProbableBtn().setSelected(Source.MOST_PROBABLE.equals(source));
	}
}
