package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.PreferencePanel;
import net.yapbam.gui.preferences.StartStateOptions;

import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSeparator;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class RestoreStatePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private JCheckBox chckbxFilter;
	private JCheckBox chckbxFile;
	private JCheckBox chckbxHiddenColumns;
	private JCheckBox chckbxTabsOrder;

	public RestoreStatePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		chckbxTabsOrder = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.tabOrder"));
		chckbxTabsOrder.setSelected(Preferences.INSTANCE.getStartStateOptions().isRememberTabsOrder());
		chckbxTabsOrder.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.tabOrder.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxConserverLordreDes = new GridBagConstraints();
		gbc_chckbxConserverLordreDes.anchor = GridBagConstraints.WEST;
		gbc_chckbxConserverLordreDes.insets = new Insets(5, 0, 5, 0);
		gbc_chckbxConserverLordreDes.gridx = 0;
		gbc_chckbxConserverLordreDes.gridy = 0;
		add(chckbxTabsOrder, gbc_chckbxConserverLordreDes);
		
		chckbxHiddenColumns = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.hiddenColumns"));
		chckbxHiddenColumns.setSelected(Preferences.INSTANCE.getStartStateOptions().isRememberHiddenColumns());
		chckbxHiddenColumns.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.hiddenColumns.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxConserverLesMasquages = new GridBagConstraints();
		gbc_chckbxConserverLesMasquages.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxConserverLesMasquages.anchor = GridBagConstraints.WEST;
		gbc_chckbxConserverLesMasquages.gridx = 0;
		gbc_chckbxConserverLesMasquages.gridy = 1;
		add(chckbxHiddenColumns, gbc_chckbxConserverLesMasquages);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.weightx = 1.0;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		add(separator, gbc_separator);
		
		chckbxFilter = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.filter"));
		chckbxFilter.setSelected(Preferences.INSTANCE.getStartStateOptions().isRememberFilter());
		chckbxFilter.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.filter.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxConserverLeFiltre = new GridBagConstraints();
		gbc_chckbxConserverLeFiltre.insets = new Insets(0, 10, 5, 0);
		gbc_chckbxConserverLeFiltre.weighty = 1.0;
		gbc_chckbxConserverLeFiltre.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxConserverLeFiltre.gridx = 0;
		gbc_chckbxConserverLeFiltre.gridy = 4;
		add(chckbxFilter, gbc_chckbxConserverLeFiltre);
		
		chckbxFile = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.openedFile"));
		chckbxFile.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chckbxFilter.setEnabled(e.getStateChange()==ItemEvent.SELECTED);
				if (e.getStateChange()==ItemEvent.DESELECTED) {
					chckbxFilter.setSelected(false);
				}
			}
		});
		chckbxFile.setSelected(Preferences.INSTANCE.getStartStateOptions().isRememberFile());
		chckbxFile.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.openFile.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxSeRappelerDu = new GridBagConstraints();
		gbc_chckbxSeRappelerDu.anchor = GridBagConstraints.WEST;
		gbc_chckbxSeRappelerDu.gridx = 0;
		gbc_chckbxSeRappelerDu.gridy = 3;
		add(chckbxFile, gbc_chckbxSeRappelerDu);
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("PreferencesDialog.StartState.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("PreferencesDialog.StartState.tooltip"); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		Preferences.INSTANCE.setStartStateOptions(new StartStateOptions(chckbxFile.isSelected(), chckbxFilter.isSelected(),
				chckbxTabsOrder.isSelected(), chckbxHiddenColumns.isSelected()));
		return false;
	}
}
