package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.PreferencePanel;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JRadioButton;
import java.awt.Insets;

public class ReportErrorPanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ReportErrorPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel label = new JLabel(LocalizationData.get("ErrorManager.preferences.introduction"));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		add(label, gbc_label);
		
		ButtonGroup group = new ButtonGroup();
		
		JRadioButton yes = new JRadioButton(LocalizationData.get("ErrorManager.preferences.sendWithoutAsking"));
		yes.setToolTipText(LocalizationData.get("ErrorManager.preferences.sendWithoutAsking.tooltip"));
		GridBagConstraints gbc_yes = new GridBagConstraints();
		gbc_yes.anchor = GridBagConstraints.WEST;
		gbc_yes.insets = new Insets(0, 0, 5, 0);
		gbc_yes.gridx = 0;
		gbc_yes.gridy = 1;
		add(yes, gbc_yes);
		group.add(yes);
		
		JRadioButton no = new JRadioButton(LocalizationData.get("ErrorManager.preferences.neverSendNorAsking"));
		no.setToolTipText(LocalizationData.get("ErrorManager.preferences.neverSendNorAsking.tooltip"));
		GridBagConstraints gbc_no = new GridBagConstraints();
		gbc_no.anchor = GridBagConstraints.WEST;
		gbc_no.insets = new Insets(0, 0, 5, 0);
		gbc_no.gridx = 0;
		gbc_no.gridy = 2;
		add(no, gbc_no);
		group.add(no);
		
		JRadioButton ask = new JRadioButton(LocalizationData.get("ErrorManager.preferences.alwaysAsk"));
		ask.setToolTipText(LocalizationData.get("ErrorManager.preferences.alwaysAsk.tooltip"));
		GridBagConstraints gbc_ask = new GridBagConstraints();
		gbc_ask.anchor = GridBagConstraints.WEST;
		gbc_ask.gridx = 0;
		gbc_ask.gridy = 3;
		add(ask, gbc_ask);
		group.add(ask);

		ask.setSelected(true); //TODO Use preferences
	}

	@Override
	public String getTitle() {
		return LocalizationData.get("ErrorManager.preferences.title");
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("ErrorManager.preferences.tooltip");
	}

	@Override
	public boolean updatePreferences() {
		// TODO Auto-generated method stub
		return false;
	}
}
