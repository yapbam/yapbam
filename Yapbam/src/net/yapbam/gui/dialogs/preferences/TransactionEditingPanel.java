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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class TransactionEditingPanel extends PreferencePanel {
	private static SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("yyMM", LocalizationData.getLocale());  //$NON-NLS-1$
	private static SimpleDateFormat LONG_FORMAT = new SimpleDateFormat("MMMM yyyy", LocalizationData.getLocale()); //$NON-NLS-1$
	
	private JCheckBox chckbxAutoFillStatement;
	private JRadioButton rdbtnShortStyle;
	private JRadioButton rdbtnLongStyle;
	private JRadioButton rdbtnBasedOnDate;
	private JRadioButton rdbtnBasedOnValue;
	private JCheckBox chckbxAskMeOnDelete;
	private JCheckBox chckbxAlertMeIf;
	private JRadioButton rdbtnCustomized;
	private JTextField formatPatternField;
	private JLabel labelExample;
	private JSeparator separator_1;
	private JPanel panel_1;
	private JLabel lblWhen;
	private JRadioButton rdbtnDupDateCurrent;
	private JRadioButton rdbtnDupDateOriginal;

	/**
	 * Create the panel.
	 */
	public TransactionEditingPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		
		chckbxAlertMeIf = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfChangeChecked")); //$NON-NLS-1$
		chckbxAlertMeIf.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfChangeChecked.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_chckbxAlertMeIf = new GridBagConstraints();
		gbc_chckbxAlertMeIf.insets = new Insets(10, 0, 5, 0);
		gbc_chckbxAlertMeIf.anchor = GridBagConstraints.WEST;
		gbc_chckbxAlertMeIf.gridx = 0;
		gbc_chckbxAlertMeIf.gridy = 0;
		add(chckbxAlertMeIf, gbc_chckbxAlertMeIf);
		
		chckbxAskMeOnDelete = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfDelete")); //$NON-NLS-1$
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
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 3;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		panel_1.setLayout(gbl_panel_1);
		
		lblWhen = new JLabel(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.title")); //$NON-NLS-1$
		GridBagConstraints gbc_lblWhen = new GridBagConstraints();
		gbc_lblWhen.anchor = GridBagConstraints.WEST;
		gbc_lblWhen.gridheight = 0;
		gbc_lblWhen.insets = new Insets(0, 5, 0, 5);
		gbc_lblWhen.weighty = 1.0;
		gbc_lblWhen.fill = GridBagConstraints.VERTICAL;
		gbc_lblWhen.gridx = 0;
		gbc_lblWhen.gridy = 0;
		panel_1.add(lblWhen, gbc_lblWhen);
		
		rdbtnDupDateCurrent = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.setDateToCurrent")); //$NON-NLS-1$
		rdbtnDupDateCurrent.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.setDateToCurrent.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_rdbtnDupDateCurrent = new GridBagConstraints();
		gbc_rdbtnDupDateCurrent.weightx = 1.0;
		gbc_rdbtnDupDateCurrent.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDupDateCurrent.gridx = 1;
		gbc_rdbtnDupDateCurrent.gridy = 0;
		panel_1.add(rdbtnDupDateCurrent, gbc_rdbtnDupDateCurrent);
		
		rdbtnDupDateOriginal = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.keepOriginalDate")); //$NON-NLS-1$
		rdbtnDupDateOriginal.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.keepOriginalDate.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbc_rdbtnDupDateOriginal = new GridBagConstraints();
		gbc_rdbtnDupDateOriginal.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDupDateOriginal.gridx = 1;
		gbc_rdbtnDupDateOriginal.gridy = 1;
		panel_1.add(rdbtnDupDateOriginal, gbc_rdbtnDupDateOriginal);
		
		ButtonGroup gpDuplicate = new ButtonGroup();
		gpDuplicate.add(rdbtnDupDateCurrent); gpDuplicate.add(rdbtnDupDateOriginal);
		
		separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.BOTH;
		gbc_separator_1.insets = new Insets(5, 50, 10, 50);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 4;
		add(separator_1, gbc_separator_1);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.weighty = 1.0;
		gbc_panel.weightx = 1.0;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 6;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		panel.setLayout(gbl_panel);
		
		chckbxAutoFillStatement = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.autoFillStatementId")); //$NON-NLS-1$
		chckbxAutoFillStatement.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
				rdbtnBasedOnDate.setEnabled(selected);
				rdbtnBasedOnValue.setEnabled(selected);
				rdbtnLongStyle.setEnabled(selected);
				rdbtnShortStyle.setEnabled(selected);
				rdbtnCustomized.setEnabled(selected);
				formatPatternField.setEnabled(selected);
				labelExample.setEnabled(selected);
				rdbtnShortStyle.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.format.short.tooltip"):""); //$NON-NLS-1$ //$NON-NLS-2$
				rdbtnLongStyle.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.format.long.tooltip"):""); //$NON-NLS-1$ //$NON-NLS-2$
				rdbtnCustomized.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.format.customized.tooltip"):""); //$NON-NLS-1$ //$NON-NLS-2$
				formatPatternField.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.format.customizedField.tooltip"):""); //$NON-NLS-1$ //$NON-NLS-2$
				rdbtnBasedOnDate.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnDate.tooltip"):""); //$NON-NLS-1$ //$NON-NLS-2$
				rdbtnBasedOnValue.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnValueDate.tooltip"):""); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
		GridBagConstraints gbc_chckbxAutoFillStatement = new GridBagConstraints();
		gbc_chckbxAutoFillStatement.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxAutoFillStatement.anchor = GridBagConstraints.WEST;
		gbc_chckbxAutoFillStatement.gridx = 0;
		gbc_chckbxAutoFillStatement.gridy = 5;
		add(chckbxAutoFillStatement, gbc_chckbxAutoFillStatement);
		chckbxAutoFillStatement.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.autoFillStatementId.tooltip")); //$NON-NLS-1$
		ItemListener itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateOkDisabledCause();
			}
		}; 
		chckbxAutoFillStatement.addItemListener(itemListener);
		
		JPanel panel_format = new JPanel();
		panel_format.setBorder(new TitledBorder(null, LocalizationData.get("TransactionEditingPreferencesPanel.format.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null)); //$NON-NLS-1$
		GridBagConstraints gbc_panel_format = new GridBagConstraints();
		gbc_panel_format.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_format.weightx = 1.0;
		gbc_panel_format.insets = new Insets(0, 10, 0, 0);
		gbc_panel_format.weighty = 1.0;
		gbc_panel_format.gridheight = 0;
		gbc_panel_format.gridx = 1;
		gbc_panel_format.gridy = 0;
		panel.add(panel_format, gbc_panel_format);
		panel_format.setLayout(new GridBagLayout());
		
		Date date = getSampleDate();
		rdbtnShortStyle = new JRadioButton(MessageFormat.format(LocalizationData.get("TransactionEditingPreferencesPanel.format.short"), //$NON-NLS-1$
				new EditingOptions(true, true, true, true, true, SHORT_FORMAT).getStatementId(date)));
		rdbtnShortStyle.setEnabled(false);
		rdbtnShortStyle.addItemListener(itemListener);
		GridBagConstraints gbc_rdbtnShortStyle = new GridBagConstraints();
		gbc_rdbtnShortStyle.gridwidth = 0;
		gbc_rdbtnShortStyle.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnShortStyle.anchor = GridBagConstraints.WEST;
		gbc_rdbtnShortStyle.gridx = 0;
		gbc_rdbtnShortStyle.gridy = 0;
		panel_format.add(rdbtnShortStyle, gbc_rdbtnShortStyle);
		
		rdbtnLongStyle = new JRadioButton(MessageFormat.format(LocalizationData.get("TransactionEditingPreferencesPanel.format.long"), //$NON-NLS-1$
				new EditingOptions(true, true, true, true, true, LONG_FORMAT).getStatementId(date)));
		rdbtnLongStyle.setEnabled(false);
		rdbtnLongStyle.addItemListener(itemListener);
		GridBagConstraints gbc_rdbtnLongStyle = new GridBagConstraints();
		gbc_rdbtnLongStyle.gridwidth = 0;
		gbc_rdbtnLongStyle.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnLongStyle.anchor = GridBagConstraints.WEST;
		gbc_rdbtnLongStyle.gridx = 0;
		gbc_rdbtnLongStyle.gridy = 1;
		panel_format.add(rdbtnLongStyle, gbc_rdbtnLongStyle);
		
		rdbtnCustomized = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.format.customized")); //$NON-NLS-1$
		rdbtnCustomized.setEnabled(false);
		rdbtnCustomized.addItemListener(itemListener);
		GridBagConstraints gbc_rdbtnCustomized = new GridBagConstraints();
		gbc_rdbtnCustomized.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnCustomized.anchor = GridBagConstraints.WEST;
		gbc_rdbtnCustomized.gridx = 0;
		gbc_rdbtnCustomized.gridy = 2;
		panel_format.add(rdbtnCustomized, gbc_rdbtnCustomized);

		rdbtnBasedOnDate = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnDate")); //$NON-NLS-1$
		rdbtnBasedOnDate.setVerticalAlignment(SwingConstants.TOP);
		rdbtnBasedOnDate.setEnabled(false);
		GridBagConstraints gbc_rdbtnBasedOnDate = new GridBagConstraints();
		gbc_rdbtnBasedOnDate.weighty = 1.0;
		gbc_rdbtnBasedOnDate.fill = GridBagConstraints.BOTH;
		gbc_rdbtnBasedOnDate.insets = new Insets(0, 20, 0, 0);
		gbc_rdbtnBasedOnDate.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnBasedOnDate.gridx = 0;
		gbc_rdbtnBasedOnDate.gridy = 1;
		panel.add(rdbtnBasedOnDate, gbc_rdbtnBasedOnDate);
		
		rdbtnBasedOnValue = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnValueDate")); //$NON-NLS-1$
		rdbtnBasedOnValue.setVerticalAlignment(SwingConstants.BOTTOM);
		rdbtnBasedOnValue.setEnabled(false);
		GridBagConstraints gbc_rdbtnBasedOnValue = new GridBagConstraints();
		gbc_rdbtnBasedOnValue.fill = GridBagConstraints.BOTH;
		gbc_rdbtnBasedOnValue.weighty = 1.0;
		gbc_rdbtnBasedOnValue.insets = new Insets(0, 20, 0, 0);
		gbc_rdbtnBasedOnValue.anchor = GridBagConstraints.SOUTHWEST;
		gbc_rdbtnBasedOnValue.gridx = 0;
		gbc_rdbtnBasedOnValue.gridy = 0;
		panel.add(rdbtnBasedOnValue, gbc_rdbtnBasedOnValue);

		ButtonGroup gp = new ButtonGroup();
		gp.add(rdbtnLongStyle); gp.add(rdbtnShortStyle); gp.add(rdbtnCustomized);

		formatPatternField = new JTextField();
		formatPatternField.setEnabled(false);
		GridBagConstraints gbc_formatPatternField = new GridBagConstraints();
		gbc_formatPatternField.insets = new Insets(0, 0, 5, 0);
		gbc_formatPatternField.fill = GridBagConstraints.HORIZONTAL;
		gbc_formatPatternField.gridx = 1;
		gbc_formatPatternField.gridy = 2;
		panel_format.add(formatPatternField, gbc_formatPatternField);
		formatPatternField.setColumns(10);
		formatPatternField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				rdbtnCustomized.setSelected(true);
				updateOkDisabledCause();
			}
		});

		labelExample = new JLabel(
				LocalizationData.get("TransactionEditingPreferencesPanel.format.customized.invalidFormat")); //$NON-NLS-1$
		labelExample.setEnabled(false);
		GridBagConstraints gbc_labelExample = new GridBagConstraints();
		gbc_labelExample.insets = new Insets(0, 5, 0, 5);
		gbc_labelExample.gridx = 2;
		gbc_labelExample.gridy = 2;
		panel_format.add(labelExample, gbc_labelExample);
		ButtonGroup gp2 = new ButtonGroup();
		gp2.add(rdbtnBasedOnDate); gp2.add(rdbtnBasedOnValue);
		
		initFromPreferences();
	}

	private Date getSampleDate() {
		Date date = new GregorianCalendar(new GregorianCalendar().get(GregorianCalendar.YEAR),1,1).getTime();
		return date;
	}
	
	private void updateOkDisabledCause() {
		if (!rdbtnCustomized.isSelected() || !chckbxAutoFillStatement.isSelected()) {
			setOkDisabledCause(null);
		} else {
			SimpleDateFormat format = null;
			try {
				format = new SimpleDateFormat(formatPatternField.getText(), LocalizationData.getLocale());
			} catch (Exception e) {}
			if ((format==null) || (format.toPattern().length()==0)) {
				labelExample.setText(LocalizationData.get("TransactionEditingPreferencesPanel.format.customized.invalidFormat")); //$NON-NLS-1$
				setOkDisabledCause(MessageFormat.format(LocalizationData.get("TransactionEditingPreferencesPanel.okDisabledCauseFormat"), getTitle())); //$NON-NLS-1$
			} else {
				labelExample.setText(MessageFormat.format(
						LocalizationData.get("TransactionEditingPreferencesPanel.format.customized.sample"), format.format(getSampleDate()))); //$NON-NLS-1$
				setOkDisabledCause(null);
			}
		}
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
		SimpleDateFormat format = null;
		if (rdbtnCustomized.isSelected()) {
			format = new SimpleDateFormat(formatPatternField.getText(), LocalizationData.getLocale());
		} else {
			format = rdbtnLongStyle.isSelected()?LONG_FORMAT:SHORT_FORMAT;
		}
		Preferences.INSTANCE.setEditingOptions(new EditingOptions(chckbxAskMeOnDelete.isSelected(), chckbxAlertMeIf.isSelected(),
						rdbtnDupDateCurrent.isSelected(), chckbxAutoFillStatement.isSelected(), rdbtnBasedOnDate.isSelected(), format));
		return false;
	}

	private void initFromPreferences() {
		EditingOptions editOptions = Preferences.INSTANCE.getEditingOptions();
		chckbxAskMeOnDelete.setSelected(editOptions.isAlertOnDelete());
		chckbxAlertMeIf.setSelected(editOptions.isAlertOnModifyChecked());
		chckbxAutoFillStatement.setSelected(editOptions.isAutoFillStatement());
		(editOptions.isDuplicateTransactionDateToCurrent()?rdbtnDupDateCurrent:rdbtnDupDateOriginal).setSelected(true);
		(editOptions.isDateBasedAutoStatement()?rdbtnBasedOnDate:rdbtnBasedOnValue).setSelected(true);
		if (editOptions.getStatementDateFormat().equals(LONG_FORMAT)) {
			rdbtnLongStyle.setSelected(true);
		} else if (editOptions.getStatementDateFormat().equals(SHORT_FORMAT)) {
			rdbtnShortStyle.setSelected(true);
		} else {
			rdbtnCustomized.setSelected(true);
			formatPatternField.setText(editOptions.getStatementDateFormat().toPattern());
			updateOkDisabledCause();
		}
	}
}
