package net.yapbam.gui.dialogs.preferences;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.yapbam.gui.EditingOptions;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;

import javax.swing.JSeparator;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class TransactionEditingPanel extends PreferencePanel {
	private JCheckBox chckbxAutoFillStatement;
	private JRadioButton rdbtnShortStyle;
	private JRadioButton rdbtnLongStyle;
	private JRadioButton rdbtnBasedOnDate;
	private JRadioButton rdbtnBasedOnValue;
	private JCheckBox chckbxAskMeOnDelete;
	private JCheckBox chckbxAlertMeIf;

	/**
	 * Create the panel.
	 */
	public TransactionEditingPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		chckbxAlertMeIf = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfChangeChecked"));
		chckbxAlertMeIf.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfChangeChecked.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxAlertMeIf = new GridBagConstraints();
		gbc_chckbxAlertMeIf.insets = new Insets(10, 0, 5, 0);
		gbc_chckbxAlertMeIf.anchor = GridBagConstraints.WEST;
		gbc_chckbxAlertMeIf.gridx = 0;
		gbc_chckbxAlertMeIf.gridy = 0;
		add(chckbxAlertMeIf, gbc_chckbxAlertMeIf);
		
		chckbxAskMeOnDelete = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfDelete"));
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
		gbc_panel.gridy = 4;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		panel.setLayout(gbl_panel);
		
		chckbxAutoFillStatement = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.autoFillStatementId"));
		chckbxAutoFillStatement.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
				rdbtnBasedOnDate.setEnabled(selected);
				rdbtnBasedOnValue.setEnabled(selected);
				rdbtnLongStyle.setEnabled(selected);
				rdbtnShortStyle.setEnabled(selected);
			}
		});
		GridBagConstraints gbc_chckbxAutoFillStatement = new GridBagConstraints();
		gbc_chckbxAutoFillStatement.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxAutoFillStatement.anchor = GridBagConstraints.WEST;
		gbc_chckbxAutoFillStatement.gridx = 0;
		gbc_chckbxAutoFillStatement.gridy = 3;
		add(chckbxAutoFillStatement, gbc_chckbxAutoFillStatement);
		chckbxAutoFillStatement.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.autoFillStatementId.tooltip")); //$NON-NLS-1$
		
		JPanel panel_format = new JPanel();
		panel_format.setBorder(new TitledBorder(null, LocalizationData.get("TransactionEditingPreferencesPanel.format.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_1.weightx = 1.0;
		gbc_panel_1.insets = new Insets(0, 10, 0, 0);
		gbc_panel_1.weighty = 1.0;
		gbc_panel_1.gridheight = 0;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		panel.add(panel_format, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		panel_format.setLayout(gbl_panel_1);
		
		Date date = new Date();
		String shortFormat = new SimpleDateFormat("yyyyMM").format(date); 
		String longFormat = new SimpleDateFormat("yyyy MMMM").format(date);
		rdbtnShortStyle = new JRadioButton(MessageFormat.format(LocalizationData.get("TransactionEditingPreferencesPanel.format.short"), shortFormat));
		rdbtnShortStyle.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.format.short.tooltip")); //$NON-NLS-1$
		rdbtnShortStyle.setEnabled(false);
		GridBagConstraints gbc_rdbtnShortStyle = new GridBagConstraints();
		gbc_rdbtnShortStyle.anchor = GridBagConstraints.WEST;
		gbc_rdbtnShortStyle.gridx = 0;
		gbc_rdbtnShortStyle.gridy = 0;
		panel_format.add(rdbtnShortStyle, gbc_rdbtnShortStyle);
		
		rdbtnLongStyle = new JRadioButton(MessageFormat.format(LocalizationData.get("TransactionEditingPreferencesPanel.format.long"), longFormat));
		rdbtnLongStyle.setEnabled(false);
		GridBagConstraints gbc_rdbtnLongStyle = new GridBagConstraints();
		gbc_rdbtnLongStyle.anchor = GridBagConstraints.WEST;
		gbc_rdbtnLongStyle.gridx = 0;
		gbc_rdbtnLongStyle.gridy = 1;
		panel_format.add(rdbtnLongStyle, gbc_rdbtnLongStyle);
		rdbtnLongStyle.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.format.long.tooltip")); //$NON-NLS-1$
		
		rdbtnBasedOnDate = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnDate"));
		rdbtnBasedOnDate.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnDate.tooltip")); //$NON-NLS-1$
		rdbtnBasedOnDate.setEnabled(false);
		GridBagConstraints gbc_rdbtnBasedOnDate = new GridBagConstraints();
		gbc_rdbtnBasedOnDate.weighty = 1.0;
		gbc_rdbtnBasedOnDate.fill = GridBagConstraints.BOTH;
		gbc_rdbtnBasedOnDate.insets = new Insets(0, 20, 0, 0);
		gbc_rdbtnBasedOnDate.anchor = GridBagConstraints.WEST;
		gbc_rdbtnBasedOnDate.gridx = 0;
		gbc_rdbtnBasedOnDate.gridy = 1;
		panel.add(rdbtnBasedOnDate, gbc_rdbtnBasedOnDate);
		
		rdbtnBasedOnValue = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnValueDate"));
		rdbtnBasedOnValue.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnValueDate.tooltip")); //$NON-NLS-1$
		rdbtnBasedOnValue.setEnabled(false);
		GridBagConstraints gbc_rdbtnBasedOnValue = new GridBagConstraints();
		gbc_rdbtnBasedOnValue.fill = GridBagConstraints.BOTH;
		gbc_rdbtnBasedOnValue.weighty = 1.0;
		gbc_rdbtnBasedOnValue.insets = new Insets(0, 20, 0, 0);
		gbc_rdbtnBasedOnValue.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnBasedOnValue.gridx = 0;
		gbc_rdbtnBasedOnValue.gridy = 0;
		panel.add(rdbtnBasedOnValue, gbc_rdbtnBasedOnValue);

		ButtonGroup gp = new ButtonGroup();
		gp.add(rdbtnLongStyle); gp.add(rdbtnShortStyle);
		ButtonGroup gp2 = new ButtonGroup();
		gp2.add(rdbtnBasedOnDate); gp2.add(rdbtnBasedOnValue);
		
		initFromPreferences();
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
		Preferences.INSTANCE.setEditingOptions(
				new EditingOptions(chckbxAskMeOnDelete.isSelected(), chckbxAlertMeIf.isSelected(),
						chckbxAutoFillStatement.isSelected(), rdbtnBasedOnDate.isSelected(), rdbtnLongStyle.isSelected()));
		return false;
	}

	private void initFromPreferences() {
		EditingOptions editOptions = Preferences.INSTANCE.getEditingOptions();
		chckbxAskMeOnDelete.setSelected(editOptions.isAlertOnDelete());
		chckbxAlertMeIf.setSelected(editOptions.isAlertOnModifyChecked());
		chckbxAutoFillStatement.setSelected(editOptions.isAutoFillStatement());
		(editOptions.isDateBasedAutoStatement()?rdbtnBasedOnDate:rdbtnBasedOnValue).setSelected(true);
		(editOptions.isLongFormatStatement()?rdbtnLongStyle:rdbtnShortStyle).setSelected(true);
	}
}
