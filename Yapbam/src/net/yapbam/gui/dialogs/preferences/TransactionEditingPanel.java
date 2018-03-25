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
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.EditingSettings;
import net.yapbam.gui.preferences.EditionWizardSettings;
import net.yapbam.gui.preferences.EditionWizardSettings.Mode;
import net.yapbam.gui.preferences.EditionWizardSettings.Source;

import javax.swing.JSeparator;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.TextWidget;
import com.fathzer.soft.ajlib.utilities.StringUtils;

@SuppressWarnings("serial")
public class TransactionEditingPanel extends PreferencePanel {
	private static final SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("yyMM", LocalizationData.getLocale());  //$NON-NLS-1$
	private static final SimpleDateFormat LONG_FORMAT = new SimpleDateFormat("MMMM yyyy", LocalizationData.getLocale()); //$NON-NLS-1$
	
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
	private JRadioButton rdbtnDupDateCurrent;
	private JRadioButton rdbtnDupDateOriginal;
	private JSeparator separator2;
	private EditionWizardPanel panel2;

	/**
	 * Create the panel.
	 */
	public TransactionEditingPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		chckbxAlertMeIf = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfChangeChecked")); //$NON-NLS-1$
		chckbxAlertMeIf.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfChangeChecked.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcChckbxAlertMeIf = new GridBagConstraints();
		gbcChckbxAlertMeIf.insets = new Insets(10, 0, 5, 0);
		gbcChckbxAlertMeIf.anchor = GridBagConstraints.WEST;
		gbcChckbxAlertMeIf.gridx = 0;
		gbcChckbxAlertMeIf.gridy = 0;
		add(chckbxAlertMeIf, gbcChckbxAlertMeIf);
		
		chckbxAskMeOnDelete = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfDelete")); //$NON-NLS-1$
		chckbxAskMeOnDelete.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.alertIfDelete.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcChckbxAskMeOnDelete = new GridBagConstraints();
		gbcChckbxAskMeOnDelete.anchor = GridBagConstraints.WEST;
		gbcChckbxAskMeOnDelete.insets = new Insets(0, 0, 5, 0);
		gbcChckbxAskMeOnDelete.gridx = 0;
		gbcChckbxAskMeOnDelete.gridy = 1;
		add(chckbxAskMeOnDelete, gbcChckbxAskMeOnDelete);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbcSeparator = new GridBagConstraints();
		gbcSeparator.weightx = 1.0;
		gbcSeparator.fill = GridBagConstraints.BOTH;
		gbcSeparator.insets = new Insets(5, 50, 10, 50);
		gbcSeparator.gridx = 0;
		gbcSeparator.gridy = 2;
		add(separator, gbcSeparator);
		
		JPanel panel1 = new JPanel();
		GridBagConstraints gbcPanel1 = new GridBagConstraints();
		gbcPanel1.insets = new Insets(0, 0, 5, 0);
		gbcPanel1.fill = GridBagConstraints.BOTH;
		gbcPanel1.gridx = 0;
		gbcPanel1.gridy = 3;
		add(panel1, gbcPanel1);
		GridBagLayout gblPanel1 = new GridBagLayout();
		panel1.setLayout(gblPanel1);
		
