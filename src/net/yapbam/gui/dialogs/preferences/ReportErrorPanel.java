package net.yapbam.gui.dialogs.preferences;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

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
		
		JLabel label1 = new JLabel();
		label1.setIcon(UIManager.getIcon("OptionPane.warningIcon")); //$NON-NLS-1$
		GridBagConstraints gbcLabel1 = new GridBagConstraints();
		gbcLabel1.insets = new Insets(0, 0, 5, 5);
		gbcLabel1.gridx = 0;
		gbcLabel1.gridy = 0;
		add(label1, gbcLabel1);
		
		JLabel label = new JLabel(LocalizationData.get("ErrorManager.preferences.introduction")); //$NON-NLS-1$
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.fill = GridBagConstraints.BOTH;
		gbcLabel.insets = new Insets(0, 0, 5, 5);
		gbcLabel.gridx = 1;
		gbcLabel.gridy = 0;
		add(label, gbcLabel);
		
		ButtonGroup group = new ButtonGroup();
		
		yes = new JRadioButton(LocalizationData.get("ErrorManager.preferences.sendWithoutAsking")); //$NON-NLS-1$
		yes.setToolTipText(LocalizationData.get("ErrorManager.preferences.sendWithoutAsking.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcYes = new GridBagConstraints();
		gbcYes.weightx = 1.0;
		gbcYes.fill = GridBagConstraints.HORIZONTAL;
		gbcYes.gridwidth = 2;
		gbcYes.anchor = GridBagConstraints.WEST;
		gbcYes.insets = new Insets(5, 5, 5, 5);
		gbcYes.gridx = 0;
		gbcYes.gridy = 1;
		add(yes, gbcYes);
		group.add(yes);
		
		no = new JRadioButton(LocalizationData.get("ErrorManager.preferences.neverSendNorAsking")); //$NON-NLS-1$
		no.setToolTipText(LocalizationData.get("ErrorManager.preferences.neverSendNorAsking.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcNo = new GridBagConstraints();
		gbcNo.fill = GridBagConstraints.HORIZONTAL;
		gbcNo.gridwidth = 2;
		gbcNo.anchor = GridBagConstraints.WEST;
		gbcNo.insets = new Insets(0, 5, 5, 5);
		gbcNo.gridx = 0;
		gbcNo.gridy = 2;
		add(no, gbcNo);
		group.add(no);
		
		JRadioButton ask = new JRadioButton(LocalizationData.get("ErrorManager.preferences.alwaysAsk")); //$NON-NLS-1$
		ask.setVerticalAlignment(SwingConstants.TOP);
		ask.setToolTipText(LocalizationData.get("ErrorManager.preferences.alwaysAsk.tooltip")); //$NON-NLS-1$
		GridBagConstraints gbcAsk = new GridBagConstraints();
		gbcAsk.weighty = 1.0;
		gbcAsk.fill = GridBagConstraints.BOTH;
		gbcAsk.insets = new Insets(0, 5, 0, 5);
		gbcAsk.gridwidth = 2;
		gbcAsk.anchor = GridBagConstraints.NORTHWEST;
		gbcAsk.gridx = 0;
		gbcAsk.gridy = 3;
		add(ask, gbcAsk);
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
		return LocalizationData.get("ErrorManager.preferences.title"); //$NON-NLS-1$
	}

	@Override
	public String getToolTip() {
		return LocalizationData.get("ErrorManager.preferences.tooltip"); //$NON-NLS-1$
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
