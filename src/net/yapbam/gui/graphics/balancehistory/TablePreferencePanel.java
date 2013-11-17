package net.yapbam.gui.graphics.balancehistory;

import java.text.MessageFormat;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;

import javax.swing.JCheckBox;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

final class TablePreferencePanel extends PreferencePanel {
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
		GridBagConstraints gbc_chckbxHilightLineThat = new GridBagConstraints();
		gbc_chckbxHilightLineThat.anchor = GridBagConstraints.WEST;
		gbc_chckbxHilightLineThat.insets = new Insets(5, 0, 5, 0);
		gbc_chckbxHilightLineThat.gridx = 0;
		gbc_chckbxHilightLineThat.gridy = 0;
		add(getChckbxHilightLineThat(), gbc_chckbxHilightLineThat);
		GridBagConstraints gbc_chckbxUseSameColors = new GridBagConstraints();
		gbc_chckbxUseSameColors.anchor = GridBagConstraints.WEST;
		gbc_chckbxUseSameColors.gridx = 0;
		gbc_chckbxUseSameColors.gridy = 1;
		add(getChckbxUseSameColors(), gbc_chckbxUseSameColors);
	}
	private static final long serialVersionUID = 1L;
	private JCheckBox chckbxUseSameColors;
	private JCheckBox chckbxHilightLineThat;

	@Override
	public boolean updatePreferences() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getToolTip() {
		return "Through this tab you can configure the display of the balance history table";
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("BalanceHistory.title");
	}
	private JCheckBox getChckbxUseSameColors() {
		if (chckbxUseSameColors == null) {
			String tab = LocalizationData.get("MainFrame.Transactions");
			String title = MessageFormat.format("<html>Same background as the \"<b>{0}</b>\" tab</html>", tab);
			chckbxUseSameColors = new JCheckBox(title);
			String tip = MessageFormat.format("<html>Check this box to use same background colors as the \"<b>{0}</b>\" tab</html>",tab);
			chckbxUseSameColors.setToolTipText(tip);
		}
		return chckbxUseSameColors;
	}
	private JCheckBox getChckbxHilightLineThat() {
		if (chckbxHilightLineThat == null) {
			chckbxHilightLineThat = new JCheckBox("Highlight alerts");
			chckbxHilightLineThat.setSelected(true);
			chckbxHilightLineThat.setToolTipText("Check this box to highlight the lines that trigger balance alerts");
		}
		return chckbxHilightLineThat;
	}
}