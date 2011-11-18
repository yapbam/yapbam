package net.yapbam.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.*;
import javax.swing.event.DocumentListener;

import net.yapbam.data.Account;
import net.yapbam.data.GlobalData;
import net.yapbam.gui.LocalizationData;
import net.yapbam.gui.util.AbstractDialog;
import net.yapbam.gui.widget.AmountWidget;
import net.yapbam.gui.widget.AutoSelectFocusListener;

public class AccountDialog extends AbstractDialog<String, Account> {
	private static final long serialVersionUID = 1L;
	
	private JTextField bankAccountField;
	private AmountWidget balanceField;
	private GlobalData globalData;

	public AccountDialog(Window owner, String message, GlobalData data) {
		super(owner, LocalizationData.get("AccountDialog.title.new"), message); //$NON-NLS-1$
		this.globalData = data;
	}
	
	@Override
	protected JPanel createCenterPane() {
		// Create the content pane.
		JPanel northPanel = new JPanel(new GridBagLayout());
		DocumentListener listener = new AutoUpdateOkButtonDocumentListener(this);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.WEST;
		if (data != null) {
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = GridBagConstraints.REMAINDER;
			northPanel.add(new JLabel(data), c);
			c.fill = GridBagConstraints.NONE;
			c.gridwidth = 1;
			c.gridy++;
		}

		JLabel titleCompte = new JLabel(LocalizationData.get("AccountDialog.account")); //$NON-NLS-1$
		northPanel.add(titleCompte, c);
		bankAccountField = new JTextField(20);
		bankAccountField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		bankAccountField.getDocument().addDocumentListener(listener);
		bankAccountField.setToolTipText(LocalizationData.get("AccountDialog.account.tooltip")); //$NON-NLS-1$
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		northPanel.add(bankAccountField, c);

		JLabel titleBalance = new JLabel(LocalizationData.get("AccountDialog.balance")); //$NON-NLS-1$
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		c.gridx = 0;
		northPanel.add(titleBalance, c);
		balanceField = new AmountWidget(LocalizationData.getLocale());
		balanceField.addFocusListener(AutoSelectFocusListener.INSTANCE);
		balanceField.setValue(new Double(0));
		balanceField.getDocument().addDocumentListener(listener);
		balanceField.setToolTipText(LocalizationData.get("AccountDialog.balance.tooltip")); //$NON-NLS-1$
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		northPanel.add(balanceField, c);

		return northPanel;
	}
	
	@Override
	protected Account buildResult() {
		Number value = (Number) this.balanceField.getValue();
		return new Account(this.bankAccountField.getText().trim(), value.doubleValue());
	}

	/** Opens the dialog, and add the newly created account to the data
	 * @param data The global data where to append the new account
	 * @param owner The frame upon which the dialog will be displayed
	 * @param message A optional message (for instance to explain that before creating a transaction, you have to
	 * 	create an account. Null if no message is required
	 * @return The newly created account or null if the operation was canceled
	 */
	public static Account open(GlobalData data, Window owner, String message) {
		AccountDialog dialog = new AccountDialog(owner, message, data);
		dialog.setVisible(true);
		Account newAccount = dialog.getResult();
		if (newAccount!=null) {
			data.add(newAccount);
		}
		return newAccount;
	}
	
	@Override
	protected String getOkDisabledCause() {
		String name = this.bankAccountField.getText().trim();
		if (name.length()==0) {
			return LocalizationData.get("AccountDialog.err1"); //$NON-NLS-1$
		} else if (!isNameOk(name)) {
			return LocalizationData.get("AccountDialog.err2"); //$NON-NLS-1$
		} else if (this.balanceField.getValue()==null) {
			return LocalizationData.get("AccountDialog.err3"); //$NON-NLS-1$
		}
		return null;
	}
	
	private boolean isNameOk(String name) {
		// Unfortunately, before Yapbam version 0.9.8, it was possible to define account names starting or ending with a space
		// We chose not to trim the readed account names because it was very hard to merge accounts with the same name except the spaces
		// (because modes could not be the same).
		// So, we have to test if the name is equivalent to a trimed previously entered name
		for (int i = 0; i < this.globalData.getAccountsNumber(); i++) {
			if (name.equalsIgnoreCase(this.globalData.getAccount(i).getName().trim())) return false;
		}
		return true;
	}
}
