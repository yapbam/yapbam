package net.yapbam.gui.dialogs.preferences;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.yapbam.gui.PreferencePanel;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class TransactionEditingPanel extends PreferencePanel {

	/**
	 * Create the panel.
	 */
	public TransactionEditingPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		
		JCheckBox chckbxAlertMeIf = new JCheckBox("Alert me if I change a checked transaction");
		chckbxAlertMeIf.setToolTipText("Display a confirm dialog if you change the amount, number or statement id of a checked transaction");
		GridBagConstraints gbc_chckbxAlertMeIf = new GridBagConstraints();
		gbc_chckbxAlertMeIf.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxAlertMeIf.anchor = GridBagConstraints.WEST;
		gbc_chckbxAlertMeIf.gridx = 0;
		gbc_chckbxAlertMeIf.gridy = 0;
		add(chckbxAlertMeIf, gbc_chckbxAlertMeIf);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.weighty = 1.0;
		gbc_panel.weightx = 1.0;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		panel.setLayout(gbl_panel);
		
		JCheckBox chckbxAutoFillStatement = new JCheckBox("Automatically fill the statement id");
		GridBagConstraints gbc_chckbxAutoFillStatement = new GridBagConstraints();
		gbc_chckbxAutoFillStatement.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxAutoFillStatement.anchor = GridBagConstraints.WEST;
		gbc_chckbxAutoFillStatement.gridx = 0;
		gbc_chckbxAutoFillStatement.gridy = 0;
		panel.add(chckbxAutoFillStatement, gbc_chckbxAutoFillStatement);
		chckbxAutoFillStatement.setToolTipText("Check this box to automatically fill the statement id of transactions");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Format", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		
		JRadioButton rdbtnShortStyle = new JRadioButton("Short Style (example : {0})");
		rdbtnShortStyle.setToolTipText("Select this button to have short statement id");
		GridBagConstraints gbc_rdbtnShortStyle = new GridBagConstraints();
		gbc_rdbtnShortStyle.gridx = 0;
		gbc_rdbtnShortStyle.gridy = 0;
		panel_1.add(rdbtnShortStyle, gbc_rdbtnShortStyle);
		
		JRadioButton rdbtnLongStyle = new JRadioButton("Long Style (example : {0})");
		GridBagConstraints gbc_rdbtnLongStyle = new GridBagConstraints();
		gbc_rdbtnLongStyle.gridx = 0;
		gbc_rdbtnLongStyle.gridy = 1;
		panel_1.add(rdbtnLongStyle, gbc_rdbtnLongStyle);
		rdbtnLongStyle.setToolTipText("Select this button to have long statement id");
		ButtonGroup gp = new ButtonGroup();
		gp.add(rdbtnLongStyle); gp.add(rdbtnShortStyle);
		
		JRadioButton rdbtnBasedOnDate = new JRadioButton("Based on date");
		rdbtnBasedOnDate.setToolTipText("Select this button to have statement id based on transaction's date");
		GridBagConstraints gbc_rdbtnBasedOnDate = new GridBagConstraints();
		gbc_rdbtnBasedOnDate.insets = new Insets(0, 20, 0, 0);
		gbc_rdbtnBasedOnDate.anchor = GridBagConstraints.WEST;
		gbc_rdbtnBasedOnDate.gridx = 0;
		gbc_rdbtnBasedOnDate.gridy = 1;
		panel.add(rdbtnBasedOnDate, gbc_rdbtnBasedOnDate);
		
		JRadioButton rdbtnBasedOnValue = new JRadioButton("Based on value date");
		rdbtnBasedOnValue.setToolTipText("Select this button to have statement id based on transaction's value date");
		GridBagConstraints gbc_rdbtnBasedOnValue = new GridBagConstraints();
		gbc_rdbtnBasedOnValue.insets = new Insets(0, 20, 0, 0);
		gbc_rdbtnBasedOnValue.anchor = GridBagConstraints.WEST;
		gbc_rdbtnBasedOnValue.gridx = 0;
		gbc_rdbtnBasedOnValue.gridy = 2;
		panel.add(rdbtnBasedOnValue, gbc_rdbtnBasedOnValue);
		gp = new ButtonGroup();
		gp.add(rdbtnBasedOnValue); gp.add(rdbtnBasedOnValue);
	}

	@Override
	public String getTitle() {
		return "Transaction editing";
	}

	@Override
	public String getToolTip() {
		return null;
	}

	@Override
	public boolean updatePreferences() {
		// TODO Auto-generated method stub
		return false;
	}

}
