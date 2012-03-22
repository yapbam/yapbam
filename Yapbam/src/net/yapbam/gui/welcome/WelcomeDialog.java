package net.yapbam.gui.welcome;

import java.awt.Window;

import javax.swing.JPanel;

import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.Preferences;

@SuppressWarnings("serial")
public class WelcomeDialog extends AbstractDialog<GlobalData, Void> {
	private WelcomePanel welcomePanel;

	public WelcomeDialog(Window owner, GlobalData data) {
		super(owner, "Welcome to Yapbam", data);
//		Image image = Toolkit.getDefaultToolkit().getImage(WelcomePlugin.class.getResource("background.png"));
//		this.setContentPane(JImage.wrapInBackgroundImage((JComponent)this.getContentPane(), image));
		this.okButton.setText(LocalizationData.get("GenericButton.close")); //$NON-NLS-1$
		this.okButton.setToolTipText(LocalizationData.get("GenericButton.close.ToolTip")); //$NON-NLS-1$
		this.cancelButton.setVisible(false);
		this.setResizable(true);
		this.pack();
		this.setMinimumSize(getSize());
	}

	@Override
	protected JPanel createCenterPane() {
		welcomePanel = new WelcomePanel(this.data);
		welcomePanel.setShowAtStartup(Preferences.INSTANCE.isWelcomeAllowed());
		return welcomePanel;
	}

	@Override
	protected Void buildResult() {
		Preferences.INSTANCE.setWelcomeAllowed(welcomePanel.isShowAtStartup());
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
