package net.yapbam.gui.welcome;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.yapbam.gui.Preferences;
import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.widget.JImage;

@SuppressWarnings("serial")
public class WelcomeDialog extends AbstractDialog {
	private WelcomePanel welcomePanel;

	public WelcomeDialog(Window owner) {
		super(owner, "Welcome to Yapbam", null);
		Image image = Toolkit.getDefaultToolkit().getImage(WelcomePlugin.class.getResource("background.png"));
		this.setContentPane(JImage.wrapInBackgroundImage((JComponent)this.getContentPane(), image));
		this.cancelButton.setVisible(false);
		this.pack();
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		welcomePanel = new WelcomePanel();
		welcomePanel.setShowAtStartup(Preferences.INSTANCE.isWelcomeAllowed());
		return welcomePanel;
	}

	@Override
	protected Object buildResult() {
		Preferences.INSTANCE.setWelcomeAllowed(welcomePanel.isShowAtStartup());
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
