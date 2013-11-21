package net.yapbam.gui.graphics.balancehistory;

import java.text.MessageFormat;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import net.yapbam.gui.Preferences;

import javax.swing.JCheckBox;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

final class TablePreferencePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private static final String SAME_COLORS = "SameColorsAsTransactionsTab"; //$NON-NLS-1$
	private static final String HIGHLIGHT_ALERTS = "HighlightAlerts"; //$NON-NLS-1$
	
	private JCheckBox chckbxUseSameColors;
	private JCheckBox chckbxHilightAlerts;

	public TablePreferencePanel() {
		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{420, 0};
		gridBagLayout.rowHeights = new int[]{23, 23, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_chckbxHilightAlerts = new GridBagConstraints();
		gbc_chckbxHilightAlerts.anchor = GridBagConstraints.WEST;
		gbc_chckbxHilightAlerts.insets = new Insets(5, 0, 5, 0);
		gbc_chckbxHilightAlerts.gridx = 0;
		gbc_chckbxHilightAlerts.gridy = 0;
		add(getChckbxHilightAlerts(), gbc_chckbxHilightAlerts);
		GridBagConstraints gbc_chckbxUseSameColors = new GridBagConstraints();
		gbc_chckbxUseSameColors.anchor = GridBagConstraints.WEST;
		gbc_chckbxUseSameColors.gridx = 0;
		gbc_chckbxUseSameColors.gridy = 1;
		add(getChckbxUseSameColors(), gbc_chckbxUseSameColors);
	}

	public static boolean isSameColors() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(SAME_COLORS));
	}
	
	public static boolean isHighlightAlerts() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(HIGHLIGHT_ALERTS, "true"));
	}

	@Override
	public boolean updatePreferences() {
		boolean isSameColors = getChckbxUseSameColors().isSelected();
		boolean isHighlightAlerts = getChckbxHilightAlerts().isSelected();
		if (isSameColors==isSameColors() && isHighlightAlerts==isHighlightAlerts()) {
			return false;
		}
		Preferences.INSTANCE.setProperty(SAME_COLORS, Boolean.toString(isSameColors));
		Preferences.INSTANCE.setProperty(HIGHLIGHT_ALERTS, Boolean.toString(isHighlightAlerts));
		return true;
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("BalanceHistory.prefs.title"); //$NON-NLS-1$
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("BalanceHistory.title"); //$NON-NLS-1$
	}
	private JCheckBox getChckbxUseSameColors() {
		if (chckbxUseSameColors == null) {
			String tab = LocalizationData.get("MainFrame.Transactions"); //$NON-NLS-1$
			String title = MessageFormat.format(LocalizationData.get("BalanceHistory.prefs.sameBackgroundAsTransactionTable"), tab); //$NON-NLS-1$
			chckbxUseSameColors = new JCheckBox(title);
			String tip = MessageFormat.format(LocalizationData.get("BalanceHistory.prefs.sameBackgroundAsTransactionTable.tooltip"),tab); //$NON-NLS-1$
			chckbxUseSameColors.setToolTipText(tip);
			chckbxUseSameColors.setSelected(isSameColors());
		}
		return chckbxUseSameColors;
	}
	private JCheckBox getChckbxHilightAlerts() {
		if (chckbxHilightAlerts == null) {
			chckbxHilightAlerts = new JCheckBox(LocalizationData.get("BalanceHistory.prefs.highlightAlerts")); //$NON-NLS-1$
			chckbxHilightAlerts.setToolTipText(LocalizationData.get("BalanceHistory.prefs.highlightAlerts.tooltip")); //$NON-NLS-1$
			chckbxHilightAlerts.setSelected(isHighlightAlerts());
		}
		return chckbxHilightAlerts;
	}
}