package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
public class FilePasswordDialog extends AbstractDialog {
	private FilePasswordPanel panel;

	public FilePasswordDialog(Window owner, String password) {
		super(owner, "File password", password);
	}

	@Override
	protected Object buildResult() {
		return this.panel.getPassword();
	}

	@Override
	protected JPanel createCenterPane(Object password) {
		panel = new FilePasswordPanel();
		if (password!=null) panel.setPassword((String) password);
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
	
	/** Gets the entered password.
	 * @return a string (an empty string if no password where entered)
	 */
	public String getPassword() {
		return (String)this.getResult();
	}
}