		JLabel lblWhen = new JLabel(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.title")); //$NON-NLS-1$
		GridBagConstraints gbcLblWhen = new GridBagConstraints();
		gbcLblWhen.anchor = GridBagConstraints.WEST;
		gbcLblWhen.gridheight = 0;
		gbcLblWhen.insets = new Insets(0, 5, 0, 5);
		gbcLblWhen.weighty = 1.0;
		gbcLblWhen.fill = GridBagConstraints.VERTICAL;
		gbcLblWhen.gridx = 0;
		gbcLblWhen.gridy = 0;
		panel1.add(lblWhen, gbcLblWhen);
		
		rdbtnDupDateCurrent = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.setDateToCurrent")); //$NON-NLS-1$
		rdbtnDupDateCurrent.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.setDateToCurrent.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcRdbtnDupDateCurrent = new GridBagConstraints();
		gbcRdbtnDupDateCurrent.weightx = 1.0;
		gbcRdbtnDupDateCurrent.anchor = GridBagConstraints.WEST;
		gbcRdbtnDupDateCurrent.gridx = 1;
		gbcRdbtnDupDateCurrent.gridy = 0;
		panel1.add(rdbtnDupDateCurrent, gbcRdbtnDupDateCurrent);
		
		rdbtnDupDateOriginal = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.keepOriginalDate")); //$NON-NLS-1$
		rdbtnDupDateOriginal.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.duplicateTransaction.keepOriginalDate.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcRdbtnDupDateOriginal = new GridBagConstraints();
		gbcRdbtnDupDateOriginal.anchor = GridBagConstraints.WEST;
		gbcRdbtnDupDateOriginal.gridx = 1;
		gbcRdbtnDupDateOriginal.gridy = 1;
		panel1.add(rdbtnDupDateOriginal, gbcRdbtnDupDateOriginal);
		
		ButtonGroup gpDuplicate = new ButtonGroup();
		gpDuplicate.add(rdbtnDupDateCurrent);
		gpDuplicate.add(rdbtnDupDateOriginal);
		
		JSeparator separator1 = new JSeparator();
		GridBagConstraints gbcSeparator1 = new GridBagConstraints();
		gbcSeparator1.fill = GridBagConstraints.BOTH;
		gbcSeparator1.insets = new Insets(5, 50, 10, 50);
		gbcSeparator1.gridx = 0;
		gbcSeparator1.gridy = 4;
		add(separator1, gbcSeparator1);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.insets = new Insets(0, 0, 5, 0);
		gbcPanel.anchor = GridBagConstraints.NORTHWEST;
		gbcPanel.weightx = 1.0;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 6;
		add(panel, gbcPanel);
		GridBagLayout gblPanel = new GridBagLayout();
		panel.setLayout(gblPanel);
		
		chckbxAutoFillStatement = new JCheckBox(LocalizationData.get("TransactionEditingPreferencesPanel.autoFillStatementId")); //$NON-NLS-1$
		chckbxAutoFillStatement.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean selected = e.getStateChange() == ItemEvent.SELECTED;
				rdbtnBasedOnDate.setEnabled(selected);
				rdbtnBasedOnValue.setEnabled(selected);
				rdbtnLongStyle.setEnabled(selected);
				rdbtnShortStyle.setEnabled(selected);
				rdbtnCustomized.setEnabled(selected);
				formatPatternField.setEnabled(selected);
				labelExample.setEnabled(selected);
				rdbtnShortStyle.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.format.short.tooltip"):StringUtils.EMPTY); //$NON-NLS-1$
				rdbtnLongStyle.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.format.long.tooltip"):StringUtils.EMPTY); //$NON-NLS-1$
				rdbtnCustomized.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.format.customized.tooltip"):StringUtils.EMPTY); //$NON-NLS-1$
				formatPatternField.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.format.customizedField.tooltip"):StringUtils.EMPTY); //$NON-NLS-1$
				rdbtnBasedOnDate.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnDate.tooltip"):StringUtils.EMPTY); //$NON-NLS-1$
				rdbtnBasedOnValue.setToolTipText(selected?LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnValueDate.tooltip"):StringUtils.EMPTY); //$NON-NLS-1$
			}
		});
		GridBagConstraints gbcChckbxAutoFillStatement = new GridBagConstraints();
		gbcChckbxAutoFillStatement.insets = new Insets(0, 0, 5, 0);
		gbcChckbxAutoFillStatement.anchor = GridBagConstraints.WEST;
		gbcChckbxAutoFillStatement.gridx = 0;
		gbcChckbxAutoFillStatement.gridy = 5;
		add(chckbxAutoFillStatement, gbcChckbxAutoFillStatement);
		chckbxAutoFillStatement.setToolTipText(LocalizationData.get("TransactionEditingPreferencesPanel.autoFillStatementId.tooltip")); //$NON-NLS-1$
		ItemListener itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateOkDisabledCause();
			}
		}; 
		chckbxAutoFillStatement.addItemListener(itemListener);
		
		JPanel panelFormat = new JPanel();
		panelFormat.setBorder(BorderFactory.createTitledBorder(null, LocalizationData.get("TransactionEditingPreferencesPanel.format.title"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION)); //$NON-NLS-1$
		GridBagConstraints gbcPanelFormat = new GridBagConstraints();
		gbcPanelFormat.anchor = GridBagConstraints.NORTHWEST;
		gbcPanelFormat.weightx = 1.0;
		gbcPanelFormat.insets = new Insets(0, 10, 0, 0);
		gbcPanelFormat.weighty = 1.0;
		gbcPanelFormat.gridheight = 0;
		gbcPanelFormat.gridx = 1;
		gbcPanelFormat.gridy = 0;
		panel.add(panelFormat, gbcPanelFormat);
		panelFormat.setLayout(new GridBagLayout());
		
		Date date = getSampleDate();
		EditingSettings dummySettings = new EditingSettings(true, true, true, true, true, SHORT_FORMAT, new EditionWizardSettings(Mode.NEVER, Source.LAST));
		rdbtnShortStyle = new JRadioButton(Formatter.format(LocalizationData.get("TransactionEditingPreferencesPanel.format.short"), //$NON-NLS-1$
				dummySettings.getStatementId(date)));
		rdbtnShortStyle.setEnabled(false);
		rdbtnShortStyle.addItemListener(itemListener);
		GridBagConstraints gbcRdbtnShortStyle = new GridBagConstraints();
		gbcRdbtnShortStyle.gridwidth = 0;
		gbcRdbtnShortStyle.insets = new Insets(0, 0, 5, 0);
		gbcRdbtnShortStyle.anchor = GridBagConstraints.WEST;
		gbcRdbtnShortStyle.gridx = 0;
		gbcRdbtnShortStyle.gridy = 0;
		panelFormat.add(rdbtnShortStyle, gbcRdbtnShortStyle);
		
		dummySettings.setStatementDateFormat(LONG_FORMAT);
		rdbtnLongStyle = new JRadioButton(Formatter.format(LocalizationData.get("TransactionEditingPreferencesPanel.format.long"), //$NON-NLS-1$
				dummySettings.getStatementId(date)));
		rdbtnLongStyle.setEnabled(false);
		rdbtnLongStyle.addItemListener(itemListener);
		GridBagConstraints gbcRdbtnLongStyle = new GridBagConstraints();
		gbcRdbtnLongStyle.gridwidth = 0;
		gbcRdbtnLongStyle.insets = new Insets(0, 0, 5, 0);
		gbcRdbtnLongStyle.anchor = GridBagConstraints.WEST;
		gbcRdbtnLongStyle.gridx = 0;
		gbcRdbtnLongStyle.gridy = 1;
		panelFormat.add(rdbtnLongStyle, gbcRdbtnLongStyle);
		
		rdbtnCustomized = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.format.customized")); //$NON-NLS-1$
		rdbtnCustomized.setEnabled(false);
		rdbtnCustomized.addItemListener(itemListener);
		GridBagConstraints gbcRdbtnCustomized = new GridBagConstraints();
		gbcRdbtnCustomized.insets = new Insets(0, 0, 0, 5);
		gbcRdbtnCustomized.anchor = GridBagConstraints.WEST;
		gbcRdbtnCustomized.gridx = 0;
		gbcRdbtnCustomized.gridy = 2;
		panelFormat.add(rdbtnCustomized, gbcRdbtnCustomized);

		rdbtnBasedOnDate = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnDate")); //$NON-NLS-1$
		rdbtnBasedOnDate.setVerticalAlignment(SwingConstants.TOP);
		rdbtnBasedOnDate.setEnabled(false);
		GridBagConstraints gbcRdbtnBasedOnDate = new GridBagConstraints();
		gbcRdbtnBasedOnDate.weighty = 1.0;
		gbcRdbtnBasedOnDate.fill = GridBagConstraints.BOTH;
		gbcRdbtnBasedOnDate.insets = new Insets(0, 20, 0, 0);
		gbcRdbtnBasedOnDate.anchor = GridBagConstraints.NORTHWEST;
		gbcRdbtnBasedOnDate.gridx = 0;
		gbcRdbtnBasedOnDate.gridy = 1;
		panel.add(rdbtnBasedOnDate, gbcRdbtnBasedOnDate);
		
		rdbtnBasedOnValue = new JRadioButton(LocalizationData.get("TransactionEditingPreferencesPanel.statementBasedOnValueDate")); //$NON-NLS-1$
		rdbtnBasedOnValue.setVerticalAlignment(SwingConstants.BOTTOM);
		rdbtnBasedOnValue.setEnabled(false);
		GridBagConstraints gbcRdbtnBasedOnValue = new GridBagConstraints();
		gbcRdbtnBasedOnValue.fill = GridBagConstraints.BOTH;
		gbcRdbtnBasedOnValue.weighty = 1.0;
		gbcRdbtnBasedOnValue.insets = new Insets(0, 20, 0, 0);
		gbcRdbtnBasedOnValue.anchor = GridBagConstraints.SOUTHWEST;
		gbcRdbtnBasedOnValue.gridx = 0;
		gbcRdbtnBasedOnValue.gridy = 0;
		panel.add(rdbtnBasedOnValue, gbcRdbtnBasedOnValue);

		ButtonGroup gp = new ButtonGroup();
		gp.add(rdbtnLongStyle);
		gp.add(rdbtnShortStyle);
		gp.add(rdbtnCustomized);

		formatPatternField = new TextWidget();
		formatPatternField.setEnabled(false);
		GridBagConstraints gbcFormatPatternField = new GridBagConstraints();
		gbcFormatPatternField.insets = new Insets(0, 0, 5, 0);
		gbcFormatPatternField.fill = GridBagConstraints.HORIZONTAL;
		gbcFormatPatternField.gridx = 1;
		gbcFormatPatternField.gridy = 2;
		panelFormat.add(formatPatternField, gbcFormatPatternField);
		formatPatternField.setColumns(10);
		formatPatternField.addPropertyChangeListener(TextWidget.TEXT_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				rdbtnCustomized.setSelected(true);
				updateOkDisabledCause();
			}
		});

		labelExample = new JLabel(
				LocalizationData.get("TransactionEditingPreferencesPanel.format.customized.invalidFormat")); //$NON-NLS-1$
		labelExample.setEnabled(false);
		GridBagConstraints gbcLabelExample = new GridBagConstraints();
		gbcLabelExample.insets = new Insets(0, 5, 0, 5);
		gbcLabelExample.gridx = 2;
		gbcLabelExample.gridy = 2;
		panelFormat.add(labelExample, gbcLabelExample);
		ButtonGroup gp2 = new ButtonGroup();
		gp2.add(rdbtnBasedOnDate);
		gp2.add(rdbtnBasedOnValue);
		
		separator2 = new JSeparator();
		GridBagConstraints gbcSeparator2 = new GridBagConstraints();
		gbcSeparator2.fill = GridBagConstraints.BOTH;
		gbcSeparator2.insets = new Insets(5, 50, 10, 50);
		gbcSeparator2.gridx = 0;
		gbcSeparator2.gridy = 7;
		add(separator2, gbcSeparator2);
		
		panel2 = new EditionWizardPanel();
		GridBagConstraints gbcPanel2 = new GridBagConstraints();
		gbcPanel2.anchor = GridBagConstraints.NORTHWEST;
		gbcPanel2.weighty = 1.0;
		gbcPanel2.gridx = 0;
		gbcPanel2.gridy = 8;
		add(panel2, gbcPanel2);
		
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
				setOkDisabledCause(Formatter.format(LocalizationData.get("TransactionEditingPreferencesPanel.okDisabledCauseFormat"), getTitle())); //$NON-NLS-1$
			} else {
				labelExample.setText(Formatter.format(
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
		format = rdbtnLongStyle.isSelected()?LONG_FORMAT:SHORT_FORMAT;
		if (chckbxAutoFillStatement.isSelected() && rdbtnCustomized.isSelected()) {
			format = new SimpleDateFormat(formatPatternField.getText(), LocalizationData.getLocale());
		}
		Preferences.INSTANCE.setEditingOptions(new EditingSettings(chckbxAskMeOnDelete.isSelected(), chckbxAlertMeIf.isSelected(),
						rdbtnDupDateCurrent.isSelected(), chckbxAutoFillStatement.isSelected(), rdbtnBasedOnDate.isSelected(),
						format, panel2.getSettings()));
		return false;
	}

	private void initFromPreferences() {
		EditingSettings editOptions = Preferences.INSTANCE.getEditionSettings();
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
		panel2.setSettings(editOptions.getEditionWizardSettings());
	}
}
