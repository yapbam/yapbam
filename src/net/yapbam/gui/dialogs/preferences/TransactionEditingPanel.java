package net.yapbam.gui.dialogs.preferences;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class TransactionEditingPanel extends PreferencePanel {

	/**
	 * Create the panel.
	 */
	public TransactionEditingPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JCheckBox chckbxAlertMeIf = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfChangeChecked")); //$NON-NLS-1$
		chckbxAlertMeIf.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfChangeChecked.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxAlertMeIf = new GridBagConstraints();
		gbc_chckbxAlertMeIf.insets = new Insets(10, 0, 5, 0);
		gbc_chckbxAlertMeIf.anchor = GridBagConstraints.WEST;
		gbc_chckbxAlertMeIf.gridx = 0;
		gbc_chckbxAlertMeIf.gridy = 0;
		add(chckbxAlertMeIf, gbc_chckbxAlertMeIf);
		
		JCheckBox chckbxAskMeOnDelete = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfDelete")); //$NON-NLS-1$
		chckbxAskMeOnDelete.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfDelete.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxAskMeOnDelete = new GridBagConstraints();
		gbc_chckbxAskMeOnDelete.anchor = GridBagConstraints.WEST;
		gbc_chckbxAskMeOnDelete.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxAskMeOnDelete.gridx = 0;
		gbc_chckbxAskMeOnDelete.gridy = 1;
		add(chckbxAskMeOnDelete, gbc_chckbxAskMeOnDelete);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.weightx = 1.0;
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.insets = new Insets(5, 50, 10, 50);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		add(separator, gbc_separator);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.weighty = 1.0;
		gbc_panel.weightx = 1.0;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		panel.setLayout(gbl_panel);
		
		JCheckBox chckbxAutoFillStatement = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.autoFillStatementId")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxAutoFillStatement = new GridBagConstraints();
		gbc_chckbxAutoFillStatement.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxAutoFillStatement.anchor = GridBagConstraints.WEST;
		gbc_chckbxAutoFillStatement.gridx = 0;
		gbc_chckbxAutoFillStatement.gridy = 0;
		panel.add(chckbxAutoFillStatement, gbc_chckbxAutoFillStatement);
		chckbxAutoFillStatement.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.autoFillStatementId.tooltip")); //$NON-NLS-1$
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, LocalizationData.get("TransactionEditingPreferencesPanel.format.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_1.weightx = 1.0;
		gbc_panel_1.insets = new Insets(0, 0, 0, 0);
		gbc_panel_1.weighty = 1.0;
		gbc_panel_1.gridheight = 0;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		panel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		panel_1.setLayout(gbl_panel_1);
		
		JRadioButton rdbtnShortStyle = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.format.short")); //$NON-NLS-1$
		rdbtnShortStyle.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.format.short.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_rdbtnShortStyle = new GridBagConstraints();
		gbc_rdbtnShortStyle.gridx = 0;
		gbc_rdbtnShortStyle.gridy = 0;
		panel_1.add(rdbtnShortStyle, gbc_rdbtnShortStyle);
		
		JRadioButton rdbtnLongStyle = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.format.long")); //$NON-NLS-1$
		GridBagConstraints gbc_rdbtnLongStyle = new GridBagConstraints();
		gbc_rdbtnLongStyle.gridx = 0;
		gbc_rdbtnLongStyle.gridy = 1;
		panel_1.add(rdbtnLongStyle, gbc_rdbtnLongStyle);
		rdbtnLongStyle.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.format.long.tooltip")); //$NON-NLS-1$
		ButtonGroup gp = new ButtonGroup();
		gp.add(rdbtnLongStyle); gp.add(rdbtnShortStyle);
		
		JRadioButton rdbtnBasedOnDate = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnDate")); //$NON-NLS-1$
		rdbtnBasedOnDate.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnDate.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_rdbtnBasedOnDate = new GridBagConstraints();
		gbc_rdbtnBasedOnDate.insets = new Insets(0, 20, 0, 0);
		gbc_rdbtnBasedOnDate.anchor = GridBagConstraints.WEST;
		gbc_rdbtnBasedOnDate.gridx = 0;
		gbc_rdbtnBasedOnDate.gridy = 1;
		panel.add(rdbtnBasedOnDate, gbc_rdbtnBasedOnDate);
		
		JRadioButton rdbtnBasedOnValue = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnValueDate")); //$NON-NLS-1$
		rdbtnBasedOnValue.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnValueDate.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_rdbtnBasedOnValue = new GridBagConstraints();
		gbc_rdbtnBasedOnValue.insets = new Insets(0, 20, 0, 0);
		gbc_rdbtnBasedOnValue.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnBasedOnValue.gridx = 0;
		gbc_rdbtnBasedOnValue.gridy = 2;
		panel.add(rdbtnBasedOnValue, gbc_rdbtnBasedOnValue);
		gp = new ButtonGroup();
		gp.add(rdbtnBasedOnValue); gp.add(rdbtnBasedOnValue);
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("TransactionEditingPreferencesPanel.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("TransactionEditingPreferencesPanel.tooltip"); //$NON-NLS-1$;
	}

	@Override
	public boolean updatePreferences() {
		// TODO Auto-generated method stub
		System.out.println ("ALERT: Preferences are not updated");
		return false;
	}
}
