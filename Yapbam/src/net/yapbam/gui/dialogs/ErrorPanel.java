package net.yapbam.gui.dialogs;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.yapbam.gui.LocalizationData;

public class ErrorPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ErrorPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{32, 411, 0};
		gbl_panel.rowHeights = new int[]{100, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel label = new JLabel();
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.fill = GridBagConstraints.VERTICAL;
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		label.setIcon(UIManager.getIcon("OptionPane.errorIcon")); //$NON-NLS-1$
		
		JLabel icon = new JLabel(LocalizationData.get("ErrorManager.sendReport.message")); //$NON-NLS-1$
		GridBagConstraints gbc_icon = new GridBagConstraints();
		gbc_icon.insets = new Insets(0, 10, 0, 0);
		gbc_icon.anchor = GridBagConstraints.NORTH;
		gbc_icon.fill = GridBagConstraints.HORIZONTAL;
		gbc_icon.gridx = 1;
		gbc_icon.gridy = 0;
		panel.add(icon, gbc_icon);
		
		JCheckBox chckbxDontAskMe = new JCheckBox(LocalizationData.get("GenericCheckBox.rememberDecision")); //$NON-NLS-1$
		chckbxDontAskMe.setHorizontalAlignment(SwingConstants.RIGHT);
		chckbxDontAskMe.setToolTipText(LocalizationData.get("GenericCheckBox.rememberDecision.tooltip")); //$NON-NLS-1$
		//TODO initialize the value. Set the preference when the button is clicked 
		GridBagConstraints gbc_chckbxDontAskMe = new GridBagConstraints();
		gbc_chckbxDontAskMe.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxDontAskMe.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxDontAskMe.anchor = GridBagConstraints.EAST;
		gbc_chckbxDontAskMe.gridx = 0;
		gbc_chckbxDontAskMe.gridy = 1;
		add(chckbxDontAskMe, gbc_chckbxDontAskMe);
	}
}
