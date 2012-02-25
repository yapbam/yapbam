package net.yapbam.gui.dialogs;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.yapbam.gui.LocalizationData;

public class ErrorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JCheckBox chckbxDontAskMe;

	/**
	 * Create the panel.
	 */
	public ErrorPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
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
		panel.setLayout(gbl_panel);
		
		JLabel icon = new JLabel();
		GridBagConstraints gbc_icon = new GridBagConstraints();
		gbc_icon.anchor = GridBagConstraints.WEST;
		gbc_icon.fill = GridBagConstraints.VERTICAL;
		gbc_icon.insets = new Insets(0, 0, 0, 5);
		gbc_icon.gridx = 0;
		gbc_icon.gridy = 0;
		panel.add(icon, gbc_icon);
		icon.setIcon(getIcon());
		
		JLabel label = new JLabel(getMessage());
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 10, 0, 0);
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.gridx = 1;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		
		chckbxDontAskMe = new JCheckBox(getDontAskMeText());
		chckbxDontAskMe.setHorizontalAlignment(SwingConstants.RIGHT);
		chckbxDontAskMe.setToolTipText(getDontAskMeTooltip());
		GridBagConstraints gbc_chckbxDontAskMe = new GridBagConstraints();
		gbc_chckbxDontAskMe.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxDontAskMe.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxDontAskMe.anchor = GridBagConstraints.EAST;
		gbc_chckbxDontAskMe.gridx = 0;
		gbc_chckbxDontAskMe.gridy = 1;
		add(chckbxDontAskMe, gbc_chckbxDontAskMe);
	}

	protected String getDontAskMeTooltip() {
		return LocalizationData.get("GenericCheckBox.rememberDecision.tooltip"); //$NON-NLS-1$
	}

	protected String getDontAskMeText() {
		return LocalizationData.get("GenericCheckBox.rememberDecision"); //$NON-NLS-1$
	}

	protected Icon getIcon() {
		return UIManager.getIcon("OptionPane.errorIcon"); //$NON-NLS-1$
	}
	
	protected String getMessage() {
		String message = LocalizationData.get("ErrorManager.sendReport.message"); //$NON-NLS-1$
		if (LocalizationData.getLocale().getLanguage().equals("fr")) { //$NON-NLS-1$
			String mailMessage = MessageFormat.format("<br><br>Vous pouvez également nous envoyer un mail à l''adresse <b>bug@yapbam.net</b> en nous indiquant ce que vous tentiez de faire lorsque ce bug est survenu et, si possible, comment le reproduire.<br>Merci de mentionner la référence suivante dans le titre du mail : {0}", new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss-Z").format(new Date()))+"</html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			message = message.replace("</html>", mailMessage); //$NON-NLS-1$
		}
		return message;
	}

	public boolean isDontAskMeSelected() {
		return chckbxDontAskMe.isSelected();
	}

	public void setDontAskMeVisible(boolean visible) {
		chckbxDontAskMe.setVisible(visible);
	}
}
