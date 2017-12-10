package net.yapbam.gui.dialogs;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.fathzer.jlocal.Formatter;
import com.fathzer.soft.ajlib.swing.widget.HTMLPane;

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
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcPanel.insets = new Insets(0, 0, 5, 5);
		gbcPanel.fill = GridBagConstraints.BOTH;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 0;
		add(panel, gbcPanel);
		GridBagLayout gblPanel = new GridBagLayout();
		panel.setLayout(gblPanel);
		
		JLabel icon = new JLabel();
		GridBagConstraints gbcIcon = new GridBagConstraints();
		gbcIcon.anchor = GridBagConstraints.WEST;
		gbcIcon.fill = GridBagConstraints.VERTICAL;
		gbcIcon.insets = new Insets(0, 0, 0, 5);
		gbcIcon.gridx = 0;
		gbcIcon.gridy = 0;
		panel.add(icon, gbcIcon);
		icon.setIcon(getIcon());
		
		Component label;
		if (hasExtendedMessage()) {
			label = new HTMLPane(getMessage());
			label.setPreferredSize(new Dimension(400*getFont().getSize()/12,220*getFont().getSize()/12));
		} else {
			label = new JLabel(getMessage());
		}
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.insets = new Insets(0, 10, 0, 0);
		gbcLabel.anchor = GridBagConstraints.WEST;
		gbcLabel.fill = GridBagConstraints.HORIZONTAL;
		gbcLabel.gridx = 1;
		gbcLabel.gridy = 0;
		panel.add(label, gbcLabel);
		
		chckbxDontAskMe = new JCheckBox(getDontAskMeText());
		chckbxDontAskMe.setHorizontalAlignment(SwingConstants.RIGHT);
		chckbxDontAskMe.setToolTipText(getDontAskMeTooltip());
		GridBagConstraints gbcChckbxDontAskMe = new GridBagConstraints();
		gbcChckbxDontAskMe.insets = new Insets(0, 0, 5, 5);
		gbcChckbxDontAskMe.fill = GridBagConstraints.HORIZONTAL;
		gbcChckbxDontAskMe.anchor = GridBagConstraints.EAST;
		gbcChckbxDontAskMe.gridx = 0;
		gbcChckbxDontAskMe.gridy = 1;
		add(chckbxDontAskMe, gbcChckbxDontAskMe);
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
		if (hasExtendedMessage()) { //$NON-NLS-1$
			String mailMessage = Formatter.format("<br><br>Vous pouvez également nous envoyer un mail à l''adresse <b>bug@yapbam.net</b> en nous indiquant ce que vous tentiez de faire lorsque ce bug est survenu et, si possible, comment le reproduire.<br>Merci de mentionner la référence suivante dans le titre du mail : {0}", new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss-Z").format(new Date()))+"</html>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			message = message.replace("</html>", mailMessage); //$NON-NLS-1$
		}
		return message;
	}

	private boolean hasExtendedMessage() {
		return "fr".equals(LocalizationData.getLocale().getLanguage()); //$NON-NLS-1$
	}

	public boolean isDontAskMeSelected() {
		return chckbxDontAskMe.isSelected();
	}

	public void setDontAskMeVisible(boolean visible) {
		chckbxDontAskMe.setVisible(visible);
	}
}
