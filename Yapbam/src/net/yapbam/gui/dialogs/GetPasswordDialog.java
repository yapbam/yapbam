package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JPanel;

import java.lang.Object;
import java.lang.String;

@SuppressWarnings("serial")
/** This class is a password ask dialog.
 * It can be customized to change the question, the alert message, etc ... 
 */
public class GetPasswordDialog extends AbstractDialog {
	private GetPasswordPanel panel;
	private boolean confirmIsRequired;
	
	private static class InitData {
		private String question;
		private Icon icon;
		private String password;
		private InitData(String question, Icon icon, String password) {
			super();
			this.question = question;
			this.icon = icon;
			this.password = password;
		}
	}

	/** Constructor.
	 * @param owner The frame in which to center the dialog
	 * @param title The dialog's title
	 * @param question The question to ask. Example: "Please type the password below"
	 * @param icon The icon to display before the question (null to set no icon)
	 * @param password The password typed by default in the dialog
	 */
	public GetPasswordDialog(Window owner, String title, String question, Icon icon, String password) {
		super(owner, title, new InitData(question, icon, password));
		this.confirmIsRequired = false;
	}

	@Override
	protected Object buildResult() {
		return this.panel.getPassword();
	}

	@Override
	protected JPanel createCenterPane(Object data) {
		panel = new GetPasswordPanel();
		InitData initialData = (InitData) data;
		if (initialData.password!=null) panel.setPassword(initialData.password);
		this.panel.setIcon(initialData.icon);
		this.panel.setQuestion(initialData.question);
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		if (confirmIsRequired && !this.panel.getPassword().equals(this.panel.getConfirmPassword())) {
			return "Confirm <> password"; //TODO
		}
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
	
	public void setConfirmIsRequired(boolean required) {
		this.confirmIsRequired = required;
		this.panel.setConfirmIsVisible(true);
		this.pack();
	}
}
