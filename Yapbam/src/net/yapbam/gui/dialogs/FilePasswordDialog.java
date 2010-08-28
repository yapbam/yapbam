package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
/** This class is a password ask dialog.
 * It can be customized to change the question, the alert message, etc ... 
 */
public class FilePasswordDialog extends AbstractDialog {
	private GetPasswordPanel panel;

	/** Constructor.
	 * @param owner The frame in which to center the dialog
	 * @param title The dialog's title
	 * @param question The question to ask. Example: "Please type the password below"
	 * @param password The password typed by default in the dialog
	 */
	public FilePasswordDialog(Window owner, String title, String question, String password) {
		super(owner, title, password);
		this.panel.setQuestion(question);
	}

	@Override
	protected Object buildResult() {
		return this.panel.getPassword();
	}

	@Override
	protected JPanel createCenterPane(Object password) {
		panel = new GetPasswordPanel();
		if (password!=null) panel.setPassword((String) password);
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		return null;
	}
	
	/** Changes the warning message.
	 * This dialog is able to display a warning message, something like:
	 * We aware that loosing your passwords leads to loosing your data.
	 * @param message the new message
	 */
	public void setWarningMessage(String message) {
		this.panel.setWarningMessage(message);
		this.pack();
	}

	/** Gets the entered password.
	 * @return a string (an empty string if no password where entered), null if the cancel button was clicked
	 */
	public String getPassword() {
		return (String)this.getResult();
	}
	
	/** Sets the password field tooltip.
	 * This tip is null by default.
	 * @param tooltip The new Tooltip
	 */
	public void setPasswordFieldToolTipText (String tooltip) {
		this.panel.setPasswordFieldToolTipText(tooltip);
	}
}
