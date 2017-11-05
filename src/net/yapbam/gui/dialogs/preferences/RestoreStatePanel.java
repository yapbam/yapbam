package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.StartStateSettings;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
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
	private JCheckBox chckbxRowsSortKeys;

	public RestoreStatePanel() {
		StartStateSettings startOptions = Preferences.INSTANCE.getStartStateOptions();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		
		chckbxTabsOrder = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.tabsOrder")); //$NON-NLS-1$
		chckbxTabsOrder.setSelected(startOptions.isRememberTabsOrder());
		chckbxTabsOrder.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.tabsOrder.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcChckbxConserverLordreDes = new GridBagConstraints();
		gbcChckbxConserverLordreDes.anchor = GridBagConstraints.WEST;
		gbcChckbxConserverLordreDes.insets = new Insets(5, 5, 5, 5);
		gbcChckbxConserverLordreDes.gridx = 0;
		gbcChckbxConserverLordreDes.gridy = 0;
		add(chckbxTabsOrder, gbcChckbxConserverLordreDes);
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("PreferencesDialog.StartState.tableTitle"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.insets = new Insets(5, 5, 5, 5);
		gbcPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 1;
		add(panel, gbcPanel);
		GridBagLayout gblPanel = new GridBagLayout();
		gblPanel.columnWidths = new int[]{0, 0};
		gblPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gblPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gblPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gblPanel);
		
		chckBxColumnsWitdth = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.columnsWidth")); //$NON-NLS-1$
		GridBagConstraints gbcChckBxColumnsWitdth = new GridBagConstraints();
		gbcChckBxColumnsWitdth.anchor = GridBagConstraints.WEST;
		gbcChckBxColumnsWitdth.insets = new Insets(0, 0, 5, 0);
		gbcChckBxColumnsWitdth.gridx = 0;
		gbcChckBxColumnsWitdth.gridy = 0;
		panel.add(chckBxColumnsWitdth, gbcChckBxColumnsWitdth);
		chckBxColumnsWitdth.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.columnsWidth.tooltip")); //$NON-NLS-1$
		chckBxColumnsWitdth.setSelected(startOptions.isRememberColumnsWidth());
		
		chckbxRowsSortKeys = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.rowOrder")); //$NON-NLS-1$
		chckbxRowsSortKeys.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.rowOrder.tooltip")); //$NON-NLS-1$
		chckbxRowsSortKeys.setSelected(startOptions.isRememberRowsSortKeys());
		GridBagConstraints gbcChckbxRowsSortKeys = new GridBagConstraints();
		gbcChckbxRowsSortKeys.anchor = GridBagConstraints.WEST;
		gbcChckbxRowsSortKeys.insets = new Insets(0, 0, 5, 0);
		gbcChckbxRowsSortKeys.gridx = 0;
		gbcChckbxRowsSortKeys.gridy = 1;
		panel.add(chckbxRowsSortKeys, gbcChckbxRowsSortKeys);
		
		chckBxColumnsOrder = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.columnsOrder")); //$NON-NLS-1$
		GridBagConstraints gbcChckBxColumnsOrder = new GridBagConstraints();
		gbcChckBxColumnsOrder.anchor = GridBagConstraints.WEST;
		gbcChckBxColumnsOrder.insets = new Insets(0, 0, 5, 0);
		gbcChckBxColumnsOrder.gridx = 0;
		gbcChckBxColumnsOrder.gridy = 2;
		panel.add(chckBxColumnsOrder, gbcChckBxColumnsOrder);
		chckBxColumnsOrder.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.columnsOrder.tooltip")); //$NON-NLS-1$
		chckBxColumnsOrder.setSelected(startOptions.isRememberColumnsOrder());
		
		chckbxHiddenColumns = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.hiddenColumns")); //$NON-NLS-1$
		GridBagConstraints gbcChckbxHiddenColumns = new GridBagConstraints();
		gbcChckbxHiddenColumns.anchor = GridBagConstraints.NORTHWEST;
		gbcChckbxHiddenColumns.gridx = 0;
		gbcChckbxHiddenColumns.gridy = 3;
		panel.add(chckbxHiddenColumns, gbcChckbxHiddenColumns);
		chckbxHiddenColumns.setSelected(startOptions.isRememberHiddenColumns());
		chckbxHiddenColumns.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.hiddenColumns.tooltip")); //$NON-NLS-1$
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("PreferencesDialog.StartState.dataTitle"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
		GridBagConstraints gbcPanel1 = new GridBagConstraints();
		gbcPanel1.insets = new Insets(5, 5, 5, 5);
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.gridx = 0;
		gbcPanel1.gridy = 2;
		add(panel1, gbcPanel1);
		GridBagLayout gblPanel1 = new GridBagLayout();
		gblPanel1.columnWidths = new int[]{0, 0};
		gblPanel1.rowHeights = new int[]{0, 0, 0};
		gblPanel1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gblPanel1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel1.setLayout(gblPanel1);
		
		chckbxFile = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.openedFile")); //$NON-NLS-1$
		chckbxFile.setSelected(startOptions.isRememberFile());
		chckbxFile.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.openFile.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcChckbxFile = new GridBagConstraints();
		gbcChckbxFile.anchor = GridBagConstraints.WEST;
		gbcChckbxFile.insets = new Insets(0, 0, 5, 0);
		gbcChckbxFile.gridx = 0;
		gbcChckbxFile.gridy = 0;
		panel1.add(chckbxFile, gbcChckbxFile);
		chckbxFile.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chckbxFilter.setEnabled(e.getStateChange()==ItemEvent.SELECTED);
				if (e.getStateChange()==ItemEvent.DESELECTED) {
					chckbxFilter.setSelected(false);
				}
			}
		});
		
		chckbxFilter = new JCheckBox(LocalizationData.get("PreferencesDialog.StartState.filter")); //$NON-NLS-1$
		GridBagConstraints gbcChckbxFilter = new GridBagConstraints();
		gbcChckbxFilter.anchor = GridBagConstraints.WEST;
		gbcChckbxFilter.insets = new Insets(0, 20, 5, 0);
		gbcChckbxFilter.gridx = 0;
		gbcChckbxFilter.gridy = 1;
		panel1.add(chckbxFilter, gbcChckbxFilter);
		chckbxFilter.setSelected(startOptions.isRememberFilter());
		chckbxFilter.setToolTipText(LocalizationData.get("PreferencesDialog.StartState.filter.tooltip")); //$NON-NLS-1$
		
		JPanel panel2 = new JPanel();
		GridBagConstraints gbcPanel2 = new GridBagConstraints();
		gbcPanel2.fill = GridBagConstraints.BOTH;
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 3;
		add(panel2, gbcPanel2);
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
		Preferences.INSTANCE.setStartStateOptions(new StartStateSettings(chckbxFile.isSelected(), chckbxFilter.isSelected(),
				chckbxTabsOrder.isSelected(), chckBxColumnsWitdth.isSelected(), chckBxColumnsOrder.isSelected(), chckbxHiddenColumns.isSelected(), chckbxRowsSortKeys.isSelected()));
		return false;
	}
}
