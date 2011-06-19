package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;
import net.yapbam.gui.preferences.PreferencePanel;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import javax.swing.JRadioButton;
import java.awt.Insets;
import javax.swing.SwingConstants;

public class ReportErrorPanel extends PreferencePanel {
	private static final long serialVersionUID = 1L;
	private JRadioButton yes;
	private JRadioButton no;

	/**
	 * Create the panel.
	 */
	public ReportErrorPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JLabel label_1 = new JLabel();
		label_1.setIcon(UIManager.getIcon("OptionPane.warningIcon")); //$NON-NLS-1$
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 0;
		add(label_1, gbc_label_1);
		
		JLabel label = new JLabel(LocalizationData.get("ErrorManager.preferences.introduction"));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.BOTH;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 0;
		add(label, gbc_label);
		
		ButtonGroup group = new ButtonGroup();
		
		yes = new JRadioButton(LocalizationData.get("ErrorManager.preferences.sendWithoutAsking"));
		yes.setToolTipText(LocalizationData.get("ErrorManager.preferences.sendWithoutAsking.tooltip"));
		GridBagConstraints gbc_yes = new GridBagConstraints();
		gbc_yes.weightx = 1.0;
		gbc_yes.fill = GridBagConstraints.HORIZONTAL;
		gbc_yes.gridwidth = 2;
		gbc_yes.anchor = GridBagConstraints.WEST;
		gbc_yes.insets = new Insets(5, 5, 5, 5);
		gbc_yes.gridx = 0;
		gbc_yes.gridy = 1;
		add(yes, gbc_yes);
		group.add(yes);
		
		no = new JRadioButton(LocalizationData.get("ErrorManager.preferences.neverSendNorAsking"));
		no.setToolTipText(LocalizationData.get("ErrorManager.preferences.neverSendNorAsking.tooltip"));
		GridBagConstraints gbc_no = new GridBagConstraints();
		gbc_no.fill = GridBagConstraints.HORIZONTAL;
		gbc_no.gridwidth = 2;
		gbc_no.anchor = GridBagConstraints.WEST;
		gbc_no.insets = new Insets(0, 5, 5, 5);
		gbc_no.gridx = 0;
		gbc_no.gridy = 2;
		add(no, gbc_no);
		group.add(no);
		
		JRadioButton ask = new JRadioButton(LocalizationData.get("ErrorManager.preferences.alwaysAsk"));
		ask.setVerticalAlignment(SwingConstants.TOP);
		ask.setToolTipText(LocalizationData.get("ErrorManager.preferences.alwaysAsk.tooltip"));
		GridBagConstraints gbc_ask = new GridBagConstraints();
		gbc_ask.weighty = 1.0;
		gbc_ask.fill = GridBagConstraints.BOTH;
		gbc_ask.insets = new Insets(0, 5, 0, 5);
		gbc_ask.gridwidth = 2;
		gbc_ask.anchor = GridBagConstraints.NORTHWEST;
		gbc_ask.gridx = 0;
		gbc_ask.gridy = 3;
		add(ask, gbc_ask);
		group.add(ask);
		
		int whatToDo = Preferences.INSTANCE.getCrashReportAction();
		if (whatToDo==1) {
			yes.setSelected(true);
		} else if (whatToDo==-1) {
			no.setSelected(true);
		} else {
			ask.setSelected(true);
		}
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
		int action = 0;
		if (yes.isSelected()) {
			action = 1;
		} else if (no.isSelected()) {
			action = -1;
		}
		Preferences.INSTANCE.setCrashReportAction(action);
		return false;
	}
}
