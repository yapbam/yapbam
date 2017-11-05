package net.yapbam.gui.graphics.balancehistory;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.dialogs.preferences.PreferencePanel;

import javax.swing.JCheckBox;

import com.fathzer.jlocal.Formatter;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

final class TablePreferencePanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private static final String SAME_COLORS = "SameColorsAsTransactionsTab"; //$NON-NLS-1$
	private static final String HIGHLIGHT_ALERTS = "HighlightAlerts"; //$NON-NLS-1$
	private static final String SEPARATE_EXPENSE_RECEIPT = "SeparateExpenseReceipt"; //$NON-NLS-1$
	
	private JCheckBox chckbxUseSameColors;
	private JCheckBox chckbxHilightAlerts;
	private JCheckBox chckbxSeparateExpenseReceipt;

	public TablePreferencePanel() {
		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		GridBagConstraints gbcChckbxHilightAlerts = new GridBagConstraints();
		gbcChckbxHilightAlerts.anchor = GridBagConstraints.WEST;
		gbcChckbxHilightAlerts.insets = new Insets(5, 5, 5, 0);
		gbcChckbxHilightAlerts.gridx = 0;
		gbcChckbxHilightAlerts.gridy = 0;
		add(getChckbxHilightAlerts(), gbcChckbxHilightAlerts);
		GridBagConstraints gbcChckbxSeparateExpenseReceipt = new GridBagConstraints();
		gbcChckbxSeparateExpenseReceipt.anchor = GridBagConstraints.WEST;
		gbcChckbxSeparateExpenseReceipt.insets = new Insets(0, 5, 5, 0);
		gbcChckbxSeparateExpenseReceipt.gridx = 0;
		gbcChckbxSeparateExpenseReceipt.gridy = 1;
		add(getChckbxSeparateExpenseReceipt(), gbcChckbxSeparateExpenseReceipt);
		GridBagConstraints gbcChckbxUseSameColors = new GridBagConstraints();
		gbcChckbxUseSameColors.insets = new Insets(0, 5, 0, 0);
		gbcChckbxUseSameColors.weightx = 1.0;
		gbcChckbxUseSameColors.weighty = 1.0;
		gbcChckbxUseSameColors.anchor = GridBagConstraints.NORTHWEST;
		gbcChckbxUseSameColors.gridx = 0;
		gbcChckbxUseSameColors.gridy = 2;
		add(getChckbxUseSameColors(), gbcChckbxUseSameColors);
	}

	public static boolean isSameColors() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(SAME_COLORS));
	}
	
	public static boolean isHighlightAlerts() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(HIGHLIGHT_ALERTS, "true")); //$NON-NLS-1$
	}
	
	public static boolean isReceiptSeparatedFromExpense() {
		return Boolean.parseBoolean(Preferences.INSTANCE.getProperty(SEPARATE_EXPENSE_RECEIPT, "false")); //$NON-NLS-1$
	}

	@Override
	public boolean updatePreferences() {
		boolean isSameColors = getChckbxUseSameColors().isSelected();
		boolean isHighlightAlerts = getChckbxHilightAlerts().isSelected();
		boolean separateExpenseReceipt = getChckbxSeparateExpenseReceipt().isSelected();
		if (isSameColors==isSameColors() && isHighlightAlerts==isHighlightAlerts() && separateExpenseReceipt==isReceiptSeparatedFromExpense()) {
			return false;
		}
		Preferences.INSTANCE.setProperty(SAME_COLORS, Boolean.toString(isSameColors));
		Preferences.INSTANCE.setProperty(HIGHLIGHT_ALERTS, Boolean.toString(isHighlightAlerts));
		Preferences.INSTANCE.setProperty(SEPARATE_EXPENSE_RECEIPT, Boolean.toString(separateExpenseReceipt));
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
			String title = Formatter.format(LocalizationData.get("BalanceHistory.prefs.sameBackgroundAsTransactionTable"), tab); //$NON-NLS-1$
			chckbxUseSameColors = new JCheckBox(title);
			String tip = Formatter.format(LocalizationData.get("BalanceHistory.prefs.sameBackgroundAsTransactionTable.tooltip"),tab); //$NON-NLS-1$
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
	private JCheckBox getChckbxSeparateExpenseReceipt() {
		if (chckbxSeparateExpenseReceipt == null) {
			chckbxSeparateExpenseReceipt = new JCheckBox(LocalizationData.get("MainFrame.Transactions.Preferences.separateExpenseReceipt")); //$NON-NLS-1$
			chckbxSeparateExpenseReceipt.setToolTipText(LocalizationData.get("MainFrame.Transactions.Preferences.separateExpenseReceipt.tooltip")); //$NON-NLS-1$
			chckbxSeparateExpenseReceipt.setSelected(isReceiptSeparatedFromExpense());
		}
		return chckbxSeparateExpenseReceipt;
	}
	
}