package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.PreferencePanel;
import net.yapbam.gui.preferences.StartStateOptions;

import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class RestoreStatePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private JCheckBox chckbxFilter;
	private JCheckBox chckbxFile;
	private JCheckBox chckbxHiddenColumns;
	private JCheckBox chckbxTabsOrder;
	private JCheckBox chckBxColumnsOrder;
	private JCheckBox chckBxColumnsWitdth;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;

	public RestoreStatePanel() {
		StartStateOptions startOptions = Preferences.INSTANCE.getStartStateOptions();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		
		chckbxTabsOrder = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.tabsOrder")); //$NON-NLS-1$
		chckbxTabsOrder.setSelected(startOptions.isRememberTabsOrder());
		chckbxTabsOrder.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.tabsOrder.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxConserverLordreDes = new GridBagConstraints();
		gbc_chckbxConserverLordreDes.anchor = GridBagConstraints.WEST;
		gbc_chckbxConserverLordreDes.insets = new Insets(5, 5, 5, 5);
		gbc_chckbxConserverLordreDes.gridx = 0;
		gbc_chckbxConserverLordreDes.gridy = 0;
		add(chckbxTabsOrder, gbc_chckbxConserverLordreDes);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(LocalizationData.get("PreferencesDialog.StartState.tableTitle"))); //$NON-NLS-1$
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(5, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		chckBxColumnsWitdth = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.columnsWidth")); //$NON-NLS-1$
		GridBagConstraints gbc_chckBxColumnsWitdth = new GridBagConstraints();
		gbc_chckBxColumnsWitdth.anchor = GridBagConstraints.WEST;
		gbc_chckBxColumnsWitdth.insets = new Insets(0, 0, 5, 0);
		gbc_chckBxColumnsWitdth.gridx = 0;
		gbc_chckBxColumnsWitdth.gridy = 0;
		panel.add(chckBxColumnsWitdth, gbc_chckBxColumnsWitdth);
		chckBxColumnsWitdth.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.columnsWidth.tooltip")); //$NON-NLS-1$
		chckBxColumnsWitdth.setSelected(startOptions.isRememberColumnsWidth());
		
		chckBxColumnsOrder = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.columnsOrder")); //$NON-NLS-1$
		GridBagConstraints gbc_chckBxColumnsOrder = new GridBagConstraints();
		gbc_chckBxColumnsOrder.anchor = GridBagConstraints.WEST;
		gbc_chckBxColumnsOrder.insets = new Insets(0, 0, 5, 0);
		gbc_chckBxColumnsOrder.gridx = 0;
		gbc_chckBxColumnsOrder.gridy = 1;
		panel.add(chckBxColumnsOrder, gbc_chckBxColumnsOrder);
		chckBxColumnsOrder.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.columnsOrder.tooltip")); //$NON-NLS-1$
		chckBxColumnsOrder.setSelected(startOptions.isRememberColumnsOrder());
		
		chckbxHiddenColumns = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.hiddenColumns")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxHiddenColumns = new GridBagConstraints();
		gbc_chckbxHiddenColumns.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxHiddenColumns.gridx = 0;
		gbc_chckbxHiddenColumns.gridy = 2;
		panel.add(chckbxHiddenColumns, gbc_chckbxHiddenColumns);
		chckbxHiddenColumns.setSelected(startOptions.isRememberHiddenColumns());
		chckbxHiddenColumns.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.hiddenColumns.tooltip")); //$NON-NLS-1$
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, LocalizationData.get("PreferencesDialog.StartState.dataTitle"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(5, 5, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 2;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		chckbxFile = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.openedFile")); //$NON-NLS-1$
		chckbxFile.setSelected(startOptions.isRememberFile());
		chckbxFile.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.openFile.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxFile = new GridBagConstraints();
		gbc_chckbxFile.anchor = GridBagConstraints.WEST;
		gbc_chckbxFile.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxFile.gridx = 0;
		gbc_chckbxFile.gridy = 0;
		panel_1.add(chckbxFile, gbc_chckbxFile);
		chckbxFile.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chckbxFilter.setEnabled(e.getStateChange()==ItemEvent.SELECTED);
				if (e.getStateChange()==ItemEvent.DESELECTED) {
					chckbxFilter.setSelected(false);
				}
			}
		});
		
		chckbxFilter = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.filter")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxFilter = new GridBagConstraints();
		gbc_chckbxFilter.anchor = GridBagConstraints.WEST;
		gbc_chckbxFilter.insets = new Insets(0, 20, 5, 0);
		gbc_chckbxFilter.gridx = 0;
		gbc_chckbxFilter.gridy = 1;
		panel_1.add(chckbxFilter, gbc_chckbxFilter);
		chckbxFilter.setSelected(startOptions.isRememberFilter());
		chckbxFilter.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.filter.tooltip")); //$NON-NLS-1$
		
		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		add(panel_2, gbc_panel_2);
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
				chckbxTabsOrder.isSelected(), chckBxColumnsWitdth.isSelected(), chckBxColumnsOrder.isSelected(), chckbxHiddenColumns.isSelected()));
		return false;
	}
}
