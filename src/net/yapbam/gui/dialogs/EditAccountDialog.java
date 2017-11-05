package net.yapbam.gui.dialogs;

import java.awt.Window;

import javax.swing.JPanel;

import com.fathzer.soft.ajlib.swing.dialog.AbstractDialog;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AutoUpdateOkButtonPropertyListener;

@SuppressWarnings("serial")
public class EditAccountDialog extends AbstractDialog<GlobalData, Account> {
	private EditAccountPanel editAccountPanel;
	
	public EditAccountDialog(Window owner, String title, GlobalData data) {
		super(owner, title, data);
	}
	
	public void setAccountIndex(int accountIndex) {
		editAccountPanel.setAccount(accountIndex);
		this.pack();
	}
	
	public void setMessage(String message) {
		editAccountPanel.setMessage(message);
		this.pack();
	}

	@Override
	protected JPanel createCenterPane() {
		editAccountPanel = new EditAccountPanel(data);
		editAccountPanel.addPropertyChangeListener(EditAccountPanel.OK_DISABLED_CAUSE_PROPERTY, new AutoUpdateOkButtonPropertyListener(this));
		return editAccountPanel;
	}

	@Override
	protected Account buildResult() {
		return editAccountPanel.getAccount();
	}

	@Override
	protected String getOkDisabledCause() {
		return editAccountPanel.getOkDisabledCause();
	}
	
	/** Opens the dialog, and add the newly created account to the data
	 * @param data The global data where to append the new account
	 * @param owner The frame upon which the dialog will be displayed
	 * @param message A optional message (for instance to explain that before creating a transaction, you have to
	 * 	create an account. Null if no message is required
	 * @return The newly created account or null if the operation was canceled
	 */
	public static Account open(GlobalData data, Window owner, String message) {
		EditAccountDialog dialog = new EditAccountDialog(owner, LocalizationData.get("AccountDialog.title.new"), data); //$NON-NLS-1$
		if (message!=null) {
			dialog.setMessage(message);
			dialog.setLocationRelativeTo(owner);
		}
		dialog.setVisible(true);
		Account newAccount = dialog.getResult();
		if (newAccount!=null) {
			data.add(newAccount);
		}
		return newAccount;
	}
	
	public static void edit(GlobalData data, Window owner, Account account) {
		EditAccountDialog dialog = new EditAccountDialog(owner, LocalizationData.get("AccountDialog.title.edit"), data); //$NON-NLS-1$
		dialog.setAccountIndex(data.indexOf(account));
		dialog.setLocationRelativeTo(dialog.getOwner());
		dialog.setVisible(true);
		Account result = dialog.getResult();
		if (result!=null) {
			if (!result.getName().equals(account.getName())) {
				data.setName(account, result.getName());
			}
			if (result.getInitialBalance()!=account.getInitialBalance()) {
				data.setInitialBalance(account, result.getInitialBalance());
			}
			if (!result.getAlertThreshold().equals(account.getAlertThreshold())) {
				data.setAlertThreshold(account, result.getAlertThreshold());
			}
			data.setComment(account, result.getComment());
		}
	}
}
