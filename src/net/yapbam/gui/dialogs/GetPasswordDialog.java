package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

@SuppressWarnings("serial")
/** This class is a password ask dialog.
 * It can be customized to change the question, the alert message, etc ... 
 */
public class GetPasswordDialog extends AbstractDialog<GetPasswordDialog.InitData, String> {
	private GetPasswordPanel panel;
	private boolean confirmIsRequired;
	
	static class InitData {
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
	protected String buildResult() {
		return this.panel.getPassword();
	}

	@Override
	protected JPanel createCenterPane() {
		panel = new GetPasswordPanel();
		if (data.password!=null) {
			panel.setPassword(data.password);
		}
		this.panel.setIcon(data.icon);
		this.panel.setQuestion(data.question);
		this.panel.addPropertyChangeListener(GetPasswordPanel.CONFIRMED_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		return panel;
	}

	@Override
	protected String getOkDisabledCause() {
		if (confirmIsRequired && !this.panel.isPasswordConfirmed()) {
			return LocalizationData.get("FilePasswordDialog.confirmIsNotOk"); //$NON-NLS-1$
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
