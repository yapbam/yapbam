package net.yapbam.gui.welcome;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.yapbam.gui.dialogs.AbstractDialog;
import net.yapbam.gui.widget.JImage;

@SuppressWarnings("serial")
public class WelcomeDialog extends AbstractDialog {
	public WelcomeDialog(Window owner) {
		super(owner, "Welcome to Yapbam", null);
		Image image = Toolkit.getDefaultToolkit().getImage(WelcomePlugin.class.getResource("background.png"));
		this.setContentPane(JImage.wrapInBackgroundImage((JComponent)this.getContentPane(), image));
		this.cancelButton.setVisible(false);
		this.pack();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		return new WelcomePanel();
	}

	@Override
	protected Object buildResult() {
		return null;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
}
